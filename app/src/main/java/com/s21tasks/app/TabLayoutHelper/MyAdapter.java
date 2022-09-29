package com.s21tasks.app.TabLayoutHelper;

import android.content.Context;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;



public class MyAdapter extends FragmentPagerAdapter {
    private Context myContext;
    int totalTabs ;
    public MyAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TabTotalFragment tabTotalFragment = new TabTotalFragment();
                return tabTotalFragment;
            case 1:
                SolvedFragment solvedFragment = new SolvedFragment();
                return solvedFragment;
            case 2:
                InProgressFragment inProgressFragment = new InProgressFragment();
                return inProgressFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
