package com.adaptivefgo.navigator.repository;

/**
 * Central repository that stores all computed trajectories.
 * All algorithms write here; all UI pages read from here.
 * This decouples algorithms from UI completely.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0010\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J4\u0010,\u001a\u0014\u0012\u0004\u0012\u00020.\u0012\u0004\u0012\u00020.\u0012\u0004\u0012\u00020.0-2\f\u0010/\u001a\b\u0012\u0004\u0012\u000201002\f\u00102\u001a\b\u0012\u0004\u0012\u00020100J\u0012\u00103\u001a\u000e\u0012\u0004\u0012\u000205\u0012\u0004\u0012\u00020\u000504J&\u00106\u001a\u00020.2\u0006\u00107\u001a\u00020.2\u0006\u00108\u001a\u00020.2\u0006\u00109\u001a\u00020.2\u0006\u0010:\u001a\u00020.J\u0006\u0010;\u001a\u00020<J\u000e\u0010=\u001a\u00020<2\u0006\u0010>\u001a\u00020\fJ\u000e\u0010?\u001a\u00020<2\u0006\u0010@\u001a\u00020\u0005J\u000e\u0010A\u001a\u00020<2\u0006\u0010B\u001a\u00020\u0007J\u000e\u0010C\u001a\u00020<2\u0006\u0010@\u001a\u00020\u0005J\u000e\u0010D\u001a\u00020<2\u0006\u0010@\u001a\u00020\u0005J\u000e\u0010E\u001a\u00020<2\u0006\u0010@\u001a\u00020\u0005J\u000e\u0010F\u001a\u00020<2\u0006\u0010@\u001a\u00020\u0005J\u000e\u0010G\u001a\u00020<2\u0006\u0010H\u001a\u00020\u000fJ\u000e\u0010I\u001a\u00020<2\u0006\u0010B\u001a\u00020\u0011J\u000e\u0010J\u001a\u00020<2\u0006\u0010@\u001a\u00020\u0005J\u000e\u0010K\u001a\u00020<2\u0006\u0010*\u001a\u00020\u0014R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00070\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00050\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00070\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0018R\u0017\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00050\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0018R\u0017\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00050\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0018R\u0017\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00050\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0018R\u0017\u0010!\u001a\b\u0012\u0004\u0012\u00020\f0\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0018R\u0017\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00050\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u0018R\u0019\u0010$\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u0018R\u0017\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00110\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\u0018R\u0017\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00050\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010\u0018R\u0017\u0010*\u001a\b\u0012\u0004\u0012\u00020\u00140\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010\u0018\u00a8\u0006L"}, d2 = {"Lcom/adaptivefgo/navigator/repository/TrajectoryRepository;", "", "()V", "_adaptiveFgoTrajectory", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/adaptivefgo/navigator/data/TrajectoryResult;", "_adaptiveWindowState", "Lcom/adaptivefgo/navigator/data/AdaptiveWindowState;", "_batchFgoTrajectory", "_fixedLagTrajectory", "_gnssTrajectory", "_isServiceRunning", "", "_kalmanTrajectory", "_latestSnapshot", "Lcom/adaptivefgo/navigator/data/SensorSnapshot;", "_navigationState", "Lcom/adaptivefgo/navigator/data/NavigationState;", "_pdrTrajectory", "_settings", "Lcom/adaptivefgo/navigator/data/AlgorithmSettings;", "adaptiveFgoTrajectory", "Lkotlinx/coroutines/flow/StateFlow;", "getAdaptiveFgoTrajectory", "()Lkotlinx/coroutines/flow/StateFlow;", "adaptiveWindowState", "getAdaptiveWindowState", "batchFgoTrajectory", "getBatchFgoTrajectory", "fixedLagTrajectory", "getFixedLagTrajectory", "gnssTrajectory", "getGnssTrajectory", "isServiceRunning", "kalmanTrajectory", "getKalmanTrajectory", "latestSnapshot", "getLatestSnapshot", "navigationState", "getNavigationState", "pdrTrajectory", "getPdrTrajectory", "settings", "getSettings", "computeErrorMetrics", "Lkotlin/Triple;", "", "reference", "", "Lcom/adaptivefgo/navigator/data/TrajectoryPoint;", "computed", "getAllTrajectories", "", "Lcom/adaptivefgo/navigator/data/AlgorithmType;", "haversineDistance", "lat1", "lon1", "lat2", "lon2", "reset", "", "setServiceRunning", "running", "updateAdaptiveFgo", "result", "updateAdaptiveWindowState", "state", "updateBatchFgo", "updateFixedLag", "updateGnss", "updateKalman", "updateLatestSnapshot", "snapshot", "updateNavigationState", "updatePdr", "updateSettings", "app_debug"})
public final class TrajectoryRepository {
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.MutableStateFlow<com.adaptivefgo.navigator.data.TrajectoryResult> _gnssTrajectory = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.StateFlow<com.adaptivefgo.navigator.data.TrajectoryResult> gnssTrajectory = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.MutableStateFlow<com.adaptivefgo.navigator.data.TrajectoryResult> _pdrTrajectory = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.StateFlow<com.adaptivefgo.navigator.data.TrajectoryResult> pdrTrajectory = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.MutableStateFlow<com.adaptivefgo.navigator.data.TrajectoryResult> _kalmanTrajectory = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.StateFlow<com.adaptivefgo.navigator.data.TrajectoryResult> kalmanTrajectory = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.MutableStateFlow<com.adaptivefgo.navigator.data.TrajectoryResult> _batchFgoTrajectory = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.StateFlow<com.adaptivefgo.navigator.data.TrajectoryResult> batchFgoTrajectory = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.MutableStateFlow<com.adaptivefgo.navigator.data.TrajectoryResult> _fixedLagTrajectory = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.StateFlow<com.adaptivefgo.navigator.data.TrajectoryResult> fixedLagTrajectory = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.MutableStateFlow<com.adaptivefgo.navigator.data.TrajectoryResult> _adaptiveFgoTrajectory = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.StateFlow<com.adaptivefgo.navigator.data.TrajectoryResult> adaptiveFgoTrajectory = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.MutableStateFlow<com.adaptivefgo.navigator.data.AdaptiveWindowState> _adaptiveWindowState = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.StateFlow<com.adaptivefgo.navigator.data.AdaptiveWindowState> adaptiveWindowState = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.MutableStateFlow<com.adaptivefgo.navigator.data.SensorSnapshot> _latestSnapshot = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.StateFlow<com.adaptivefgo.navigator.data.SensorSnapshot> latestSnapshot = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.MutableStateFlow<com.adaptivefgo.navigator.data.NavigationState> _navigationState = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.StateFlow<com.adaptivefgo.navigator.data.NavigationState> navigationState = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.MutableStateFlow<com.adaptivefgo.navigator.data.AlgorithmSettings> _settings = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.StateFlow<com.adaptivefgo.navigator.data.AlgorithmSettings> settings = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isServiceRunning = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isServiceRunning = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.adaptivefgo.navigator.repository.TrajectoryRepository INSTANCE = null;
    
    private TrajectoryRepository() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.adaptivefgo.navigator.data.TrajectoryResult> getGnssTrajectory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.adaptivefgo.navigator.data.TrajectoryResult> getPdrTrajectory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.adaptivefgo.navigator.data.TrajectoryResult> getKalmanTrajectory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.adaptivefgo.navigator.data.TrajectoryResult> getBatchFgoTrajectory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.adaptivefgo.navigator.data.TrajectoryResult> getFixedLagTrajectory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.adaptivefgo.navigator.data.TrajectoryResult> getAdaptiveFgoTrajectory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.adaptivefgo.navigator.data.AdaptiveWindowState> getAdaptiveWindowState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.adaptivefgo.navigator.data.SensorSnapshot> getLatestSnapshot() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.adaptivefgo.navigator.data.NavigationState> getNavigationState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.adaptivefgo.navigator.data.AlgorithmSettings> getSettings() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isServiceRunning() {
        return null;
    }
    
    public final void updateGnss(@org.jetbrains.annotations.NotNull()
    com.adaptivefgo.navigator.data.TrajectoryResult result) {
    }
    
    public final void updatePdr(@org.jetbrains.annotations.NotNull()
    com.adaptivefgo.navigator.data.TrajectoryResult result) {
    }
    
    public final void updateKalman(@org.jetbrains.annotations.NotNull()
    com.adaptivefgo.navigator.data.TrajectoryResult result) {
    }
    
    public final void updateBatchFgo(@org.jetbrains.annotations.NotNull()
    com.adaptivefgo.navigator.data.TrajectoryResult result) {
    }
    
    public final void updateFixedLag(@org.jetbrains.annotations.NotNull()
    com.adaptivefgo.navigator.data.TrajectoryResult result) {
    }
    
    public final void updateAdaptiveFgo(@org.jetbrains.annotations.NotNull()
    com.adaptivefgo.navigator.data.TrajectoryResult result) {
    }
    
    public final void updateAdaptiveWindowState(@org.jetbrains.annotations.NotNull()
    com.adaptivefgo.navigator.data.AdaptiveWindowState state) {
    }
    
    public final void updateLatestSnapshot(@org.jetbrains.annotations.NotNull()
    com.adaptivefgo.navigator.data.SensorSnapshot snapshot) {
    }
    
    public final void updateNavigationState(@org.jetbrains.annotations.NotNull()
    com.adaptivefgo.navigator.data.NavigationState state) {
    }
    
    public final void updateSettings(@org.jetbrains.annotations.NotNull()
    com.adaptivefgo.navigator.data.AlgorithmSettings settings) {
    }
    
    public final void setServiceRunning(boolean running) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<com.adaptivefgo.navigator.data.AlgorithmType, com.adaptivefgo.navigator.data.TrajectoryResult> getAllTrajectories() {
        return null;
    }
    
    public final void reset() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlin.Triple<java.lang.Double, java.lang.Double, java.lang.Double> computeErrorMetrics(@org.jetbrains.annotations.NotNull()
    java.util.List<com.adaptivefgo.navigator.data.TrajectoryPoint> reference, @org.jetbrains.annotations.NotNull()
    java.util.List<com.adaptivefgo.navigator.data.TrajectoryPoint> computed) {
        return null;
    }
    
    public final double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        return 0.0;
    }
}