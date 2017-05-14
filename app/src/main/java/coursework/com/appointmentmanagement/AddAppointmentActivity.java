package coursework.com.appointmentmanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static android.content.ContentValues.TAG;

public class AddAppointmentActivity extends AppCompatActivity {
    private OkHttpClient client;
    SQLiteDatabase db;
    EditText appointmentTitleEditText, appointmentDetailsEditText, appointmentTimeEditText;
    ArrayList<String> titlesList = new ArrayList<>();
    String dateString;
    private Request request;
    int date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);
		
		//Set toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add appointment");
       
	   	//Get data passed 
        Intent intent = getIntent();
        date  = intent.getIntExtra("Date", 0);
        String title= intent.getStringExtra("title");
        int time = intent.getIntExtra("time", 0);
        String details = intent.getStringExtra("details");
        appointmentTitleEditText =(EditText)findViewById(R.id.appointment_title);
        appointmentTimeEditText =(EditText)findViewById(R.id.appointment_time);
        appointmentDetailsEditText =(EditText)findViewById(R.id.appointment_details);
		
		//Ensure no details are null
        if (title != null) {
            String newTitle = title.replaceAll("[^A-Za-z ]+", "");
            appointmentTitleEditText.setText(newTitle);
        }
        if (time != 0) {
            String timeString = time + "";
            appointmentTimeEditText.setText(timeString);
        }
        if (details != null) {
            appointmentDetailsEditText.setText(details);
        }
		
        //create database if it doesn't alredy exist
        db= openOrCreateDatabase("Mydb", MODE_PRIVATE, null);
        //create new table if it doesn't alredy exist
        db.execSQL("create table if not exists appointmentTable(title varchar, date varchar, time int, details varchar)");
    }
	
	//Handle back key presses 
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //Handle title bar actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (id){
            default:
                this.finish();
                return true;
        }
    }

    //This method will call on when we click on insert button
    public void insert(View v)
    {
        Cursor c=db.rawQuery("select * from appointmentTable order by time asc", null);
        c.moveToFirst();
        int index = 0;
        try {
        do
        {
		//Take data from the database to check if the title already exists to ensure that data isn't duplicated

                final String title = c.getString(c.getColumnIndex("title"));
                final String dateString = c.getString(1);
                final int time = c.getInt(2);
                final String details = c.getString(3);
                titlesList.add(title);

        }while(c.moveToNext());
        }catch (Exception ignored){

        }
        try {
            String appointmentTitle = appointmentTitleEditText.getText().toString();
            String appointmentTime = appointmentTimeEditText.getText().toString();
            String appointmentDetails = appointmentDetailsEditText.getText().toString();
            int timeInteger = Integer.parseInt(appointmentTime.replaceAll("[\\D]", ""));

            appointmentTitleEditText.setText("");
            appointmentTimeEditText.setText("");
            appointmentDetailsEditText.setText("");
            dateString = date + "";
            String appointmentTitleWithDate = appointmentTitle + date;
            if (titlesList.contains(appointmentTitleWithDate)) {
                //Show an alert dialog when the about button is clicked
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Duplicate appointment titles");
                builder.setMessage(appointmentTitle + " already exists, please choose a different event title");
                builder.setPositiveButton("OK", null);
                builder.show();
            } else {
                //insert data into able
                db.execSQL("insert into appointmentTable values('" + appointmentTitleWithDate + "','" + dateString + "','" + timeInteger + "','" + appointmentDetails + "')");
                //display Toast
                Toast.makeText(this, "values inserted successfully.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Please fill in the fields", Toast.LENGTH_LONG).show();
        }
    }
    public void getDataFromServerAndPresent(View view) {
        EditText wordToSendEditText = (EditText) findViewById(R.id.thesaurus_word);
        String wordToSendString = wordToSendEditText.getText().toString();
        Intent intent  = new Intent(this, ThesaurusActivity.class);
        intent.putExtra("word", wordToSendString);
        startActivity(intent);
        }

    public void openThesaurusReplace(View view) {
        EditText wordToSendEditText = (EditText) findViewById(R.id.appointment_details);
        int startSelection=wordToSendEditText .getSelectionStart();
        int endSelection=wordToSendEditText .getSelectionEnd();
        String title = appointmentTitleEditText.getText().toString();
        int time= Integer.parseInt(appointmentTimeEditText.getText().toString());
        String details = appointmentDetailsEditText.getText().toString();
        String selectedText = wordToSendEditText .getText().toString().substring(startSelection, endSelection);
        Intent intent  = new Intent(this, ThesaurusReplaceActivity.class);
        intent.putExtra("word", selectedText);
        intent.putExtra("Date", date);
        intent.putExtra("title", title);
        intent.putExtra("time", time);
        intent.putExtra("details", details);
        startActivity(intent);
    }
}
