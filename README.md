# 📍 Adaptive Fusion Navigation

Adaptive Fusion Navigation is a real-time pedestrian navigation system designed to improve positioning accuracy in challenging indoor and outdoor environments. The project combines **GNSS**, **Pedestrian Dead Reckoning (PDR)**, and **Adaptive Factor Graph Optimization (FGO)** to provide reliable localization even during signal interruptions and urban obstructions.

Unlike traditional navigation systems that rely heavily on GPS alone, this project dynamically adapts its optimization strategy based on:

* GNSS signal quality
* pedestrian motion stability
* sensor confidence

The result is a navigation system that remains accurate, stable, and computationally efficient even in difficult environments such as indoor corridors, staircases, and urban canyons.

This project was developed as part of research focused on adaptive optimization techniques for smartphone-based pedestrian navigation. 

---

# ✨ Key Features

* Real-time pedestrian navigation
* GNSS + PDR sensor fusion
* Adaptive Sliding Window Factor Graph Optimization
* Indoor–outdoor seamless tracking
* Dynamic optimization window selection
* Google Maps integration
* Cloud-assisted batch optimization using Google Cloud Run
* Multi-layer trajectory visualization
* Real-time navigation statistics
* Motion variance analysis
* Error comparison dashboard
* Route optimization and smoothing
* CSV export support

---

# 🧠 Core Research Contribution

The main contribution of this project is the implementation of an **Adaptive Sliding Window Factor Graph Optimization** framework.

Instead of using a fixed optimization window, the system dynamically adjusts the window size using:

* GNSS quality score
* PDR stability score
* heading certainty
* motion variance

### Adaptive Strategy

| Environment                 | Window Size          | Behavior                 |
| --------------------------- | -------------------- | ------------------------ |
| Strong GNSS + Stable Motion | Small Window (5–8)   | Faster and efficient     |
| Weak GNSS + Unstable Motion | Large Window (20–30) | More robust and accurate |

This approach achieves:

* higher accuracy than Kalman Filters
* lower latency than Batch FGO
* better adaptability than Fixed-Lag FGO



---

# 🏗 System Architecture

```text id="jz9u2l"
Smartphone Sensors
│ GPS / Accelerometer / Gyroscope / Magnetometer
▼
LocalizationService (Foreground Service)
│
├── GnssProcessor
├── PdrProcessor
├── KalmanFilterProcessor
├── FixedLagFgoProcessor
├── AdaptiveFgoProcessor
└── CloudSyncModule → GCP Cloud Run → Batch FGO
```

All sensor data is collected centrally and distributed to multiple localization algorithms in real time. This architecture avoids recomputation and allows efficient comparison between different fusion approaches. 

---

# 🛠 Technology Stack

### Mobile Development

* Kotlin
* Android SDK
* Android Sensor APIs
* Google Maps SDK
* Material Design 3

### Backend & Cloud

* Google Cloud Platform (GCP)
* Google Cloud Run
* Flask
* REST APIs
* Retrofit
* OkHttp

### Navigation & Optimization

* Factor Graph Optimization (FGO)
* GNSS/PDR Fusion
* Extended Kalman Filter (EKF)
* GraphHopper Routing

### Storage & Visualization

* Room Database
* MPAndroidChart
* StateFlow
* Gson



---

# 📱 Application Pages

The Android application contains 11 interactive pages for visualization, experimentation, and algorithm comparison.

| Page | Module         | Purpose                     |
| ---- | -------------- | --------------------------- |
| 1    | GNSS           | Raw GPS trajectory          |
| 2    | PDR            | Drift visualization         |
| 3    | Kalman Filter  | EKF-based fusion            |
| 4    | Batch FGO      | Cloud-based optimization    |
| 5    | Fixed-Lag FGO  | Sliding window optimization |
| 6    | Adaptive FGO   | Proposed adaptive algorithm |
| 7    | Compare        | Overlay comparison          |
| 8    | Error Analysis | RMSE and accuracy metrics   |
| 9    | Live Tracking  | Real-time localization      |
| 10   | Navigation     | Route guidance              |
| 11   | Settings       | Experiment controls         |



---

# 📊 Experimental Results

The system was evaluated on mixed indoor–outdoor pedestrian routes containing:

* open-sky regions
* urban obstruction areas
* indoor corridors
* staircase environments
* GNSS outage zones

### Positioning Accuracy

| Method                  | RMSE   |
| ----------------------- | ------ |
| Raw GNSS                | 4.82 m |
| Kalman Filter           | 2.86 m |
| Fixed Window FGO        | 2.31 m |
| Adaptive FGO (Proposed) | 1.47 m |

The proposed Adaptive FGO achieved:

* smoother trajectories
* lower positioning error
* improved indoor stability
* real-time execution with low latency



---

# ⚙ Sensor Pipeline

All localization algorithms receive the same centralized sensor snapshot containing:

* GNSS data
* IMU readings
* heading estimation
* step detection
* motion variance
* uncertainty metrics

Processing Rates:

* Sensors → 50 Hz
* Algorithms → 10 Hz

This enables fair real-time comparison between localization approaches. 

---

# ☁ Cloud Integration

The project integrates Google Cloud Run for batch optimization.

### Cloud Features

* scalable serverless deployment
* asynchronous optimization
* trajectory refinement
* cloud-assisted validation

The system automatically syncs optimized trajectories without blocking real-time mobile execution. 

---

# 📂 Project Structure

```text id="4xf26h"
app/                    Android application
cloud-run-python/       Cloud optimization backend
screenshots/            Application screenshots
datasets/               Navigation datasets
```

---

# 🚀 Setup Instructions

### Clone Repository

```bash id="v4ax5z"
git clone https://github.com/yourusername/adaptive-fusion-navigation.git
```

---

### Configure API Keys

Create a `local.properties` file:

```properties id="jlwm6w"
MAPS_API_KEY=YOUR_GOOGLE_MAPS_API_KEY
GCP_BATCH_FGO_URL=YOUR_CLOUD_RUN_ENDPOINT
```

---

### Build Project

```bash id="6br1be"
./gradlew build
```

Run the project using Android Studio or install the APK directly on an Android device.

---

# 🔮 Future Improvements

Planned enhancements include:

* incremental graph optimization using iSAM
* multi-device evaluation
* tightly coupled GNSS integration
* visual-inertial fusion
* power optimization
* AI-assisted motion prediction



---

# 📖 Research Basis

This project is based on the research work:

**Adaptive Window Factor Graph Optimization for PDR-GNSS Fusion in Real-Time Pedestrian Navigation** 

---

# 📄 License

This project is intended for:

* academic research
* educational purposes
* experimental navigation systems
