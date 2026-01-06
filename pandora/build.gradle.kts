plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.vanniktech.maven.publish)
}


android {
    namespace = "com.lodz.android.pandora"
    compileSdk = 36

    defaultConfig {
        minSdk = 23
        buildConfigField("int", "versionCode", "75")
        buildConfigField("String", "versionName", "\"2.1.7\"") //成功上传

        consumerProguardFiles("consumer-rules.pro")
    }

    viewBinding {
        enable = true
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
//    api(libs.lodz.core)
    api(project(":corekt"))
    // RxJava
    api(libs.reactivex.rxjava3)
    // Retrofit
    api(libs.squareup.retrofit2.retrofit)
    api(libs.squareup.retrofit2.adapter.rxjava3)
    api(libs.squareup.retrofit2.converter.jackson)
    api(libs.fasterxml.jackson)
    // RxJava生命周期
    api(libs.trello.rxlifecycle4)
    // EventBus
    api(libs.greenrobot.eventbus)
}

//----------------------- 发布到 Maven Central  ------------------------------
val PUBLISH_GROUP_ID = "ink.lodz"
val PUBLISH_ARTIFACT_ID = "pandora"
val PUBLISH_VERSION = "2.1.7"

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()

    coordinates(PUBLISH_GROUP_ID, PUBLISH_ARTIFACT_ID, PUBLISH_VERSION)

    pom {

        name.set(PUBLISH_ARTIFACT_ID)
        description.set("This is a android base component library.")
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