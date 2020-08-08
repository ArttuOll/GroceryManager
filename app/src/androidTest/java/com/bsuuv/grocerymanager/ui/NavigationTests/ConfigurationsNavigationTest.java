package com.bsuuv.grocerymanager.ui.NavigationTests;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;

import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.ui.ConfigurationsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@SmallTest
@RunWith(AndroidJUnit4.class)
public class ConfigurationsNavigationTest {
    @Rule
    public ActivityTestRule<ConfigurationsActivity> mConfigsTestRule =
            new ActivityTestRule<>(ConfigurationsActivity.class);

    @Test
    public void isInView() {
        onView(withId(R.id.config_recyclerview_placeholder)).check(matches(isDisplayed()));
    }

    @Test
    public void toNewFoodItem() {
        onView(withId(R.id.configs_fab)).perform(click());
        onView(withId(R.id.imageView_new_fooditem)).check(matches(isDisplayed()));
    }
}
