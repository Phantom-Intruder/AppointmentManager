package coursework.com.appointmentmanagement;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteChoiceActivity extends AppCompatActivity {
    int date;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_choice);

        //Create toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Delete appointment");

        //Get data passed from previous intent
        Intent intent = getIntent();
        date  = intent.getIntExtra("Date", 0);

        //Create database of not exists
        db= openOrCreateDatabase("Mydb", MODE_PRIVATE, null);
        db.execSQL("create table if not exists appointmentTable(title varchar, date varchar, time varchar, details varchar)");
    }

    public void selectAppointmentToDelete(View view) {
        Intent intent = new Intent(this, DeleteEntryActivity.class);
        intent.putExtra("Date", date);
        startActivity(intent);
    }

    public void deleteAllAppointments(View view) {
        db.execSQL("delete from appointmentTable where date='"+date+"'");
        Toast.makeText(this, "Successfully deleted", Toast.LENGTH_LONG).show();
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
}
