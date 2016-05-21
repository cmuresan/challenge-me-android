package com.example.cristianmmuresan.challengeme.ui.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.cristianmmuresan.challengeme.R;

public class HomeActivity extends AppCompatActivity {

    private static final int FRAGMENT_RECORD = 0;
    private static final int FRAGMENT_MY_ACTIVITIES = 1;

    private TabLayout tabLayoutRules;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_viewpager);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayoutRules = (TabLayout) findViewById(R.id.tab_layout);

        //Setup ViewPager
        setupViewPager();

        //Setup Tabs
        tabLayoutRules.setupWithViewPager(viewPager);

        //Set Typeface
        setTypeface();
    }

    private void setTypeface() {
        try {
            for(int i= 0 ; i<tabLayoutRules.getTabCount();i++){
                TextView view = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_item, null, false);
                view.setText(tabLayoutRules.getTabAt(i).getText());
                tabLayoutRules.getTabAt(i).setCustomView(view);
                view.setSelected(i == 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case FRAGMENT_RECORD:
                    return RecordActivityFragment.newInstance();
                case FRAGMENT_MY_ACTIVITIES:
                    return MyActivitiesFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case FRAGMENT_RECORD:
                    return getString(R.string.record_activity);
                case FRAGMENT_MY_ACTIVITIES:
                    return getString(R.string.my_activities_tab);
            }
            return null;
        }
    }
}
