package vfa.vfdemo.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import vfa.vfdemo.R;
import vfa.vfdemo.data.ImageEntity;
import vfa.vflib.fragments.VFFragment;

/**
 * Created by Vitalify on 3/1/17.
 */

public class FragPager extends VFFragment{

    private ViewPager _viewPager;

    List<ImageEntity> listImage = new ArrayList<>();

    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_pager;
    }

    @Override
    public void onViewLoaded() {
        listImage = ImageEntity.getDemoList();

        _viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        HDiPagerAdapter _pageAdapter = new HDiPagerAdapter(getChildFragmentManager());
        _viewPager.setAdapter(_pageAdapter);
    }

    class HDiPagerAdapter extends FragmentStatePagerAdapter {

        public HDiPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ImageEntity image = listImage.get(position);
            FragPage fg = new FragPage();
            fg.imageEntity = image;

            return fg;
        }

        @Override
        public int getCount() {
            return listImage.size();
        }
    }
}
