package com.example.fit5046_g4_whatshouldido.validations

class FormValidation {
    fun validateSignUpForm(email:String, password:String, confirmPassword:String) : String {

        var toastMessage = ""
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")


        if (password.length < 6) {
            toastMessage = "Password should be of length 6"
        }

        else if (password != confirmPassword) {
            toastMessage = "Password fields do not match"
        }

        else if (!email.matches(emailRegex)) {
            toastMessage = "Please enter a valid email"
        }

        return toastMessage
    }
}