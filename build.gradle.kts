plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    //https://github.com/google/ksp/releases
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    //https://github.com/vanniktech/gradle-maven-publish-plugin/
    alias(libs.plugins.vanniktech.maven.publish) apply false
}