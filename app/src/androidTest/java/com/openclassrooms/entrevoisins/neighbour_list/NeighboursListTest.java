
package com.openclassrooms.entrevoisins.neighbour_list;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNull.notNullValue;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;
import com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity;
import com.openclassrooms.entrevoisins.utils.DeleteViewAction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
/**
 * Test class for list of neighbours
 */
@RunWith(AndroidJUnit4.class)
public class NeighboursListTest {

    // This is fixed
    private static int ITEMS_COUNT = 12;

    private ListNeighbourActivity mActivity;

    private NeighbourApiService mApiService;

    @Rule
    public ActivityTestRule<ListNeighbourActivity> mActivityRule =
            new ActivityTestRule(ListNeighbourActivity.class);

    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
        mApiService = DI.getNeighbourApiService();
    }

    /**
     * We ensure that our recyclerview is displaying at least on item
     */
    @Test
    public void myNeighboursList_shouldNotBeEmpty() {
        // First scroll to the position that needs to be matched and click on it.
        onView(allOf(withId(R.id.list_neighbours), isDisplayed()))
                .check(matches(hasMinimumChildCount(1)));
    }

    /**
     * When we delete an item, the item is no more shown
     */
    @Test
    public void myNeighboursList_deleteAction_shouldRemoveItem() {
        // Given : We remove the element at position 2
        onView(allOf(withId(R.id.list_neighbours), isDisplayed()))
                .check(withItemCount(ITEMS_COUNT));
        // When perform a click on a delete icon
        onView(allOf(withId(R.id.list_neighbours), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteViewAction()));
        // Then : the number of element is 11
        onView(allOf(withId(R.id.list_neighbours), isDisplayed()))
                .check(withItemCount(ITEMS_COUNT-1));
    }

    /**
     * When we click on an item, an activity is opened
     * with al the information of the neighbour
     */
    @Test
    public void neighbour_shouldBeDisplayed() {
        onView(allOf(withId(R.id.list_neighbours), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Neighbour neighbour = mApiService
                .getNeighbours().get(0);
        onView(withId(R.id.avatar)).check(matches(isDisplayed()));
        onView(withId(R.id.nameWhite))
                .check(matches(withText(neighbour.getName())));
        onView(withId(R.id.phoneNumber))
                .check(matches(withText(neighbour.getPhoneNumber())));
        onView(withId(R.id.address))
                .check(matches(withText(neighbour.getAddress())));
        onView(withId(R.id.socialNetWork))
                .check(matches(withText("www.facebook.fr/"+neighbour.getName().toLowerCase())));
        onView(withId(R.id.aboutMe))
                .check(matches(withText(neighbour.getAboutMe())));
    }

    /**
     * when we go to the Favorite Tab we make sure that
     * only the favorite are displayed
     * @throws InterruptedException
     */
    @Test
    public void myFavoriteNeighboursList_shouldOnlyDisplayFavorite() throws InterruptedException {
        // First swipe to favorite to check if it is empty
        onView(allOf(withId(R.id.list_neighbours), isDisplayed()))
                .perform(swipeLeft());
        // wait a little for the emulator to have time to swipe
        Thread.sleep(500);
        onView(allOf(withId(R.id.list_neighbours), isDisplayed()))
                .check(withItemCount(0));
        onView(allOf(withId(R.id.list_neighbours), isDisplayed()))
                .perform(swipeRight());
        // wait a little for the emulator to have time to swipe
        Thread.sleep(500);
        onView(allOf(withId(R.id.list_neighbours), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(allOf(withId(R.id.add_favorite_neighbour), isDisplayed()))
                .perform(click());
        onView(withContentDescription(R.string.abc_action_bar_up_description))
                .perform(click());
        onView(allOf(withId(R.id.list_neighbours), isDisplayed()))
                .perform(swipeLeft());
        // wait a little for the emulator to have time to swipe
        Thread.sleep(500);
        onView(allOf(withId(R.id.list_neighbours), isDisplayed()))
                .check(matches(hasMinimumChildCount(1)));
    }
}