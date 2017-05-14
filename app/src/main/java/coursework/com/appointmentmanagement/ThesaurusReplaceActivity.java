package coursework.com.appointmentmanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class ThesaurusReplaceActivity extends Activity {
    private OkHttpClient client;
    private Request request;
    Response response;
    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;
    private ArrayList<String> logData = new ArrayList<>();
    String[] dataFromInputStream;
    int date;
    String title;
    int time;
    String details;
    String wordToSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thesaurus_replace);
        Intent intent = getIntent();

        /*Create the word that needs to be sent and put it in a URL.
            Send the URL request and get the response
            Convert the response from XML to readable format
         */

        wordToSend = intent.getStringExtra("word");
        date  = intent.getIntExtra("Date", 0);
        title= intent.getStringExtra("title");
        time = intent.getIntExtra("time", 0);
        Log.d(TAG, "alerted123 " + time + ".." + title);

        details = intent.getStringExtra("details");
        String url = "http://thesaurus.altervista.org/thesaurus/v1?word="+wordToSend+"&language=en_US& key=AtwIFkvWfU2rdYlngsoi&output=xml";
        client = new OkHttpClient();

        request = new Request.Builder()
                .url(url)
                .build();

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                Call call = client.newCall(request);
                response = null;
                try {
                    response = call.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



        thread.start();
        while (thread.isAlive()){

        }

        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, logData);
        final Intent intent1 = new Intent(this, AddAppointmentActivity.class);

        final Thread threadShowData = new Thread(new Runnable(){
            @Override
            public void run() {
                InputStream is = response.body().byteStream();

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = null;
                try {
                    dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(is);



                    Element element=doc.getDocumentElement();
                    element.normalize();

                    NodeList nList = doc.getElementsByTagName("list");
                    int indexOfMainArray = 0;
                    for (int i=0; i<nList.getLength(); i++) {

                        Node node = nList.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element element2 = (Element) node;

                            String dateFromInputStreamString =  getValue("synonyms", element2);
                            dateFromInputStreamString = dateFromInputStreamString.replace('|',',');
                            String[] dateFromInputStream = dateFromInputStreamString.split(",");

                            System.out.print("");
                            for (String aDateFromInputStream : dateFromInputStream) {

                                logData.add(aDateFromInputStream);
                                //tv1.setText(tv1.getText()+"\n    " + aDateFromInputStream+"\n");
                                indexOfMainArray++;

                            }

                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            mainListView = (ListView) findViewById( R.id.mainListView );

                            // Create ArrayAdapter using the planet list.
                            mainListView.setAdapter( listAdapter );
                            mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    details = details.replace(wordToSend, logData.get(i));
                                    intent1.putExtra("Date", date);
                                    intent1.putExtra("title", title);
                                    intent1.putExtra("time", time);

                                    intent1.putExtra("details", details);
                                    startActivity(intent1);
                                }
                            });
                        }
                    });
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
           threadShowData.start();

            }

    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }
    }

