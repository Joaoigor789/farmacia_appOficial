package com.example.farmacia_app;

import android.graphics.Bitmap;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;


    //vinculado ao VigilanciaTelaActivity
    // ImageFragmentStateAdapter - Adapter para ViewPager2
    public class ImageFragmentStateAdapter extends FragmentStateAdapter {
        private final List<Integer> imageList;

        public ImageFragmentStateAdapter(FragmentActivity fa, List<Integer> imageList) {
            super(fa);
            this.imageList = imageList;
        }

        @NotNull
        @Override
        public Fragment createFragment(int position) {
            return new VigilanciaTelaActivity.ImageFragment(imageList.get(position));
        }

        @Override
        public int getItemCount() {
            return imageList.size();
        }
    }



