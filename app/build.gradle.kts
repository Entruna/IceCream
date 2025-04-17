plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.icecream"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.icecream"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.example.icecream.CustomHiltTestRunner"
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
    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.material3)

    // Debug & UI test tooling
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Image loading
    implementation(libs.coil.compose)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.compiler)
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.compiler.android.test)

    // Networking
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.rxjava2)
    implementation(libs.androidx.room.rxjava3)
    implementation(libs.androidx.room.guava)
    implementation(libs.androidx.room.paging)
    ksp(libs.androidx.room.compiler)
    testImplementation(libs.androidx.room.testing)

    // Tests
    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.junit.v115)
    androidTestImplementation(libs.androidx.core)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.rules)
}

kapt {
    correctErrorTypes = true
}
