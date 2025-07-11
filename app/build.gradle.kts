plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    id("com.google.gms.google-services")
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
        val geminiApiKey = project.rootProject.file("local.properties")
            .readLines()
            .find { it.startsWith("gemini_api_key") }
            ?.split("=")?.get(1)?.trim() ?: ""

        buildConfigField("String", "GEMINI_API_KEY", "\"$geminiApiKey\"")

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
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "META-INF/versions/9/OSGI-INF/MANIFEST.MF"
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1" // or higher
    }
}

dependencies {
    implementation(libs.tasks.genai)
    implementation("androidx.compose.ui:ui:1.7.0")
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("dev.shreyaspatil.generativeai:generativeai-google:0.9.0-1.1.0")
    implementation(libs.androidx.material3.v121)
    implementation (libs.mpandroidchart)
    implementation (libs.androidx.compose.ui.ui)
    implementation (libs.material3)
    implementation (libs.androidx.navigation.compose.v271)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.generativeai) // generative ai v0.9.0
    implementation(platform(libs.firebase.bom.v33130)) // Firebase BOM(manages compatible versions)
    implementation(libs.firebase.ml.modeldownloader) // Firebase - ML Downloader Service
    implementation(libs.tensorflow.lite) // Tensorflow - 2.3.0
    implementation(libs.firebase.analytics) // Firebase - Analytics Service
    implementation(libs.firebase.auth) // Firebase - Authentication Services
    implementation(libs.firebase.firestore) // Firebase - FireStore DB services
    val credentialManagerVersion = "1.5.0"
    implementation("androidx.credentials:credentials:$credentialManagerVersion")
    implementation("androidx.credentials:credentials-play-services-auth:$credentialManagerVersion")
    implementation(libs.googleid)
    implementation(libs.play.services.auth)
    implementation(libs.retrofit) // Retrofit
    implementation(libs.converter.gson) // Retrofit - GSON converter
    implementation(libs.gson)
    implementation(libs.identity.jvm) // Retrofit - Gson
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