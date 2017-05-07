package itsci.mju.com.mjurescue.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import itsci.mju.com.mjurescue.GPSService.GPS_ServiceStudentNow;
import itsci.mju.com.mjurescue.ListTitleNews.ListTitleNewsFragment;
import itsci.mju.com.mjurescue.RequestAssistance.RequestAssistanceFragment;
import itsci.mju.com.mjurescue.ListTitleAID.ListTitleAIDFragment;
import itsci.mju.com.mjurescue.ViewStudentProfile.ViewStudentProfileFragment;
import itsci.mju.com.mjurescue.R;


public class HomeStudentActivity extends AppCompatActivity {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_tabs_notfi,
            R.drawable.ic_tabs_profile,
            R.drawable.ic_tabs_news,
            R.drawable.ic_tabs_aid
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        Intent i = new Intent(getApplicationContext(),GPS_ServiceStudentNow.class);
        startService(i);
    }

    /**
     * Set icon to tab menu
     */
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

    /**
     * Add Fragment to tab menu
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new RequestAssistanceFragment(),"Alert");
        adapter.addFrag(new ViewStudentProfileFragment(), "Profile");
        adapter.addFrag(new ListTitleNewsFragment(), "News");
        adapter.addFrag(new ListTitleAIDFragment(), "AID");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
