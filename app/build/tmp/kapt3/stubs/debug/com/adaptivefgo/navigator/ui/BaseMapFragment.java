package com.adaptivefgo.navigator.ui;

/**
 * Base class for all map-based fragment pages.
 * Handles MapView lifecycle and provides helper methods for drawing trajectories.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n\b&\u0018\u0000 *2\u00020\u0001:\u0001*B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u000b\u001a\u0004\u0018\u00010\f2\u0006\u0010\r\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u000fH\u0004J\u001a\u0010\u0010\u001a\u0004\u0018\u00010\f2\u0006\u0010\r\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u000fH\u0004J\u001e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\r\u001a\u00020\u00042\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0014H\u0004J2\u0010\u0015\u001a\u0004\u0018\u00010\u00162\u0006\u0010\r\u001a\u00020\u00042\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u000f0\u00142\u0006\u0010\u0017\u001a\u00020\u00182\b\b\u0002\u0010\u0019\u001a\u00020\u001aH\u0004J\u001e\u0010\u001b\u001a\u00020\u00122\u0006\u0010\r\u001a\u00020\u00042\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0014H\u0004J\"\u0010\u001c\u001a\u00020\u00122\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u00182\b\u0010 \u001a\u0004\u0018\u00010!H\u0004J\b\u0010\"\u001a\u00020\u0012H\u0016J\b\u0010#\u001a\u00020\u0012H\u0016J\u0010\u0010$\u001a\u00020\u00122\u0006\u0010\r\u001a\u00020\u0004H&J\b\u0010%\u001a\u00020\u0012H\u0016J\b\u0010&\u001a\u00020\u0012H\u0016J\u0010\u0010\'\u001a\u00020\u00122\u0006\u0010(\u001a\u00020!H\u0016J\u0010\u0010)\u001a\u00020\u00122\u0006\u0010\r\u001a\u00020\u0004H\u0002R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0084\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006+"}, d2 = {"Lcom/adaptivefgo/navigator/ui/BaseMapFragment;", "Landroidx/fragment/app/Fragment;", "()V", "googleMap", "Lcom/google/android/gms/maps/GoogleMap;", "getGoogleMap", "()Lcom/google/android/gms/maps/GoogleMap;", "setGoogleMap", "(Lcom/google/android/gms/maps/GoogleMap;)V", "mapView", "Lcom/google/android/gms/maps/MapView;", "addEndMarker", "Lcom/google/android/gms/maps/model/Marker;", "map", "point", "Lcom/adaptivefgo/navigator/data/TrajectoryPoint;", "addStartMarker", "centerOnTrajectory", "", "points", "", "drawTrajectory", "Lcom/google/android/gms/maps/model/Polyline;", "color", "", "width", "", "fitBoundsToTrajectory", "initMapView", "view", "Landroid/view/View;", "mapViewId", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onLowMemory", "onMapReady", "onPause", "onResume", "onSaveInstanceState", "outState", "setupMap", "Companion", "app_debug"})
public abstract class BaseMapFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private com.google.android.gms.maps.GoogleMap googleMap;
    @org.jetbrains.annotations.Nullable()
    private com.google.android.gms.maps.MapView mapView;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.Map<com.adaptivefgo.navigator.data.AlgorithmType, java.lang.Integer> COLORS = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.Map<com.adaptivefgo.navigator.data.AlgorithmType, java.lang.String> NAMES = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.adaptivefgo.navigator.ui.BaseMapFragment.Companion Companion = null;
    
    public BaseMapFragment() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    protected final com.google.android.gms.maps.GoogleMap getGoogleMap() {
        return null;
    }
    
    protected final void setGoogleMap(@org.jetbrains.annotations.Nullable()
    com.google.android.gms.maps.GoogleMap p0) {
    }
    
    protected final void initMapView(@org.jetbrains.annotations.NotNull()
    android.view.View view, int mapViewId, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupMap(com.google.android.gms.maps.GoogleMap map) {
    }
    
    public abstract void onMapReady(@org.jetbrains.annotations.NotNull()
    com.google.android.gms.maps.GoogleMap map);
    
    @org.jetbrains.annotations.Nullable()
    protected final com.google.android.gms.maps.model.Polyline drawTrajectory(@org.jetbrains.annotations.NotNull()
    com.google.android.gms.maps.GoogleMap map, @org.jetbrains.annotations.NotNull()
    java.util.List<com.adaptivefgo.navigator.data.TrajectoryPoint> points, int color, float width) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    protected final com.google.android.gms.maps.model.Marker addStartMarker(@org.jetbrains.annotations.NotNull()
    com.google.android.gms.maps.GoogleMap map, @org.jetbrains.annotations.NotNull()
    com.adaptivefgo.navigator.data.TrajectoryPoint point) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    protected final com.google.android.gms.maps.model.Marker addEndMarker(@org.jetbrains.annotations.NotNull()
    com.google.android.gms.maps.GoogleMap map, @org.jetbrains.annotations.NotNull()
    com.adaptivefgo.navigator.data.TrajectoryPoint point) {
        return null;
    }
    
    protected final void centerOnTrajectory(@org.jetbrains.annotations.NotNull()
    com.google.android.gms.maps.GoogleMap map, @org.jetbrains.annotations.NotNull()
    java.util.List<com.adaptivefgo.navigator.data.TrajectoryPoint> points) {
    }
    
    protected final void fitBoundsToTrajectory(@org.jetbrains.annotations.NotNull()
    com.google.android.gms.maps.GoogleMap map, @org.jetbrains.annotations.NotNull()
    java.util.List<com.adaptivefgo.navigator.data.TrajectoryPoint> points) {
    }
    
    @java.lang.Override()
    public void onResume() {
    }
    
    @java.lang.Override()
    public void onPause() {
    }
    
    @java.lang.Override()
    public void onSaveInstanceState(@org.jetbrains.annotations.NotNull()
    android.os.Bundle outState) {
    }
    
    @java.lang.Override()
    public void onLowMemory() {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001d\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u001d\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\n0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\b\u00a8\u0006\f"}, d2 = {"Lcom/adaptivefgo/navigator/ui/BaseMapFragment$Companion;", "", "()V", "COLORS", "", "Lcom/adaptivefgo/navigator/data/AlgorithmType;", "", "getCOLORS", "()Ljava/util/Map;", "NAMES", "", "getNAMES", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.Map<com.adaptivefgo.navigator.data.AlgorithmType, java.lang.Integer> getCOLORS() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.Map<com.adaptivefgo.navigator.data.AlgorithmType, java.lang.String> getNAMES() {
            return null;
        }
    }
}