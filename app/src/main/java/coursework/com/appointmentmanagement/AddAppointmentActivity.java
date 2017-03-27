package coursework.com.appointmentmanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static android.content.ContentValues.TAG;

public class AddAppointmentActivity extends Activity {
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
        //initialize all view objects
        Intent intent = getIntent();
        date  = intent.getIntExtra("Date", 0);
        String title= intent.getStringExtra("title");
        int time = intent.getIntExtra("time", 0);
        String details = intent.getStringExtra("details");
        appointmentTitleEditText =(EditText)findViewById(R.id.appointment_title);
        appointmentTimeEditText =(EditText)findViewById(R.id.appointment_time);
        appointmentDetailsEditText =(EditText)findViewById(R.id.appointment_details);
        if (title != null) {
            appointmentTitleEditText.setText(title);
        }
        if (time != 0) {
            String timeString = time + "";
            appointmentTimeEditText.setText(timeString);
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
        String time= appointmentTimeEditText.getText().toString();
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
