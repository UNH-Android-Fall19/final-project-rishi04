package edu.newhaven.final_project.Register;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.newhaven.final_project.MainActivity;
import edu.newhaven.final_project.R;

public class login_Activity extends AppCompatActivity {

    private ImageButton btRegister;
    private TextView tvLogin;
    private EditText userEmail, userPassword;
    private Button login;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();


        userEmail = (EditText) findViewById(R.id.login_email);
        userPassword = (EditText) findViewById(R.id.login_password);
        login = (Button) findViewById(R.id.login_button);

        btRegister  = findViewById(R.id.btRegister);
        tvLogin     = findViewById(R.id.tvLogin);
        loadingBar = new ProgressDialog(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allowUserToLogin();
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                switchtoregister();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            sendUserToMainActivity();
        }
    }

    private void allowUserToLogin() {
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please Enter the Email",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please Enter the Password",Toast.LENGTH_SHORT).show();

        }
        else{
            loadingBar.setTitle("Logging In");
            loadingBar.setMessage("Please Wait");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful()){

                       sendUserToMainActivity();
                       Toast.makeText(login_Activity.this,"you are logged in",Toast.LENGTH_SHORT).show();
                       loadingBar.dismiss();
                   }
                   else {
                       String message = task.getException().getMessage();
                       Toast.makeText(login_Activity.this,"hi "+message,Toast.LENGTH_SHORT).show();
                       loadingBar.dismiss();
                   }
                }
            });

        }
    }

    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent (login_Activity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void  switchtoregister() {
            Intent intent   = new Intent(login_Activity.this,register_Activity.class);

            startActivity(intent);
    }


    /*@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {

        Intent intent = new Intent(login_Activity.this, register_Activity.class);
        startActivity(intent);
        if (v == btRegister){
            Intent intent   = new Intent(login_Activity.this,register_Activity.class);
            Pair[] pairs    = new Pair[1];
            pairs[0] = new Pair<View,String>(tvLogin,"tvLogin");
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(login_Activity.this,pairs);
            startActivity(intent,activityOptions.toBundle());

        }
    }*/
}
