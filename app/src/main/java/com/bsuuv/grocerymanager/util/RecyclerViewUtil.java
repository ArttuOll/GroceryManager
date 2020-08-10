package com.bsuuv.grocerymanager.util;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewUtil {

    public static void toggleRecyclerViewVisibility(RecyclerView recyclerView,
                                                    TextView recyclerViewPlaceholder,
                                                    int visibility, int placeholderStrResourceId) {
        if (visibility == View.VISIBLE) {
            recyclerView.setVisibility(visibility);
            recyclerViewPlaceholder.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            recyclerViewPlaceholder.setVisibility(View.VISIBLE);
            recyclerViewPlaceholder.setText(placeholderStrResourceId);
        }
    }
}
