plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.vanniktech.maven.publish)
}


android {
    namespace = "com.lodz.android.corekt"
    compileSdk = 36

    defaultConfig {
        minSdk = 23
        buildConfigField("int", "versionCode", "58")
        buildConfigField("String", "versionName", "\"2.1.3\"") //成功上传

        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures.buildConfig = true

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }
}

dependencies {
    // 支持库
    api(libs.androidx.appcompat)
    api(libs.google.material)
    api(libs.androidx.swiperefreshlayout)
    api(libs.androidx.recyclerview)
    api(libs.androidx.cardview)
    api(libs.androidx.viewpager2)
    api(libs.androidx.annotation)
    api(libs.androidx.constraintlayout)
    api(libs.androidx.multidex)
    api(libs.androidx.corektx)
    api(libs.androidx.activityKtx)
    api(libs.androidx.fragmentKtx)
    api(libs.androidx.lifecycle.viewmodelKtx)
    api(libs.androidx.lifecycle.livedataKtx)
    api(libs.androidx.lifecycle.runtimeKtx)
    api(libs.androidx.startup)
    api(libs.androidx.datastore)
    api(libs.androidx.documentfile)
    api(libs.androidx.splashscreen)
    api(libs.google.flexbox)
    api(libs.kotlinx.coroutines)
}

//----------------------- 发布到 Maven Central  ------------------------------
val PUBLISH_GROUP_ID = "ink.lodz"
val PUBLISH_ARTIFACT_ID = "core-kt"
val PUBLISH_VERSION = "2.1.3"

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()

    coordinates(PUBLISH_GROUP_ID, PUBLISH_ARTIFACT_ID, PUBLISH_VERSION)

    pom {

        name.set(PUBLISH_ARTIFACT_ID)
        description.set("This is a simple tools lib for android project.")
        inceptionYear.set("2026")
        url.set("https://github.com/LZ9/AgileDevKt")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("Lodz")
                name.set("Lodz")
                email.set("lz.mms@foxmail.com")
                url.set("https://github.com/LZ9")
            }
        }
        scm {
            url.set("https://github.com/LZ9/AgileDevKt/tree/master")
            connection.set("scm:git:github.com/LZ9/AgileDevKt.git")
            developerConnection.set("scm:git:ssh://github.com/LZ9/AgileDevKt.git")
        }
    }
}