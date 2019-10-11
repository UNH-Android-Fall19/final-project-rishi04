package edu.newhaven.final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private ViewPager screenPager;
    introViewPageAdapter IntroViewPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        List<screenItem> mList = new ArrayList<>();

        mList.add(new screenItem("MORE OF WHAT YOU LOVE","GET RECOMMENDATIONS TO YOUR FAVOURITE ANIME OR MANGA",R.drawable.onepiece));
        mList.add(new screenItem("SHARE YOUR REACTION","GET THE LATEST RATINGS AND REVIEWS AND LEAVE YOUR OWN",R.drawable.onepiece));
        mList.add(new screenItem("JOIN THE COMMUNITY","JOIN TO FIND LIKE MINDED PEOPLE AND MAKE NEW NEW FRIENDS WITH GLOBAL ACTIVITY FEED",R.drawable.onepiece));

        screenPager = findViewById(R.id.screen_viewpager);
        IntroViewPageAdapter = new introViewPageAdapter(this,mList);
        screenPager.setAdapter(IntroViewPageAdapter);


    }
}
