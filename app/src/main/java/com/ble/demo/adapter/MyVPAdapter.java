package com.ble.demo.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class MyVPAdapter extends FragmentStateAdapter {
    private List<Fragment> list;
    public MyVPAdapter(@NonNull FragmentActivity activity, List<Fragment> list) {
        super(activity);
        this.list = list;

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
