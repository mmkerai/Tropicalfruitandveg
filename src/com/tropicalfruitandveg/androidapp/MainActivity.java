package com.tropicalfruitandveg.androidapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity 
{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
/*	    case R.id.settings:
		    startActivity(new Intent(this, Settings.class));
		    return true;
*/	    default:
	    
	    return super.onOptionsItemSelected(item);
		}
	}
	
    // Called when the user clicks the search or show buttons
    public void gettfvinfo(View view)
    {
    	String message;
        // Do something in response to button
        if(view.getId() == R.id.but_show)		// browse (show all) button
        {
        	message = "browse";   	
        }
        else	// must be the search button
        {
        	EditText searchText = (EditText) findViewById(R.id.searchText);
        	message = searchText.getText().toString();
        }

        Intent intent = new Intent(this, Summary.class);
    	intent.putExtra("SEARCH", message);
        startActivity(intent);
    }

}
