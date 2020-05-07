package ysn.com.demo.scalelayoutbehavior.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author yangsanning
 * @ClassName FragmentPagerAdapter
 * @Description 一句话概括作用
 * @Date 2020/5/7
 */
public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

    private final List<String> titles = new ArrayList<>();
    private final List<Fragment> fragments = new ArrayList<>();

    public FragmentPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(String title, Fragment fragment) {
        titles.add(title);
        fragments.add(fragment);
    }
}