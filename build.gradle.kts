
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.21"
    id("io.izzel.taboolib") version "1.56"
//    id("com.github.johnrengelman.shadow") version "7.1.2"
}

val taboolibVersion: String by project

repositories {
    mavenLocal ()
    mavenCentral()
    //taboolib
    maven {
        url = uri("https://repo.tabooproject.org/repository/releases/")
        isAllowInsecureProtocol = true
    }
    //物品库
    maven {
        url = uri("https://r.irepo.space/maven/")
        isAllowInsecureProtocol = true
    }
    //papi
    maven {
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }

//    maven {
//        name = "CodeMC"
//        url = uri("https://repo.codemc.io/repository/maven-public/")
//    }
}

taboolib {
    install(
        "common",
        "common-5",
        "platform-bukkit",
        "module-configuration",
        "module-chat",
        "module-lang",
        "module-ui",
        "expansion-player-database",
        "module-database"
    )
    description {
        contributors {
            name("facered")
        }
        dependencies {
            bukkitApi("1.13")
            name("PlaceholderAPI").optional(true)
            name("MythicMobs").optional(true)
            name("NeigeItems").optional (true)
            name("Pouvoir")
        }

    }

    // relocate("com.google.gson", "com.google.gson2_9_1")
    classifier = null
    version = taboolibVersion
}



dependencies {

//    implementation("de.tr7zw:item-nbt-api:2.12.0")
    compileOnly("ink.ptms.core:v11802:11802:mapped")
    compileOnly("ink.ptms.core:v11802:11802:universal")
    compileOnly("ink.ptms.core:v11701:11701:mapped")
    compileOnly("ink.ptms.core:v11701:11701:universal")
    compileOnly (fileTree(baseDir = "src/libs"))
    //物品库
    compileOnly("pers.neige.neigeitems:NeigeItems:1.15.19")
    //数据库连接池
    compileOnly("com.zaxxer:HikariCP:4.0.3")
    //papi 插件
    compileOnly("me.clip:placeholderapi:2.10.9") { isTransitive = false }
    //aviator脚本
    implementation("com.googlecode.aviator:aviator:5.3.3")

//    compileOnly(kotlin("stdlib"))

    implementation("com.google.code.gson:gson:2.10.1")

//    implementation("io.github.karlatemp:unsafe-accessor:1.7.0")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnitPlatform()
}


tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
tasks.withType<Jar> ().configureEach {
    destinationDirectory.set(File ("F:\\mc\\mcPaperServer\\plugins"))
}