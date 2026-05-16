package com.adaptivefgo.navigator.algorithms;

/**
 * GNSS Processor - Page 1
 * Converts raw GNSS readings into a trajectory.
 * Demonstrates satellite noise, multipath error, signal instability.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tJ\u0006\u0010\n\u001a\u00020\u0007R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/adaptivefgo/navigator/algorithms/GnssProcessor;", "", "()V", "points", "", "Lcom/adaptivefgo/navigator/data/TrajectoryPoint;", "process", "", "snapshot", "Lcom/adaptivefgo/navigator/data/SensorSnapshot;", "reset", "app_debug"})
public final class GnssProcessor {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.adaptivefgo.navigator.data.TrajectoryPoint> points = null;
    
    public GnssProcessor() {
        super();
    }
    
    public final void process(@org.jetbrains.annotations.NotNull()
    com.adaptivefgo.navigator.data.SensorSnapshot snapshot) {
    }
    
    public final void reset() {
    }
}