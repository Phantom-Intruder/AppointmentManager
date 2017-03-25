package coursework.com.appointmentmanagement;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddAppointmentActivity extends Activity {
    SQLiteDatabase db;
    TextView tv;
    EditText et1,et2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);
        //initialize all view objects
        tv=(TextView)findViewById(R.id.textView1);
        et1=(EditText)findViewById(R.id.appointment_title);
        et2=(EditText)findViewById(R.id.appointment_details);
        //create database if not already exist
        db= openOrCreateDatabase("Mydb", MODE_PRIVATE, null);
        //create new table if not already exist
        db.execSQL("create table if not exists mytable(name varchar, sur_name varchar)");
    }
    //This method will call on when we click on insert button
    public void insert(View v)
    {
        String name=et1.getText().toString();
        String sur_name=et2.getText().toString();
        et1.setText("");
        et2.setText("");
        //insert data into able
        db.execSQL("insert into mytable values('"+name+"','"+sur_name+"')");
        //display Toast
        Toast.makeText(this, "values inserted successfully.", Toast.LENGTH_LONG).show();
        display();
    }
    //This method will call when we click on display button
    public void display()
    {
        //use cursor to keep all data
        //cursor can keep data of any data type
        Cursor c=db.rawQuery("select * from mytable", null);
        tv.setText("");
        //move cursor to first position
        c.moveToFirst();
        //fetch all data one by one
        do
        {
            //we can use c.getString(0) here
            //or we can get data using column index
            String name=c.getString(c.getColumnIndex("name"));
            String surname=c.getString(1);
            //display on text view
            tv.append("Name:"+name+" and SurName:"+surname+"\n");
            //move next position until end of the data
        }while(c.moveToNext());
    }
}
