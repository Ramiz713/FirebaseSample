package com.itis2019.firebasesample.utils

import android.text.TextUtils
import android.widget.EditText

class Helpers {
    companion object {
         fun validateEditText(editText: EditText, text: String): Boolean {
            if (TextUtils.isEmpty(text)) {
                editText.error = "Cannot be empty!"
                return false
            }
            return true
        }
    }
}

