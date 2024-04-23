package com.example.musicevents.ui.screens.login

class LoginViewModel {
    fun onLoginClick(name: String, pass: String): Boolean{
        return name.isNotBlank() && pass.isNotBlank()
    }

    fun onRegisterClick(name: String, pass: String, email: String): Boolean{
        return name.isNotBlank() && pass.isNotBlank() && email.isNotBlank()
    }
}