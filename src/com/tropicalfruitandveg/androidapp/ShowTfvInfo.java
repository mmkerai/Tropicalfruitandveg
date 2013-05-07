package com.tropicalfruitandveg.androidapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class ShowTfvInfo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    // Get the message from the intent
	    Intent intent = getIntent();
	    String tfv = intent.getStringExtra("TFV");
		setContentView(R.layout.activity_detail);

		WebView webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(false);
		webView.loadUrl("http://api.tropicalfruitandveg.com/tfvhtmlapi.php?tfvitem=" + tfv);

		// Show the Up button in the action bar. Use for SDK 16 and up
//		getActionBar().setDisplayHomeAsUpEnabled(true);
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
	}

}
