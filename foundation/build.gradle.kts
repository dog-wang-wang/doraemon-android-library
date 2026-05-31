plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = DoraFoundationVersion.NAME_SPACE
    compileSdk {
        version = release(ModuleVersion.SDK_VERSION_COMPILE) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = ModuleVersion.SDK_VERSION_MIN

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // log传递
    api(project(":log"))
    // gson传递
    api(libs.gson)
    // androidx的core
    api(libs.androidx.core.ktx)
    api(libs.androidx.activity.ktx)
    // lifecycle
    implementation(libs.bundles.lifecycle)
    // MMKV不透传
    implementation(libs.mmkv)
}