plugins {
    id("com.android.application")
}

android {
    namespace = "com.paaniglass.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.paaniglass.app"
        minSdk = 26
        targetSdk = 36          // Google Play ki latest target API requirement
        versionCode = 1
        versionName = "1.0"
    }

    // Release signing: keystore ki details environment variables se aati hain
    // (GitHub Secrets ya apne PC par). Password kabhi code mein na likhein.
    val ksPath = System.getenv("PAANI_KEYSTORE_PATH")
    signingConfigs {
        if (ksPath != null && file(ksPath).exists()) {
            create("upload") {
                storeFile = file(ksPath)
                storePassword = System.getenv("PAANI_KEYSTORE_PASSWORD")
                keyAlias = System.getenv("PAANI_KEY_ALIAS")
                keyPassword = System.getenv("PAANI_KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        release {
            signingConfigs.findByName("upload")?.let { signingConfig = it }
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("androidx.activity:activity:1.10.1")
    implementation("com.google.android.material:material:1.12.0")
}
