plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    //added
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")

}

android {
    namespace = "com.nickdieda.smartstudy"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.nickdieda.smartstudy"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled=true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }


}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.1")

    //compose destination
    val destinationVersion="1.9.63"
    implementation("io.github.raamcosta.compose-destinations:core:$destinationVersion")
    ksp("io.github.raamcosta.compose-destinations:ksp:$destinationVersion")


    //room
    val roomVersion="2.7.2"
    implementation("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    //Dagger-Hilt
    val dagger ="2.50"
    implementation("com.google.dagger:hilt-android:$dagger")
    ksp("com.google.dagger:hilt-android-compiler:$dagger")
    ksp("androidx.hilt:hilt-compiler:1.2.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    //fonts
    implementation("androidx.compose.ui:ui-text-google-fonts:1.8.3")


    //Desugaring
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")

}