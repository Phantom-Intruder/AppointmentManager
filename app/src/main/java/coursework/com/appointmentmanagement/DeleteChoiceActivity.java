package coursework.com.appointmentmanagement;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteChoiceActivity extends Activity {
    int date;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_choice);
        Intent intent = getIntent();
        date  = intent.getIntExtra("Date", 0);
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
}
