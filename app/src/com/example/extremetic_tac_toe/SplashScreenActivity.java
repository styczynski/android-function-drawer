package com.example.extremetic_tac_toe;

import com.example.extremetic_tac_toe.tinyandui.TDelayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class SplashScreenActivity extends Activity {

	 ImageView img;
	 Bitmap bmp;
	 Canvas canvas;
	 ProgressBar pbar;
	
	 public Canvas drawOnBegin() {
		 bmp = Bitmap.createBitmap(600, 600, Config.ARGB_8888);
	     canvas = new Canvas(bmp);
	     return canvas;
	 }
	 
	 public void drawOnEnd(Canvas canv) {
		 canvas = canv;
		 canvas.save();
		 runOnUiThread(new Runnable() {
			@Override
			public void run() {
				img.setImageBitmap(bmp);
			}
			 
		 });
	 }
	 
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.splash_screen_layout);
	        img = (ImageView) findViewById(R.id.splash_screen_image);
	        pbar = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);
	        bmp = Bitmap.createBitmap(300, 300, Config.ARGB_8888);
	        canvas = new Canvas(bmp);
	        new PrefetchData().execute();
	 }
	 
	 public void setSplashProgressValue(int value) {
		 pbar.setProgress(value);
	 }
	 
    private class PrefetchData extends AsyncTask<Void, Void, Void> {
    	
        @Override
        protected void onPreExecute() {
            super.onPreExecute();     
 
        }
 
        @Override
        protected synchronized Void doInBackground(Void... arg0) {
            TDelayer del = new TDelayer(/*5000*/0);
                        
            try {
            	int xvar = 0;
            		
					Canvas c = SplashScreenActivity.this.drawOnBegin();
		            Paint paint = new Paint();
		            paint.setColor(Color.TRANSPARENT);
		            canvas.drawPaint(paint);
		            paint.setColor(Color.WHITE);
		            
		            xvar = 150;
		            
		            c.drawBitmap(BitmapFactory.decodeResource(getResources(),
		            		R.drawable.sts_logo2), 25, 80, paint);
		            SplashScreenActivity.this.drawOnEnd(c);
					
					paint.setColor(Color.BLACK);
					paint.setTextSize(32);
					c.drawText("Styczynsky Digital Systems presents", 35, 400, paint);
					paint.setTextSize(37);
		            c.drawText("Simple Function Drawer", 35, 450, paint);
		            c.drawText("    (version very early alpha)", 35, 500, paint);
		            
		            LatexParser.init();
		            
		            while(!del.isRealized()) {      	
						runOnUiThread(new Runnable() {	
							@Override
							public void run() {
								SplashScreenActivity.this.pbar.setProgress(SplashScreenActivity.this.pbar.getProgress()+1);
								SplashScreenActivity.this.pbar.refreshDrawableState();
								img.setColorFilter((int)(Math.random()*Color.WHITE));
								img.refreshDrawableState();
								
							}
						});
						wait(10);
	            	}
            	//}
				//del.waitUntil(500);
			} catch (Throwable e) {
				e.printStackTrace();
			}
            
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
  	    	startActivity(intent);
            finish();
        }
 
    }
}
