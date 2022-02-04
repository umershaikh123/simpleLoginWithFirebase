package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class register2 extends AppCompatActivity {

    TextView tvLoginHere;
    Button btnRegister;
    Boolean CheckInfo = false;
    int e;

    private EditText Editname ,  Editemail , Editpassword ,EditconfirmPassword;
    private Button Submit ;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        Submit = findViewById(R.id.Register_act_register);


        Editname = (EditText) findViewById(R.id.editTextName);
        Editemail = (EditText) findViewById(R.id.editTextEnterEmail);
        Editpassword = (EditText) findViewById(R.id.editTextEnterPassword);
        EditconfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);

        tvLoginHere = findViewById(R.id.tvLoginHere);
        btnRegister = findViewById(R.id.btnRegister);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mAuthCurrentUser = mAuth.getCurrentUser();

        Submit.setOnClickListener(view -> { {
                boolean info = registerUser();
                if(info == true) {
                    Intent intent = new Intent(getApplicationContext() , login2.class);
                    startActivity(intent);
                }
            }
        });

        tvLoginHere.setOnClickListener(view -> {
            startActivity(new Intent(register2.this, login2.class));
        });
    }





    private boolean registerUser() {
        String name = Editname.getText().toString().trim();
        String email = Editemail.getText().toString().trim();
        String password = Editpassword.getText().toString().trim();
        String confirmPassword = EditconfirmPassword.getText().toString().trim();
        FirebaseUser mAuthCurrentUser = mAuth.getCurrentUser();



        if (name.isEmpty()) {
            Editname.setError("Name is required");
            Editname.requestFocus();

            return false;
        }

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


        if (confirmPassword.isEmpty()) {
            EditconfirmPassword.setError("Confirm your password");
            EditconfirmPassword.requestFocus();
            return false;
        }


        if (!confirmPassword.equals(password)) {
            EditconfirmPassword.setError("Your password is not same as Confirm password");
            EditconfirmPassword.requestFocus();
            return false;
        }


        if(mAuthCurrentUser != null) {
            String CheckEmail = mAuthCurrentUser.getEmail();

            if (email.contains(CheckEmail)) {

                Editemail.setError("This email already exists");
                Editemail.requestFocus();
                return false;
            }

        }


        mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                        boolean isNewUser = Objects.requireNonNull(task.getResult().getSignInMethods()).isEmpty();

                        if (isNewUser) {
                            Log.e("TAG", "Is New User!");
                            e = 1;
                        }

                        else {
                            Log.e("TAG", "Is Old User!");
                            Editemail.setError("This email already exists");
                            Editemail.requestFocus();
                             e = 0;
                            Log.e("TAG", "This email already exists");
                        }


                    }
                });


        if (e == 0) {
            return false;
        }



        mAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){


                    try
                    {
                        throw Objects.requireNonNull(task.getException());

                    }


                    catch (FirebaseAuthUserCollisionException existEmail)
                    {
                        Log.d(".register2", "onComplete: exist_email");
                        Editemail.setError("This Email already Exists");
                        Editemail.requestFocus();
                        return;

                    }
                    catch (Exception e)
                    {
                        Log.d(".register2", "onComplete: " + e.getMessage());
                    }

                    User user = new User(name , email , password);
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                            .setValue(user);
                    Toast.makeText(getApplicationContext(), "Registration Successfull", Toast.LENGTH_SHORT).show();

                }


                else {
                    Toast.makeText(getApplicationContext(), "Registration  Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return true;
    }



}

  /* private EditText Editname ,  Editemail , Editpassword ,EditconfirmPassword;
   private Button Submit ;
   private ProgressBar progressBar;
   private FirebaseAuth mAuth;
   private Boolean correctInfo = false;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        Submit = findViewById(R.id.Register_act_register);
        Submit.setOnClickListener(r);

        mAuth = FirebaseAuth.getInstance();
        Editname = (EditText) findViewById(R.id.editTextName);
        Editemail = (EditText) findViewById(R.id.editTextEnterEmail);
        Editpassword = (EditText) findViewById(R.id.editTextEnterPassword);
        EditconfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);


    }




    View.OnClickListener r = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext() , login2.class);
           boolean info = registerUser();
           if(info == true) {
               startActivity(intent);
           }

        }
    };


    private boolean registerUser(){
        String name = Editname.getText().toString().trim();
        String email = Editemail.getText().toString().trim();
        String password = Editpassword.getText().toString().trim();
        String confirmPassword = EditconfirmPassword.getText().toString().trim();

        if(name.isEmpty()) {
            Editname.setError("Name is required");
            Editname.requestFocus();
            return false;
        }

        if(email.isEmpty()) {
            Editemail.setError("Email is required");
            Editemail.requestFocus();
            return false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Editemail.setError("Please provide valid email");
            Editemail.requestFocus();
            return false;
        }

        if(password.isEmpty()) {
            Editpassword.setError("Password is required");
            Editpassword.requestFocus();
            return false;
        }

        if(password.length() < 6) {
            Editpassword.setError("Your password length should be above 6 characters");
            Editpassword.requestFocus();
            return false;
        }


        if(confirmPassword.isEmpty()) {
            EditconfirmPassword.setError("Confirm your password");
            EditconfirmPassword.requestFocus();
            return false;
        }


        if(!confirmPassword.equals(password)) {
            EditconfirmPassword.setError("Your password is not same as Confirm password");
            EditconfirmPassword.requestFocus();
            return false;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    FirebaseUser mAuthCurrentUserser = mAuth.getCurrentUser();
                    User user = new User(name , email , password);
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Authentication Successfull", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }

                            else {
                                Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })

                    ;
                }

                else {
                    Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return  true;

    }

} */