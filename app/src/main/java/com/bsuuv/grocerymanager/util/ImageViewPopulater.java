package com.bsuuv.grocerymanager.util;

import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import java.io.File;

public class ImageViewPopulater {

    public static void populateFromUri(Context context, String uri, ImageView imageView) {
        Glide.with(context).load(new File(uri)).into(imageView);
    }
}
