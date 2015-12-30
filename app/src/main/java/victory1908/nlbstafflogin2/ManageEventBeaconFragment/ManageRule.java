package victory1908.nlbstafflogin2.ManageEventBeaconFragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import victory1908.nlbstafflogin2.BaseFragment;
import victory1908.nlbstafflogin2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManageRule extends BaseFragment {

    public ManageRule() {
        // Required empty public constructor
    }

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewFragment = inflater.inflate(R.layout.fragment_managerule, container, false);

        viewPager = (ViewPager) viewFragment.findViewById(R.id.viewPager);

        tabLayout = (TabLayout) viewFragment.findViewById(R.id.tabLayout);

        viewPager.setAdapter(new CustomAdapter(getChildFragmentManager(), getContext()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        return viewFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        //        tabLayout.setupWithViewPager(viewPager);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });

        if (ViewCompat.isLaidOut(tabLayout)) {
            tabLayout.setupWithViewPager(viewPager);
        } else {
            tabLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    tabLayout.setupWithViewPager(viewPager);
                    tabLayout.removeOnLayoutChangeListener(this);
                }
            });
        }

        super.onActivityCreated(savedInstanceState);

    }


    //TabLayout and ViewPager class
    private class CustomAdapter extends FragmentPagerAdapter {

        private String fragments[] = {"Assign Rules", "Manage Rules"};

        public CustomAdapter(FragmentManager fragmentManager, Context context) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new AssignEventToBeacon();
                case 1:
                    return new ManageEventAssigned();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments[position];
        }

    }

}
