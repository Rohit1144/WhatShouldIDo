plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.fit5046_g4_whatshouldido"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.fit5046_g4_whatshouldido"
        minSdk = 35
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
    implementation(libs.generativeai) // generative ai v0.9.0
    implementation(platform(libs.firebase.bom.v33130)) // Firebase BOM(manages compatible versions)
    implementation(libs.firebase.ml.modeldownloader) // Firebase - ML Downloader Service
    implementation(libs.tensorflow.lite) // Tensorflow - 2.3.0
    implementation(libs.firebase.analytics) // Firebase - Analytics Service
    implementation(libs.firebase.auth.ktx) // Firebase - Authentication Services
    implementation(libs.firebase.firestore.ktx) // Firebase - FireStore DB services
    implementation(libs.retrofit) // Retrofit
    implementation(libs.converter.gson) // Retrofit - GSON converter
    implementation(libs.gson) // Retrofit - Gson
    ksp(libs.androidx.room.compiler) // Room compiler
    implementation(libs.androidx.room.runtime) // Room runtime
    implementation(libs.androidx.room.ktx) // Room ktx?
    implementation(libs.androidx.navigation.compose) // Android navigation
    implementation(libs.androidx.material)  // Android material
    implementation(libs.font.awesome) // Font-Awesome Icons
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
}