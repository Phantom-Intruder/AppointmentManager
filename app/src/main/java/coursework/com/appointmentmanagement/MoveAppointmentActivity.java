package coursework.com.appointmentmanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class MoveAppointmentActivity extends AppCompatActivity{
    int date;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_appointment);

        //Create toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Move appointment");

        //Get data passed
        Intent intent = getIntent();
        date  = intent.getIntExtra("Date", 0);

        //Create table if not exists
        db= openOrCreateDatabase("Mydb", MODE_PRIVATE, null);
        db.execSQL("create table if not exists appointmentTable(title varchar, date varchar, time int, details varchar)");

        //Go through database
        Cursor c=db.rawQuery("select * from appointmentTable order by time asc", null);
        TextView displayDeleteOptionsText = (TextView) findViewById(R.id.appointments);
        displayDeleteOptionsText.setText("");
        //move cursor to first position
        c.moveToFirst();
        //fetch all data one by one
        int index = 0;
        do
        {
            //we can use c.getString(0) here
            //or we can get data using column index
            try {
                String title = c.getString(c.getColumnIndex("title"));
                String dateString = c.getString(1);
                int time = c.getInt(2);
                String details = c.getString(3);

                //display on text view
                if (Integer.parseInt(dateString) == date){
                    String newTitle = title.replaceAll("[^A-Za-z ]+", "");
                    index++;
                    displayDeleteOptionsText.append(index+ " " + time + " " + newTitle + "\n");
                }
            }catch (Exception e){
                return;
            }
        }while(c.moveToNext());
    }

    public void moveRecord(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_choice_box);
        int recordToDelete = 0;
        recordToDelete = Integer.parseInt(editText.getText().toString());
        if (recordToDelete==0) {
            Toast.makeText(this, "Type in a number", Toast.LENGTH_LONG).show();

        }else{
            Cursor c=db.rawQuery("select * from appointmentTable order by time asc", null);
            TextView displayDeleteOptionsText = (TextView) findViewById(R.id.appointments);
            displayDeleteOptionsText.setText("");
            //move cursor to first position
            c.moveToFirst();
            boolean isRemoved = false;

            //fetch all data one by one
            int index = 0;
            do
            {

                try {
                    final String title = c.getString(c.getColumnIndex("title"));
                    final String dateString = c.getString(1);
                    final int time = c.getInt(2);
                    final String details = c.getString(3);
                    //display on text view
                    if (Integer.parseInt(dateString) == date){
                        index++;
                        if (index==recordToDelete) {
                            isRemoved = true;

                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("Edit confirmation");
                            builder.setMessage("Would you like to move event: "+title.replaceAll("[^A-Za-z ]+", ""));
                            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //If user responds yes then save the game data and exit
                                    moveData(title, dateString, time, details);
                                }
                            }).create();

                            builder.setPositiveButton("No",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Do nothing
                                }
                            }).create();
                            builder.show();

                        }
                    }
                }catch (Exception e){
                    return;
                }
            }while(c.moveToNext());
            if (!isRemoved) {
                //If data not in database
                Toast.makeText(this, "The entered value doesn't have a record in the database", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MoveAppointmentActivity.class);
                intent.putExtra("Date", date);
                startActivity(intent);
            }
        }

    }

    private void moveData(String title, String dateString, int time, String details) {
        db.execSQL("delete from appointmentTable where title = '" + title + "'");
        DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker2);
        title = title.replaceAll("[^A-Za-z ]+", "");

        int date = 0;

        date = datePicker.getDayOfMonth();
        dateString = date+"";
        if (date == 0){
            Toast.makeText(this, "Please choose a date", Toast.LENGTH_LONG).show();
        }else{
            title = title+dateString;
            db.execSQL("insert into appointmentTable values('"+title+"','"+dateString+"','"+time+"','"+details+"')");
        }
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    }

