package com.bsuuv.grocerymanager.db.viewmodel;

import com.bsuuv.grocerymanager.util.DateHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Calendar;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DateHelperTests {

    @Mock
    private Calendar mMockCalendar;

    @InjectMocks
    private DateHelper mDateHelper;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(mMockCalendar.get(anyInt())).thenReturn(2);
    }

    @Test
    public void timeUntilNextGroceryDay_groceryWeekDayInFuture() {

    }
}
