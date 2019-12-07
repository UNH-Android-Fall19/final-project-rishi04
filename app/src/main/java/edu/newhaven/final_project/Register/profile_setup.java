package edu.newhaven.final_project.Register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.newhaven.final_project.MainActivity;
import edu.newhaven.final_project.R;

public class profile_setup extends AppCompatActivity {

    private EditText Username, Firstname, Lastname, Address;
    private Button profile_save;
    private CircleImageView ProfileImage;
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;
    private DatabaseReference UserRef;
    private StorageReference UserProfileImageRef;
    private Uri ImageUri;
    String currentUserID;
    final static int gallery_Pick = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        Username = findViewById(R.id.Profile_Username);
        Firstname = findViewById(R.id.Profile_firstname);
        Lastname = findViewById(R.id.Profile_lastname);
        Address = findViewById(R.id.Profile_address);

        profile_save = findViewById(R.id.Profile_save);
        ProfileImage = findViewById(R.id.Profile_image);

        loadingBar = new ProgressDialog(this);

        profile_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveAccountInformation();

        }
    });

        ProfileImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent galleryIntent = new Intent();
            galleryIntent.setAction((Intent.ACTION_GET_CONTENT));
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, gallery_Pick);
        }
    });

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.hasChild("Profileimage")){
                        String image = dataSnapshot.child("Profileimage").getValue().toString();
                        Picasso.with(profile_setup.this).load(image).placeholder(R.drawable.profile).into(ProfileImage);

                    }
                    else{
                        Toast.makeText(profile_setup.this,"Please select profie image first",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == gallery_Pick && resultCode == RESULT_OK)
        {

            Uri ImageUri = data.getData();
            //Picasso.with(this).load(ImageUri).into(ProfileImage);
            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this );
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK)
            {
                loadingBar.setTitle("Saving Information");
                loadingBar.setMessage("Please Wait");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);

                Uri resultUri = result.getUri();


                final StorageReference filepath = UserProfileImageRef.child(currentUserID + "Profile Images/.jpeg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(profile_setup.this,"Profile Image is saved successfully",Toast.LENGTH_SHORT).show();

                            String downloadUrL =  task.getResult().getStorage().getDownloadUrl().toString();
                            String path = filepath.getPath();
                            UserRef.child("Profileimage").setValue(path).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Intent selfIntent = new Intent(profile_setup.this, profile_setup.class);
                                        startActivity(selfIntent);
                                        Toast.makeText(profile_setup.this,"PProfile Image is saved successfully",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                    else {
                                        String message= task.getException().getMessage();
                                        Toast.makeText(profile_setup.this,"PProfile Image save  unsuccessfully" + message,Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }

                                }
                            });
                        }

                    }
                });
            }
            else {

                Toast.makeText(profile_setup.this,"Error Occurred: image cannot be cropped . Try Again",Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }
    }

    private void SaveAccountInformation() {
        String username = Username.getText().toString();
        String firstname = Firstname.getText().toString();
        String lastname = Lastname.getText().toString();
        String address = Address.getText().toString();


        if(TextUtils.isEmpty(username)){
            Toast.makeText(this,"Please Enter the Username",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(firstname)){
            Toast.makeText(this,"Please Enter the Firstname",Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(lastname)){
            Toast.makeText(this,"Please Enter the Lastname",Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(address)){
            Toast.makeText(this,"Please Enter the Address",Toast.LENGTH_SHORT).show();

        }
        else{

            loadingBar.setTitle("Saving Information");
            loadingBar.setMessage("Please Wait");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap userMap = new HashMap();
            userMap.put("Username", username);
            userMap.put("Firstname", firstname);
            userMap.put("Lastname", lastname);
            userMap.put("Address", address);
            userMap.put("Status", "New to the App");
            userMap.put("Interest", "None");

            UserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        SendUserToMainActivity();
                        Toast.makeText(profile_setup.this,"Saved",Toast.LENGTH_LONG).show();

                    }
                    else{
                        String message = task.getException().getMessage();
                        Toast.makeText(profile_setup.this,"Error"+message,Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }


    }

    private void SendUserToMainActivity() {
        Intent SetupIntent = new Intent(profile_setup.this, MainActivity.class);
        SetupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(SetupIntent);
        finish();
    }
}
