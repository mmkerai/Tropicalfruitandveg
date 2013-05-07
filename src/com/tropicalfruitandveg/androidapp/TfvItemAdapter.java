package com.tropicalfruitandveg.androidapp;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TfvItemAdapter extends ArrayAdapter<TfvItem>
{
    private ArrayList<TfvItem> users;
    
    
    public TfvItemAdapter(Context context, int textViewResourceId, ArrayList<TfvItem> users)
    {
        super(context, textViewResourceId, users);
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        if (v == null)
        {
            LayoutInflater vi = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.tfv_item, null);
        }
             
        TfvItem user = users.get(position);
        if (user != null)
        {
            TextView tfvname = (TextView) v.findViewById(R.id.tfvname);
            TextView botname = (TextView) v.findViewById(R.id.botname);
            TextView othname = (TextView) v.findViewById(R.id.othname);
            ImageView tfvimage = (ImageView) v.findViewById(R.id.tfvimage);

            if (tfvname != null)
            {
                tfvname.setText(user.tfvname);
            }

            if(botname != null)
            {
            	botname.setText(user.botname);
            }

            if(othname != null)
            {
            	othname.setText(user.othname);
            }

            if(tfvimage != null)
            {
            	            	
//            	tfvimage.setImageBitmap(bmp);
                new DownloadImageTask(tfvimage).execute(user.tfvimage);
            }
        }
       
        return v;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> 
    {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) 
        {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) 
        {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try 
            {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } 
            catch (Exception e) 
            {
//              Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) 
        {
            bmImage.setImageBitmap(result);
        }
    }
 
}
