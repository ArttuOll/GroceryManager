package com.bsuuv.grocerymanager.ui.NavigationTests;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.ui.MainActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@SmallTest
@RunWith(AndroidJUnit4.class)
public class MainActivityNavigationTest {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule =
      new ActivityTestRule<>(MainActivity.class);

  @Test
  public void toConfigs() {
    openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation()
        .getTargetContext());
    onView(withText("Manage food-items")).perform(click());
    onView(withId(R.id.config_recyclerview_placeholder)).check(matches(isDisplayed()));
  }

  @Test
  public void isInView() {
    onView(withId(R.id.main_recyclerview_placeholder)).check(matches(isDisplayed()));
  }

}
