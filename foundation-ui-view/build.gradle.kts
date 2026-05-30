plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = DoraFoundationViewVersion.NAME_SPACE
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
    compileOnly(project(":foundation"))
    // androidx传递
    api(libs.bundles.androidx)
}