package com.stochitacatalin.betterprogrammer;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.stochitacatalin.betterprogrammer.ui.main.PlaceholderFragment;
import com.stochitacatalin.betterprogrammer.ui.main.SectionsPagerAdapter;

import es.dmoral.toasty.Toasty;

public class GamesLearnActivity extends AppCompatActivity {

    ChapterItem chapter;
    TopicItem topic;
    public ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_learn);

        chapter = (ChapterItem) getIntent().getSerializableExtra("chapter");
        topic = (TopicItem) getIntent().getSerializableExtra("topic");

        final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(),chapter);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            int lastpos=0;
            @Override
            public void onPageSelected(int position) {
                if(position!= 0 && sectionsPagerAdapter.getTabs()[position - 1].type.equals("learn")){
                    ((SectionsPagerAdapter) viewPager.getAdapter()).complete(position - 1);
                }
                if (position == 0 || lastpos > position || sectionsPagerAdapter.getTabs()[position - 1].completed) {
                    lastpos = position;
                }
                else {
                    viewPager.setCurrentItem(lastpos);
                    Toasty.warning(GamesLearnActivity.this, "Please complete this first!", Toast.LENGTH_LONG, true).show();
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewPager.getAdapter().notifyDataSetChanged();
    }
}