package com.bsuuv.grocerymanager.util;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Utility class for repeating <code>RecyclerView</code> related tasks.
 */
public class RecyclerViewUtil {

  /**
   * @param recyclerView             The <code>RecyclerView</code> to act on
   * @param recyclerViewPlaceholder  Placeholder <code>TextView</code> for the <code>RecyclerView</code>
   * @param visibility               Visibility value from the class <code>View</code> to set on the
   *                                 <code>RecyclerView</code>
   * @param placeholderStrResourceId String resource id for the string to be displayed in the
   *                                 placeholder
   */
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
