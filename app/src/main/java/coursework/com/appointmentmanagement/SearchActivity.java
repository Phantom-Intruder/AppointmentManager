package coursework.com.appointmentmanagement;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class SearchActivity extends Activity {
    SQLiteDatabase db;
    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;
    private ArrayList<String> logDataTitles = new ArrayList<>();
    private ArrayList<Integer> logDataTimes = new ArrayList<>();
    private ArrayList<String> logDataDetails = new ArrayList<>();
    private ArrayList<String> logDataDates = new ArrayList<>();

    private ArrayList<String> resultData = new ArrayList<>();

    private ArrayList<String> searchData;
    private ArrayList<Integer> searchDataTime;
    private ArrayList<String> searchDataDetails;
    private ArrayList<String> searchDataDates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    public void searchForQuery(View view) {
        EditText searchEditText = (EditText) findViewById(R.id.search_box);
        String stringToSearch = searchEditText.getText().toString();
        db= openOrCreateDatabase("Mydb", MODE_PRIVATE, null);
        db.execSQL("create table if not exists appointmentTable(title varchar, date varchar, time int, details varchar)");

        Cursor c=db.rawQuery("select * from appointmentTable order by time asc", null);
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
                Log.d(TAG, "data123 "+ time);
                String details = c.getString(3);

                //display on text view
                String newTitle = title.replaceAll("[^A-Za-z ]+", "");
                logDataTitles.add(newTitle);
                logDataDetails.add(details);
                logDataTimes.add(time);
                logDataDates.add(dateString);

            }catch (Exception e){
                return;
            }
        }while(c.moveToNext());
        searchData = new ArrayList<>();
        searchDataTime = new ArrayList<>();
        searchDataDetails = new ArrayList<>();
        searchDataDates = new ArrayList<>();
        resultData = new ArrayList<>();
        Date d = new Date();

        stringToSearch = stringToSearch.toLowerCase();
        for (int i = 0 ; i < logDataTitles.size(); i++){
            String title = logDataTitles.get(i).toLowerCase();
            int date = Integer.parseInt(logDataDates.get(i));

            if (title.contains(stringToSearch) && date > d.getDate()){
                resultData.add(logDataTitles.get(i));
                searchData.add(logDataTitles.get(i));
                searchDataTime.add(logDataTimes.get(i));
                searchDataDetails.add(logDataDetails.get(i));
                searchDataDates.add(logDataDates.get(i));
            }
        }
        for (int i = 0 ; i < logDataDetails.size(); i++){
            String details = logDataDetails.get(i).toLowerCase();
            int date = Integer.parseInt(logDataDates.get(i));
            if (details.contains(stringToSearch) && date > d.getDate()){
                resultData.add(logDataDetails.get(i));
                searchData.add(logDataTitles.get(i));
                searchDataTime.add(logDataTimes.get(i));
                searchDataDetails.add(logDataDetails.get(i));
                searchDataDates.add(logDataDates.get(i));
            }
        }
        mainListView = (ListView) findViewById( R.id.mainListView );

        // Create ArrayAdapter using the planet list.
        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, resultData);

        mainListView.setAdapter( listAdapter );

        final Intent intent1 = new Intent(this, AddAppointmentActivity.class);
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "alerted123 "+searchData.get(i));
                intent1.putExtra("Date", searchDataDates.get(i));
                intent1.putExtra("title", searchData.get(i));
                intent1.putExtra("time", searchDataTime.get(i));
                intent1.putExtra("details", searchDataDetails.get(i));
                startActivity(intent1);
            }
        });
    }
}
