package com.adaptivefgo.navigator.ui.error;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J6\u0010\t\u001a\u0014\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b0\n2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000e0\rH\u0002J\b\u0010\u0010\u001a\u00020\u0011H\u0002J$\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0016J\u001a\u0010\u001a\u001a\u00020\u00112\u0006\u0010\u001b\u001a\u00020\u00132\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0016J\b\u0010\u001c\u001a\u00020\u0011H\u0002J.\u0010\u001d\u001a\u00020\u00112$\u0010\u001e\u001a \u0012\u0004\u0012\u00020 \u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b0\n0\u001fH\u0002J(\u0010!\u001a\u00020\u00112\u001e\u0010\"\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020 \u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\r0#0\rH\u0002J.\u0010$\u001a\u00020\u00112$\u0010\u001e\u001a \u0012\u0004\u0012\u00020 \u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b0\n0\u001fH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2 = {"Lcom/adaptivefgo/navigator/ui/error/ErrorAnalysisFragment;", "Landroidx/fragment/app/Fragment;", "()V", "barChart", "Lcom/github/mikephil/charting/charts/BarChart;", "lineChart", "Lcom/github/mikephil/charting/charts/LineChart;", "tvTable", "Landroid/widget/TextView;", "computeRelativeError", "Lkotlin/Triple;", "", "ref", "", "Lcom/adaptivefgo/navigator/data/TrajectoryPoint;", "target", "observeData", "", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onViewCreated", "view", "setupCharts", "updateBarChart", "metrics", "", "Lcom/adaptivefgo/navigator/data/AlgorithmType;", "updateLineChart", "trajectories", "Lkotlin/Pair;", "updateTable", "app_debug"})
public final class ErrorAnalysisFragment extends androidx.fragment.app.Fragment {
    private com.github.mikephil.charting.charts.BarChart barChart;
    private com.github.mikephil.charting.charts.LineChart lineChart;
    private android.widget.TextView tvTable;
    
    public ErrorAnalysisFragment() {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupCharts() {
    }
    
    private final void observeData() {
    }
    
    private final kotlin.Triple<java.lang.Double, java.lang.Double, java.lang.Double> computeRelativeError(java.util.List<com.adaptivefgo.navigator.data.TrajectoryPoint> ref, java.util.List<com.adaptivefgo.navigator.data.TrajectoryPoint> target) {
        return null;
    }
    
    private final void updateBarChart(java.util.Map<com.adaptivefgo.navigator.data.AlgorithmType, kotlin.Triple<java.lang.Double, java.lang.Double, java.lang.Double>> metrics) {
    }
    
    private final void updateLineChart(java.util.List<? extends kotlin.Pair<? extends com.adaptivefgo.navigator.data.AlgorithmType, ? extends java.util.List<com.adaptivefgo.navigator.data.TrajectoryPoint>>> trajectories) {
    }
    
    private final void updateTable(java.util.Map<com.adaptivefgo.navigator.data.AlgorithmType, kotlin.Triple<java.lang.Double, java.lang.Double, java.lang.Double>> metrics) {
    }
}