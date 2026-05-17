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
    // androidx传递
    api(libs.bundles.androidx)
    // material传递
    api(libs.material)
    // lifecycle
    implementation(libs.bundles.lifecycle)
    // MMKV不透传
    implementation(libs.mmkv)
    // 测试依赖
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}