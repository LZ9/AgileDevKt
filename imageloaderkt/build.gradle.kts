plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.vanniktech.maven.publish)
}

android {
    namespace = "com.lodz.android.imageloaderkt"
    compileSdk = 36

    defaultConfig {
        minSdk = 23
        buildConfigField("int", "versionCode", "30")
        buildConfigField("String", "versionName", "\"1.3.0\"") //成功上传

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
    implementation(libs.androidx.annotation)

    // glide
    api(libs.bumptech.glide)
    ksp(libs.bumptech.glide.ksp)
    api(libs.bumptech.glide.okhttp3.integration)
    api(libs.wasabeef.glide.transformations)
}

//----------------------- 发布到 Maven Central  ------------------------------
val PUBLISH_GROUP_ID = "ink.lodz"
val PUBLISH_ARTIFACT_ID = "imageloader-kt"
val PUBLISH_VERSION = "1.3.0"

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()

    coordinates(PUBLISH_GROUP_ID, PUBLISH_ARTIFACT_ID, PUBLISH_VERSION)

    pom {

        name.set(PUBLISH_ARTIFACT_ID)
        description.set("This is a simple image loader.")
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