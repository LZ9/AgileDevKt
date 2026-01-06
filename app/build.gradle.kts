import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.lodz.android.agiledevkt"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.lodz.android.agiledevkt"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true// 启用dex分包
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        ndk { abiFilters += listOf("armeabi", "armeabi-v7a", "arm64-v8a", "x86", "x86_64") }
    }

    viewBinding {
        enable = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    // 程序在编译的时候会检查lint，有任何错误提示会停止build，我们可以关闭这个开关
    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }

    buildFeatures {
        buildConfig = true
    }

    val LOG_DEBUG = "LOG_DEBUG"
    val FILE_AUTHORITY = "FILE_AUTHORITY"
    defaultConfig {
        buildConfigField("boolean", LOG_DEBUG, "true")
        buildConfigField("String", FILE_AUTHORITY, "\"com.lodz.android.agiledevkt.fileprovider\"")
    }

    buildTypes {
        debug {
            isMinifyEnabled = false // 是否进行混淆
            isDebuggable = true // 是否支持调试
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            manifestPlaceholders["FILE_AUTHORITY"] = defaultConfig.buildConfigFields[FILE_AUTHORITY]?.value?.replace("\"", "") ?: ""
        }

        release {
            buildConfigField("boolean", LOG_DEBUG, "false")
            isMinifyEnabled = true
            isDebuggable = false
            isJniDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            manifestPlaceholders["FILE_AUTHORITY"] = defaultConfig.buildConfigFields[FILE_AUTHORITY]?.value?.replace("\"", "") ?: ""
        }
    }

    applicationVariants.all {
        outputs.all {
            val formattedDate = SimpleDateFormat("yyyyMMddHHmm").format(Date())
            val out = this
            if (out is BaseVariantOutputImpl){
                out.outputFileName = "AgileDevKt-v${defaultConfig.versionName}-${name}-${formattedDate}.apk"
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

        // 单元测试
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
    }
}