package com.adaptivefgo.navigator.service;

/**
 * LocalizationService - Core Background Processing System
 *
 * - Runs as Android Foreground Service
 * - Collects ALL sensors centrally (one pipeline)
 * - Distributes data to all 5 algorithms simultaneously
 * - Stores results in TrajectoryRepository
 * - UI reads repository; no algorithm code in UI
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00ce\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0014\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 Z2\u00020\u00012\u00020\u00022\u00020\u0003:\u0001ZB\u0005\u00a2\u0006\u0002\u0010\u0004J\b\u00107\u001a\u000208H\u0002J\b\u00109\u001a\u00020\u001bH\u0002J\b\u0010:\u001a\u00020\u001bH\u0002J\b\u0010;\u001a\u00020<H\u0002J\u0010\u0010=\u001a\u00020\u001b2\u0006\u0010>\u001a\u00020\u001bH\u0002J\b\u0010?\u001a\u00020<H\u0002J\u001a\u0010@\u001a\u00020<2\b\u0010A\u001a\u0004\u0018\u00010\u000b2\u0006\u0010B\u001a\u00020.H\u0016J\u0014\u0010C\u001a\u0004\u0018\u00010D2\b\u0010E\u001a\u0004\u0018\u00010FH\u0016J\b\u0010G\u001a\u00020<H\u0016J\b\u0010H\u001a\u00020<H\u0016J\u0010\u0010I\u001a\u00020<2\u0006\u0010J\u001a\u00020KH\u0016J\u0010\u0010L\u001a\u00020<2\u0006\u0010M\u001a\u00020NH\u0016J\"\u0010O\u001a\u00020.2\b\u0010E\u001a\u0004\u0018\u00010F2\u0006\u0010P\u001a\u00020.2\u0006\u0010Q\u001a\u00020.H\u0016J\b\u0010R\u001a\u00020<H\u0002J\b\u0010S\u001a\u00020<H\u0002J\b\u0010T\u001a\u00020<H\u0002J\b\u0010U\u001a\u00020<H\u0002J\b\u0010V\u001a\u00020<H\u0002J\u0010\u0010W\u001a\u00020<2\u0006\u0010X\u001a\u00020YH\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\t0\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020!X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020#X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010%\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\u001bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\'\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020)X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010*\u001a\u0004\u0018\u00010+X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020.X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u000200X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u00101\u001a\u000202X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00103\u001a\u000204X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u00105\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u00106\u001a\u00020\u001bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006["}, d2 = {"Lcom/adaptivefgo/navigator/service/LocalizationService;", "Landroid/app/Service;", "Landroid/hardware/SensorEventListener;", "Landroid/location/LocationListener;", "()V", "accel", "", "accelBuffer", "Lkotlin/collections/ArrayDeque;", "", "accelSensor", "Landroid/hardware/Sensor;", "adaptiveFgoProcessor", "Lcom/adaptivefgo/navigator/algorithms/AdaptiveFgoProcessor;", "cloudSync", "Lcom/adaptivefgo/navigator/cloud/CloudSyncModule;", "cn0Values", "", "fixedLagProcessor", "Lcom/adaptivefgo/navigator/algorithms/FixedLagFgoProcessor;", "gnssProcessor", "Lcom/adaptivefgo/navigator/algorithms/GnssProcessor;", "gnssStatusCallback", "Landroid/location/GnssStatus$Callback;", "gyro", "gyroSensor", "heading", "", "kalmanProcessor", "Lcom/adaptivefgo/navigator/algorithms/KalmanFilterProcessor;", "lastGnssData", "Lcom/adaptivefgo/navigator/data/GnssData;", "lastNotificationUpdate", "", "locationManager", "Landroid/location/LocationManager;", "mag", "magnetometerSensor", "motionVariance", "orientationAngles", "pdrProcessor", "Lcom/adaptivefgo/navigator/algorithms/PdrProcessor;", "processingJob", "Lkotlinx/coroutines/Job;", "rotationMatrix", "satelliteCount", "", "sensorManager", "Landroid/hardware/SensorManager;", "serviceScope", "Lkotlinx/coroutines/CoroutineScope;", "stepDetected", "", "stepDetectorSensor", "stepLength", "buildNotification", "Landroid/app/Notification;", "computeHeadingUncertainty", "computeMotionVariance", "createNotificationChannel", "", "estimateStepLength", "accMag", "initSensors", "onAccuracyChanged", "sensor", "accuracy", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onCreate", "onDestroy", "onLocationChanged", "location", "Landroid/location/Location;", "onSensorChanged", "event", "Landroid/hardware/SensorEvent;", "onStartCommand", "flags", "startId", "processSnapshot", "resetAll", "startProcessingLoop", "startSensors", "stopSensors", "updateNotification", "snapshot", "Lcom/adaptivefgo/navigator/data/SensorSnapshot;", "Companion", "app_debug"})
public final class LocalizationService extends android.app.Service implements android.hardware.SensorEventListener, android.location.LocationListener {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHANNEL_ID = "localization_channel";
    public static final int NOTIFICATION_ID = 1001;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String TAG = "LocalizationService";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_START = "ACTION_START";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_STOP = "ACTION_STOP";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_RESET = "ACTION_RESET";
    private android.hardware.SensorManager sensorManager;
    private android.location.LocationManager locationManager;
    @org.jetbrains.annotations.Nullable()
    private android.hardware.Sensor accelSensor;
    @org.jetbrains.annotations.Nullable()
    private android.hardware.Sensor gyroSensor;
    @org.jetbrains.annotations.Nullable()
    private android.hardware.Sensor magnetometerSensor;
    @org.jetbrains.annotations.Nullable()
    private android.hardware.Sensor stepDetectorSensor;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.NotNull()
    private volatile float[] accel;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.NotNull()
    private volatile float[] gyro;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.NotNull()
    private volatile float[] mag;
    @kotlin.jvm.Volatile()
    private volatile boolean stepDetected = false;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.NotNull()
    private volatile com.adaptivefgo.navigator.data.GnssData lastGnssData;
    @kotlin.jvm.Volatile()
    private volatile double heading = 0.0;
    @org.jetbrains.annotations.NotNull()
    private final float[] rotationMatrix = null;
    @org.jetbrains.annotations.NotNull()
    private final float[] orientationAngles = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.collections.ArrayDeque<java.lang.Float> accelBuffer = null;
    @org.jetbrains.annotations.NotNull()
    private final com.adaptivefgo.navigator.algorithms.GnssProcessor gnssProcessor = null;
    @org.jetbrains.annotations.NotNull()
    private final com.adaptivefgo.navigator.algorithms.PdrProcessor pdrProcessor = null;
    @org.jetbrains.annotations.NotNull()
    private final com.adaptivefgo.navigator.algorithms.KalmanFilterProcessor kalmanProcessor = null;
    @org.jetbrains.annotations.NotNull()
    private final com.adaptivefgo.navigator.algorithms.FixedLagFgoProcessor fixedLagProcessor = null;
    @org.jetbrains.annotations.NotNull()
    private final com.adaptivefgo.navigator.algorithms.AdaptiveFgoProcessor adaptiveFgoProcessor = null;
    @org.jetbrains.annotations.NotNull()
    private final com.adaptivefgo.navigator.cloud.CloudSyncModule cloudSync = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope serviceScope = null;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job processingJob;
    private int satelliteCount = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.Float> cn0Values = null;
    private double stepLength = 0.65;
    private double motionVariance = 0.0;
    @org.jetbrains.annotations.NotNull()
    private final android.location.GnssStatus.Callback gnssStatusCallback = null;
    private long lastNotificationUpdate = 0L;
    @org.jetbrains.annotations.NotNull()
    public static final com.adaptivefgo.navigator.service.LocalizationService.Companion Companion = null;
    
    public LocalizationService() {
        super();
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    @java.lang.Override()
    public int onStartCommand(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
    
    private final void startProcessingLoop() {
    }
    
    private final void processSnapshot() {
    }
    
    @java.lang.Override()
    public void onSensorChanged(@org.jetbrains.annotations.NotNull()
    android.hardware.SensorEvent event) {
    }
    
    @java.lang.Override()
    public void onAccuracyChanged(@org.jetbrains.annotations.Nullable()
    android.hardware.Sensor sensor, int accuracy) {
    }
    
    @java.lang.Override()
    public void onLocationChanged(@org.jetbrains.annotations.NotNull()
    android.location.Location location) {
    }
    
    private final double computeMotionVariance() {
        return 0.0;
    }
    
    private final double computeHeadingUncertainty() {
        return 0.0;
    }
    
    private final double estimateStepLength(double accMag) {
        return 0.0;
    }
    
    private final void initSensors() {
    }
    
    private final void startSensors() {
    }
    
    private final void stopSensors() {
    }
    
    private final void resetAll() {
    }
    
    private final void createNotificationChannel() {
    }
    
    private final android.app.Notification buildNotification() {
        return null;
    }
    
    private final void updateNotification(com.adaptivefgo.navigator.data.SensorSnapshot snapshot) {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public android.os.IBinder onBind(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/adaptivefgo/navigator/service/LocalizationService$Companion;", "", "()V", "ACTION_RESET", "", "ACTION_START", "ACTION_STOP", "CHANNEL_ID", "NOTIFICATION_ID", "", "TAG", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}