# 📱 Android Permission Access (SDK 24 - 36)

![GitHub](https://img.shields.io/badge/Android%20SDK-24%20to%2036-green)
![Kotlin](https://img.shields.io/badge/Kotlin-100%25-blue)
![License](https://img.shields.io/badge/License-MIT-orange)

A **dynamic and easy-to-use** Android **runtime permission access** built with **Kotlin**.  
It supports **all permissions from SDK 24 (Nougat) to SDK 36** and ensures **smooth user experience
** with **custom dialogs, rationale handling, and API-safe permission requests**.

---

## 🌟 **Features**

✔ **Supports all Android runtime permissions dynamically**  
✔ **Automatically handles API differences** (e.g., **Tiramisu+ media permissions, Q+ background
location**)  
✔ **Optimized performance with lazy initialization**  
✔ **Shows custom rationale dialogs for denied permissions**  
✔ **Auto-redirects to Settings if permissions are permanently denied**  
✔ **Lightweight and easy to integrate**

---

## 🚀 **Installation**

To add this library to your Android project, include the following dependency in your build.gradle:
```gradle
dependencies {
    implementation 'com.github.HieronimusMorgan:dynamic-android-permission:<latest-version>'
}
```
Make sure you have JitPack enabled in your settings.gradle or build.gradle:
```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
---

## 📖 **Usage**

### **1️⃣ Initialize Permission Access**

In your `Activity` Kotlin:

```kotlin
private lateinit var permission: PermissionAccess

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val requiredPermissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.RECORD_AUDIO
    )

    val customDialogConfig = PermissionAccess.DialogConfig(
        title = "Required Permissions",
        message = "To continue, the app needs these permissions:",
        positiveButton = "Allow",
        negativeButton = "Deny",
        deniedTitle = "Permissions Not Granted",
        deniedMessage = "You must enable these permissions in settings to use the app.",
        deniedPositiveButton = "Open Settings",
        deniedNegativeButton = "Close"
    )

    permissionAccess = PermissionAccess(
        this,
        requiredPermissions,
        object : PermissionAccess.PermissionCallback {
            override fun onPermissionGranted() {
                Toast.makeText(
                    this@PermissionActivity,
                    "Permission access granted",
                    Toast.LENGTH_SHORT
                ).show()
            }
        },
        customDialogConfig
    )

    findViewById<Button>(R.id.requestPermissionButton).setOnClickListener {
        permissionAccess.checkAndRequestPermissions()
    }
}
```

In your `Activity` Java:

```java
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
```

---

## 🔧 **How It Works**

1. **Automatically requests only valid permissions** (based on API version).
2. **If denied**, shows a **custom rationale dialog** explaining why the permission is needed.
3. **If still denied**, redirects the user to the **app settings** to manually enable permissions.
4. **Once granted**, executes the provided callback (`PermissionCallback`).

---

## 🎯 **Supported Permissions by Android Version**

| API Level            | Permissions Supported                                              |
|----------------------|--------------------------------------------------------------------|
| **24+** (Nougat)     | Camera, Location, Microphone, Contacts, SMS, Phone, Bluetooth, NFC |
| **29+** (Q)          | Background Location, Activity Recognition                          |
| **31+** (S)          | Bluetooth Connect, Bluetooth Scan                                  |
| **33+** (Tiramisu)   | Read Media Images, Video, Audio, Post Notifications                |
| **34+** (Android 14) | Foreground Service, Foreground Service Media Projection            |

---

## 📜 **License**

This project is licensed under the **MIT License**.  
Feel free to use, modify, and contribute! 🚀

---

## 👥 **Contributing**

1. Fork this repo 🍴
2. Create a new branch (`feature-xyz`) 🌿
3. Make your changes and commit (`git commit -m "Added feature XYZ"`) 📝
4. Push (`git push origin feature-xyz`) 🚀
5. Open a **Pull Request** 📩

---

## 💬 **Questions / Issues?**

Feel free to **open an issue** or reach out
via [GitHub Issues](https://github.com/HieronimusMorgan/dynamic-android-permission/issues).

Happy Coding! 🚀  
