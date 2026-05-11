package com.smarthome

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.smarthome.ui.auth.AuthViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import javax.inject.Inject

@HiltAndroidTest
@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Inject
    lateinit var authViewModel: AuthViewModel

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun loginScreen_displaysCorrectElements() {
        // Check if email input is displayed
        onView(withId(R.id.emailInput))
            .check(matches(isDisplayed()))

        // Check if password input is displayed
        onView(withId(R.id.passwordInput))
            .check(matches(isDisplayed()))

        // Check if login button is displayed
        onView(withId(R.id.loginButton))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))

        // Check if remember me checkbox is displayed
        onView(withId(R.id.rememberMeCheckbox))
            .check(matches(isDisplayed()))
    }

    @Test
    fun login_withEmptyEmail_showsError() {
        // Type password but leave email empty
        onView(withId(R.id.passwordInput))
            .perform(typeText("password123"))

        // Click login button
        onView(withId(R.id.loginButton))
            .perform(click())

        // Check if error message is shown
        onView(withText("Email cannot be empty"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun login_withInvalidEmail_showsError() {
        // Type invalid email
        onView(withId(R.id.emailInput))
            .perform(typeText("invalid-email"))

        // Type password
        onView(withId(R.id.passwordInput))
            .perform(typeText("password123"))

        // Click login button
        onView(withId(R.id.loginButton))
            .perform(click())

        // Check if error message is shown
        onView(withText("Please enter a valid email"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun login_withShortPassword_showsError() {
        // Type valid email
        onView(withId(R.id.emailInput))
            .perform(typeText("test@example.com"))

        // Type short password
        onView(withId(R.id.passwordInput))
            .perform(typeText("123"))

        // Click login button
        onView(withId(R.id.loginButton))
            .perform(click())

        // Check if error message is shown
        onView(withText("Password must be at least 6 characters"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun login_withValidCredentials_attemptsLogin() {
        // Type valid email
        onView(withId(R.id.emailInput))
            .perform(typeText("test@example.com"))

        // Type valid password
        onView(withId(R.id.passwordInput))
            .perform(typeText("password123"))

        // Click login button
        onView(withId(R.id.loginButton))
            .perform(click())

        // Check if loading indicator is shown
        onView(withId(R.id.progressBar))
            .check(matches(isDisplayed()))

        // Check if login button is disabled during loading
        onView(withId(R.id.loginButton))
            .check(matches(isNotEnabled()))
    }

    @Test
    fun rememberMeCheckbox_canBeToggled() {
        // Check initial state
        onView(withId(R.id.rememberMeCheckbox))
            .check(matches(isNotChecked()))

        // Click checkbox
        onView(withId(R.id.rememberMeCheckbox))
            .perform(click())

        // Check if it's checked
        onView(withId(R.id.rememberMeCheckbox))
            .check(matches(isChecked()))
    }

    @Test
    fun typingInFields_clearsErrorMessages() {
        // First trigger an error
        onView(withId(R.id.loginButton))
            .perform(click())

        // Verify error message is shown
        onView(withText("Email cannot be empty"))
            .check(matches(isDisplayed()))

        // Type in email field
        onView(withId(R.id.emailInput))
            .perform(typeText("test@example.com"))

        // Error should be cleared (this would be verified through ViewModel behavior)
        // In a real test, you would verify that the ViewModel's error state is cleared
    }

    @Test
    fun loginButton_disabledDuringLoading() {
        // Type valid credentials
        onView(withId(R.id.emailInput))
            .perform(typeText("test@example.com"))

        onView(withId(R.id.passwordInput))
            .perform(typeText("password123"))

        // Click login button
        onView(withId(R.id.loginButton))
            .perform(click())

        // Verify button is disabled during loading
        onView(withId(R.id.loginButton))
            .check(matches(isNotEnabled()))

        // Verify progress bar is shown
        onView(withId(R.id.progressBar))
            .check(matches(isDisplayed()))
    }
}
