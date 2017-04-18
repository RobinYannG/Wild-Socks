package fr.wcs.wildcommunitysocks;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ModifyPassword extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private ImageButton buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        buttonSend = (ImageButton) findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(this);

    }

    public void resetPassword () {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = editTextEmail.getText().toString().trim();

        if (TextUtils.isEmpty(emailAddress)){
            new SweetAlertDialog(ModifyPassword.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(getString(R.string.error))
                    .setContentText(getString(R.string.email_empty))
                    .show();
            return;
        }

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            new SweetAlertDialog(ModifyPassword.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText(getString(R.string.error))
                                    .setContentText(getString(R.string.send_fail))
                                    .show();
                        }
                        else {
                            new SweetAlertDialog(ModifyPassword.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText(getString(R.string.success))
                                    .setContentText(getString(R.string.msg_send))
                                    .show();
                            startActivity(new Intent(ModifyPassword.this, IdentificationActivity.class));
                            finish();
                        }
                    }
                });
    }

    public void onClick(View v){
        if(v == buttonSend){
            resetPassword();
        }
    }
}
