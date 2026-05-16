package com.adaptivefgo.navigator.cloud;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u001d\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\u000f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\t\u0010\r\u001a\u00020\u0006H\u00c6\u0003J#\u0010\u000e\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0006H\u00c6\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0012\u001a\u00020\u0013H\u00d6\u0001J\t\u0010\u0014\u001a\u00020\u0015H\u00d6\u0001R\u0016\u0010\u0005\u001a\u00020\u00068\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u001c\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0016"}, d2 = {"Lcom/adaptivefgo/navigator/cloud/BatchFgoResponse;", "", "optimizedTrajectory", "", "Lcom/adaptivefgo/navigator/cloud/TrajectoryPointDto;", "computationTimeMs", "", "(Ljava/util/List;J)V", "getComputationTimeMs", "()J", "getOptimizedTrajectory", "()Ljava/util/List;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"})
public final class BatchFgoResponse {
    @com.google.gson.annotations.SerializedName(value = "optimized_trajectory")
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.adaptivefgo.navigator.cloud.TrajectoryPointDto> optimizedTrajectory = null;
    @com.google.gson.annotations.SerializedName(value = "computation_time_ms")
    private final long computationTimeMs = 0L;
    
    public BatchFgoResponse(@org.jetbrains.annotations.NotNull()
    java.util.List<com.adaptivefgo.navigator.cloud.TrajectoryPointDto> optimizedTrajectory, long computationTimeMs) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.adaptivefgo.navigator.cloud.TrajectoryPointDto> getOptimizedTrajectory() {
        return null;
    }
    
    public final long getComputationTimeMs() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.adaptivefgo.navigator.cloud.TrajectoryPointDto> component1() {
        return null;
    }
    
    public final long component2() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.adaptivefgo.navigator.cloud.BatchFgoResponse copy(@org.jetbrains.annotations.NotNull()
    java.util.List<com.adaptivefgo.navigator.cloud.TrajectoryPointDto> optimizedTrajectory, long computationTimeMs) {
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