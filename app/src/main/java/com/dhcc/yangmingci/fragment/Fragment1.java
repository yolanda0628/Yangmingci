package com.dhcc.yangmingci.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.dhcc.yangmingci.R;
import com.dhcc.yangmingci.index.Index0Fragment;
import com.dhcc.yangmingci.index.Index1Fragment;
import com.dhcc.yangmingci.index.Index2Fragment;

import java.util.ArrayList;

/**
 * Created by pengbangqin on 2016/8/3.
 */
public class Fragment1 extends Fragment {
    private ArrayList<Fragment> fragments;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=View.inflate(getActivity(), R.layout.fragment1,null);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        PagerSlidingTabStrip tab = (PagerSlidingTabStrip) view.findViewById(R.id.tab);
        fragments = new ArrayList<>();
        fragments.add(new Index0Fragment());
        fragments.add(new Index1Fragment());
        fragments.add(new Index2Fragment());

        viewPager.setAdapter(new IndexAdapter(getActivity().getSupportFragmentManager()));
        tab.setViewPager(viewPager);
        return view;
    }

    private class IndexAdapter extends FragmentPagerAdapter {

        private String[] titles = {"统计","安全", "介绍"};

        public IndexAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

}