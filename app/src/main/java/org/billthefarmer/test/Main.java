
package org.billthefarmer.test;

import android.app.Activity;
import android.os.Bundle;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.billthefarmer.view.CustomCalendarView;

public class Main extends Activity
{
    public final static String TAG = "Main";

    private CustomCalendarView calendarView;

    // onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Initialize CustomCalendarView from layout
        calendarView = (CustomCalendarView) findViewById(R.id.calendar);

        // Show Monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);

        // Show/hide overflow days of a month
        // calendarView.setShowOverflowDate(false);

        // Initialize calendar with date
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());

        // call refreshCalendar to update calendar the view
        calendarView.refreshCalendar(currentCalendar);
    }
}
