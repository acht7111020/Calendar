package com.example.jeanlee.calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CalendarViewActivity extends Activity {

    public Calendar month;
    public ListView list ;
    private ImageView todo;
    private PopupWindow popupWindow;
    private EditText dotimepicker1;
    private EditText dotimepicker2;
    private EditText dowrite;
    private View view;
    int[] image;

    List<Map<String, Object>> listview_list = new ArrayList<Map<String, Object>>();
    //public CalendarAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);
        month = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String input = df.format(month.getTime());
        onNewIntent(input);

        CalendarAdapter adapter = new CalendarAdapter(this, month);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(adapter);

        TextView title  = (TextView) findViewById(R.id.title);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

        ImageView previous  = (ImageView) findViewById(R.id.btn_prev_month);
        previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(month.get(Calendar.MONTH)== month.getActualMinimum(Calendar.MONTH)) {
                    month.set((month.get(Calendar.YEAR)-1),month.getActualMaximum(Calendar.MONTH),month.get(Calendar.DAY_OF_MONTH));
                } else {
                    month.set(Calendar.MONTH,month.get(Calendar.MONTH)-1);
                }

                refreshCalendar();
            }
        });

        ImageView next  = (ImageView) findViewById(R.id.btn_next_month);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(month.get(Calendar.MONTH)== month.getActualMaximum(Calendar.MONTH)) {
                    month.set((month.get(Calendar.YEAR)+1),month.getActualMinimum(Calendar.MONTH),month.get(Calendar.DAY_OF_MONTH));
                } else {
                    month.set(Calendar.MONTH,month.get(Calendar.MONTH)+1);
                }

                refreshCalendar();

            }
        });

        gridview.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                TextView date = (TextView)v.findViewById(R.id.day);
                date.setTextColor(Color.GRAY);
                date.setBackgroundColor(Color.argb(150,204,255,204));
               // v.setBackgroundResource(R.drawable.onclick_calendarback);


                //if(date instanceof TextView && !date.getText().equals("")) {

                   // Intent intent = new Intent();
                   // String day = date.getText().toString();
                   // if(day.length()==1) {
                   //     day = "0"+day;
                   // }
                    // return chosen date as string format
                   // intent.putExtra("date", android.text.format.DateFormat.format("yyyy-MM", month)+"-"+day);
                  //  setResult(RESULT_OK, intent);
                  //  finish();
               // }

            }
        });


        image = new int[]{R.drawable.happy , R.drawable.medicine , R.drawable.meeting , R.drawable.deadline,
                    R.drawable.pencil};

        list = (ListView)findViewById(R.id.dailyView1);
        SimpleAdapter listadapter = new SimpleAdapter(CalendarViewActivity.this , getData() ,R.layout.listview_item,
                new String[] { "title", "info", "listview_icon" },
                new int[] { R.id.title, R.id.info, R.id.listview_icon }
        );
        list.setAdapter(listadapter);

        todo = (ImageView)findViewById(R.id.calendar_todo);
        todo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showWindow(v);


            }
        });

    }

    private List<? extends Map<String, ?>> getData() {
        // TODO Auto-generated method stub
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map =
                new HashMap<String, Object>();
        map.put("title", "G1");
        map.put("info", "紅豆");
        map.put("listview_icon", R.drawable.happy);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "G2");
        map.put("info", "綠豆");
        map.put("listview_icon", R.drawable.pencil);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "G3");
        map.put("info", "土豆");
        map.put("listview_icon", R.drawable.deadline);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "G4");
        map.put("info", "毛豆");
        map.put("listview_icon", R.drawable.medicine);
        list.add(map);

        return list;
    }

    private void getData(String title ,String time) {
        // TODO Auto-generated method stub
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map =
                new HashMap<String, Object>();

        map.put("title",title);
        map.put("info",time);
        map.put("listview_icon",image[2]);
        listview_list.add(map);

    }

    public void refreshCalendar()
    {
        TextView title  = (TextView) findViewById(R.id.title);

        CalendarAdapter adapter = new CalendarAdapter(this, month);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(adapter);
        //adapter.refreshDays();
        //adapter.notifyDataSetChanged();

        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
    }

    public void onNewIntent(String nowaday) {
        String date = nowaday;
        String[] dateArr = date.split("-"); // date format is yyyy-mm-dd
        month.set(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1])-1 , Integer.parseInt(dateArr[2]));
    }


    private void showWindow(View parent) {

        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.calendar_add_popupwindow, null);


        }
        // set windows

        popupWindow =  new PopupWindow(view, android.view.ViewGroup.LayoutParams.FILL_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT ,true);

        // set disappear out of windows
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));

        //WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //int xPos =  (windowManager.getDefaultDisplay().getHeight() / 2  - 100);

        popupWindow.showAtLocation(view, Gravity.LEFT ,0,380);


        dowrite = (EditText)view.findViewById(R.id.popup_write);
        dotimepicker1 = (EditText)view.findViewById(R.id.popup_picktime1);
        dotimepicker2 = (EditText)view.findViewById(R.id.popup_picktime2);
        Button dobutton = (Button)view.findViewById(R.id.poppup_button);
        int t = month.get(Calendar.HOUR_OF_DAY);
        dotimepicker1.setHint(Integer.toString(t));
        dotimepicker2.setHint("00");


        dobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String s1 = dowrite.getText().toString();
                String s2 = dotimepicker1.getText().toString();
                String s3 = dotimepicker2.getText().toString();
                String s4 ="time = "+ s2 + " : " +s3;
                getData(s1,s4);
                SimpleAdapter listadapter = new SimpleAdapter(CalendarViewActivity.this ,listview_list ,R.layout.listview_item,
                        new String[] { "title", "info", "listview_icon" },
                        new int[] { R.id.title, R.id.info, R.id.listview_icon }
                );
                list.setAdapter(listadapter);
                if (popupWindow != null) {
                   popupWindow.dismiss();
                }

            }

        });






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //public void onItemClick(AdapterView<?> parent, View view, int position,long id) {

    //}
}
