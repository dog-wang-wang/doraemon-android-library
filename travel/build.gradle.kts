plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    //为了处理room-compiler的冲突问题
    alias(libs.plugins.ksp)
    // 为了处理room的版本迁移的时候的schema
    alias(libs.plugins.room)
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
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    // 对应插件的生成schema功能
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(project(":foundation"))
    implementation(project(":foundation-ui-compose"))
    implementation(libs.image.compose.coil)
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
