package com.vitaz.gifaro

import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.GeneralSwipeAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Swipe
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.*
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vitaz.gifaro.misc.Constants
import com.vitaz.gifaro.test_utils.MyViewAction
import com.vitaz.gifaro.test_utils.RecyclerViewItemCountAssertion
import com.vitaz.gifaro.test_utils.atPosition
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun onLaunchTabLayoutDisplayFirstOption() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withText("All"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun tabLayoutCanChangeTheFragmentOnClick() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withText("Favourites"))
            .perform(click())

        // Fragment can be changed on click
        onView(allOf(withId(R.id.error_message), isCompletelyDisplayed()))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    fun canAddAndDeleteGifToFavourite() {
        ActivityScenario.launch(MainActivity::class.java)

        Thread.sleep(2000)

        // Add first item to favourites
        onView(withId(R.id.gifListRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, MyViewAction.clickChildViewWithId(R.id.favouriteButton)));

        Thread.sleep(500)

        onView(withText("Favourites"))
            .perform(click())

        // Check if favourites fragment loaded successfully
        onView(allOf(withId(R.id.favouriteListRecyclerView), isCompletelyDisplayed()))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

        // Make sure only one item in the list
        onView(withId(R.id.favouriteListRecyclerView)).check(RecyclerViewItemCountAssertion(1))

        // Click on delete button
        onView(withId(R.id.favouriteListRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, MyViewAction.clickChildViewWithId(R.id.deleteButton)))

        Thread.sleep(500)

        // Make sure the only favourite item have been deleted and message is being shown
        onView(allOf(withId(R.id.error_message), isCompletelyDisplayed()))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

    }

    @Test
    fun scrollingOfMainListAddNewItems() {
        ActivityScenario.launch(MainActivity::class.java)

        Thread.sleep(2000)

        // Get number of initial items
        onView(withId(R.id.gifListRecyclerView)).check(RecyclerViewItemCountAssertion(Constants.REQUEST_LIMIT))

        // Reach the bottom of the list
        onView(withId(R.id.gifListRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(Constants.REQUEST_LIMIT - 1, scrollTo()))

        //Scroll down a bit more to trigger data fetching
        onView(withId(R.id.gifListRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(Constants.REQUEST_LIMIT - 2, GeneralSwipeAction(
                Swipe.SLOW, GeneralLocation.BOTTOM_CENTER, GeneralLocation.TOP_CENTER,
                Press.FINGER)
            )
        )

        // Wait for loading
        Thread.sleep(2000)

        // Check if the number doubled
        onView(withId(R.id.gifListRecyclerView)).check(RecyclerViewItemCountAssertion(Constants.REQUEST_LIMIT * 2))
    }

    @Test
    fun newQueryInSearchViewLoadNewData() {
        ActivityScenario.launch(MainActivity::class.java)

        Thread.sleep(2000)

        val testString = "Test"

        onView(withId(R.id.search)).perform(typeText(testString), pressKey(
            KeyEvent.KEYCODE_ENTER))

        // Wait for loading
        Thread.sleep(2000)

        onView(withId(R.id.gifListRecyclerView))
            .check(matches(atPosition(0, hasDescendant(withText(containsString(testString))))))

    }

}
