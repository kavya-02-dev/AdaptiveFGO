# AdaptiveFGO Navigator

**Real-Time GNSS–PDR Fusion and Adaptive Factor Graph Optimization Platform**

A full Android research application implementing 6 localization algorithms with a centralized sensor pipeline, real-time visualization, and GCP cloud batch optimization.

---

## Architecture

```
Smartphone Sensors
  │ GPS / Accelerometer / Gyroscope / Magnetometer
  ▼
LocalizationService (Kotlin Foreground Service)
  │  Central sensor pipeline — collects once, distributes to all
  ├──▶ GnssProcessor         → GNSS raw trajectory
  ├──▶ PdrProcessor          → Pedestrian Dead Reckoning
  ├──▶ KalmanFilterProcessor → EKF GNSS+PDR fusion
  ├──▶ FixedLagFgoProcessor  → Sliding window FGO
  ├──▶ AdaptiveFgoProcessor  → Adaptive window FGO ⭐
  └──▶ CloudSyncModule       → GCP Cloud Run → Batch FGO
          │
          ▼
    TrajectoryRepository (StateFlow)
          │
          ▼
    MainActivity (ViewPager2 — 11 pages)
    All pages read from repository instantly — no recomputation on swipe
```

---

## Setup

### 1. Clone & Open in Android Studio

```bash
git clone <repo>
# Open AdaptiveFGO/ in Android Studio Arctic Fox or newer
```

### 2. Configure API Keys

Copy `local.properties.template` to `local.properties` and fill in:

```properties
sdk.dir=/path/to/your/Android/sdk
MAPS_API_KEY=YOUR_GOOGLE_MAPS_API_KEY
GCP_BATCH_FGO_URL=YOUR_CLOUD_RUN_URL
```

**Get Google Maps API Key:**
1. Go to https://console.cloud.google.com
2. Enable "Maps SDK for Android"
3. Create credentials → API Key
4. Restrict to your app package: `com.adaptivefgo.navigator`

### 3. Deploy GCP Cloud Run (Optional — for Page 4 Batch FGO)

```bash
cd cloud-run-python/

# Install Google Cloud CLI if needed
# https://cloud.google.com/sdk/docs/install

gcloud auth login
gcloud config set project YOUR_PROJECT_ID

# Deploy (free tier: 2M requests/month, 360K CPU-seconds)
gcloud run deploy adaptivefgo-batch \
  --source . \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --memory 512Mi

# Copy the Service URL to local.properties GCP_BATCH_FGO_URL
```

If you skip GCP deployment, Page 4 (Batch FGO) will use an on-device fallback.

### 4. Build & Run

- Connect Android device (API 26+) or start emulator
- Run `▶ app` in Android Studio
- Grant location permissions when prompted
- Tap ▶ FAB to start the localization service

---

## Pages

| Page | Algorithm | Key Feature |
|------|-----------|-------------|
| 1 GNSS | Raw GPS | Baseline: shows noise & multipath |
| 2 PDR | Inertial only | Cumulative drift visible |
| 3 Kalman | EKF fusion | Improved stability |
| 4 Batch FGO | GCP Cloud Run | Full graph optimization |
| 5 Fixed-Lag FGO | Sliding window | Real-time feasible |
| 6 ★ Adaptive FGO | **Novel method** | Dynamic window adaptation |
| 7 Compare | All 6 overlaid | Visual superiority of Adaptive FGO |
| 8 Error | RMSE/Mean/Max | Quantitative validation |
| 9 Live | Real-time markers | GNSS vs PDR vs Adaptive FGO |
| 10 Navigate | Navigation mode | Long-press map to set destination |
| 11 Settings | Experiment | Adjust error parameters |

---

## Core Research Contribution (Page 6)

**Adaptive FGO** dynamically adjusts the optimization window size using:

