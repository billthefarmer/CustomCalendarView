
package org.billthefarmer.view;

import java.util.Calendar;

public interface CalendarListener
{
    void onDateSelected(Calendar date);
    void onMonthChanged(Calendar date);
}
