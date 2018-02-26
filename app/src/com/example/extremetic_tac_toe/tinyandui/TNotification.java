package com.example.extremetic_tac_toe.tinyandui;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.extremetic_tac_toe.MainActivity;
import com.example.extremetic_tac_toe.R;

public class TNotification {

	private int mID = -1;
	private String mTitle = "";
	private String mContent = "";
	private int mIcon = 0;
	private NotificationCompat.Builder mBuilder = null;
	
	public int getIcon() {
		return mIcon;
	}
	
	public void setIcon(int pIcon) {
		mIcon = pIcon;
	}
	
	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public String getContent() {
		return mContent;
	}

	public void setContent(String mMessage) {
		this.mContent = mMessage;
	}

	public int getID() {
		return mID;
	}

	
	public <T extends Activity> TNotification( int pID, String pTitle, String pMessage, int pIcon, T activity ) {
		mID = pID;
		mTitle = pTitle;
		mContent = pMessage;
		mIcon = pIcon;
		 PendingIntent pendingIntent = PendingIntent.getActivity(
		  	      activity, 
		  	      0,
		  	      new Intent(),
		  	      Intent.FLAG_ACTIVITY_NEW_TASK);
			mBuilder = new NotificationCompat.Builder(activity)
				    .setSmallIcon(mIcon)
				    .setContentTitle(mTitle)
				    .setContentText(mContent)
				    .setStyle(new NotificationCompat.BigTextStyle().bigText(mContent+":bigMessage!"))
				    .addAction (R.drawable.ic_launcher, "dismiss", pendingIntent)
					.addAction (R.drawable.ic_launcher, "snooze", pendingIntent);
			mBuilder.setContentIntent(pendingIntent);
			NotificationManager mNotifyMgr = (NotificationManager) activity.getSystemService(Activity.NOTIFICATION_SERVICE);
			mNotifyMgr.notify(mID, mBuilder.build());
	}
	
	public void setProgress(int pMax, int pProgress, boolean pIndeterminate) {
		mBuilder.setProgress(pMax, pProgress, pIndeterminate);
	}
	
	public void setProgressFinished() {
		mBuilder.setProgress(0, 0, false);
	}
	
	public <T extends Activity> void cancel(T activity) {
		NotificationManager mNotifyMgr = (NotificationManager) activity.getSystemService(Activity.NOTIFICATION_SERVICE);
		mNotifyMgr.cancel(mID);
	}
	
	public <T extends Activity> void build(T activity) {
		NotificationManager mNotifyMgr = (NotificationManager) activity.getSystemService(Activity.NOTIFICATION_SERVICE);
		mNotifyMgr.notify(mID, mBuilder.build());
	}
	
}
