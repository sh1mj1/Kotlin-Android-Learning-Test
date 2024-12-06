plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.learningtest"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.learningtest"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    packaging {
        resources {
            excludes += "META-INF/**"
            excludes += "win32-x86*/**"
        }
    }
    buildFeatures {
        compose = true
    }
}

composeCompiler {
    enableStrongSkippingMode = true
}

dependencies {
    implementation(libs.bundles.androidx)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.bundles.androidTest)

    implementation(libs.kotlin.reflect)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    testImplementation(libs.mockwebserver)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
