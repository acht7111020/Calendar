package com.example.jeanlee.calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import sqlite.helper.CalendarDBhelper;
import sqlite.model.Journal;
import sqlite.model.Task;


public class ChangeJournal extends ActionBarActivity {

    private EditText mTitleText;
    private EditText mDescripText;
    protected CalendarDBhelper db;
    private static EditText mDate;
    static List<Journal> list;
    private ViewGroup mContainerView;
    private DatePickerDialog dialog = null;
    Journal journal;
    Intent intent;



    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            view.updateDate(year, month, day);
            month++;
            mDate.setText(year+"/"+month+"/"+day);

        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_input);
        mTitleText = (EditText) findViewById(R.id.journal_edit_title);
        mDescripText=(EditText)findViewById(R.id.journal_edit_description);
        mDate=(EditText)findViewById(R.id.datetext);
        Button saveButton = (Button) findViewById(R.id.save_button);
        Button cancelButton=(Button)findViewById(R.id.cancel_Button);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.pink));


       /* Get values from Intent */
        intent=getIntent();
        long id=intent.getLongExtra("id",-1);
        db=CalendarDBhelper.getInstance(this);

            journal=db.getJournal(id);

            mTitleText.setText(journal.getTitle().toString());
            mDate.setText(journal.getDateAt().toString());
            mDescripText.setText(journal.getDescrip().toString());

            saveButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    addJournalNow(mTitleText,mDescripText,mDate,journal);
                    setResult(100, intent);

                    finish();

                }
            });


            cancelButton.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){

                    finish();
                    return;
                }

            });

    }
    private static final String ACTIVITY_TAG="INPUT";

    public void addJournalNow(EditText title,EditText descrip,EditText mDate,Journal j) {
        String sTitle = title.getText().toString();
        String sDescrip= descrip.getText().toString();
        String sDate=mDate.getText().toString();
        if (sTitle.equalsIgnoreCase("")) {
            Toast.makeText(this, "Enter the journal description first!!",
                    Toast.LENGTH_LONG).show();
        } else {
            j.setDescrip(sDescrip);
            j.setTitle(sTitle);
            j.setDateAt(sDate);
            db.updateJournal(j);
            Log.e("chanfeinnnnn","changeinnnnnn");
            //get the id of the created journal
        }

    }


//    private void makeToast() {
//        Toast.makeText(ChangeJournal.this, "Please maintain a summary",
//                Toast.LENGTH_LONG).show();
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_journal_input, menu);
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

}
