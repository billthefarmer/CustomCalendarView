
package org.billthefarmer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// CustomCalendarView
public class CustomCalendarView extends LinearLayout
{
    // Days of week
    private static final int weekdays[] =
    {
        R.id.weekday_1, R.id.weekday_2, R.id.weekday_3,
        R.id.weekday_4, R.id.weekday_5, R.id.weekday_6,
        R.id.weekday_7
    };

    // Weeks of month
    private static final int weeks[] =
    {
        R.id.week_1, R.id.week_2, R.id.week_3,
        R.id.week_4, R.id.week_5, R.id.week_6
    };

    // Days of month
    private static final int days[] =
    {
        R.id.day_1,  R.id.day_2,  R.id.day_3,  R.id.day_4,
        R.id.day_5,  R.id.day_6,  R.id.day_7,  R.id.day_8,
        R.id.day_9,  R.id.day_10, R.id.day_11, R.id.day_12,
        R.id.day_13, R.id.day_14, R.id.day_15, R.id.day_16,
        R.id.day_17, R.id.day_18, R.id.day_19, R.id.day_20,
        R.id.day_21, R.id.day_22, R.id.day_23, R.id.day_24,
        R.id.day_25, R.id.day_26, R.id.day_27, R.id.day_28,
        R.id.day_29, R.id.day_30, R.id.day_31, R.id.day_32,
        R.id.day_33, R.id.day_34, R.id.day_35, R.id.day_36,
        R.id.day_37, R.id.day_38, R.id.day_39, R.id.day_40,
        R.id.day_41,  R.id.day_42
    };

    private Context context;

    private int firstDayOfWeek = Calendar.MONDAY;
    private List<DayDecorator> decorators = null;
    private CalendarListener calendarListener;
    private Calendar currentCalendar;

    private int currentMonthIndex = 0;
    private boolean isOverflowDateVisible = true;

    public CustomCalendarView (Context context, AttributeSet attrs)
    {
        super(context, attrs);

        this.context = context;

        initializeCalendar();
    }

    // CustomCalendarView
    private void CustomCalendarView()
    {
        final LayoutInflater inflater =
            (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.main, this, true);

        ImageButton previous = (ImageButton) findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    currentMonthIndex--;
                    currentCalendar = Calendar.getInstance(Locale.getDefault());
                    currentCalendar.add(Calendar.MONTH, currentMonthIndex);

                    refreshCalendar(currentCalendar);
                    if (calendarListener != null)
                    {
                        calendarListener.onMonthChanged(currentCalendar);
                    }
                }
            });

        ImageButton next = (ImageButton) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener()
            {
            @Override
            public void onClick(View v)
                {
                currentMonthIndex++;
                currentCalendar = Calendar.getInstance(Locale.getDefault());
                currentCalendar.add(Calendar.MONTH, currentMonthIndex);
                refreshCalendar(currentCalendar);

                if (calendarListener != null)
                {
                    calendarListener.onMonthChanged(currentCalendar);
                }
            }
        });

        // Initialize calendar for current month
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());

        setFirstDayOfWeek(Calendar.MONDAY);
        refreshCalendar(currentCalendar);
    }

    // initializeWeekLayout
    private void initializeWeekLayout()
    {
        TextView dayOfWeek;
        String dayOfTheWeekString;

        final String[] weekDaysArray =
            DateFormatSymbols.getInstance(Locale.getDefault()).getShortWeekdays();
        for (int i = 1; i < weekDaysArray.length; i++)
        {
            dayOfTheWeekString = weekDaysArray[i];
            if (dayOfTheWeekString.length() > 3)
            {
                dayOfTheWeekString = dayOfTheWeekString.substring(0, 3).toUpperCase();
            }

            dayOfWeek =
                (TextView) findViewById(weekdays[getWeekIndex(i, currentCalendar) - 1]);
            dayOfWeek.setText(dayOfTheWeekString);
        }
    }

    // setDaysInCalendar
    private void setDaysInCalendar()
    {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTime(currentCalendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.setFirstDayOfWeek(getFirstDayOfWeek());
        int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);

        // Calculate dayOfMonthIndex
        int dayOfMonthIndex = getWeekIndex(firstDayOfMonth, calendar);
        int actualMaximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        final Calendar startCalendar = (Calendar) calendar.clone();
        //Add required number of days
        startCalendar.add(Calendar.DATE, -(dayOfMonthIndex - 1));
        int monthEndIndex = 42 - (actualMaximum + dayOfMonthIndex - 1);

        DayView dayView;
        for (int i = 1; i <= 42; i++)
        {
            dayView = (DayView) findViewById(days[i - 1]);
            if (dayView == null)
                continue;

            //Apply the default styles
            dayView.setOnClickListener(null);
            dayView.bind(startCalendar.getTime(), getDecorators());
            dayView.setVisibility(View.VISIBLE);

            if (CalendarUtils.isSameMonth(calendar, startCalendar))
            {
                dayView.setOnClickListener(onDayOfMonthClickListener);
                dayView.setBackgroundColor(calendarBackgroundColor);
                dayView.setTextColor(dayOfWeekTextColor);
                //Set the current day color
                markDayAsCurrentDay(startCalendar);
            }

            else
            {
                dayView.setBackgroundColor(disabledDayBackgroundColor);
                dayView.setTextColor(disabledDayTextColor);

                if (!isOverflowDateVisible())
                    dayView.setVisibility(View.GONE);

                else if (i >= 36 && ((float) monthEndIndex / 7.0f) >= 1)
                {
                    dayView.setVisibility(View.GONE);
                }
            }
            dayView.decorate();


            startCalendar.add(Calendar.DATE, 1);
            dayOfMonthIndex++;
        }

        // If the last week row has no visible days, hide it or show it in case
        ViewGroup weekRow = (ViewGroup) findViewById(weeks[5]);
        dayView = (DayView) findViewById(days[35]);
        if (dayView.getVisibility() != VISIBLE)
        {
            weekRow.setVisibility(GONE);
        }

        else
        {
            weekRow.setVisibility(VISIBLE);
        }
    }
}
