import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.lodz.android.agiledevkt"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.lodz.android.agiledevkt"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true// 启用dex分包
//        ndk { abiFilters += listOf("armeabi", "armeabi-v7a", "arm64-v8a", "x86", "x86_64") }
    }

    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    val LOG_DEBUG = "LOG_DEBUG"
    val FILE_AUTHORITY = "FILE_AUTHORITY"
    val fileAuthorityValue = "com.lodz.android.agiledevkt.fileprovider"
    defaultConfig {
        buildConfigField("boolean", LOG_DEBUG, "true")
        buildConfigField("String", FILE_AUTHORITY, "\"$fileAuthorityValue\"")
    }

    buildTypes {
        debug {
            isMinifyEnabled = false // 是否进行混淆
            isDebuggable = true // 是否支持调试
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            manifestPlaceholders["FILE_AUTHORITY"] = fileAuthorityValue
        }

        release {
            buildConfigField("boolean", LOG_DEBUG, "false")
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            isJniDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            manifestPlaceholders["FILE_AUTHORITY"] = fileAuthorityValue
        }
    }
}

androidComponents{
    onVariants{variant ->
        variant.outputs.forEach{ output ->
            val formattedDate = SimpleDateFormat("yyyyMMddHHmm").format(Date())
            val versionName = output.versionName.orNull ?: "1.0"
            output.outputFileName.set("AgileDevKt-v${versionName}-${variant.name}-${formattedDate}.apk")
        }
    }
}

dependencies {
    implementation(project(":pandora"))
    implementation(project(":imageloaderkt"))

    // 权限申请
    implementation(libs.permissions.dispatcher)
    // 腾讯定位SDK
    implementation(files("libs/TencentLocationSdk_v7.1.4.3_r4aa8ac63_20180911_165428.jar"))
    // 高德定位SDK
    implementation(libs.amap.location)
    // 大图加载缩放
    implementation(libs.davemorrissey.scaleimage)
    // PhotoView
    implementation(libs.chrisbanes.photoview)
    // 滚动选择
    implementation(files("libs/Android-PickerView-4.1.9.aar"))
    implementation(files("libs/wheelview-4.1.0.aar"))
    // RxDownload4
    implementation(libs.ssseasonnn.rxdownload)
    // okio
    implementation(libs.squareup.okio)
    // jsbridge
    implementation(libs.lodz.jsbridge)
    // 动画
    implementation(libs.airbnb.lottie)
}