# CustomCalendarView [![Build Status](https://travis-ci.org/billthefarmer/CustomCalendarView.svg?branch=master)](https://travis-ci.org/billthefarmer/CustomCalendarView) [![JitPack](https://jitpack.io/v/billthefarmer/CustomCalendarView.svg)](https://jitpack.io/#billthefarmer/CustomCalendarView)
Android custom calendar view - not quite forked from https://github.com/npanigrahy/Custom-Calendar-View

![Calendar](https://github.com/billthefarmer/billthefarmer.github.io/raw/master/images/Calendar.png)

The [npanigrahy/Custom-Calendar-View](https://github.com/npanigrahy/Custom-Calendar-View) library,
although the best I could find, has several problems
* It is slow starting, probably due to finding views using keys rather than ids
* If used in a dialog it can be very wide, especially in landscape, possibly due to
  using `android:layout_width="match_parent"` rather than `wrap_content`
* Uses a colour rather than a drawable for the selected entry background, so
  you can't use a rounded rectangle, for example, without providing a DayDecorator

This is an attempt to fix some of the above without breaking it. It all works as it should, but the calendar
is the same size as [CalendarView](https://developer.android.com/reference/android/widget/CalendarView.html).

## Use in project
Add the driver to your build.gradle with
```gradle
allprojects {
  repositories {
    jcenter()
    maven { url "https://jitpack.io" }
  }
}
```
and:
```gradle
dependencies {
  compile 'com.github.billthefarmer:CustomCalendarView:v1.0'
}
```

## Using CustomCalendarView Library

The GitHub project source includes a simple test application. Once the
library is added to your project, you can include the
CustomCalendarView into your activity/fragment layout using the
following code snippets.
```xml
<org.billthefarmer.view.CustomCalendarView
	android:id="@+id/calendar_view"
	android:layout_width="match_parent"
	android:layout_height="wrap_content" />
```
The above code snippet will show the simple Calendar View with the
default design. You can use the following attributes to customize the
appearance of the calendar.
```xml
<org.billthefarmer.view.CustomCalendarView
        android:id="@+id/calendar_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@color/off_white"
        app:calendarBackgroundColor="@color/off_white"
        app:calendarTitleTextColor="@color/black"
        app:currentDayOfMonthColor="@color/blue"
        app:dayOfMonthTextColor="@color/black"
        app:dayOfWeekTextColor="@color/black"
        app:disabledDayBackgroundColor="@color/off_white"
        app:disabledDayTextColor="@color/grey"
        app:selectedDayBackground="@[color|drawable]/[blue|selected]"
        app:titleLayoutBackgroundColor="@color/white"
        app:weekLayoutBackgroundColor="@color/white" />
```
Initialize the calendar view to control the behavior of the calendar
using the following methods.
```java
//Initialize CustomCalendarView from layout
calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);

//Initialize calendar with date
Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());

//Show Monday as first date of week
calendarView.setFirstDayOfWeek(Calendar.MONDAY);

//Show/hide overflow days of a month
calendarView.setShowOverflowDate(false);

//call refreshCalendar to update calendar the view
calendarView.refreshCalendar(currentCalendar);

//Handling custom calendar events
calendarView.setCalendarListener(new CalendarListener()
{
    @Override
    public void onDateSelected(Date date)
    {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Toast.makeText(MainActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMonthChanged(Date date)
    {
        SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
        Toast.makeText(MainActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
    }
});
```

## Using Custom TypeFace
```java
//Setting custom font
final Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Arch_Rival_Bold.ttf");
if (typeface != null)
{
    calendarView.setCustomTypeface(typeface);
    calendarView.refreshCalendar(currentCalendar);
}
```

## Using Day Decorators
```java
//adding calendar day decorators
List decorators = new ArrayList<>();
decorators.add(new ColorDecorator());
calendarView.setDecorators(decorators);
calendarView.refreshCalendar(currentCalendar);
```
