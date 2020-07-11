package com.bsuuv.grocerymanager.activities.NavigationTests;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@SmallTest
@RunWith(AndroidJUnit4.class)
public class MainActivityNavigationTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void toConfigsAndBack() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation()
                .getTargetContext());
        onView(withText("Configure")).perform(click());
        onView(withId(R.id.config_recyclerview)).check(matches(isDisplayed()));

        onView(withContentDescription("Navigate up")).perform(click());
        onView(withId(R.id.main_recyclerview)).check(matches(isDisplayed()));
    }

    @Test
    public void isInView() {
        onView(withId(R.id.main_recyclerview)).check(matches(isDisplayed()));
    }

}
