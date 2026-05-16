package com.adaptivefgo.navigator;

/**
 * Permissions onboarding screen shown if location permissions are denied.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\bH\u0002J\u0012\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0014J\b\u0010\r\u001a\u00020\nH\u0002J\b\u0010\u000e\u001a\u00020\nH\u0002J\b\u0010\u000f\u001a\u00020\nH\u0002J\b\u0010\u0010\u001a\u00020\nH\u0002R\u001a\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/adaptivefgo/navigator/PermissionsActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "permissionsLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "", "", "hasRequiredPermissions", "", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "openAppSettings", "requestPermissions", "showRationale", "startMain", "app_debug"})
public final class PermissionsActivity extends androidx.appcompat.app.AppCompatActivity {
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String[]> permissionsLauncher = null;
    
    public PermissionsActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final boolean hasRequiredPermissions() {
        return false;
    }
    
    private final void requestPermissions() {
    }
    
    private final void showRationale() {
    }
    
    private final void openAppSettings() {
    }
    
    private final void startMain() {
    }
}