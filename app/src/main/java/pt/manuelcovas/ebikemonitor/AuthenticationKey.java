package pt.manuelcovas.ebikemonitor;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    AlertDialog errorDialog;
    Button authenticationKeySave;
    FloatingActionButton authenticationKeyError;
    Drawable checkDrawable, errorDrawable;
    int colorCheck, colorError;
    boolean shown = false;

    PrivateKey key;
    Signature signature;


    public AuthenticationKey(MainActivity mainActivity) {

        authenticationKeyEditText = mainActivity.findViewById(R.id.authentication_key_edittext);
        authenticationKeyError = mainActivity.findViewById(R.id.authentication_key_error);
        authenticationKeySave = mainActivity.findViewById(R.id.authentication_key_save);

        errorDialog = new AlertDialog.Builder(mainActivity).setTitle("Authentication Key").setPositiveButton("OK", null).create();
        authenticationKeyError.setOnClickListener(showErrorDialog);

        checkDrawable = mainActivity.getDrawable(R.drawable.ic_round_check_circle_24px);
        errorDrawable = mainActivity.getDrawable(R.drawable.ic_round_error_24px);
        colorCheck = ContextCompat.getColor(mainActivity, R.color.colorAccent);
        colorError = ContextCompat.getColor(mainActivity, R.color.alertRed);

        executor = mainActivity.getMainExecutor();

        try {
            signature = Signature.getInstance("SHA256withRSA");
        } catch (Exception e) {
            new AlertDialog.Builder(mainActivity).setMessage("Failed to prepare algorithm SHA256withRSA.\nError: "+e.getMessage()+"\n\nPlatform does not support signing algorithm.\nAuthenticated BLE commands will fail.").setTitle("Error").setPositiveButton("OK", null).create().show();
        }
    }

    View.OnClickListener showErrorDialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            errorDialog.setMessage(errorMessage);
            errorDialog.show();
        }
    };

    private void setInputValidity(final boolean valid, final String message) {

        errorMessage = message;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                authenticationKeySave.setEnabled(valid);
                authenticationKeyError.setImageDrawable(valid ? checkDrawable : errorDrawable);
                authenticationKeyError.setColorFilter(valid ? colorCheck : colorError);
                if (!shown) {
                    authenticationKeyError.show();
                    shown = true;
                }
            }
        });
    }



    @Override
    public void afterTextChanged(Editable s) {
        try {
            String trimmedKey = s.toString().replace("-----BEGIN RSA PRIVATE KEY-----\n", "")
                                            .replace("\n-----END RSA PRIVATE KEY-----", "")
                                            .replace("-----BEGIN PRIVATE KEY-----\n", "")
                                            .replace("\n-----END PRIVATE KEY-----", "");
            key = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(trimmedKey, Base64.DEFAULT)));
            setInputValidity(true, "RSA private key valid.");

        } catch (Exception e) {
            setInputValidity(false, e.getMessage());
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}
