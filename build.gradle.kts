// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

    //adiciona o plugin Google ao projeto permitindo a integração com os serviços do Firebase.
    id("com.google.gms.google-services") version "4.4.4" apply false
}