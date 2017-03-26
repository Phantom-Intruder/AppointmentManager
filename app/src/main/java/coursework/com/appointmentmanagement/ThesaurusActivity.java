package coursework.com.appointmentmanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.IOException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class ThesaurusActivity extends Activity {
    private OkHttpClient client;
    private Request request;
    TextView tv1;
    Response response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thesaurus);
        Intent intent = getIntent();
        tv1=(TextView)findViewById(R.id.textView1);

        String wordToSend = intent.getStringExtra("word");
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
        try {
            InputStream is = response.body().byteStream();

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element=doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("list");

            for (int i=0; i<nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;
                    tv1.setText(tv1.getText()+"\n    " + getValue("synonyms", element2)+"\n");
                    tv1.setText(tv1.getText()+"-----------------------");
                }
            }

        } catch (Exception e) {e.printStackTrace();}
    }




    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }
}
