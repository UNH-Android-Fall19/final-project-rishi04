package edu.newhaven.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.newhaven.final_project.Model.User;

public class SignUpActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference users ;

    EditText email_text, user_name_text,password_text;
    Button sign_up,sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        email_text = (EditText) findViewById(R.id.email_text);
        user_name_text = (EditText) findViewById(R.id.user_name_text);
        password_text = (EditText) findViewById(R.id.password_text);

        sign_up = (Button)findViewById(R.id.sign_up);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final User user= new User(email_text.getText().toString(),user_name_text.getText().toString(),
                        password_text.getText().toString());
                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(user.getUsername()).exists()) {
                            Toast.makeText(SignUpActivity.this, "User name already Exists", Toast.LENGTH_SHORT).show();
                        } else {
                            users.child(user.getUsername()).setValue(user);

                            Toast.makeText(SignUpActivity.this, "Success Register", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        getSupportActionBar().hide();
    }
}
