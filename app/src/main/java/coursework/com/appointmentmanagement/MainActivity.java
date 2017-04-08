package coursework.com.appointmentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Appointment Manager");
    }

    //Handle back button presses
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
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public void openAddAppointmentActivity(View view) {
        DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker);
        int date = 0;
        date = datePicker.getDayOfMonth();
        if (date == 0){
            Toast.makeText(this, "Please choose a date", Toast.LENGTH_LONG).show();
        }else{
        Intent intent = new Intent(this, AddAppointmentActivity.class);
        intent.putExtra("Date", date);
        startActivity(intent);
    }}

    public void openDeleteAppointmentActivity(View view) {
        DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker);
        int date = 0;
        date = datePicker.getDayOfMonth();
        if (date == 0){
            Toast.makeText(this, "Please choose a date", Toast.LENGTH_LONG).show();
        }else{
            Intent intent = new Intent(this, DeleteChoiceActivity.class);
            intent.putExtra("Date", date);
            startActivity(intent);
        }}

    public void openEditActivity(View view) {
        DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker);
        int date = 0;
        date = datePicker.getDayOfMonth();
        if (date == 0){
            Toast.makeText(this, "Please choose a date", Toast.LENGTH_LONG).show();
        }else{
            Intent intent = new Intent(this, ViewChoiceActivity.class);
            intent.putExtra("Date", date);
            startActivity(intent);
        }
    }

    public void openMoveAppointmentActivity(View view) {
        DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker);
        int date = 0;
        date = datePicker.getDayOfMonth();
        if (date == 0){
            Toast.makeText(this, "Please choose a date", Toast.LENGTH_LONG).show();
        }else{
            Intent intent = new Intent(this, MoveAppointmentActivity.class);
            intent.putExtra("Date", date);
            startActivity(intent);
        }
    }

    public void openSearchActivity(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}
