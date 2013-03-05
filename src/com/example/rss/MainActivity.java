package com.example.rss;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public class RssLoadingTask extends AsyncTask<Void, Void, Void> {
		
		String url;
		public RssLoadingTask(String url2) {
			this.url = url2;
		}
		 @Override
		 protected void onPostExecute(Void result) {
		  // TODO Auto-generated method stub
		  //displayRss();

		        try{
		        	d.dismiss();
		        ListView l = (ListView) findViewById(R.id.listView1);
		        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,
                        android.R.layout.simple_list_item_1, DayofWeek);
				l.setAdapter(adapter);
		        
		        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						// TODO Auto-generated method stub
						Uri uri = Uri.parse(links.get(arg2).toString());
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(intent);
						//Toast.makeText(getBaseContext(), "OK You have selected "+DayofWeek[arg2], Toast.LENGTH_SHORT).show();
					}
		        	
				});
		        } catch (Exception e) {
		        	
	            }
		 }

		 @Override
		 protected void onPreExecute() {
		  // TODO Auto-generated method stub
		  //preReadRss();
		 }

		 @Override
		 protected void onProgressUpdate(Void... values) {
		  // TODO Auto-generated method stub
		  //super.onProgressUpdate(values);
		 }

		 @Override
		 protected Void doInBackground(Void... arg0) {
		  // TODO Auto-generated method stub
			 
			 fetchData(this.url);
			 return null;
		 }

		}
	
	List DayofWeek;
	List links;
	ProgressDialog d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new RssLoadingTask("http://androiduos.wordpress.com/feed").execute();
        d = new ProgressDialog(this);
        d.setTitle("Fetching Data...");
        d.show();
        
        
        
   
    }
    
    public void fetchData(String url2) {
    	 DayofWeek = new ArrayList();
         links = new ArrayList();
    	
        try {
        URL url = new URL(url2);

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(false);
        XmlPullParser xpp = factory.newPullParser();

            // We will get the XML from an input stream
        xpp.setInput(getInputStream(url), "UTF-8");
        
        boolean insideItem = false;
        int i = 0;
        // Returns the type of current event: START_TAG, END_TAG, etc..
        
        int eventType = xpp.getEventType();
    
    while (eventType != XmlPullParser.END_DOCUMENT) {
        if (eventType == XmlPullParser.START_TAG) {

            if (xpp.getName().equalsIgnoreCase("item")) {
                insideItem = true;
            } else if (xpp.getName().equalsIgnoreCase("title")) {
                if (insideItem) {
                    DayofWeek.add(xpp.nextText());
                    
                }
            } else if (xpp.getName().equalsIgnoreCase("link")) {
                if (insideItem)
                    links.add(xpp.nextText()); //extract the link of article
            }
        }else if(eventType==XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")){
            insideItem=false;
        }

        eventType = xpp.next(); //move to next element
    }
    

}  catch (Exception e) {
	Log.d("Error","Error aa gaya");
	e.printStackTrace();
    
}

    }
    public InputStream getInputStream(URL url) {
    	
        try {
        	
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
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
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.refresh:
        	new RssLoadingTask("http://androiduos.wordpress.com/feed").execute();
            d.show();
            return true;
        
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
}
