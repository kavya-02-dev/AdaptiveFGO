package com.adaptivefgo.navigator;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000f\u001a\u00020\u0010H\u0002J\b\u0010\u0011\u001a\u00020\u0010H\u0002J\u0012\u0010\u0012\u001a\u00020\u00102\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0014J\b\u0010\u0015\u001a\u00020\u0010H\u0014J\u0010\u0010\u0016\u001a\u00020\u00102\u0006\u0010\u0017\u001a\u00020\u0005H\u0002J\b\u0010\u0018\u001a\u00020\u0010H\u0002J\b\u0010\u0019\u001a\u00020\u0010H\u0002J\b\u0010\u001a\u001a\u00020\u0010H\u0002J\b\u0010\u001b\u001a\u00020\u0010H\u0002R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00050\nX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\fR\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/adaptivefgo/navigator/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "PAGE_TITLES", "", "", "binding", "Lcom/adaptivefgo/navigator/databinding/ActivityMainBinding;", "permissionsLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "", "requiredPermissions", "[Ljava/lang/String;", "viewPager", "Landroidx/viewpager2/widget/ViewPager2;", "checkPermissionsAndStart", "", "observeServiceState", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "sendServiceCommand", "action", "setupServiceButton", "setupViewPager", "startLocalizationService", "stopLocalizationService", "app_debug"})
public final class MainActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.adaptivefgo.navigator.databinding.ActivityMainBinding binding;
    private androidx.viewpager2.widget.ViewPager2 viewPager;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> PAGE_TITLES = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String[] requiredPermissions = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String[]> permissionsLauncher = null;
    
    public MainActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupViewPager() {
    }
    
    private final void setupServiceButton() {
    }
    
    private final void observeServiceState() {
    }
    
    private final void checkPermissionsAndStart() {
    }
    
    private final void startLocalizationService() {
    }
    
    private final void stopLocalizationService() {
    }
    
    private final void sendServiceCommand(java.lang.String action) {
    }
    
    @java.lang.Override()
    protected void onDestroy() {
    }
}