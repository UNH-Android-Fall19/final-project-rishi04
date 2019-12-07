package edu.newhaven.final_project;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.newhaven.final_project.Card_Recycler_View.Anime;
import edu.newhaven.final_project.Card_Recycler_View.RecyclerViewAdapter;
import edu.newhaven.final_project.Post.PostActivity;
import edu.newhaven.final_project.Register.login_Activity;
import edu.newhaven.final_project.Register.profile_setup;
import edu.newhaven.final_project.Register.register_Activity;


public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toogle;
    private RecyclerView postList;
    private Toolbar mToolbar;
    private CircleImageView NavProfileImage;
    private TextView NavProfileUserName;
    private ImageButton newPostbutton;


    private ActionBar actionBar;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference UserRef;

    List<Anime> AnimeCards ;

    String currentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mToolbar = findViewById(R.id.update_post_page_toolbar);
        setSupportActionBar(mToolbar);


        newPostbutton = findViewById(R.id.add_new_post_button);


        if (user != null) {
            currentUserID = user.getUid();
            UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
            UserRef.child(currentUserID).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){

                        if(dataSnapshot.hasChild("Username")){
                            String Username = dataSnapshot.child("Username").getValue().toString();
                            NavProfileUserName.setText(Username);
                        }
                        if(dataSnapshot.hasChild("Profileimage")){
                            String image = dataSnapshot.child("Profileimage").getValue().toString();
                            Picasso.with(MainActivity.this).load(image).placeholder(R.drawable.profile).into(NavProfileImage);
                        }

                        else{
                            Toast.makeText(MainActivity.this,"Profile doesn' exist",Toast.LENGTH_SHORT).show();

                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawable_layout);


        navigationView = (NavigationView) findViewById(R.id.navigation_view);


        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);

        NavProfileImage = (CircleImageView) navView.findViewById(R.id.nav_profile_image);
        NavProfileUserName = (TextView) navView.findViewById(R.id.nav_user_full_name);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem Item) {

                UserMenuSelector(Item);
                return false;
            }
        });

        newPostbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendusersToPostActivity();
            }
        });

        AnimeCards = new ArrayList<>();
        AnimeCards.add(new Anime("Naruto","Action Anime","Description Anime",R.drawable.naruto));
        AnimeCards.add(new Anime("One Piece","Adventure Anime","Description Anime",R.drawable.onepiececover));
        AnimeCards.add(new Anime("Bleach","Action Anime","Description Anime",R.drawable.bleach));
        AnimeCards.add(new Anime("Dragon Ball Z","Martial Arts Anime","Description Anime",R.drawable.dragonball));
        AnimeCards.add(new Anime("Thunder Cats","Futuristic Anime","Description Anime",R.drawable.thundercats));
        AnimeCards.add(new Anime("Cowboy Bebop","Futuristic Anime","Description Anime",R.drawable.cowboybebop));
        AnimeCards.add(new Anime("Samurai Champloo","Action  Anime","Description Anime",R.drawable.samuarichamploo));
        AnimeCards.add(new Anime("Pokemon","Adventure Anime","Description Anime",R.drawable.pokemon));
        AnimeCards.add(new Anime("Naruto","Action Anime","Description Anime",R.drawable.naruto));
        AnimeCards.add(new Anime("One Piece","Adventure Anime","Description Anime",R.drawable.onepiececover));
        AnimeCards.add(new Anime("Bleach","Action Anime","Description Anime",R.drawable.bleach));
        AnimeCards.add(new Anime("Dragon Ball Z","Martial Arts Anime","Description Anime",R.drawable.dragonball));
        AnimeCards.add(new Anime("Thunder Cats","Futuristic Anime","Description Anime",R.drawable.thundercats));
        AnimeCards.add(new Anime("Cowboy Bebop","Futuristic Anime","Description Anime",R.drawable.cowboybebop));
        AnimeCards.add(new Anime("Samurai Champloo","Action  Anime","Description Anime",R.drawable.samuarichamploo));
        AnimeCards.add(new Anime("Pokemon","Adventure Anime","Description Anime",R.drawable.pokemon));




    }


    private void SendusersToPostActivity() {
        Intent AddNewPostIntent = new Intent(MainActivity.this, PostActivity.class);
        startActivity(AddNewPostIntent);
    }

    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);

        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            sendUserToLoginActivity();
        }
        else{
            CheckUserExist();
        }
    }

    private void CheckUserExist() {
        final String currentUserId = mAuth.getCurrentUser().getUid();
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(currentUserId)){
                    sendUserToProfileSetupActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendUserToProfileSetupActivity() {

        Intent profileIntent = new Intent(MainActivity.this, profile_setup.class);
        profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(profileIntent);
        finish();

    }


    private void sendUserToLoginActivity() {

        Intent loginIntent = new Intent(MainActivity.this, login_Activity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private  void UserMenuSelector(MenuItem item){
        switch (item.getItemId())
        {
            case R.id.nav_post:
                SendusersToPostActivity();
                break;

            case R.id.nav_Profile:
                Toast.makeText(this,"Profile",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_home:
                Toast.makeText(this,"Home",Toast.LENGTH_SHORT).show();
                sendUserToProfileSetupActivity();
                break;
            case R.id.nav_friends:
                Toast.makeText(this,"Friends List",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_track:
                RecyclerView myrv = (RecyclerView) findViewById(R.id.user_post_list);
                RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this,AnimeCards );
                myrv.setLayoutManager(new GridLayoutManager(this,3));
                myrv.setAdapter(myAdapter);
                Toast.makeText(this,"Anime",Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_settings:
                Toast.makeText(this,"Settings",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_Logout:
                Toast.makeText(this,"Logout",Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                sendUserToLoginActivity();
                break;
        }
    }

}
