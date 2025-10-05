plugins {
    java
}

group = "core"
version = "1.0-SNAPSHOT"

val lwjglVersion = "3.3.6"
val jomlVersion = "1.10.8"
val lwjglNatives = "natives-windows"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("org.lwjgl:lwjgl")
    implementation("org.lwjgl:lwjgl-opengl")
    implementation("org.lwjgl:lwjgl-stb")

    implementation ("org.lwjgl:lwjgl:$lwjglNatives")
    implementation ("org.lwjgl:lwjgl-opengl:$lwjglNatives")
    implementation ("org.lwjgl:lwjgl-stb:$lwjglNatives")
    implementation("org.joml:joml:$jomlVersion")


}

tasks.test {
    useJUnitPlatform()
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}

tasks.withType<JavaExec> {
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}