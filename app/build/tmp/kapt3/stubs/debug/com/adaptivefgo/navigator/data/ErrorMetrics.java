package com.adaptivefgo.navigator.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0013\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BK\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\u001a\b\u0002\u0010\b\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00050\n0\t\u0012\b\b\u0002\u0010\f\u001a\u00020\r\u00a2\u0006\u0002\u0010\u000eJ\t\u0010\u0019\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u0005H\u00c6\u0003J\u001b\u0010\u001d\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00050\n0\tH\u00c6\u0003J\t\u0010\u001e\u001a\u00020\rH\u00c6\u0003JW\u0010\u001f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\u001a\b\u0002\u0010\b\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00050\n0\t2\b\b\u0002\u0010\f\u001a\u00020\rH\u00c6\u0001J\u0013\u0010 \u001a\u00020!2\b\u0010\"\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010#\u001a\u00020\rH\u00d6\u0001J\t\u0010$\u001a\u00020%H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R#\u0010\b\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00050\n0\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0014R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0014R\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018\u00a8\u0006&"}, d2 = {"Lcom/adaptivefgo/navigator/data/ErrorMetrics;", "", "algorithm", "Lcom/adaptivefgo/navigator/data/AlgorithmType;", "rmse", "", "meanError", "maxError", "errorOverTime", "", "Lkotlin/Pair;", "", "windowSize", "", "(Lcom/adaptivefgo/navigator/data/AlgorithmType;DDDLjava/util/List;I)V", "getAlgorithm", "()Lcom/adaptivefgo/navigator/data/AlgorithmType;", "getErrorOverTime", "()Ljava/util/List;", "getMaxError", "()D", "getMeanError", "getRmse", "getWindowSize", "()I", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", "other", "hashCode", "toString", "", "app_debug"})
public final class ErrorMetrics {
    @org.jetbrains.annotations.NotNull()
    private final com.adaptivefgo.navigator.data.AlgorithmType algorithm = null;
    private final double rmse = 0.0;
    private final double meanError = 0.0;
    private final double maxError = 0.0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<kotlin.Pair<java.lang.Long, java.lang.Double>> errorOverTime = null;
    private final int windowSize = 0;
    
    public ErrorMetrics(@org.jetbrains.annotations.NotNull()
    com.adaptivefgo.navigator.data.AlgorithmType algorithm, double rmse, double meanError, double maxError, @org.jetbrains.annotations.NotNull()
    java.util.List<kotlin.Pair<java.lang.Long, java.lang.Double>> errorOverTime, int windowSize) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.adaptivefgo.navigator.data.AlgorithmType getAlgorithm() {
        return null;
    }
    
    public final double getRmse() {
        return 0.0;
    }
    
    public final double getMeanError() {
        return 0.0;
    }
    
    public final double getMaxError() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<kotlin.Pair<java.lang.Long, java.lang.Double>> getErrorOverTime() {
        return null;
    }
    
    public final int getWindowSize() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.adaptivefgo.navigator.data.AlgorithmType component1() {
        return null;
    }
    
    public final double component2() {
        return 0.0;
    }
    
    public final double component3() {
        return 0.0;
    }
    
    public final double component4() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<kotlin.Pair<java.lang.Long, java.lang.Double>> component5() {
        return null;
    }
    
    public final int component6() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.adaptivefgo.navigator.data.ErrorMetrics copy(@org.jetbrains.annotations.NotNull()
    com.adaptivefgo.navigator.data.AlgorithmType algorithm, double rmse, double meanError, double maxError, @org.jetbrains.annotations.NotNull()
    java.util.List<kotlin.Pair<java.lang.Long, java.lang.Double>> errorOverTime, int windowSize) {
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