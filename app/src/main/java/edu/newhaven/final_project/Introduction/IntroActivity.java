package edu.newhaven.final_project.Introduction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import edu.newhaven.final_project.MainActivity;
import edu.newhaven.final_project.R;

public class IntroActivity extends AppCompatActivity {

   // Initialize all the variables on activity_intro page
    private ViewPager screenPager;
    introViewPageAdapter IntroViewPageAdapter;
    TabLayout tabIndicator;
    Button next_button;
    Button get_started;
    Animation button_anime;


    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Hide action bar


        // initiate views
        next_button = findViewById(R.id.button_next);
        get_started = findViewById(R.id.button_getStarted);
        tabIndicator = findViewById(R.id.tab_indicator);
        button_anime = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);

        // setting up list view for the slider view

        final List<screenItem> mList = new ArrayList<>();

        // adding list view information to the list

        mList.add(new screenItem("MORE OF WHAT YOU LOVE","GET RECOMMENDATIONS TO YOUR FAVOURITE ANIME OR MANGA",R.drawable.onepiece));
        mList.add(new screenItem("SHARE YOUR REACTION","GET THE LATEST RATINGS AND REVIEWS AND LEAVE YOUR OWN",R.drawable.onepiece));
        mList.add(new screenItem("JOIN THE COMMUNITY","JOIN TO FIND LIKE MINDED PEOPLE AND MAKE NEW NEW FRIENDS WITH GLOBAL ACTIVITY FEED",R.drawable.onepiece));
        mList.add(new screenItem("WELCOME","WELCOME TO THE APP",R.drawable.onepiece));
        screenPager = findViewById(R.id.screen_viewpager);
        IntroViewPageAdapter = new introViewPageAdapter(this,mList);
        screenPager.setAdapter(IntroViewPageAdapter);

        // connect tab view to our view pager
        tabIndicator.setupWithViewPager(screenPager);

        // set next button to move from slide to slide

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = screenPager.getCurrentItem();
                if (position < mList.size()) {

                    position++;
                    screenPager.setCurrentItem(position);


                }

                if (position == mList.size()-1) { // when we rech to the last screen



                    loadLastScreen();

                }



            }


        });
        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(tab.getPosition() == mList.size()-1){
                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        get_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent SignUpActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(SignUpActivity);

            }
        });
    }

    private void loadLastScreen() {
        next_button.setVisibility(View.INVISIBLE);
        get_started.setVisibility(View.VISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);

        get_started.setAnimation(button_anime);
    }
}
