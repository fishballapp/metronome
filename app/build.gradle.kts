plugins {
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
  namespace = "com.wearda.metronome"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.wearda.metronome"
    minSdk = 30
    targetSdk = 33
    versionCode = 8
    versionName = "0.4.1"
    vectorDrawables {
      useSupportLibrary = true
    }

  }

  buildTypes {
    release {
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.1"
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

dependencies {
  implementation(libs.play.services.wearable)
  implementation(platform(libs.compose.bom))
  implementation(libs.ui)
  implementation(libs.ui.tooling.preview)
  implementation(libs.compose.material)
  implementation(libs.compose.foundation)
  implementation(libs.activity.compose)
  implementation(libs.core.splashscreen)
  implementation(libs.wear.tooling.preview)
  implementation(libs.compose.navigation)
  implementation(libs.material.icons.extended)
  implementation(libs.horologist.compose.layout)
  androidTestImplementation(platform(libs.compose.bom))
  androidTestImplementation(libs.ui.test.junit4)
  debugImplementation(libs.ui.tooling)
  debugImplementation(libs.ui.test.manifest)
}