package com.bsuuv.grocerymanager.ui.NavigationTests;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;

import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.ui.NewFoodItem;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@SmallTest
@RunWith(AndroidJUnit4.class)
public class NewFoodItemNavigationTest {

    @Rule
    public ActivityTestRule<NewFoodItem> mNewFoodItemTestRule =
            new ActivityTestRule<>(NewFoodItem.class);

    @Test
    public void allElementsInView() {
        onView(withId(R.id.imageView_new_fooditem)).check(matches(isDisplayed()));
        onView(withId(R.id.new_fooditem_unit_dropdown)).check(matches(isDisplayed()));
        onView(withId(R.id.fab_new_fooditem)).check(matches(isDisplayed()));
        onView(withId(R.id.editText_label)).check(matches(isDisplayed()));
        onView(withId(R.id.editText_info)).check(matches(isDisplayed()));
        onView(withId(R.id.editText_freq)).check(matches(isDisplayed()));
        onView(withId(R.id.editText_amount)).check(matches(isDisplayed()));
        onView(withId(R.id.editText_brand)).check(matches(isDisplayed()));
    }
}
