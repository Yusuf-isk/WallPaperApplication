package com.example.wallpaperapplication.utils;

import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.DisplayMetrics;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.wallpaperapplication.R;

import java.io.IOException;
import java.util.BitSet;

public class Functions {

    public static void changeMainFragment(FragmentActivity fragmentActivity, Fragment fragment) {
        fragmentActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container,fragment)
                .commit();

    }
    public static void changeMainFragmentWithBack(FragmentActivity fragmentActivity, Fragment fragment) {
        fragmentActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container,fragment)
                .addToBackStack(null)
                .commit();

    }
    public static  boolean setWallpaper(Activity activity, Bitmap bitmap) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Bitmap tempBitmap = scaleCenterCrop(bitmap,height,width);
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(activity);
        try {
            wallpaperManager.setBitmap(tempBitmap);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static Bitmap scaleCenterCrop(Bitmap source,int newHeight,int newWidth) {
        int sourceHeight = source.getHeight();
        int sourceWidth = source.getWidth();

        float xScale = (float) newWidth/sourceWidth;
        float yScale = (float) newHeight/sourceHeight;
        float scale = Math.max(xScale,yScale);

        float scaleWidth = scale *sourceWidth;
        float scaleHeight = scale * sourceHeight;

        float left = (newWidth - scaleWidth) /2;
        float top = (newHeight - scaleHeight)/2;

        RectF targetRect = new RectF(left,top,left+scaleWidth,top+scaleHeight);
        Bitmap dest = Bitmap.createBitmap(newWidth,newHeight,source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source,null,targetRect,null);

        return dest;

    }
}
