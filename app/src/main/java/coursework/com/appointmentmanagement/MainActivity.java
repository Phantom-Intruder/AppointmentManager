package coursework.com.appointmentmanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
