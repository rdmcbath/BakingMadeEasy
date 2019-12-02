package com.example.android.bakingmadeeasy.UI;


import androidx.test.espresso.ViewInteraction;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.example.android.bakingmadeeasy.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class Exo2Test {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void exo2Test() {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.main_recipe_recycler), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(3, click()));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.recipe_steps_list),
                        withParent(withId(R.id.recipe_steps_container)),
                        isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(2, click()));

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.next_button), withText("Next")));
        appCompatButton.perform(scrollTo(), click());

    }

}
