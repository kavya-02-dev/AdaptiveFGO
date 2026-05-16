package com.adaptivefgo.navigator.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u001d\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BU\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\t\u0012\b\b\u0002\u0010\r\u001a\u00020\t\u0012\b\b\u0002\u0010\u000e\u001a\u00020\t\u00a2\u0006\u0002\u0010\u000fJ\t\u0010\u001d\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0007H\u00c6\u0003J\t\u0010 \u001a\u00020\tH\u00c6\u0003J\t\u0010!\u001a\u00020\u000bH\u00c6\u0003J\t\u0010\"\u001a\u00020\tH\u00c6\u0003J\t\u0010#\u001a\u00020\tH\u00c6\u0003J\t\u0010$\u001a\u00020\tH\u00c6\u0003JY\u0010%\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\t2\b\b\u0002\u0010\r\u001a\u00020\t2\b\b\u0002\u0010\u000e\u001a\u00020\tH\u00c6\u0001J\u0013\u0010&\u001a\u00020\u000b2\b\u0010\'\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010(\u001a\u00020)H\u00d6\u0001J\t\u0010*\u001a\u00020+H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u000e\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0013R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\r\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0013R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\f\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0013R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001c\u00a8\u0006,"}, d2 = {"Lcom/adaptivefgo/navigator/data/SensorSnapshot;", "", "timestamp", "", "gnss", "Lcom/adaptivefgo/navigator/data/GnssData;", "imu", "Lcom/adaptivefgo/navigator/data/ImuData;", "heading", "", "stepDetected", "", "stepLength", "motionVariance", "headingUncertainty", "(JLcom/adaptivefgo/navigator/data/GnssData;Lcom/adaptivefgo/navigator/data/ImuData;DZDDD)V", "getGnss", "()Lcom/adaptivefgo/navigator/data/GnssData;", "getHeading", "()D", "getHeadingUncertainty", "getImu", "()Lcom/adaptivefgo/navigator/data/ImuData;", "getMotionVariance", "getStepDetected", "()Z", "getStepLength", "getTimestamp", "()J", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "other", "hashCode", "", "toString", "", "app_debug"})
public final class SensorSnapshot {
    private final long timestamp = 0L;
    @org.jetbrains.annotations.NotNull()
    private final com.adaptivefgo.navigator.data.GnssData gnss = null;
    @org.jetbrains.annotations.NotNull()
    private final com.adaptivefgo.navigator.data.ImuData imu = null;
    private final double heading = 0.0;
    private final boolean stepDetected = false;
    private final double stepLength = 0.0;
    private final double motionVariance = 0.0;
    private final double headingUncertainty = 0.0;
    
    public SensorSnapshot(long timestamp, @org.jetbrains.annotations.NotNull()
    com.adaptivefgo.navigator.data.GnssData gnss, @org.jetbrains.annotations.NotNull()
    com.adaptivefgo.navigator.data.ImuData imu, double heading, boolean stepDetected, double stepLength, double motionVariance, double headingUncertainty) {
        super();
    }
    
    public final long getTimestamp() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.adaptivefgo.navigator.data.GnssData getGnss() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.adaptivefgo.navigator.data.ImuData getImu() {
        return null;
    }
    
    public final double getHeading() {
        return 0.0;
    }
    
    public final boolean getStepDetected() {
        return false;
    }
    
    public final double getStepLength() {
        return 0.0;
    }
    
    public final double getMotionVariance() {
        return 0.0;
    }
    
    public final double getHeadingUncertainty() {
        return 0.0;
    }
    
    public SensorSnapshot() {
        super();
    }
    
    public final long component1() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.adaptivefgo.navigator.data.GnssData component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.adaptivefgo.navigator.data.ImuData component3() {
        return null;
    }
    
    public final double component4() {
        return 0.0;
    }
    
    public final boolean component5() {
        return false;
    }
    
    public final double component6() {
        return 0.0;
    }
    
    public final double component7() {
        return 0.0;
    }
    
    public final double component8() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.adaptivefgo.navigator.data.SensorSnapshot copy(long timestamp, @org.jetbrains.annotations.NotNull()
    com.adaptivefgo.navigator.data.GnssData gnss, @org.jetbrains.annotations.NotNull()
    com.adaptivefgo.navigator.data.ImuData imu, double heading, boolean stepDetected, double stepLength, double motionVariance, double headingUncertainty) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}