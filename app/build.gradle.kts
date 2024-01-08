import io.grpc.internal.SharedResourceHolder.release
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

val localPropertiesFile: File = rootProject.file("local.properties")
val localProperties =  Properties()
localProperties.load(FileInputStream(localPropertiesFile))

android {
    namespace = "com.company.watchlist"
    compileSdk = 34


    if (project.hasProperty("propertyfile") && project.hasProperty("key.store")) {

        signingConfigs {
            create("release") {
                keyAlias = localProperties["key.alias"] as String
                keyPassword = localProperties["key.alias.password"] as String
                storeFile = project.properties["key.store"]?.let { file(it) }
                storePassword = localProperties["key.store.password"] as String
            }
        }
    }


    defaultConfig {
        applicationId = "com.company.watchlist"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"


        buildConfigField ("String", "TMDB_API_KEY", localProperties.getProperty("TMDB_API_KEY") ) // localProperties['TMDB_API_KEY']  "\"${properties.getProperty('WEB_CLIENT_ID')}\""

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isDebuggable = false
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
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //Lifecycle Compose dependencies
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    implementation("androidx.navigation:navigation-compose:2.7.6")

    //OnBoarding Pager
    implementation ("com.google.accompanist:accompanist-pager:0.28.0")

    //Dagger-Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    ksp ("com.google.dagger:hilt-android-compiler:2.48")
    ksp ("androidx.hilt:hilt-compiler:1.1.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    implementation("androidx.hilt:hilt-navigation:1.1.0")

    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.12.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.1")

    //Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    //Coil
    implementation("io.coil-kt:coil-compose:2.5.0")

    //paging3
    implementation ("androidx.paging:paging-runtime-ktx:3.2.1")
    implementation ("androidx.paging:paging-compose:3.2.1")

    //Room
    implementation("androidx.room:room-ktx:2.6.1")
    ksp ("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-paging:2.6.1")
}