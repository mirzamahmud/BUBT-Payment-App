package com.example.bubt;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity
{

    // variables
    private EditText emailText;
    private EditText passwordText;
    private Button loginButton;
    private TextView newUserText;

    private FirebaseAuth firebaseAuth;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary));

        // reference of widgets
        emailText = (EditText) findViewById(R.id.email_text);
        passwordText = (EditText) findViewById(R.id.password_text);
        loginButton = (Button) findViewById(R.id.login_button);
        newUserText = (TextView) findViewById(R.id.new_user_text);

        // initialize firebase
        firebaseAuth = FirebaseAuth.getInstance();

        // add event to login button
        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // add value to edittexts
                String textEmail = emailText.getText().toString();
                String textPassword = passwordText.getText().toString();

                if(TextUtils.isEmpty(textEmail) || TextUtils.isEmpty(textPassword))
                {
                    Toast.makeText(LoginActivity.this, "Please, fill up all sections", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loginUser(textEmail, textPassword);
                }

            }
        });

        // add event to newUserText
        newUserText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loginUser(String Email, String Password)
    {
        // read database value for user login
        firebaseAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    Toast.makeText(LoginActivity.this, "Account Logged in Successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(LoginActivity.this, e.getMessage(),  Toast.LENGTH_SHORT).show();
            }
        });
    }
}