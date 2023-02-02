package com.fastfoodapp.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fastfoodapp.Fragments.CategorisFragment;
import com.fastfoodapp.Fragments.FoodsFragment;

public class TabsAccessorSettingsAdapter extends FragmentPagerAdapter {


    public TabsAccessorSettingsAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public TabsAccessorSettingsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                CategorisFragment categorisFragment = new CategorisFragment();
                return categorisFragment;
            case 1:
                FoodsFragment foodsFragment = new FoodsFragment();
                return foodsFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Catégories";
            case 1:
                return "Produits";
            default:
                return null;
        }
    }
}
