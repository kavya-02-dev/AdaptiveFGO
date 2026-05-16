"""
AdaptiveFGO - GCP Cloud Run Python Service
Performs Batch Factor Graph Optimization using scipy optimization.
Receives trajectory factors from Android app, returns globally optimized trajectory.

Deploy to GCP Cloud Run:
  gcloud run deploy adaptivefgo-batch \
    --source . \
    --platform managed \
    --region us-central1 \
    --allow-unauthenticated
"""

import os
import time
import logging
import numpy as np
from flask import Flask, request, jsonify
from scipy.optimize import minimize
from scipy.sparse import diags
import json

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = Flask(__name__)


def haversine_distance(lat1, lon1, lat2, lon2):
    """Compute Haversine distance in meters."""
    R = 6371000.0
    dlat = np.radians(lat2 - lat1)
    dlon = np.radians(lon2 - lon1)
    a = np.sin(dlat/2)**2 + np.cos(np.radians(lat1)) * np.cos(np.radians(lat2)) * np.sin(dlon/2)**2
    return 2 * R * np.arctan2(np.sqrt(a), np.sqrt(1-a))


def batch_fgo_optimize(factors):
    """
    Batch Factor Graph Optimization using scipy.
    
    The factor graph models:
    - GNSS unary factors: penalize deviation from GPS measurement
    - PDR binary factors: penalize deviation from inertial prediction
    
    Returns globally optimized trajectory minimizing total weighted residuals.
    """
    n = len(factors)
    if n < 2:
        return [{'timestamp': f['timestamp'], 'latitude': f['gnss_lat'],
                 'longitude': f['gnss_lon'], 'accuracy': 1.0} for f in factors]

    # Initial estimate: GNSS positions
    x0 = np.zeros(2 * n)
    for i, f in enumerate(factors):
        x0[2*i] = f['gnss_lat']
        x0[2*i+1] = f['gnss_lon']

    def cost_function(x):
        """Total weighted sum of squared residuals."""
        total_cost = 0.0
        
        for i, f in enumerate(factors):
            lat = x[2*i]
            lon = x[2*i+1]
            
            # GNSS unary factor
            gnss_noise_sq = max(f['gnss_noise'], 1.0) ** 2
            gnss_res_lat = (lat - f['gnss_lat']) ** 2 / gnss_noise_sq
            gnss_res_lon = (lon - f['gnss_lon']) ** 2 / gnss_noise_sq
            total_cost += gnss_res_lat + gnss_res_lon

            # PDR binary factor (motion constraint between consecutive poses)
            if i > 0:
                prev_lat = x[2*(i-1)]
                prev_lon = x[2*(i-1)+1]
                pdr_noise_sq = max(f['pdr_noise'], 0.1) ** 2
                
                predicted_lat = prev_lat + f['pdr_delta_lat']
                predicted_lon = prev_lon + f['pdr_delta_lon']
                
                pdr_res_lat = (lat - predicted_lat) ** 2 / pdr_noise_sq
                pdr_res_lon = (lon - predicted_lon) ** 2 / pdr_noise_sq
                total_cost += 0.5 * (pdr_res_lat + pdr_res_lon)

        return total_cost

    def gradient(x):
        """Analytical gradient for faster convergence."""
        grad = np.zeros_like(x)
        
        for i, f in enumerate(factors):
            lat = x[2*i]
            lon = x[2*i+1]
            gnss_noise_sq = max(f['gnss_noise'], 1.0) ** 2

            # GNSS gradient
            grad[2*i] += 2.0 * (lat - f['gnss_lat']) / gnss_noise_sq
            grad[2*i+1] += 2.0 * (lon - f['gnss_lon']) / gnss_noise_sq

            # PDR gradient
            if i > 0:
                prev_lat = x[2*(i-1)]
                prev_lon = x[2*(i-1)+1]
                pdr_noise_sq = max(f['pdr_noise'], 0.1) ** 2

                predicted_lat = prev_lat + f['pdr_delta_lat']
                predicted_lon = prev_lon + f['pdr_delta_lon']

                pdr_grad_lat = (lat - predicted_lat) / pdr_noise_sq
                pdr_grad_lon = (lon - predicted_lon) / pdr_noise_sq

                grad[2*i] += pdr_grad_lat
                grad[2*i+1] += pdr_grad_lon
                grad[2*(i-1)] -= pdr_grad_lat
                grad[2*(i-1)+1] -= pdr_grad_lon

        return grad

    # Run L-BFGS-B optimization (efficient for large sparse problems)
    result = minimize(
        cost_function,
        x0,
        jac=gradient,
        method='L-BFGS-B',
        options={'maxiter': 500, 'ftol': 1e-9, 'gtol': 1e-7}
    )

    # Extract optimized trajectory
    optimized = []
    for i, f in enumerate(factors):
        opt_lat = result.x[2*i]
        opt_lon = result.x[2*i+1]
        
        # Compute accuracy estimate from residual
        gnss_dist = haversine_distance(opt_lat, opt_lon, f['gnss_lat'], f['gnss_lon'])
        accuracy = max(0.5, min(gnss_dist, f['gnss_noise']))
        
        optimized.append({
            'timestamp': f['timestamp'],
            'latitude': float(opt_lat),
            'longitude': float(opt_lon),
            'accuracy': float(accuracy)
        })

    return optimized


@app.route('/optimize', methods=['POST'])
def optimize():
    """
    POST /optimize
    Body: {"factors": [...]}
    Returns: {"optimized_trajectory": [...], "computation_time_ms": ...}
    """
    start_time = time.time()
    
    try:
        data = request.get_json()
        if not data or 'factors' not in data:
            return jsonify({'error': 'Missing factors field'}), 400

        factors = data['factors']
        if len(factors) < 2:
            return jsonify({'error': 'Need at least 2 factors'}), 400

        logger.info(f"Optimizing {len(factors)} factors")
        
        optimized = batch_fgo_optimize(factors)
        
        elapsed_ms = int((time.time() - start_time) * 1000)
        logger.info(f"Optimization complete: {len(optimized)} points in {elapsed_ms}ms")

        return jsonify({
            'optimized_trajectory': optimized,
            'computation_time_ms': elapsed_ms,
            'factors_count': len(factors)
        })

    except Exception as e:
        logger.error(f"Optimization failed: {e}")
        return jsonify({'error': str(e)}), 500


@app.route('/health', methods=['GET'])
def health():
    return jsonify({'status': 'healthy', 'service': 'AdaptiveFGO Batch FGO'})


if __name__ == '__main__':
    port = int(os.environ.get('PORT', 8080))
    app.run(host='0.0.0.0', port=port, debug=False)
