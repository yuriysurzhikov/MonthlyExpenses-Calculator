import java.util.Properties
import java.io.FileInputStream

plugins {
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.yuriisurzhykov.expensescalculator"
    compileSdk = Configs.CompileSDKVersion

    defaultConfig {
        applicationId = "com.yuriisurzhykov.expensescalculator"
        minSdk = Configs.MinSDKVersion
        targetSdk = Configs.TargetSDKVersion
        versionCode = getBuildVersionCode()
        versionName = getBuildVersionName()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            val properties = Properties()
            FileInputStream(file("signing.properties")).use { stream ->
                properties.load(stream)
            }
            storeFile = file(properties.getProperty("keystoreFile"))
            storePassword = properties.getProperty("keystorePassword").toString()
            keyAlias = properties.getProperty("keyAlias").toString()
            keyPassword = properties.getProperty("keyPassword").toString()
        }
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(AndroidDeps.AndroidCoreX)
    implementation(AndroidDeps.AndroidXCompat)
    implementation(AndroidDeps.Material)
    implementation(AndroidDeps.ConstraintLayout)
    testImplementation(TestingDeps.JUnit4)
    androidTestImplementation(TestingDeps.AndroidJUnit)
    androidTestImplementation(TestingDeps.EspressoCore)
}

fun getBuildVersionName(): String {
    return try {
        System.getenv("BUILD_VERSION_NAME")?.toString() ?: "1.0"
    } catch (e: Exception) {
        println("Failed get version name: ${e.message}")
        "1.0"
    }
}

fun getBuildVersionCode(): Int {
    return try {
        System.getenv("BUILD_VERSION_CODE")?.toInt() ?: 1
    } catch (e: Exception) {
        println("Failed get version name: ${e.message}")
        1
    }
}