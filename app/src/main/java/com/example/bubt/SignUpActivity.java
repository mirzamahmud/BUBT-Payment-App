package com.example.bubt;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity
{

    // widgets variable
    private EditText nameText;
    private EditText idText;
    private EditText deptText;
    private EditText emailText;
    private EditText passwordText;
    private Button signupButton;
    private TextView haveAccount;

    // database variables
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    // progress dialogue variables
    ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(SignUpActivity.this, R.color.colorPrimary));

        // reference widgets
        nameText = (EditText) findViewById(R.id.name_text);
        idText = (EditText) findViewById(R.id.student_teacher_id_text);
        deptText = (EditText) findViewById(R.id.dept_text);
        emailText = (EditText) findViewById(R.id.email_text);
        passwordText = (EditText) findViewById(R.id.password_text);
        signupButton = (Button) findViewById(R.id.signup_button);
        haveAccount = (TextView) findViewById(R.id.have_account_text);

        // initialize firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        // initialize progress dialogue
        progressDialog = new ProgressDialog(this);

        // add event to signupButton
        signupButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // variables that user enter into the edit text
                String textName = nameText.getText().toString();
                String textID = idText.getText().toString();
                String textDept = deptText.getText().toString();
                String textEmail = emailText.getText().toString();
                String textPassword = passwordText.getText().toString();

                // check any field isn't empty
                if (TextUtils.isEmpty(textName) || TextUtils.isEmpty(textID) || TextUtils.isEmpty(textDept) || TextUtils.isEmpty(textEmail) || TextUtils.isEmpty(textPassword))
                {
                    Toast.makeText(SignUpActivity.this, "Please Fill up all these sections", Toast.LENGTH_SHORT).show();
                }
                // check the length of the password
                else if (textPassword.length() < 6)
                {
                    Toast.makeText(SignUpActivity.this, "Password is too Short, use at least 6 characters", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    // send data
                    signupUsers(textName, textID, textDept, textEmail, textPassword);
                }
            }
        });

        // add event to haveAccountText
        haveAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void signupUsers(final String name, final String id, final String dept, final String email, final String password)
    {
        // progress dialogue
        progressDialog.setTitle("Account Create");
        progressDialog.setMessage("Be Patient, Account is Creating");
        progressDialog.show();

        // start passing  data to firebase
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>()
        {
            @Override
            public void onSuccess(AuthResult authResult)
            {
                HashMap<String, Object> map = new HashMap<>();
                map.put("name", name);
                map.put("id", id);
                map.put("department", dept);
                map.put("email", email);
                map.put("password", password);
                map.put("user id", firebaseAuth.getCurrentUser().getUid());

                // add this map to database
                databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            // progress dialogue dismiss
                            progressDialog.dismiss();

                            Toast.makeText(SignUpActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SignUpActivity.this, UploadImageActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                // progress dialogue dismiss
                progressDialog.dismiss();

                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}