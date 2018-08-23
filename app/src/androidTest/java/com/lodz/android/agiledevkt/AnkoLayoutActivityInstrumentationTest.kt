package com.lodz.android.agiledevkt

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.lodz.android.agiledevkt.modules.anko.AnkoLayoutActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * AnkoLayout单元测试
 * Created by zhouL on 2018/7/25.
 */
@RunWith(AndroidJUnit4::class)
class AnkoLayoutActivityInstrumentationTest {

    @get:Rule
    val mActivityRule = ActivityTestRule<AnkoLayoutActivity>(AnkoLayoutActivity::class.java)

    @Test
    fun UItest() {

        try {
            Thread.sleep(1000)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Espresso.onView(ViewMatchers.withId(android.R.id.edit))
                .perform(ViewActions.typeText("123"), ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(android.R.id.inputExtractEditText))
                .perform(ViewActions.typeText("q"), ViewActions.closeSoftKeyboard())

        Espresso.onView(ViewMatchers.withId(android.R.id.button1))
                .perform(ViewActions.click())

        val expectedText = "123-q"
        Espresso.onView(ViewMatchers.withId(android.R.id.text1))
                .check(ViewAssertions.matches(ViewMatchers.withText(expectedText)))
    }
}