```
GNSS Quality Score  = f(CN0, accuracy, satellite count)
PDR Stability Score = f(accelerometer variance, step regularity)
Heading Certainty   = f(magnetometer quality, gyro agreement)

Window Size = MIN + (1 - Combined Quality) × (MAX - MIN) × Sensitivity

Strong GNSS + Stable PDR  →  Small window (5-8)   — fast, efficient
Weak GNSS + Unstable PDR  →  Large window (20-30)  — robust, accurate
```

This achieves:
- Higher accuracy than Kalman Filter
- Lower latency than Batch FGO
- More accurate than Fixed-Lag FGO in dynamic conditions

---

## Sensor Pipeline

All sensors collected **once** centrally in `LocalizationService` and distributed to all algorithms. Each algorithm receives an identical `SensorSnapshot` object:

```kotlin
data class SensorSnapshot(
    val gnss: GnssData,           // latitude, longitude, accuracy, CN0, sats
    val imu: ImuData,             // accel, gyro, magnetometer
    val heading: Double,          // computed from accel+mag
    val stepDetected: Boolean,    // from TYPE_STEP_DETECTOR
    val stepLength: Double,       // Weinberg model estimate
    val motionVariance: Double,   // from accelerometer buffer
    val headingUncertainty: Double
)
```

Processing rate: **10 Hz** (algorithms) | **50 Hz** (sensors)

---

## Technology Stack

| Layer | Technology |
|-------|------------|
| UI | Kotlin, Android ViewPager2, Material3 |
| Sensors | GPS, Accelerometer, Gyroscope, Magnetometer |
| Maps | Google Maps SDK for Android |
| Background | Android Foreground Service |
| Architecture | Repository Pattern + StateFlow |
| Charts | MPAndroidChart |
| Cloud | GCP Cloud Run (Python, Flask, scipy) |
| HTTP | OkHttp + Retrofit |

---

## GCP Free Tier Usage

- **Cloud Run**: 2M requests/month free
- **Minimum instances**: 0 (scales to zero when not used)
- The app syncs every **30 seconds** with minimum **10 factors** accumulated
- Typical session: ~2 Cloud Run invocations/minute ≈ well within free tier

---

## Permissions Required

```
ACCESS_FINE_LOCATION      — GPS positioning
ACCESS_COARSE_LOCATION    — Network fallback
FOREGROUND_SERVICE        — Background processing
HIGH_SAMPLING_RATE_SENSORS — Fast IMU (>200Hz)
INTERNET                  — GCP sync
```

---

## File Structure

```
AdaptiveFGO/
├── app/src/main/java/com/adaptivefgo/navigator/
│   ├── algorithms/
│   │   ├── GnssProcessor.kt
│   │   ├── PdrProcessor.kt           (Weinberg step length model)
│   │   ├── KalmanFilterProcessor.kt  (4-state EKF)
│   │   ├── FixedLagFgoProcessor.kt   (Gauss-Newton, window=10)
│   │   └── AdaptiveFgoProcessor.kt   ⭐ Core research
│   ├── cloud/
│   │   └── CloudSyncModule.kt        (GCP sync)
│   ├── data/
│   │   └── Models.kt                 (all data classes)
│   ├── repository/
│   │   └── TrajectoryRepository.kt   (central state store)
│   ├── service/
│   │   └── LocalizationService.kt    (foreground service)
│   ├── ui/
│   │   ├── BaseMapFragment.kt
│   │   ├── gnss/, pdr/, kalman/, batchfgo/, fixedlagfgo/
│   │   ├── adaptivefgo/              ⭐ Main research page
│   │   ├── comparison/
│   │   ├── error/
│   │   ├── tracking/
│   │   ├── navigation/
│   │   └── settings/
│   └── MainActivity.kt               (ViewPager2, 11 pages)
├── cloud-run-python/
│   ├── main.py                       (Flask + scipy L-BFGS-B optimization)
│   ├── requirements.txt
│   └── Dockerfile
└── local.properties.template         ← copy to local.properties
```
