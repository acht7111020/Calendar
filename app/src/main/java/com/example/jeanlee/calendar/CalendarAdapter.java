package com.example.jeanlee.calendar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by 心愉 on 2014/12/31.
 */
public class CalendarAdapter extends BaseAdapter  {


    //TODO: NEW CALENDAR
    static final int FIRST_DAY_OF_WEEK =0; // Sunday = 0, Monday = 1

    private Context mContext;
    private Calendar month;
    private Calendar selectedDate;
    public String[] days;
    private LinearLayout lin;
    private ImageView icon1;
    private ImageView icon2;

    public CalendarAdapter(Context c, Calendar monthCalendar) {
        month = monthCalendar;
        selectedDate = (Calendar)monthCalendar.clone();
        mContext = c;
        month.set(Calendar.DAY_OF_MONTH, 1);

        refreshDays();
    }



    public int getCount() {
        return days.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new view for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TextView dayView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
           // LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           // v = vi.inflate(R.layout.gridview_item, null);
            v = LayoutInflater.from(mContext).inflate(R.layout.gridview_item, null);

            AbsListView.LayoutParams param = new AbsListView.LayoutParams(
                    android.view.ViewGroup.LayoutParams.FILL_PARENT,
                    140);
            v.setLayoutParams(param);
        }


        dayView = (TextView)v.findViewById(R.id.day);
        lin = (LinearLayout)v.findViewById(R.id.day_item);
        icon1 = (ImageView)v.findViewById(R.id.grid_icon1);
        icon2 = (ImageView)v.findViewById(R.id.grid_icon2);
        // disable empty days from the beginning
        if(days[position].equals("") ) {
            dayView.setClickable(false);
            dayView.setFocusable(false);
          //  dayView.setVisibility(View.INVISIBLE);
            lin.setVisibility(View.INVISIBLE);
        }
        else {
            dayView.setVisibility(View.VISIBLE);
            // mark current day as focused
            if(month.get(Calendar.YEAR)== selectedDate.get(Calendar.YEAR) && month.get(Calendar.MONTH)== selectedDate.get(Calendar.MONTH) &&
                    days[position].equals(""+selectedDate.get(Calendar.DAY_OF_MONTH))) {
                //lin = (LinearLayout)v.findViewById(R.id.day_item);
               // lin.setBackgroundResource(R.drawable.blue);
                v.setBackgroundResource(R.drawable.blue);
                dayView.setTextColor(Color.WHITE);

                icon1.setImageResource(R.drawable.meeting);
                icon2.setImageResource(R.drawable.deadline);
                icon1.setVisibility(View.VISIBLE);
                icon2.setVisibility(View.VISIBLE);

            }
            else {

                //v.setBackgroundResource(R.drawable.pink_calendar);
            }
        }
        dayView.setText(days[position]);

        // create date string for comparison
        String date = days[position];

        if(date.length()==1) {
            date = "0"+date;
        }
        String monthStr = ""+(month.get(Calendar.MONTH)+1);
        if(monthStr.length()==1) {
            monthStr = "0"+monthStr;
        }


        return v;
    }

    public void refreshDays()
    {


        int lastDay = month.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDay = (int)month.get(Calendar.DAY_OF_WEEK);

        // figure size of the array
        if(firstDay==1){
            days = new String[lastDay+(FIRST_DAY_OF_WEEK*6)];
        }
        else {
            days = new String[lastDay+firstDay-(FIRST_DAY_OF_WEEK+1)];
        }

        int j=FIRST_DAY_OF_WEEK;

        // populate empty days before first real day
        if(firstDay>1) {
            for(j=0;j<firstDay-FIRST_DAY_OF_WEEK;j++) {
                days[j] = "";
            }
        }
        else {
            for(j=0;j<FIRST_DAY_OF_WEEK*6;j++) {
                days[j] = "";
            }
            j=FIRST_DAY_OF_WEEK*6+1; // sunday => 1, monday => 7
        }

        // populate days
        int dayNumber = 1;
        for(int i=j-1;i<days.length;i++) {
            days[i] = ""+dayNumber;
            dayNumber++;
        }
    }



}