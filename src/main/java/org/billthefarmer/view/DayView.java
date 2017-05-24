
package org.billthefarmer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DayView extends TextView
{
    private Date date;
    private List<DayDecorator> decorators;

    public DayView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void bind(Date date, List<DayDecorator> decorators)
    {
        this.date = date;
        this.decorators = decorators;

        final SimpleDateFormat df =
            new SimpleDateFormat("d", Locale.getDefault());
        int day = Integer.parseInt(df.format(date));
        setText(String.valueOf(day));
    }

    public void decorate()
    {
        //Set custom decorators
        if (decorators != null)
        {
            for (DayDecorator decorator : decorators)
            {
                decorator.decorate(this);
            }
        }
    }

    public Date getDate()
    {
        return date;
    }
}
