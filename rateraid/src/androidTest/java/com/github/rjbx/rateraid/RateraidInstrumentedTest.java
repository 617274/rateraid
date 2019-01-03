package com.github.rjbx.rateraid;

import android.content.Context;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import android.widget.Button;

import com.github.rjbx.calibrater.Calibrater;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@androidx.test.filters.LargeTest
@RunWith(AndroidJUnit4.class)
public class RateraidInstrumentedTest {

    private Button mIncrementButton;
    private Button mDecrementButton;
    private Context mContext;
    private long mCount;
    private Rateraid.Builder mWeightsBuilder;

    @Before
    public void setUp() {
        mContext = InstrumentationRegistry.getInstrumentation().getContext();
        mIncrementButton = new Button(mContext);
        mDecrementButton = new Button(mContext);
    }

    @Test
    public void testAddButtonSetClickListenerCallback() {
        Float[] percentages = { .25f, .25f, .25f, .25f };
        for (float magnitude = 0.01f; magnitude < 0.1f; magnitude += 0.01f) {
            mWeightsBuilder = Rateraid.with(percentages, magnitude);
            for (int index = 0; index < 4; index++) {

                mWeightsBuilder.addButtonSet(mIncrementButton, mDecrementButton, index);
                mWeightsBuilder.build();
                float sum;

                mIncrementButton.performClick();
                mDecrementButton.performClick();
                mDecrementButton.performClick();
                mIncrementButton.performClick();
                assertEquals(.25f, percentages[index], magnitude);
                sum = 0;
                for (int i = 0; i < percentages.length; i++) {
                    sum += percentages[i];
                    if (i == percentages.length - 1) assertEquals(1f, sum, magnitude);
                }
                while (percentages[index] < 1f) {
                    mIncrementButton.performClick();
                    sum = 0;
                    for (int i = 0; i < percentages.length; i++) {
                        sum += percentages[i];
                        if (i == percentages.length - 1) assertEquals(1f, sum, magnitude);
                    }
                }

                mDecrementButton.performClick();
                mDecrementButton.performClick();
                mIncrementButton.performClick();
                mIncrementButton.performClick();
                assertEquals(1f, percentages[index], magnitude);
                sum = 0;
                for (int i = 0; i < percentages.length; i++) {
                    sum += percentages[i];
                    if (i == percentages.length - 1) assertEquals(1f, sum, magnitude);
                }
                while (percentages[index] > 0f) {
                    mDecrementButton.performClick();
                    sum = 0;
                    for (int i = 0; i < percentages.length; i++) {
                        sum += percentages[i];
                        if (i == percentages.length - 1) assertEquals(1f, sum, magnitude);
                    }
                }

                mIncrementButton.performClick();
                mIncrementButton.performClick();
                mDecrementButton.performClick();
                mDecrementButton.performClick();
                assertEquals(0f, percentages[index], magnitude);
                sum = 0;
                for (int i = 0; i < percentages.length; i++) {
                    sum += percentages[i];
                    if (i == percentages.length - 1) assertEquals(1f, sum, magnitude);
                }
                while (percentages[index] < .25f) {
                    mIncrementButton.performClick();
                    sum = 0;
                    for (int i = 0; i < percentages.length; i++) {
                        sum += percentages[i];
                        if (i == percentages.length - 1) assertEquals(1f, sum, magnitude);
                    }
                }

                Calibrater.resetRatings(percentages, false);
            }
        }
    }

    @Test
    public void testSetClickListenerUserDefinedCallback() {
        Float[] percentages = { .25f, .25f, .25f, .25f };
        for (float magnitude = 0.01f; magnitude < 0.1f; magnitude += 0.01f) {
            mWeightsBuilder = Rateraid.with(percentages, magnitude);
            for (int index = 0; index < 4; index++) {

                mWeightsBuilder.addButtonSet(mIncrementButton, mDecrementButton, index);
                mWeightsBuilder.build().setOnClickListener(clickedView -> mCount++);

                mIncrementButton.performClick();
                mDecrementButton.performClick();
                mDecrementButton.performClick();
                mIncrementButton.performClick();
                while (percentages[index] < 1f) mIncrementButton.performClick();

                mDecrementButton.performClick();
                mDecrementButton.performClick();
                mIncrementButton.performClick();
                mIncrementButton.performClick();
                while (percentages[index] > 0f) mDecrementButton.performClick();

                mIncrementButton.performClick();
                mIncrementButton.performClick();
                mDecrementButton.performClick();
                mDecrementButton.performClick();
                while (percentages[index] < .25f) mIncrementButton.performClick();

                Calibrater.resetRatings(percentages, false);
            }
        }
        assertEquals(2896, mCount);
    }
}