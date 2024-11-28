package com.example.asm1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    EditText edt_pass, edt_Email;
    Button btn_Back, btn_Signup;
    String email, pass;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edt_Email= findViewById(R.id.edt_Email);
        edt_pass= findViewById(R.id.edt_pass);
        btn_Back= findViewById(R.id.btn_Back);
        btn_Signup= findViewById(R.id.btn_Signup);
        auth= FirebaseAuth.getInstance();

        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        btn_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email= edt_Email.getText().toString();
                pass= edt_pass.getText().toString();
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SignUpActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                            finish();
                        }else {
                            Toast.makeText(SignUpActivity.this, "Đăng ký không thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}