plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = DoraShareVersion.NAME_SPACE
    compileSdk {
        version = release(ModuleVersion.SDK_VERSION_COMPILE) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = DoraShareVersion.APPLICATION_ID
        minSdk = ModuleVersion.SDK_VERSION_MIN
        targetSdk = ModuleVersion.SDK_VERSION_TARGET
        versionCode = DoraShareVersion.VERSION_CODE
        versionName = DoraShareVersion.VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    flavorDimensions += "channel"

    productFlavors {
        create(LibraryInfo.VARIANT_INNER) {
            dimension = "channel"
        }
        create(LibraryInfo.VARIANT_VIVO) {
            dimension = "channel"
        }
        create(LibraryInfo.VARIANT_OPPO) {
            dimension = "channel"
        }
        create(LibraryInfo.VARIANT_XIAOMI) {
            dimension = "channel"
        }
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
        dataBinding = true
        compose = true
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
}

dependencies {
    implementation(project(":foundation"))

    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}