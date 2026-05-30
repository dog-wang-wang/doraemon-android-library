plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = DoraTravelVersion.NAME_SPACE
    compileSdk {
        version = release(ModuleVersion.SDK_VERSION_COMPILE) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = DoraTravelVersion.APPLICATION_ID
        minSdk = ModuleVersion.SDK_VERSION_MIN
        targetSdk = ModuleVersion.SDK_VERSION_TARGET
        versionCode = DoraTravelVersion.VERSION_CODE
        versionName = DoraTravelVersion.VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    debugImplementation(libs.androidx.compose.ui.tooling)
}