package com.morg.permission

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PermissionActivity : AppCompatActivity() {
    private lateinit var permissionAccess: PermissionAccess
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_permission)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        permissionAccess = PermissionAccess(
            this,
            arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
            object : PermissionAccess.PermissionCallback {
                override fun onPermissionGranted() {
                    Toast.makeText(
                        this@PermissionActivity,
                        "Notification access granted",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            dialogConfig = PermissionAccess.DialogConfig(
                title = "Notification Access",
                message = "This app requires notification access to function properly.",
                positiveButton = "Grant",
                negativeButton = "Cancel",
                deniedTitle = "Notification Access Denied",
                deniedMessage = "Notification access is essential. Please enable it in settings.",
                deniedPositiveButton = "Go to Settings",
                deniedNegativeButton = "Cancel"
            )
        )

        val button: Button = findViewById(R.id.btn_permission_notification)
        button.setOnClickListener {
            permissionAccess.checkAndRequestPermissions() // ✅ Only requests permission here
        }
    }
}