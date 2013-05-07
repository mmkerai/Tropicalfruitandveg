package com.tropicalfruitandveg.androidapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Summary extends Activity
{
	public static int index = 0;
	public static final int BAD = 0;
	public static final int GOOD = 1;
	private String message;
	private String error = "";
	private Handler myInternetHandle;
	private ArrayList<TfvItem> items = new ArrayList<TfvItem>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	    // Get the message from the intent
	    Intent intent = getIntent();
	    message = intent.getStringExtra("SEARCH");
	    setContentView(R.layout.activity_summary);
		
	    myInternetHandle = new Handler()
		{
			@Override
			public void handleMessage(Message msg) 
			{
				int status = msg.what;
			    if(status == BAD)
			    {
					TextView tv = new TextView(getApplicationContext());
				    tv.setTextSize(14);
				    tv.setText(error);
				    tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
				    tv.setBackgroundColor(0xFFFFFFA0);		// background to yellow - first byte is alpha
					setContentView(tv);
			    }
			    else
			    {
					final ListView listView = (ListView) findViewById(R.id.myListViewId);
					TfvItemAdapter adapter = new TfvItemAdapter(getApplicationContext(),android.R.layout.simple_list_item_1, items);
	
					// Assign adapter to ListView
					listView.setAdapter(adapter); 
					listView.setClickable(true);
					listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
					{
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) 
						{
							index = position;
						    TfvItem myc = (TfvItem) listView.getItemAtPosition(position);
	//					    Toast.makeText(getApplicationContext(),"Item(1) " + myc.tfvname +" Item(2) " + myc.botname, Toast.LENGTH_LONG).show();
						    Intent myIntent = new Intent(getApplicationContext(), ShowTfvInfo.class);
						    myIntent.putExtra("TFV", myc.tfvname);
						    startActivity(myIntent);
						}  
					});
			    }
			}
		};
	    
		if(isOnline() == false)
		{
			new AlertDialog.Builder(this)
		    .setTitle("Network Connection not Available")
		    .setMessage("This app requires a network connection.")
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() 
		    {
		        public void onClick(DialogInterface dialog, int which) 
		        { 
		            finish();
		        }
		    })
/*			    .setNegativeButton("No", new DialogInterface.OnClickListener() 
		    {
		        public void onClick(DialogInterface dialog, int which) 
		        { 
		            // do nothing
		        }
		    })
*/			    .show();			
		}
		else
		{
//		    Toast.makeText(getApplicationContext(),"New Thread", Toast.LENGTH_LONG).show();
			// Need new thread to access Internet
			Thread t = new Thread(new FetchInternet());
			String txt;
			if(message.contentEquals("browse"))		// if blank then must be show all
			{
		    	message = "all";
		    	txt = "Retrieving full list";
			}
			else
			{
				txt = "Searching for " + message;
			}

		    Toast.makeText(getApplicationContext(),txt, Toast.LENGTH_LONG).show();
	        t.start(); 				    
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    //respond to menu item selection
		switch (item.getItemId()) 
		{
	    case R.id.about:
		    startActivity(new Intent(this, About.class));
		    return true;

	    default:
	    
	    return super.onOptionsItemSelected(item);
		}
	}

	@Override
    protected void onPause()
    {
        super.onPause();
    }
    
	@Override
    protected void onResume()
	{
	   super.onResume();
//	   listView.setSelection(index);
	}

	public boolean isOnline()
	{
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
	    NetworkInfo ni = cm.getActiveNetworkInfo();
	    if (ni!=null && ni.isAvailable() && ni.isConnected()) 
	    {
	        return true;
	    }
	    return false;
	}
	
	private class FetchInternet implements Runnable
	{		
	    public void run()
	    {
    		int status = BAD;
	    	try
	    	{
			    String sstr = "http://tropicalfruitandveg.com/api/tfvjsonapi.php?search=" + message;
				String tfvjsonresponse = getTfvFeed(sstr);
				// process the response from the Internet
			    try
			    {
//		    		JSONArray jsonArray = new JSONArray(tfvjsonresponse);
					JSONObject jObject = new JSONObject(tfvjsonresponse);
					if(jObject.has("error"))
					{
						error = jObject.getString("error");
						Log.i("TFV", "JSON error " + error);
					    status = BAD;
					}
					String count = jObject.getString("tfvcount");
					Log.i("TFV", "TFV count is "+ count);
					JSONArray results = jObject.getJSONArray("results");

				    for (int i = 0; i < results.length(); i++)
		    		{
		    			JSONObject res = results.getJSONObject(i);
		    			String tname = res.getString("tfvname");
		    			String bname = res.getString("botname");
		    			String oname = res.getString("othname");
		    			String timage = res.getString("imageurl");
		    			TfvItem tfv = new TfvItem(tname, bname, oname, timage);
		    			items.add(tfv);
		    		}
				    status = GOOD;
			    }
			    catch (Exception e)
			    {
			    	e.printStackTrace();
			    }

//				myInternetHandle.sendEmptyMessage(state);
	    	}
	    	catch (Throwable t)
	    	{
	    		t.printStackTrace();
	    	}

	    	myInternetHandle.sendEmptyMessage(status);
	    }
	    
		public String getTfvFeed(String url)
		{
		    StringBuilder builder = new StringBuilder();
		    HttpClient client = new DefaultHttpClient();
		    HttpGet httpGet = new HttpGet(url);
		    try
		    {
	    		HttpResponse response = client.execute(httpGet);
	    		StatusLine statusLine = response.getStatusLine();
	    		int statusCode = statusLine.getStatusCode();
	    		if (statusCode == 200)
	    		{
	    			HttpEntity entity = response.getEntity();
	    			InputStream content = entity.getContent();
	    			BufferedReader reader = new BufferedReader(new InputStreamReader(content));
	    			String line;
	    			while ((line = reader.readLine()) != null)
	    			{
	    				builder.append(line);
	    			}
	    		}
	    		else
	    		{
	    			Log.e("TFV", "Failed to download file");
	    		}
	    	}
		    catch (ClientProtocolException e)
		    {
		    	e.printStackTrace();
		    }
		    catch (IOException e)
		    {
		    	e.printStackTrace();
		    }
		    
		    return builder.toString();
		}
		
	}

}

