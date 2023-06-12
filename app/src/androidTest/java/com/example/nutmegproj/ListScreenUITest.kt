package com.example.nutmegproj

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.testproject.presentation.rate.FailedContent
import com.example.testproject.presentation.rate.ListViewModel
import com.example.testproject.presentation.rate.LoadingContent
import com.example.testproject.presentation.rate.SuccessContent
import com.example.testproject.presentation.rate.model.BaseRateCurrencyUIContent
import com.example.testproject.presentation.rate.model.RateCurrencyUIContent
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class ListScreenUITest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var baseCurrencyField: String
    private lateinit var failedFetching: String

    @Before
    fun setUp() {
        composeTestRule.activity.apply {
            baseCurrencyField = getString(R.string.base_currency)
            failedFetching = getString(R.string.failed_fetching)
        }
    }

    @Test
    fun given_success_state_then_should_render_list() {
        var isActionInvoked = false
        composeTestRule.setContent {
            SuccessContent(
                state = ListViewModel.ListUIState.Success(
                    baseRate = BaseRateCurrencyUIContent(
                        RateCurrencyUIContent("Japanese Yen", "JPY", 1.0.toBigDecimal()),
                        1.0.toBigDecimal()
                    ),
                    rates = listOf(
                        RateCurrencyUIContent("United States Dollar", "USD", 1.0.toBigDecimal()),
                    )
                ),
                onActionInvoked = {
                    isActionInvoked = true
                }
            )
        }
        composeTestRule
            .onAllNodesWithTag("success_content_list")
            .assertCountEquals(1)
        composeTestRule
            .onNodeWithText(baseCurrencyField)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Japanese Yen")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("JPY")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("United States Dollar")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("USD")
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("amount_field")
            .performTextInput("2.0")
        assert(isActionInvoked)

        isActionInvoked = false
        composeTestRule.onNodeWithText("USD")
            .performClick()
        assert(isActionInvoked)
    }

    @Test
    fun given_failed_state_then_should_render_error_text() {
        composeTestRule.setContent {
            FailedContent()
        }
        composeTestRule.onNodeWithText(failedFetching)
            .assertIsDisplayed()
    }

    @Test
    fun given_loading_state_then_should_render_list() {
        composeTestRule.setContent {
            LoadingContent()
        }
        composeTestRule.onNodeWithTag("loading")
            .assertIsDisplayed()
    }
}