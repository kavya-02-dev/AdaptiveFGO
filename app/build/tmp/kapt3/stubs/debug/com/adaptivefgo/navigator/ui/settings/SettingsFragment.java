package com.adaptivefgo.navigator.ui.settings;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0015\u001a\u00020\u0016H\u0002J\b\u0010\u0017\u001a\u00020\u0018H\u0002J$\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u0016J\u001a\u0010!\u001a\u00020\u00182\u0006\u0010\"\u001a\u00020\u001a2\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u0016J\b\u0010#\u001a\u00020\u0018H\u0002J\u0010\u0010$\u001a\u00020\u00182\u0006\u0010%\u001a\u00020\u0016H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006&"}, d2 = {"Lcom/adaptivefgo/navigator/ui/settings/SettingsFragment;", "Landroidx/fragment/app/Fragment;", "()V", "btnApply", "Landroid/widget/Button;", "btnReset", "btnResetTrajectory", "sbAdaptiveSens", "Landroid/widget/SeekBar;", "sbGnssNoise", "sbHeadingNoise", "sbMaxWindow", "sbMinWindow", "sbStepLengthError", "tvAdaptiveSens", "Landroid/widget/TextView;", "tvGnssNoise", "tvHeadingNoise", "tvMaxWindow", "tvMinWindow", "tvStepLengthError", "buildSettings", "Lcom/adaptivefgo/navigator/data/AlgorithmSettings;", "loadCurrentSettings", "", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onViewCreated", "view", "setupListeners", "updateLabels", "s", "app_debug"})
public final class SettingsFragment extends androidx.fragment.app.Fragment {
    private android.widget.TextView tvStepLengthError;
    private android.widget.SeekBar sbStepLengthError;
    private android.widget.TextView tvHeadingNoise;
    private android.widget.SeekBar sbHeadingNoise;
    private android.widget.TextView tvGnssNoise;
    private android.widget.SeekBar sbGnssNoise;
    private android.widget.TextView tvMinWindow;
    private android.widget.SeekBar sbMinWindow;
    private android.widget.TextView tvMaxWindow;
    private android.widget.SeekBar sbMaxWindow;
    private android.widget.TextView tvAdaptiveSens;
    private android.widget.SeekBar sbAdaptiveSens;
    private android.widget.Button btnApply;
    private android.widget.Button btnReset;
    private android.widget.Button btnResetTrajectory;
    
    public SettingsFragment() {
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
    
    private final void loadCurrentSettings() {
    }
    
    private final void setupListeners() {
    }
    
    private final com.adaptivefgo.navigator.data.AlgorithmSettings buildSettings() {
        return null;
    }
    
    private final void updateLabels(com.adaptivefgo.navigator.data.AlgorithmSettings s) {
    }
}