package com.e.go4lunch;

import android.content.Context;
import android.view.Gravity;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.repositories.WorkmatesRepository;
import com.e.go4lunch.ui.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {



    @Rule
    public ActivityTestRule activityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void bottomNavigation_displayRestaurants(){
        onView(ViewMatchers.withId(R.id.action_list)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.fragmentRestaurant)).check(matches(isDisplayed()));
    }
    @Test
    public void bottomNavigation_displayMap(){
        onView(ViewMatchers.withId(R.id.action_map)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.fragmentMap)).check(matches(isDisplayed()));
    }
    @Test
    public void bottomNavigation_displayListWorkmates(){
        onView(ViewMatchers.withId(R.id.action_workmates)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.fragmentListWorkmates)).check(matches(isDisplayed()));
    }
    @Test
    public void CheckNavigationDrawerClose(){
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(isRoot()).perform(ViewActions.swipeLeft());
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT)));
    }
    @Test
    public void display_toolbar(){
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }
    @Test
    public void click_on_showLunch(){
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.lunch_drawer));
        onView(withId(R.id.activity_details)).check(matches(isDisplayed()));
    }
    @Test
    public void click_on_logOut(){
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.logout_drawer));
        onView(withText(R.string.titlle_alert)).check(matches(isDisplayed()));
    }
   
    @Test
    public void display_Rv_Restaurant(){
        onView(ViewMatchers.withId(R.id.action_list)).perform(ViewActions.click());
        onView(withId(R.id.recycler_view_restaurant)).check(matches(isDisplayed()));
    }
    @Test
    public void display_Rv_Workmates(){
        onView(ViewMatchers.withId(R.id.action_workmates)).perform(ViewActions.click());
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
    }



}
