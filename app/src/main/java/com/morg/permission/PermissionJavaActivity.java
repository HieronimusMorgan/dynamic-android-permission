package com.morg.permission;

import android.Manifest;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PermissionJavaActivity extends AppCompatActivity {
    private PermissionAccess permissionAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_permission_java);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button button = findViewById(R.id.btn_permission_notification);

        permissionAccess = new PermissionAccess(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                () -> Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show(),
                new PermissionAccess.DialogConfig(
                        "Notification Access",
                        "This app requires notification access to function properly.",
                        "Grant",
                        "Cancel",
                        "Notification Access Denied",
                        "Notification access is essential. Please enable it in settings.",
                        "Go to Settings",
                        "Cancel"
                )
        );

        button.setOnClickListener(v -> {
            permissionAccess.registerPermissionLauncher();
        });
    }
}