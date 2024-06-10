package com.example.notelite;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class Login_Activity extends AppCompatActivity {
    LinearLayout llGetInfo;
    private EditText etID, etSecurity, etName;
    private Button btnLogin, btnStart;
    private TextInputLayout layoutID, layoutSecurity, layoutName;
    private Database_Helper_Class dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        llGetInfo = findViewById(R.id.llGetInfo);

        etID = findViewById(R.id.etID);
        etName = findViewById(R.id.etName);
        etSecurity = findViewById(R.id.etSecurity);
        btnLogin = findViewById(R.id.btnlogin); // Correct ID
//        btnStart = findViewById(R.id.btnStart);
        layoutID = findViewById(R.id.layoutID);
        layoutSecurity = findViewById(R.id.layoutSecurity);
        layoutName = findViewById(R.id.layoutName);

        dbHelper = new Database_Helper_Class(this);

        llGetInfo.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slogan_anim));

        etID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 13) {
                    layoutID.setError("ID must be exactly 13 characters");
                } else {
                    layoutID.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    startActivity(new Intent(Login_Activity.this, MainActivity.class));
                    finish();
                }
            }
        });

//        btnStart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Login_Activity.this, MainActivity.class));
//                finish();
//            }
//        });

    }

    private boolean validateInputs() {
        String name = etName.getText().toString();
        String id = etID.getText().toString();
        String securityCode = etSecurity.getText().toString();

        layoutID.setError(null);
        layoutSecurity.setError(null);
        layoutName.setError(null);

        boolean isNameValid = true, isIDValid = true, isSecurityValid = true;

        if (name.isEmpty()) {
            layoutName.setError("Name is required");
            Toast.makeText(this, "Incorrect credentials", Toast.LENGTH_SHORT).show();

            isNameValid = false;
        }

        else if (!dbHelper.validateUserName(name)) {
            layoutName.setError("Incorrect name :(");
            Toast.makeText(this, "Incorrect credentials", Toast.LENGTH_SHORT).show();

            isNameValid = false;
        }

        if (id.length() != 13) {
            layoutID.setError("CNIC must be exactly 13 characters");
            isIDValid = false;
            Toast.makeText(this, "Incorrect credentials", Toast.LENGTH_SHORT).show();

        }

        else if (!dbHelper.validateUserID(id)) {
            layoutID.setError("Incorrect ID :(");
            Toast.makeText(this, "Incorrect credentials", Toast.LENGTH_SHORT).show();

            isIDValid = false;
        }

        if (securityCode.isEmpty()) {
            layoutSecurity.setError("Security key is required");
            Toast.makeText(this, "Incorrect credentials", Toast.LENGTH_SHORT).show();

            isSecurityValid = false;
        }

        else if (!dbHelper.validateUserSecurityKey(securityCode)) {
            layoutSecurity.setError("Incorrect security key :(");
            Toast.makeText(this, "Incorrect credentials", Toast.LENGTH_SHORT).show();

            isSecurityValid = false;
        }

        if (isNameValid && isIDValid && isSecurityValid) {


            if (dbHelper.validateUser(name, id, securityCode)) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                return true;
            }

            else {
                Toast.makeText(this, "Incorrect credentials", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return false;
    }
}
