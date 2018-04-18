package com.example.m1.tripproj.HelperClasses;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


public class InputValidation {

    private Context context;

    public InputValidation(Context context) {
        this.context = context;
    }

    public boolean isInputsFilled(TextInputEditText emailInput, TextInputLayout textInputLayout, String message) {
        String value = emailInput.getText().toString().trim();
        if (value.isEmpty()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(emailInput);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        Log.i("test", value);
        return true;

    }

    public boolean checkEmailFormat(TextInputEditText emailInput, TextInputLayout inputLayout, String message) {
        String value = emailInput.getText().toString().trim();
        if ((value.isEmpty()) || !(android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches())) {
            inputLayout.setError(message);
            hideKeyboardFrom(emailInput);
            return false;
        } else {
            inputLayout.setErrorEnabled(false);
        }

        Log.i("test", value);
        return true;
    }

    public boolean isInputsMatched(TextInputEditText passInput, TextInputEditText confirmPassInput, TextInputLayout inputLayout, String message) {
        String value1 = passInput.getText().toString().trim();
        String value2 = confirmPassInput.getText().toString().trim();
        if (!value1.contentEquals(value2)) {
            inputLayout.setError(message);
            hideKeyboardFrom(confirmPassInput);
            return false;
        } else {
            inputLayout.setErrorEnabled(false);
        }
        Log.i("test", value1);
        Log.i("test", value2);
        return true;
    }

    private void hideKeyboardFrom(View view) {
        InputMethodManager inputMethodMgr= (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodMgr.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}

