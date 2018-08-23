package com.penoder.mylibrary.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;

public class ImgLoadUtil {


    public static void loadImg(String url, ImageView imageView) {
        loadImg(url, 0, imageView, null);
    }

    public static void loadImg(String url, int placeholderImageRes, ImageView imageView) {
        loadImg(url, placeholderImageRes, imageView, null);
    }

    public static void loadImg(String url, int placeholderImageRes, ImageView imgView, RequestListener<String, GlideDrawable> requestListener) {
        Glide.with(imgView.getContext())
                .load(url)
                .placeholder(placeholderImageRes)
                .centerCrop()
                .listener(requestListener)
                .into(imgView);
    }

    public static void loadImg(String url, int placeholderImageRes, ImageView imgView, int width, int height, RequestListener<String, GlideDrawable> requestListener) {
        Glide.with(imgView.getContext())
                .load(url)
                .override(width, height)
                .placeholder(placeholderImageRes)
                .centerCrop()
                .listener(requestListener)
                .into(imgView);
    }

}
