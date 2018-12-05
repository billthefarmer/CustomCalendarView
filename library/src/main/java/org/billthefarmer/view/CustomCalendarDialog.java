////////////////////////////////////////////////////////////////////////////////
//
//  CustomCalendarDialog - Custom calendar dialog for Android
//
//  Copyright © 2017  Bill Farmer
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
////////////////////////////////////////////////////////////////////////////////

package org.billthefarmer.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import java.text.DateFormat;
import java.util.Calendar;

// CustomCalendarDialog
public class CustomCalendarDialog extends AlertDialog
    implements CalendarListener, DialogInterface.OnClickListener
{
    private final static String TAG = "CustomCalendarDialog";

    private OnDateSetListener listener;
    private CustomCalendarView calendarView;
    private Calendar date;

    // CustomCalendarDialog
    public CustomCalendarDialog(Context context,
                                OnDateSetListener listener,
                                int year, int month, int dayOfMonth)
    {
        this(context, 0, listener, year, month, dayOfMonth);
    }

    // CustomCalendarDialog
    public CustomCalendarDialog(Context context, int themeResId,
                                OnDateSetListener listener,
                                int year, int month, int dayOfMonth)
    {
        super(context, themeResId);

        this.listener = listener;

        date = Calendar.getInstance();
        date.set(year, month, dayOfMonth);

        setTitle(DateFormat.getDateInstance(DateFormat.FULL)
                 .format(date.getTime()));

        String ok =
            getContext().getResources().getString(android.R.string.ok);
        setButton(DialogInterface.BUTTON_POSITIVE, ok, this);
        String cancel =
            getContext().getResources().getString(android.R.string.cancel);
        setButton(DialogInterface.BUTTON_NEGATIVE, cancel, this);

        View view =
            LayoutInflater.from(getContext()).inflate(R.layout.dialog, null);
        setView(view);

        calendarView = view.findViewById(R.id.calendar);
        calendarView.setCalendarListener(this);

        // Show Monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);

        // Show/hide overflow days of a month
        // calendarView.setShowOverflowDate(false);

        // Call refreshCalendar to update calendar
        calendarView.refreshCalendar(date);
    }

    // onDateSelected
    @Override
    public void onDateSelected(Calendar date)
    {
        this.date = date;
        setTitle(DateFormat.getDateInstance(DateFormat.FULL)
                 .format(date.getTime()));
    }

    // onMonthChanged
    @Override
    public void onMonthChanged(Calendar date)
    {
        setTitle(DateFormat.getDateInstance(DateFormat.FULL)
                 .format(date.getTime()));
    }

    // onClick
    @Override
    public void onClick(DialogInterface dialog, int which)
    {
        switch (which)
        {
        case DialogInterface.BUTTON_POSITIVE:
            if (listener != null)
                listener.onDateSet(calendarView,
                                   date.get(Calendar.YEAR),
                                   date.get(Calendar.MONTH),
                                   date.get(Calendar.DATE));
            break;
        }
    }

    // getCalendarView
    public CustomCalendarView getCalendarView()
    {
        return calendarView;
    }

    // OnDateSetListener
    public interface OnDateSetListener
    {
        void onDateSet(CustomCalendarView view,
                       int year, int month, int date);
    }
}
