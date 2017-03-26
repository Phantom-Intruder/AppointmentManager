package coursework.com.appointmentmanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class AddAppointmentActivity extends Activity {
    SQLiteDatabase db;
    TextView tv;
    EditText appointmentTitleEditText, appointmentDetailsEditText, appointmentTimeEditText;
    ArrayList<String> titlesList = new ArrayList<>();
    String dateString;
    int date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);
        //initialize all view objects
        tv=(TextView)findViewById(R.id.textView1);
        Intent intent = getIntent();
        date  = intent.getIntExtra("Date", 0);
        String title= intent.getStringExtra("title");
        String time = intent.getStringExtra("time");
        String details = intent.getStringExtra("details");


        appointmentTitleEditText =(EditText)findViewById(R.id.appointment_title);
        appointmentTimeEditText =(EditText)findViewById(R.id.appointment_time);
        appointmentDetailsEditText =(EditText)findViewById(R.id.appointment_details);
        if (title != null) {
            appointmentTitleEditText.setText(title);
        }
        if (time != null) {
            appointmentTimeEditText.setText(time);
        }
        if (details != null) {
            appointmentDetailsEditText.setText(details);
        }
        //create database if not already exist

        db= openOrCreateDatabase("Mydb", MODE_PRIVATE, null);
        //create new table if not already exist
        db.execSQL("create table if not exists appointmentTable(title varchar, date varchar, time int, details varchar)");
    }
    //This method will call on when we click on insert button
    public void insert(View v)
    {
        String appointmentTitle = appointmentTitleEditText.getText().toString();
        String appointmentTime = appointmentTimeEditText.getText().toString();
        String appointmentDetails = appointmentDetailsEditText.getText().toString();
        int timeInteger = Integer.parseInt(appointmentTime.replaceAll("[\\D]", ""));

        appointmentTitleEditText.setText("");
        appointmentTimeEditText.setText("");
        appointmentDetailsEditText.setText("");
        //insert data into able
        dateString = date+"";
        String appointmentTitleWithDate = appointmentTitle+date;
        display();
        if (titlesList.contains(appointmentTitleWithDate)){
            //Show an alert dialog when the about button is clicked
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Duplicate appointment titles");
            builder.setMessage(appointmentTitle+" already exists, please choose a different event title");
            builder.setPositiveButton("OK", null);
            builder.show();
        }else{

        db.execSQL("insert into appointmentTable values('"+appointmentTitleWithDate+"','"+dateString+"','"+timeInteger+"','"+appointmentDetails+"')");
        //display Toast

        Toast.makeText(this, "values inserted successfully."+ dateString, Toast.LENGTH_LONG).show();
    }}
    //This method will call when we click on display button
    public void display()
    {
        //use cursor to keep all data
        //cursor can keep data of any data type
        Cursor c=db.rawQuery("select * from appointmentTable", null);
        tv.setText("");
        //move cursor to first position
        c.moveToFirst();
        //fetch all data one by one
        do
        {
            //we can use c.getString(0) here
            //or we can get data using column index
            try {
                String title = c.getString(c.getColumnIndex("title"));
                String date = c.getString(1);
                int time = c.getInt(2);
                String details = c.getString(3);
                titlesList.add(title);
                //display on text view
                tv.append("Title: " + title + " and date: " + date + " and Time: " + time + "and Details: " + details + "\n");
            }catch (Exception e){
                return;
            }
        }while(c.moveToNext());
    }
}
