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
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

// DayView
public class DayView extends TextView
{
    private Calendar date;
    private List<DayDecorator> decorators;

    // DayView
    public DayView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    // bind
    public void bind(Calendar date, List<DayDecorator> decorators)
    {
        this.date = date;
        this.decorators = decorators;

        final DateFormat dateFormat =
            new SimpleDateFormat("d", Locale.getDefault());
        setText(dateFormat.format(date.getTime()));
    }

    // decorate
    public void decorate()
    {
        // Set custom decorators
        if (decorators != null)
        {
            for (DayDecorator decorator : decorators)
            {
                decorator.decorate(this);
            }
        }
    }

    // getDate
    public Calendar getDate()
    {
        return date;
    }
}
