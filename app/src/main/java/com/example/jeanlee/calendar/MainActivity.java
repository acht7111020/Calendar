package com.example.jeanlee.calendar;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.ImageButton;

public class MainActivity extends ActionBarActivity{

    ImageButton calendarButton;
    ImageButton journalButton;
    ImageButton notesButton;
    ImageButton albumButton;
    ImageButton todoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        todoButton=(ImageButton)findViewById(R.id.btLeft1);
        todoButton.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), TodoActivity.class);
                startActivity(intent);
            }

        });


        albumButton=(ImageButton)findViewById(R.id.btLeft2);
        albumButton.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), AlbumActivity.class);
                startActivity(intent);
            }

        });


        journalButton=(ImageButton)findViewById(R.id.btRight1);
        journalButton.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), JournalActivity.class);
                startActivity(intent);
            }

        });

        notesButton=(ImageButton)findViewById(R.id.btRight2);
        notesButton.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), NoteActivity.class);
                startActivity(intent);
            }

        });

        calendarButton=(ImageButton)findViewById(R.id.btLeft3);
        calendarButton.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), CalendarActivity.class);
                startActivity(intent);
            }

        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
