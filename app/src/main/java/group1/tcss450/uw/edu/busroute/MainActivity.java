package group1.tcss450.uw.edu.busroute;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import group1.tcss450.uw.edu.busroute.Model.News;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /**
     * url to connect to our API.
     */
    private static final String mURL = "https://westus.api.cognitive.microsoft.com/linguistics/v1.0/analyze";
    /**
     * keys for connect to external database.
     */
    private static  final String mKey = "b80390842a2449528662a1a01e98d032";
        private static final String mKey1 = "2807d9ffb29043c7a13e7bcf706a81a2";
    /**
     * ListView field
     */
    private ListView mListView;
    private TextView mTextView;
    private EditText mEditText;
    private String[] listOfWords;
    private String mString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        mListView = (ListView) findViewById(R.id.Search_ListView);
        mTextView = (TextView) findViewById(R.id.Text_View);
        mEditText = (EditText) findViewById(R.id.Search_Text);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }
    /**
     * when a button is clicked.
     * @param view Object.
     */
    public void buttonClicked(View view) {
        AsyncTask<String, Void, String> task = null;
        mString = mEditText.getText().toString();
        switch (view.getId()) {
            case R.id.Submit_button:
                task = new PostWebServiceTask();
                task.execute(mURL);
                break;
            default:
                throw new IllegalStateException("Not implemented");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Categories) {
            // Handle the camera action
        } else if (id == R.id.nav_mapView) {

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /**
     * sample code provided by instructor.
     */
    private class PostWebServiceTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 1) {
                throw new IllegalArgumentException("Two String arguments required.");
            }
            HttpClient httpclient = HttpClients.createDefault();

            //modified example code from microsoft
            try
            {

                HttpEntity entity;

                URIBuilder builder = new URIBuilder(mURL);

                URI uri = builder.build();
                HttpPost request = new HttpPost(uri);
                request.setHeader("Content-Type", "application/json");
                request.setHeader("Ocp-Apim-Subscription-Key", mKey);

                String sent = mString;

                listOfWords = sent.split(" ");

                //Request Body
                StringEntity reqEntity = new StringEntity("{\"language\" : \"en\",\n" +
                        "\t\"analyzerIds\" : [\"4fa79af1-f22c-408d-98bb-b7d7aeef7f04\", \"22a6b758-420f-4745-8a3c-46835a67c0d2\"],\n" +
                        "\t\"text\" :" + "\""+ sent  +"\"}" );        //  \"I want to get pizza\" }");
                request.setEntity(reqEntity);




                HttpResponse response = httpclient.execute(request);
                entity = response.getEntity();
                String result =  new String(EntityUtils.toString(entity));

                //debug purposes.
                if (entity != null)
                {
                    Log.d("entity not null", result);
                }

                return result;
            }
            catch (Exception e)
            {

                String result = "Unable to connect, Reason: " + e.getMessage();
                return e.getMessage();
            }
        }
        @Override
        protected void onPostExecute(String result) {

            News news = null;

// Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            News[] newses = new News[0];
            try {
                JSONArray value = new JSONArray(result);
//                JSONArray value = resultObj.getJSONArray("value");
                newses = new News[value.length()];
                //listItems = new String[value.length()];
                for (int i = 0; i < value.length()-1; i++){
                    JSONObject oneNews = (JSONObject) value.get(i);
                  //  Log.d("LoadFromDB one", oneNews.getString("url"));
                    newses[i] = new News(oneNews);
                    //listItems[i] = newses[i].getName();
                }

            } catch (JSONException e){
                e.printStackTrace();
            }

            final News[] tempNewses = newses;
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i< tempNewses.length - 1; i++) {
                sb.append(tempNewses[i].getResult());
            }
            String tagOnly = sb.toString().substring(2, sb.toString().length() - 2);
            String[] tags = tagOnly.split(",");
            Log.d("Size: ", tags.length + "");

            for (int i = 0; i < tags.length; i++) {
                if (tags[i].equals("\"NN\"" )|| tags[i].equals("\"NNP\"") || tags[i].equals("\"NNS\"" )|| tags[i].equals("\"NNPS\""))
                {
                    Log.d("Index ", i + "");
                    Log.d("Word ", listOfWords[i]);
                }
            }

            Log.d("result", sb.toString());


//                //TODO need to check format to see if that's the right slash / vs \
//                //TODO this code only handles input with a single NNP tag. It overwrites old with new if a sentence has more than one
//                //TODO should consider keyWord being an array and incrememnting a counter in the while loop so we can return and parse all the NNPs
//                //TODO for some reason this doesn't work on Dylan's computer at all. It's not seeing any of this as a proper project and autocomplete + emulator aren't working :(
//                StringTokenizer st = new StringTokenizer(result, " /"); //delim on space and the slash between word/tag
//                String keyWord = null;
//                String prevWord = null;
//                while (st.hasMoreTokens()) {
//                    String temp = st.nextToken();
//                    if (temp.equals("NNP")) {
//                        keyWord = prevWord + '/' + temp; // word/tag
//                    }
//                }
//                return keywWord;


            mTextView.setText(sb.toString());

        }
    }
}
