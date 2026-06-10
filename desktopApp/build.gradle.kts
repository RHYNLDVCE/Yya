import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

dependencies {
    implementation(projects.shared)

    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutinesSwing)
    implementation("ch.qos.logback:logback-classic:1.4.14")

    implementation(libs.compose.uiToolingPreview)
    implementation(libs.koin.core)
}

compose.desktop {
    application {
        mainClass = "com.muslima.yya.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.muslima.yya"
            packageVersion = "1.0.0"
        }
    }
}