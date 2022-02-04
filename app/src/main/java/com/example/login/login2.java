package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class login2 extends AppCompatActivity {

    TextInputEditText etRegEmail;
    TextInputEditText etRegPassword;
    TextView tvLoginHere;
    Button btnRegister;

    private EditText  Editemail , Editpassword ;
    private Button login ;
    private Button register ;
    private Boolean correctInfo = false;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    int e = 0 ; // email is incorrect
    int p = 0; // pass is incorrect
    int ep = 0; // pass or email is incorrect
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

    login = findViewById(R.id.loginButton);

    mAuth = FirebaseAuth.getInstance();
    Editemail = (EditText) findViewById(R.id.LoginTextEmailAddress);
    Editpassword = (EditText) findViewById(R.id.LoginTextPassword);

    btnRegister = findViewById(R.id.login_act_register);


        login.setOnClickListener(view -> { {
        boolean info = registerUser();
        if(info == true) {
            Intent intent = new Intent(getApplicationContext() , login2.class);
            startActivity(intent);
        }
    }
    });


        btnRegister.setOnClickListener(view -> { {
            Intent intent = new Intent(getApplicationContext() , register2.class);
            startActivity(intent);
        }
        });
}





    private boolean registerUser() {


        String email = Editemail.getText().toString().trim();
        String password = Editpassword.getText().toString().trim();




        if (email.isEmpty()) {
            Editemail.setError("Email is required");
            Editemail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Editemail.setError("Please provide valid email");
            Editemail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            Editpassword.setError("Password is required");
            Editpassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            Editpassword.setError("Your password length should be above 6 characters");
            Editpassword.requestFocus();
            return false;
        }




        mAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {
                    FirebaseUser mAuthCurrentUser = mAuth.getCurrentUser();
                    try
                    {
                        throw Objects.requireNonNull(task.getException());
                    }
                    // if user enters wrong email.
                    catch (FirebaseAuthInvalidUserException invalidEmail)
                    {
                        Log.d(".login2", "onComplete: invalid_email");
                        Editemail.setError("Incorrect Email");
                        Editemail.requestFocus();


                    }
                    // if user enters wrong password.
                    catch (FirebaseAuthInvalidCredentialsException wrongPassword)
                    {
                        Log.d(".login2", "onComplete: wrong_password");
                        Editpassword.setError("Incorrect Password");
                        Editpassword.requestFocus();


                    }

                    catch (Exception e)
                    {
                        Log.d(".login2", "onComplete: " + e.getMessage());
                    }


                    if (mAuthCurrentUser != null) {
                        Toast.makeText(getApplicationContext(), "Login Successfull", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(login2.this, MainActivity.class));
                    }

                    ep = 1;
                }

                else {
                    Toast.makeText(getApplicationContext(), "Wrong password or email", Toast.LENGTH_SHORT).show();
                    ep = 0;
                }
            }
        });

        if (ep == 0) {
            return false;
        }

        else  {
            return  true;
        }
    }



}



  /*  TextInputEditText etLoginEmail;
    TextInputEditText etLoginPassword;
    TextView tvRegisterHere;
    Button btnLogin;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPass);
        tvRegisterHere = findViewById(R.id.tvRegisterHere);
        btnLogin = findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(view -> {
            loginUser();
        });

        tvRegisterHere.setOnClickListener(view ->{
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });


    }

    private void loginUser(){
        String email = etLoginEmail.getText().toString();
        String password = etLoginPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            etLoginEmail.setError("Email cannot be empty");
            etLoginEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            etLoginPassword.setError("Password cannot be empty");
            etLoginPassword.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else{
                        Toast.makeText(LoginActivity.this, "Log in Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}





    /* Button login , register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        register = findViewById(R.id.login_act_register);
        register.setOnClickListener(r);



    }


    View.OnClickListener r = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext() , register2.class);
            startActivity(intent);
        }
    };


} */