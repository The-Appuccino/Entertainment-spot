import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.0"

    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")

    id("kotlin-kapt")
}

// Load api.properties
val apiPropertiesFile = rootProject.file("local.properties")
val apiProperties = Properties()
apiProperties.load(FileInputStream(apiPropertiesFile))

android {
    namespace = "com.appuccino.entertainment_spot"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.appuccino.entertainment_spot"
        minSdk = 27
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Inject API keys
        buildConfigField("String", "TMDB_API_KEY", apiProperties["TMDB_API_KEY"].toString())
        buildConfigField("String", "WATCHMODE_API_KEY", apiProperties["WATCHMODE_API_KEY"].toString())
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildToolsVersion = "35.0.1"
}

dependencies {

    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation ("androidx.media3:media3-exoplayer:1.9.0")
    implementation ("androidx.media3:media3-ui:1.9.0")
    implementation("androidx.navigation:navigation-fragment:2.9.6")

    //for JSON parsing
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:34.7.0"))

    implementation("com.google.firebase:firebase-firestore")

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")


    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.okhttp3:okhttp:5.3.2")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

    implementation("com.github.bumptech.glide:glide:5.0.5")
    kapt("com.github.bumptech.glide:compiler:5.0.5")

    implementation ("com.pierfrancescosoffritti.androidyoutubeplayer:core:13.0.0")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
}