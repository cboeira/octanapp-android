package com.example.octanapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.octanapp.R;

public class RedefinirSenhaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redefinir_senha);

        Toolbar toolbar = findViewById(R.id.toolbar_redefinir_senha);
        toolbar.setTitle("Redefinir senha");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText txtEmail = findViewById(R.id.redefinir_email);
        Button btRedefinir = findViewById(R.id.bt_redefinir_confirmacao);

        btRedefinir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validado = true;
                if (txtEmail.getText().length() == 0) {
                    txtEmail.setError("Campo e-mail obrigatório");
                    txtEmail.requestFocus();
                    validado = false;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).matches()) {
                    txtEmail.setError("Favor inserir um email válido");
                    txtEmail.requestFocus();
                    validado = false;
                }
                if (validado) {
                    Toast.makeText(getApplicationContext(), "Nova senha enviada para: "+txtEmail.getText().toString(), Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }
}
