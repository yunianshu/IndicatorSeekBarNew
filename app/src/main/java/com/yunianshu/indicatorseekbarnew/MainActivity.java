package com.yunianshu.indicatorseekbarnew;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.yunianshu.indicatorseekbarnew.donation.BaseActivity;
import com.yunianshu.indicatorseekbarnew.donation.DonationFragment;
import com.yunianshu.indicatorseekbarnew.fragment.ContinuousFragment;
import com.yunianshu.indicatorseekbarnew.fragment.CustomFragment;
import com.yunianshu.indicatorseekbarnew.fragment.DiscreteFragment;
import com.yunianshu.indicatorseekbarnew.fragment.IndicatorFragment;
import com.yunianshu.indicatorseekbarnew.fragment.JavaBuildFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * created by zhuangguangquan on 2017/9/6
 */

public class MainActivity extends BaseActivity {

    private String[] mTypeTabs = new String[0];
    private List<Fragment> mFragmentList = new ArrayList<>();

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initCreate() {
        super.initCreate();
        initFragment();
        initViews();
    }

    private void initFragment() {
        mFragmentList.add(new ContinuousFragment());
        mFragmentList.add(new DiscreteFragment());
        mFragmentList.add(new CustomFragment());
        mFragmentList.add(new JavaBuildFragment());
        mFragmentList.add(new IndicatorFragment());
        mFragmentList.add(new DonationFragment());
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        setSupportActionBar(toolbar);
        updateLayoutDirectionStatus(toolbar);
        mTypeTabs = getResources().getStringArray(R.array.tab_types);

        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));

        tabLayout.setupWithViewPager(viewPager);
    }

    private void updateLayoutDirectionStatus(Toolbar toolbar) {
        boolean isRtl = getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
        int directionResId = isRtl ? R.string.layout_direction_rtl : R.string.layout_direction_ltr;
        toolbar.setSubtitle(getString(R.string.layout_direction_status, getString(directionResId)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_language) {
            showLanguageSwitchDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLanguageSwitchDialog() {
        final String[] languageNames = getResources().getStringArray(R.array.language_names);
        final String[] languageValues = getResources().getStringArray(R.array.language_values);
        int checkedIndex = getCheckedLanguageIndex(languageValues);
        new AlertDialog.Builder(this)
                .setTitle(R.string.language_switch_title)
                .setSingleChoiceItems(languageNames, checkedIndex, (dialog, which) -> {
                    switchLanguage(languageValues[which]);
                    dialog.dismiss();
                })
                .show();
    }

    private int getCheckedLanguageIndex(String[] languageValues) {
        String selectedLanguageTag = LocaleHelper.getSavedLanguageTag(this);
        if (selectedLanguageTag.isEmpty()) {
            selectedLanguageTag = LocaleHelper.getCurrentLanguageTag(this);
        }
        for (int i = 0; i < languageValues.length; i++) {
            if (LocaleHelper.isSameLanguage(selectedLanguageTag, languageValues[i])) {
                return i;
            }
        }
        return 0;
    }

    private void switchLanguage(String languageTag) {
        String selectedLanguageTag = LocaleHelper.getSavedLanguageTag(this);
        if (selectedLanguageTag.isEmpty()) {
            selectedLanguageTag = LocaleHelper.getCurrentLanguageTag(this);
        }
        if (LocaleHelper.isSameLanguage(selectedLanguageTag, languageTag)) {
            return;
        }
        LocaleHelper.setSavedLanguageTag(this, languageTag);
        recreate();
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mTypeTabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTypeTabs[position];
        }
    }


}
