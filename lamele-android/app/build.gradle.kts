plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.compose")
}

fun readLocalProperty(name: String, default: String): String {
    val f = rootProject.file("local.properties")
    if (!f.exists()) return default
    for (raw in f.readLines()) {
        val line = raw.trim()
        if (line.isEmpty() || line.startsWith("#")) continue
        val eq = line.indexOf('=')
        if (eq <= 0) continue
        val k = line.substring(0, eq).trim()
        if (k != name) continue
        var v = line.substring(eq + 1).trim()
        if (v.length >= 2 &&
            ((v.startsWith("\"") && v.endsWith("\"")) || (v.startsWith("'") && v.endsWith("'")))
        ) {
            v = v.substring(1, v.length - 1)
        }
        return v
    }
    return default
}

val mimoKey = readLocalProperty("mimo.api.key", "").replace("\"", "\\\"")
val mimoModel = readLocalProperty("mimo.model", "mimo-v2.5-pro").replace("\"", "\\\"")
val mimoBaseUrl =
    readLocalProperty("mimo.base.url", "https://api.xiaomimimo.com/v1").replace("\"", "\\\"")

android {
    namespace = "com.lamele.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.lamele.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "2.0.0-prd"
        buildConfigField("String", "MIMO_API_KEY", "\"$mimoKey\"")
        buildConfigField("String", "MIMO_MODEL", "\"$mimoModel\"")
        buildConfigField("String", "MIMO_BASE_URL", "\"$mimoBaseUrl\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2024.10.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.navigation:navigation-compose:2.8.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")

    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    implementation("androidx.datastore:datastore-preferences:1.1.1")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.osmdroid:osmdroid-android:6.1.18")
    implementation("androidx.preference:preference-ktx:1.2.1")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
