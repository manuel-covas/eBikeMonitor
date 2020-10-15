package pt.manuelcovas.ebikemonitor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.concurrent.Executor;


public class AuthenticationKey implements TextWatcher {

    Executor executor;
    EditText authenticationKeyEditText;

    String errorMessage = "No value.";
    AlertDialog errorDialog, authenticationKeyInfoDialog;
    Button authenticationKeySave;
    FloatingActionButton authenticationKeyInfo, authenticationKeyShow, authenticationKeyError;
    Drawable checkDrawable, errorDrawable;
    int colorCheck, colorError;
    boolean shown = false;

    BiometricPrompt biometricPrompt;
    SharedPreferences sharedPreferences;
    String keyText;
    PrivateKey key; boolean keyValid = false;
    Signature signature;


    public AuthenticationKey(final MainActivity mainActivity) {

        sharedPreferences = mainActivity.getSharedPreferences(MainActivity.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        errorDialog = new AlertDialog.Builder(mainActivity).setTitle("Authentication Key").setPositiveButton("OK", null).create();
        authenticationKeyInfoDialog = new AlertDialog.Builder(mainActivity).setMessage(R.string.authorization_key_info_message).setTitle("Authentication Key").setPositiveButton("OK", null).create();

        authenticationKeyInfo = mainActivity.findViewById(R.id.authentication_key_info);
        authenticationKeyInfo.setOnClickListener(onAuthenticationKeyInfoClick);
        authenticationKeyShow = mainActivity.findViewById(R.id.authentication_key_show);
        authenticationKeyShow.setOnClickListener(showAuthenticationKey);
        authenticationKeyEditText = mainActivity.findViewById(R.id.authentication_key_edittext);
        authenticationKeySave = mainActivity.findViewById(R.id.authentication_key_save);
        authenticationKeySave.setOnClickListener(saveAuthenticationKey);
        authenticationKeyError = mainActivity.findViewById(R.id.authentication_key_error);
        authenticationKeyError.setOnClickListener(showErrorDialog);

        checkDrawable = ContextCompat.getDrawable(mainActivity, R.drawable.ic_round_check_circle_24px);
        errorDrawable = ContextCompat.getDrawable(mainActivity, R.drawable.ic_round_error_24px);
        colorCheck = ContextCompat.getColor(mainActivity, R.color.colorAccent);
        colorError = ContextCompat.getColor(mainActivity, R.color.alertRed);

        executor = mainActivity.getMainExecutor();

        biometricPrompt = new BiometricPrompt(mainActivity, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(mainActivity, "Authentication failed:\n" + errString, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(mainActivity, "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                authenticationKeyEditText.setText(keyText);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(mainActivity, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        loadSavedKey();

        try {
            signature = Signature.getInstance("SHA256withRSA");
        } catch (Exception e) {
            new AlertDialog.Builder(mainActivity).setMessage("Failed to prepare algorithm SHA256withRSA.\nError: "+e.getMessage()+"\n\nPlatform does not support signing algorithm.\nAuthenticated BLE commands will fail.").setTitle("Error").setPositiveButton("OK", null).create().show();
        }
    }


    private void loadSavedKey() {
        if (sharedPreferences.contains("authenticationKey")) {
            keyText = sharedPreferences.getString("authenticationKey", "Failed to load saved key.");
            parseKey(keyText);
        }
    }


    private boolean parseKey(String s) {
        try {
            String trimmedKey = s.replace("-----BEGIN RSA PRIVATE KEY-----\n", "")
                    .replace("\n-----END RSA PRIVATE KEY-----", "")
                    .replace("-----BEGIN PRIVATE KEY-----\n", "")
                    .replace("\n-----END PRIVATE KEY-----", "");
            key = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(trimmedKey, Base64.DEFAULT)));
            keyValid = true;
            errorMessage = "RSA private key valid.";
        } catch (Exception e) {
            keyValid = false;
            errorMessage = e.getMessage();
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {
                authenticationKeyError.setImageDrawable(keyValid ? checkDrawable : errorDrawable);
                authenticationKeyError.setColorFilter(keyValid ? colorCheck : colorError);
                if (!shown) {
                    authenticationKeyError.show();
                    shown = true;
                }
            }
        });
        return keyValid;
    }


    View.OnClickListener onAuthenticationKeyInfoClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            authenticationKeyInfoDialog.show();
        }
    };
    View.OnClickListener showAuthenticationKey = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            androidx.biometric.BiometricPrompt.PromptInfo promptInfo = new androidx.biometric.BiometricPrompt.PromptInfo.Builder()
                .setTitle("View RSA Private Key")
                .setSubtitle("Authenticate to show stored value.")
                .setDeviceCredentialAllowed(true)
                .build();
            biometricPrompt.authenticate(promptInfo);
        }
    };


    View.OnClickListener saveAuthenticationKey = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            keyText = authenticationKeyEditText.getText().toString();
            sharedPreferences.edit().putString("authenticationKey", keyText).apply();
            authenticationKeySave.setEnabled(false);
        }
    };
    View.OnClickListener showErrorDialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            errorDialog.setMessage(errorMessage);
            errorDialog.show();
        }
    };


    @Override
    public void afterTextChanged(Editable s) {
        if (parseKey(s.toString())) {
            authenticationKeySave.setEnabled(true);
        }else{
            authenticationKeySave.setEnabled(false);
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
}
