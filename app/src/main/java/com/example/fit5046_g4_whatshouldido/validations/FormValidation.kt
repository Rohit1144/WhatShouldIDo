package com.example.fit5046_g4_whatshouldido.validations

import android.content.Context
import android.widget.Toast
import com.google.ai.client.generativeai.type.Content

class FormValidation {
    fun validateSignUpForm(password:String, confirmPassword:String) : String {

        var toastMessage = ""

        if (password.length < 6) {
            toastMessage = "Password should be of length 6"
        }

        else if (password != confirmPassword) {
            toastMessage = "Password fields do not match"
        }


        return toastMessage
    }
}