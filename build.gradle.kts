buildscript {
    val agp_version by extra("8.9")
    //val agp_version1 by extra("8.2.2")
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.9.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
}