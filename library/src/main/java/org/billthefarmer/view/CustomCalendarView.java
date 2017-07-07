/*
 * Copyright (c) 2016 Stacktips {link: http://stacktips.com}.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.billthefarmer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
    private static final String TAG = "CustomCalendarView";
    private static final int CALENDAR_DAYS = 42;

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

    private GestureDetector gestureDetector;

    private int firstDayOfWeek = Calendar.MONDAY;
    private List<DayDecorator> decorators = null;
    private CalendarListener calendarListener;
    private Calendar currentCalendar;
    private Date lastSelectedDay;
    private Typeface customTypeface;

    private int disabledDayBackgroundColor;
    private int disabledDayTextColor;
    private int calendarBackgroundColor;
    private int weekLayoutBackgroundColor;
    private int calendarTitleBackgroundColor;
    private int selectedDayBackground;
    private int selectedDayTextColor;
    private int calendarTitleTextColor;
    private int dayOfWeekTextColor;
    private int dayOfMonthTextColor;
    private int currentDayOfMonth;

    private int currentMonthIndex = 0;
    private int currentYearIndex = 0;

    private boolean isOverflowDateVisible = true;

    // CustomCalendarView
    public CustomCalendarView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        this.context = context;

        gestureDetector =
            new GestureDetector(context, new GestureListener());

        getAttributes(attrs);
        initializeCalendar();
    }

    // getAttributes
    private void getAttributes(AttributeSet attrs)
    {
        final TypedArray typedArray =
            context.obtainStyledAttributes(attrs, R.styleable
                                           .CustomCalendarView, 0, 0);
        calendarBackgroundColor =
            typedArray.getColor(R.styleable
                                .CustomCalendarView_calendarBackgroundColor,
                                getResources().getColor(R.color.white));
        calendarTitleBackgroundColor =
            typedArray.getColor(R.styleable
                                .CustomCalendarView_titleLayoutBackgroundColor,
                                getResources().getColor(R.color.white));
        calendarTitleTextColor =
            typedArray.getColor(R.styleable
                                .CustomCalendarView_calendarTitleTextColor,
                                getResources().getColor(R.color.black));
        weekLayoutBackgroundColor =
            typedArray.getColor(R.styleable
                                .CustomCalendarView_weekLayoutBackgroundColor,
                                getResources().getColor(R.color.white));
        dayOfWeekTextColor =
            typedArray.getColor(R.styleable
                                .CustomCalendarView_dayOfWeekTextColor,
                                getResources().getColor(R.color.black));
        dayOfMonthTextColor =
            typedArray.getColor(R.styleable
                                .CustomCalendarView_dayOfMonthTextColor,
                                getResources().getColor(R.color.black));
        disabledDayBackgroundColor =
            typedArray.getColor(R.styleable
                                .CustomCalendarView_disabledDayBackgroundColor,
                                getResources()
                                .getColor(R.color
                                          .day_disabled_background_color));
        disabledDayTextColor =
            typedArray.getColor(R.styleable
                                .CustomCalendarView_disabledDayTextColor,
                                getResources()
                                .getColor(R.color.day_disabled_text_color));
        selectedDayBackground =
            typedArray.getInteger(R.styleable
                                  .CustomCalendarView_selectedDayBackground,
                                  R.drawable.selected);
        selectedDayTextColor =
            typedArray.getColor(R.styleable
                                .CustomCalendarView_selectedDayTextColor,
                                getResources().getColor(R.color.white));
        currentDayOfMonth =
            typedArray.getColor(R.styleable
                                .CustomCalendarView_currentDayOfMonthColor,
                                getResources()
                                .getColor(R.color.current_day_of_month));
        typedArray.recycle();
    }

    // initializeCalendar
    private void initializeCalendar()
    {
        View content = View.inflate(context, R.layout.calendar, this);

        View calendar = findViewById(R.id.calendar);
        if (calendar != null)
            calendar.setBackgroundColor(calendarBackgroundColor);

        ImageButton previous = (ImageButton) findViewById(R.id.previous);
        if (previous != null)
            previous.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onSwipeRight();
            }
        });

        ImageButton next = (ImageButton) findViewById(R.id.next);
        if (next != null)
            next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onSwipeLeft();
            }
        });

        // Initialize calendar for current month
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());

        setFirstDayOfWeek(Calendar.MONDAY);
        refreshCalendar(currentCalendar);
    }

    // dispatchTouchEvent
    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        gestureDetector.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    // onSwipeLeft
    private void onSwipeLeft()
    {
        currentMonthIndex++;
        currentCalendar =
            Calendar.getInstance(Locale.getDefault());
        currentCalendar.add(Calendar.MONTH, currentMonthIndex);
        currentCalendar.add(Calendar.YEAR, currentYearIndex);
        refreshCalendar(currentCalendar);

        if (calendarListener != null)
            calendarListener.onMonthChanged(currentCalendar);
    }

    // onSwipeRight
    private void onSwipeRight()
    {
        currentMonthIndex--;
        currentCalendar =
            Calendar.getInstance(Locale.getDefault());
        currentCalendar.add(Calendar.MONTH, currentMonthIndex);
        currentCalendar.add(Calendar.YEAR, currentYearIndex);
        refreshCalendar(currentCalendar);

        if (calendarListener != null)
            calendarListener.onMonthChanged(currentCalendar);
    }

    // onSwipeDown
    private void onSwipeDown()
    {
        currentYearIndex++;
        currentCalendar =
            Calendar.getInstance(Locale.getDefault());
        currentCalendar.add(Calendar.MONTH, currentMonthIndex);
        currentCalendar.add(Calendar.YEAR, currentYearIndex);
        refreshCalendar(currentCalendar);

        if (calendarListener != null)
            calendarListener.onMonthChanged(currentCalendar);
    }

    // onSwipeUp
    private void onSwipeUp()
    {
        currentYearIndex--;
        currentCalendar =
            Calendar.getInstance(Locale.getDefault());
        currentCalendar.add(Calendar.MONTH, currentMonthIndex);
        currentCalendar.add(Calendar.YEAR, currentYearIndex);
        refreshCalendar(currentCalendar);

        if (calendarListener != null)
            calendarListener.onMonthChanged(currentCalendar);
    }

    // initializeTitleLayout
    private void initializeTitleLayout()
    {
        View titleLayout = findViewById(R.id.title);
        if (titleLayout != null)
            titleLayout.setBackgroundColor(calendarTitleBackgroundColor);

        final String monthTextArray[] =
            DateFormatSymbols.getInstance().getShortMonths();
        String monthText =
            monthTextArray[currentCalendar.get(Calendar.MONTH)];
        String titleFormat =
            context.getResources().getString(R.string.title_format);
        String titleText = String.format(titleFormat, monthText,
                                         currentCalendar.get(Calendar.YEAR));
        TextView dateTitle = (TextView) findViewById(R.id.month);
        if (dateTitle != null)
        {
            dateTitle.setTextColor(calendarTitleTextColor);
            dateTitle.setText(titleText);

            if (getCustomTypeface() != null)
                dateTitle.setTypeface(getCustomTypeface(), Typeface.BOLD);
        }
    }

    // initializeWeekLayout
    private void initializeWeekLayout()
    {
        TextView dayOfWeek;
        String dayOfTheWeekString;

        final String[] weekDaysArray =
            DateFormatSymbols.getInstance().getShortWeekdays();
        for (int i = 1; i < weekDaysArray.length; i++)
        {
            dayOfTheWeekString = weekDaysArray[i];

            dayOfWeek = (TextView)
                        findViewById(weekdays[getWeekIndex(i, currentCalendar)
                                              - 1]);
            if (dayOfWeek != null)
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
        int monthEndIndex =
            CALENDAR_DAYS - (actualMaximum + dayOfMonthIndex - 1);

        DayView dayView;
        for (int i = 1; i <= CALENDAR_DAYS; i++)
        {
            dayView = (DayView) findViewById(days[i - 1]);
            if (dayView == null)
                continue;

            //Apply the default styles
            dayView.setOnClickListener(null);
            dayView.bind(startCalendar, getDecorators());
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

        // If the last week row has no visible days, hide it or show
        // it in case
        ViewGroup weekRow = (ViewGroup) findViewById(weeks[5]);
        if (weekRow != null)
        {
            dayView = (DayView) findViewById(days[35]);
            if (dayView != null)
            {
                if (dayView.getVisibility() != VISIBLE)
                    weekRow.setVisibility(GONE);

                else
                    weekRow.setVisibility(VISIBLE);
            }
        }
    }

    // clearDayOfTheMonthStyle
    private void clearDayOfTheMonthStyle(Date currentDate)
    {
        if (currentDate != null)
        {
            final Calendar calendar = getTodaysCalendar();
            calendar.setFirstDayOfWeek(getFirstDayOfWeek());
            calendar.setTime(currentDate);

            final DayView dayView = getDayOfMonthText(calendar);
            dayView.setBackgroundColor(calendarBackgroundColor);
            dayView.setTextColor(dayOfWeekTextColor);
            dayView.decorate();
        }
    }

    // getDayOfMonthText
    private DayView getDayOfMonthText(Calendar currentCalendar)
    {
        return (DayView) getView(currentCalendar);
    }

    // getDayIndexByDate
    private int getDayIndexByDate(Calendar currentCalendar)
    {
        int monthOffset = getMonthOffset(currentCalendar);
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        int index = currentDay + monthOffset;
        return index;
    }

    // getMonthOffset
    private int getMonthOffset(Calendar currentCalendar)
    {
        final Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(getFirstDayOfWeek());
        calendar.setTime(currentCalendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayWeekPosition = calendar.getFirstDayOfWeek();
        int dayPosition = calendar.get(Calendar.DAY_OF_WEEK);

        if (firstDayWeekPosition == 1)
            return dayPosition - 1;

        else
        {
            if (dayPosition == 1)
                return 6;

            else
                return dayPosition - 2;
        }
    }

    // getWeekIndex
    private int getWeekIndex(int weekIndex, Calendar currentCalendar)
    {
        int firstDayWeekPosition = currentCalendar.getFirstDayOfWeek();
        if (firstDayWeekPosition == 1)
            return weekIndex;

        else
        {

            if (weekIndex == 1)
                return 7;

            else
                return weekIndex - 1;
        }
    }

    // getView
    private View getView(Calendar currentCalendar)
    {
        int index = getDayIndexByDate(currentCalendar);
        View childView = findViewById(days[index - 1]);
        return childView;
    }

    // getTodaysCalendar
    private Calendar getTodaysCalendar()
    {
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
        currentCalendar.setFirstDayOfWeek(getFirstDayOfWeek());
        return currentCalendar;
    }

    // refreshCalendar
    public void refreshCalendar(Calendar currentCalendar)
    {
        this.currentCalendar = currentCalendar;
        this.currentCalendar.setFirstDayOfWeek(getFirstDayOfWeek());

        // Set date title
        initializeTitleLayout();

        // Set weeks days titles
        initializeWeekLayout();

        // Initialize and set days in calendar
        setDaysInCalendar();
    }

    // getFirstDayOfWeek
    public int getFirstDayOfWeek()
    {
        return firstDayOfWeek;
    }

    // setFirstDayOfWeek
    public void setFirstDayOfWeek(int firstDayOfWeek)
    {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    // markDayAsCurrentDay
    public void markDayAsCurrentDay(Calendar calendar)
    {
        if (calendar != null && CalendarUtils.isToday(calendar))
        {
            DayView dayOfMonth = getDayOfMonthText(calendar);
            dayOfMonth.setTextColor(currentDayOfMonth);
        }
    }

    // markDayAsSelectedDay
    public void markDayAsSelectedDay(Date currentDate)
    {
        final Calendar currentCalendar = getTodaysCalendar();
        currentCalendar.setFirstDayOfWeek(getFirstDayOfWeek());
        currentCalendar.setTime(currentDate);

        // Clear previous marks
        clearDayOfTheMonthStyle(lastSelectedDay);

        // Store current values as last values
        storeLastValues(currentDate);

        // Mark current day as selected
        DayView view = getDayOfMonthText(currentCalendar);
        view.setBackgroundResource(selectedDayBackground);
            
        view.setTextColor(selectedDayTextColor);
    }

    // storeLastValues
    private void storeLastValues(Date currentDate)
    {
        lastSelectedDay = currentDate;
    }

    // setCalendarListener
    public void setCalendarListener(CalendarListener calendarListener)
    {
        this.calendarListener = calendarListener;
    }

    // onDayOfMonthClickListener
    private OnClickListener onDayOfMonthClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            // Extract day selected
            final TextView dayOfMonthText = (TextView) view;

            // Fire event
            final Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(getFirstDayOfWeek());
            calendar.setTime(currentCalendar.getTime());
            calendar.set(Calendar.DAY_OF_MONTH,
                         Integer.valueOf(dayOfMonthText.getText().toString()));
            markDayAsSelectedDay(calendar.getTime());

            //Set the current day color
            markDayAsCurrentDay(currentCalendar);

            if (calendarListener != null)
                calendarListener.onDateSelected(calendar);
        }
    };

    // getDecorators
    public List<DayDecorator> getDecorators()
    {
        return decorators;
    }

    // setDecorators
    public void setDecorators(List<DayDecorator> decorators)
    {
        this.decorators = decorators;
    }

    // isOverflowDateVisible
    public boolean isOverflowDateVisible()
    {
        return isOverflowDateVisible;
    }

    // setShowOverflowDate
    public void setShowOverflowDate(boolean isOverFlowEnabled)
    {
        isOverflowDateVisible = isOverFlowEnabled;
    }

    // setCustomTypeface
    public void setCustomTypeface(Typeface customTypeface)
    {
        this.customTypeface = customTypeface;
    }

    // getCustomTypeface
    public Typeface getCustomTypeface()
    {
        return customTypeface;
    }

    // getCurrentCalendar
    public Calendar getCurrentCalendar()
    {
        return currentCalendar;
    }

    // GestureListener
    private class GestureListener
        extends GestureDetector.SimpleOnGestureListener
    {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        // onDown
        @Override
        public boolean onDown(MotionEvent e)
        {
            return true;
        }

        // onFling
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY)
        {
            boolean result = false;

            try
            {
                float diffX = e2.getX() - e1.getX();
                float diffY = e2.getY() - e1.getY();
                if (Math.abs(diffX) > Math.abs(diffY))
                {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD &&
                        Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD)
                    {
                        if (diffX > 0)
                        {
                            onSwipeRight();
                        }

                        else
                        {
                            onSwipeLeft();
                        }

                        result = true;
                    }
                }

                else
                {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD &&
                        Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD)
                    {
                        if (diffY > 0)
                        {
                            onSwipeDown();
                        }

                        else
                        {
                            onSwipeUp();
                        }

                        result = true;
                    }
                }
            }

            catch (Exception e) {}

            return result;
        }
    }
}
