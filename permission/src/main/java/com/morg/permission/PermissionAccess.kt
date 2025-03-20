package com.morg.permission

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class PermissionAccess(
    private val activity: AppCompatActivity,
    private val permissions: Array<String>,
    private val callback: PermissionCallback,
    private val dialogConfig: DialogConfig = DialogConfig()
) {
    data class DialogConfig(
        val title: String = "Permissions Required",
        val message: String = "This app requires the following permissions:",
        val positiveButton: String = "Grant",
        val negativeButton: String = "Cancel",
        val deniedTitle: String = "Permission Denied",
        val deniedMessage: String = "Some permissions are essential. Please enable them in settings.",
        val deniedPositiveButton: String = "Go to Settings",
        val deniedNegativeButton: String = "Cancel"
    )

    private var requestPermissionLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        if (results.all { it.value }) {
            callback.onPermissionGranted()
        } else {
            showPermissionDeniedDialog()
        }
    }

    private val permissionNames = mutableMapOf(
        android.Manifest.permission.CAMERA to "Camera",
        android.Manifest.permission.RECORD_AUDIO to "Microphone",
        android.Manifest.permission.READ_CONTACTS to "Read Contacts",
        android.Manifest.permission.WRITE_CONTACTS to "Write Contacts",
        android.Manifest.permission.GET_ACCOUNTS to "Get Accounts",
        android.Manifest.permission.ACCESS_FINE_LOCATION to "Fine Location",
        android.Manifest.permission.ACCESS_COARSE_LOCATION to "Coarse Location",
        android.Manifest.permission.READ_EXTERNAL_STORAGE to "Read External Storage",
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE to "Write External Storage",
        android.Manifest.permission.BLUETOOTH to "Bluetooth",
        android.Manifest.permission.BLUETOOTH_ADMIN to "Bluetooth Admin",
        android.Manifest.permission.NFC to "NFC",
        android.Manifest.permission.INTERNET to "Internet",
        android.Manifest.permission.ACCESS_NETWORK_STATE to "Access Network State",
        android.Manifest.permission.ACCESS_WIFI_STATE to "Access WiFi State",
        android.Manifest.permission.CHANGE_WIFI_STATE to "Change WiFi State",
        android.Manifest.permission.SEND_SMS to "Send SMS",
        android.Manifest.permission.RECEIVE_SMS to "Receive SMS",
        android.Manifest.permission.READ_SMS to "Read SMS",
        android.Manifest.permission.READ_PHONE_STATE to "Read Phone State",
        android.Manifest.permission.CALL_PHONE to "Call Phone",
        android.Manifest.permission.READ_CALL_LOG to "Read Call Log",
        android.Manifest.permission.BODY_SENSORS to "Body Sensors"
    )

    init {
        // Android 10+ (Q)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissionNames[android.Manifest.permission.ACCESS_BACKGROUND_LOCATION] =
                "Background Location"
            permissionNames[android.Manifest.permission.ACTIVITY_RECOGNITION] =
                "Activity Recognition"
        }
        // Android 12+ (S)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionNames[android.Manifest.permission.BLUETOOTH_CONNECT] = "Bluetooth Connect"
            permissionNames[android.Manifest.permission.BLUETOOTH_SCAN] = "Bluetooth Scan"
        }
        // Android 13+ (T)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionNames[android.Manifest.permission.READ_MEDIA_IMAGES] = "Read Media Images"
            permissionNames[android.Manifest.permission.READ_MEDIA_VIDEO] = "Read Media Video"
            permissionNames[android.Manifest.permission.READ_MEDIA_AUDIO] = "Read Media Audio"
            permissionNames[android.Manifest.permission.POST_NOTIFICATIONS] = "Post Notifications"
        }
        // Android 14+ (UpsideDownCake)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            permissionNames[android.Manifest.permission.FOREGROUND_SERVICE] = "Foreground Service"
            permissionNames[android.Manifest.permission.FOREGROUND_SERVICE_MEDIA_PROJECTION] =
                "Foreground Service Media Projection"
            permissionNames[android.Manifest.permission.FOREGROUND_SERVICE_LOCATION] =
                "Foreground Service Location"
        }
    }


    fun registerPermissionLauncher() {
        requestPermissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { results ->
            if (results.all { it.value }) {
                callback.onPermissionGranted()
            } else {
                showPermissionDeniedDialog()
            }
        }
    }

    fun checkAndRequestPermissions() {
        val filteredPermissions = permissions.filter { isPermissionSupported(it) }.toTypedArray()

        val deniedPermissions = filteredPermissions.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }

        if (deniedPermissions.isEmpty()) {
            callback.onPermissionGranted()
        } else {
            if (deniedPermissions.any { activity.shouldShowRequestPermissionRationale(it) }) {
                showPermissionRationaleDialog(deniedPermissions)
            } else {
                requestPermissionLauncher.launch(deniedPermissions.toTypedArray())
            }
        }
    }

    private fun isPermissionSupported(permission: String): Boolean {
        return when (permission) {
            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION -> Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
            android.Manifest.permission.ACTIVITY_RECOGNITION -> Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
            android.Manifest.permission.READ_MEDIA_IMAGES,
            android.Manifest.permission.READ_MEDIA_VIDEO,
            android.Manifest.permission.READ_MEDIA_AUDIO -> Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

            android.Manifest.permission.BLUETOOTH_CONNECT,
            android.Manifest.permission.BLUETOOTH_SCAN -> Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

            else -> true
        }
    }

    private fun showPermissionRationaleDialog(deniedPermissions: List<String>) {
        val permissionList = deniedPermissions.joinToString("\n") { permissionNames[it] ?: it }
        AlertDialog.Builder(activity)
            .setTitle(dialogConfig.title)
            .setMessage("${dialogConfig.message}\n\n$permissionList")
            .setPositiveButton(dialogConfig.positiveButton) { _, _ ->
                requestPermissionLauncher.launch(deniedPermissions.toTypedArray()) // ✅ FIXED
            }
            .setNegativeButton(dialogConfig.negativeButton) { _, _ ->
                showPermissionDeniedDialog()
            }
            .setCancelable(false)
            .show()
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(activity)
            .setTitle(dialogConfig.deniedTitle)
            .setMessage(dialogConfig.deniedMessage)
            .setPositiveButton(dialogConfig.deniedPositiveButton) { _, _ ->
                openAppSettings(activity)
            }
            .setNegativeButton(dialogConfig.deniedNegativeButton, null)
            .setCancelable(false)
            .show()
    }

    private fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent)
    }

    interface PermissionCallback {
        fun onPermissionGranted()
    }
}