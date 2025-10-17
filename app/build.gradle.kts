plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "jp.co.zaico.codingtest"
    compileSdk = 35

    defaultConfig {
        applicationId = "jp.co.zaico.codingtest"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    implementation(platform(libs.kotlin.bom)) // 1.9.24 にロック

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Retrofit / OkHttp
    implementation(libs.retrofit)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    // kotlinx-serialization（1.6.3固定を使用中ならOK）
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)

    // Retrofit × kotlinx-serialization converter
    implementation(libs.retrofit.kotlinx.serialization)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Compose
    implementation(libs.androidx.activity)           // Activity KTX
    implementation(libs.androidx.activity.compose)   // Compose 用 Activity
    implementation(libs.androidx.ui)
    implementation(libs.androidx.foundation.layout)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.material3)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.tooling.preview)

    // Ktor（統一版）
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    // serialization（1.6.3固定）
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)

    // Coroutines（1.7.3に一本化）
    implementation(libs.kotlinx.coroutines.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // unit test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
    testImplementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))
    testImplementation("com.squareup.okhttp3:mockwebserver")

    // androidTest
    androidTestImplementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))
    androidTestImplementation("com.squareup.okhttp3:mockwebserver")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")

    // 最終ロック（念のため）
    constraints {
        implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.24")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-common:1.9.24")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.24")
        implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.24")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.6.3")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    }
    testImplementation(kotlin("test"))
}

configurations.all {
    resolutionStrategy.force(
        "org.jetbrains.kotlinx:kotlinx-serialization-core:1.6.3",
        "org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3"
    )
}

hilt {
    enableAggregatingTask = false
}
