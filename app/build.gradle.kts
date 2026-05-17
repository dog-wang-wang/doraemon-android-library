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
    // 引用本地模块
    implementation(project(":foundation"))
    implementation(project(":net"))
    // compose的相关依赖
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    implementation(libs.bundles.composeBundle)
    // 测试依赖
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}