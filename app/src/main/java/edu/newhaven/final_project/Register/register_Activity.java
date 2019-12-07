package edu.newhaven.final_project.Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.newhaven.final_project.MainActivity;
import edu.newhaven.final_project.R;

public class register_Activity extends AppCompatActivity {

    private RelativeLayout rlayout;
    private Animation animation;
    private EditText UserName, UserEmail, UserPassword, UserConfirmPassword;
    private Button SignUp;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rlayout = findViewById(R.id.rlayout);
        animation = AnimationUtils.loadAnimation(this,R.anim.uptodowndiagonal);
        rlayout.setAnimation(animation);
        loadingBar = new ProgressDialog(this);


        mAuth = FirebaseAuth.getInstance();
        UserEmail = (EditText) findViewById(R.id.register_email);
        UserName = (EditText) findViewById(R.id.register_username);
        UserPassword = (EditText) findViewById(R.id.register_password);
        UserConfirmPassword = (EditText) findViewById(R.id.register_repassword);
        SignUp = (Button) findViewById(R.id.sign_up);


        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });

    }

    private void createNewAccount() {
        String email = UserEmail.getText().toString();
        String username = UserName.getText().toString();
        String password = UserPassword.getText().toString();
        String Repassword = UserConfirmPassword.getText().toString();


        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please Enter the Email",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(username)){
            Toast.makeText(this,"Please Enter the Username",Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please Enter the password",Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(Repassword)){
            Toast.makeText(this,"Please re enter the password",Toast.LENGTH_SHORT).show();

        }
        else{
            loadingBar.setTitle("Logging In");
            loadingBar.setMessage("Please Wait");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful()){
                        SendUserTOMainActivity();
                        Toast.makeText(register_Activity.this,"Sign up complete",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                    else{
                        String message = task.getException().getMessage();
                        Toast.makeText(register_Activity.this,"Error Occurred" + message,Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }

                 private void SendUserTOMainActivity() {

                    Intent loginIntent = new Intent(register_Activity.this, login_Activity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                    finish();
                 }
             });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

