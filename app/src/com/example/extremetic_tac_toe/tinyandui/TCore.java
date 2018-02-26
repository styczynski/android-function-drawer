package com.example.extremetic_tac_toe.tinyandui;

import java.util.Vector;

import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleLayoutGameActivity;

import com.example.extremetic_tac_toe.locale.logging.Logger;
import com.example.extremetic_tac_toe.tinyandui.algorithm.Pair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v4.app.NotificationCompat;
import android.view.Display;

public final class TCore {

	protected static BitmapTextureAtlas mDefaultComponentTextureAtlas = null;
	protected static TextureRegion mDefaultComponentTextureRegion = null;
	
	protected static BaseGameActivity globalActivity = null;

	protected static Font mDefaultComponentFont = null;
	
	protected static Vector< Pair< String, Pair<BitmapTextureAtlas,TextureRegion> > > bitmapAtlasRegistry = new Vector< Pair< String, Pair<BitmapTextureAtlas,TextureRegion> > >();
	protected static Vector< Pair<String, Font> > fontRegistry = new Vector< Pair< String, Font> >();
	
	private static int displayWidth = 0;
	private static int displayHeight = 0;
	
	public static BaseGameActivity getActivity() {
		return globalActivity;
	}
	
	public static final int getScreenW() {
		System.out.println("Screen dim is: "+((Object)displayWidth).toString()+"x"+((Object)displayHeight).toString());
		return displayWidth;
	}
	
	public static final int getScreenH() {
		System.out.println("Screen dim is: "+((Object)displayWidth).toString()+"x"+((Object)displayHeight).toString());
		
		return displayHeight;
	}
	
	public enum ComponentClassID {
		T_BUTTON,
		T_TEXT_BUTTON,
		T_DEFAULT,
		T_FENCE,
		T_PROGRESS_BAR;
	};
	
	public static float[] getDefaultComponentBounds(ComponentClassID ccid) {
		return new float[]{ 175, 85 };
		/*switch(ccid) {
			case T_DEFAULT:
				return new float[]{ 300, 150 };
			case T_BUTTON:
				return new float[]{ 300, 150 };
			case T_TEXT_BUTTON:
				return new float[]{ 300, 150 };
			default:
				return new float[]{ 300, 150 };
		}*/
	}
	
	public static <T extends BaseGameActivity> void loadFont( String path, T activity, int size, int color ) {
		Pair<String, Font> newEntry = new Pair<String, Font>();
		newEntry.first = path;
		newEntry.second = FontFactory.createFromAsset(activity.getFontManager(), activity.getTextureManager(), 256, 256, activity.getAssets(), path, size, true, color);
		fontRegistry.add(newEntry);
		newEntry.second.load();
	}
	
	
	public static <T extends BaseGameActivity> Font getFont( String path, T activity, int size, int color ) {
		int reglen = fontRegistry.size();
		for(int it=0;it<reglen;++it) {
			if(fontRegistry.get(it).first.equals(path)) {
				return fontRegistry.get(it).second;
			}
		}
		loadFont(path, activity, size, color);
		reglen = fontRegistry.size();
		for(int it=0;it<reglen;++it) {
			if(fontRegistry.get(it).first.equals(path)) {
				return fontRegistry.get(it).second;
			}
		}
		return null;
	}
	
	
	public static <T extends BaseGameActivity> void loadBitmap( String path, T activity ) {
		Pair< String, Pair<BitmapTextureAtlas,TextureRegion> > newEntry = new Pair< String, Pair<BitmapTextureAtlas,TextureRegion> >();
		newEntry.second = new Pair<BitmapTextureAtlas,TextureRegion>();
		newEntry.first = path;
	
		//60x30
		newEntry.second.first = new BitmapTextureAtlas(activity.getTextureManager(), 760, 730);
		newEntry.second.second = BitmapTextureAtlasTextureRegionFactory.createFromAsset(newEntry.second.first, activity, path, 0, 0);
		newEntry.second.first.load();
		bitmapAtlasRegistry.add(newEntry);
	}
	
	public static <T extends BaseGameActivity> TextureRegion getBitmap( String path, T activity ) {
		System.out.println("Finding bitmap {"+path+"}");
		int reglen = bitmapAtlasRegistry.size();
		for(int it=0;it<reglen;++it) {
			if(bitmapAtlasRegistry.get(it).first.equals(path)) {
				System.out.println("FOUND!");
				return bitmapAtlasRegistry.get(it).second.second;
			}
		}
		System.out.println("LOADED!");
		loadBitmap(path, activity);
		reglen = bitmapAtlasRegistry.size();
		for(int it=0;it<reglen;++it) {
			if(bitmapAtlasRegistry.get(it).first.equals(path)) {
				System.out.println("FOUND!");
				return bitmapAtlasRegistry.get(it).second.second;
			}
		}
		System.out.println("NOT FOUND!");
		return null;
	}
	
	public static <T extends BaseGameActivity> void init(T activity, int screenW, int screenH) {
		globalActivity = activity;
		bitmapAtlasRegistry.clear();
		mDefaultComponentTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(),  760, 730);
		mDefaultComponentTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mDefaultComponentTextureAtlas, activity, "taui/uistyle.png", 0, 0);
		mDefaultComponentTextureAtlas.load();

		FontFactory.setAssetBasePath("gfx/");
		mDefaultComponentFont = FontFactory.createFromAsset(activity.getFontManager(), activity.getTextureManager(), 256, 256, activity.getAssets(), "taui/default_font.ttf", 46, true, android.graphics.Color.WHITE);
		mDefaultComponentFont.load();
		
		final Display display = activity.getWindowManager().getDefaultDisplay();
	    displayWidth = screenW;
	    displayHeight = screenH;
	}
	
	public static <T extends BaseGameActivity> TInteractiveSprite getSprite(String path, T activity) {
		return new TInteractiveSprite(0, 0, getBitmap(path, activity), activity.getVertexBufferObjectManager() );
	}
	
	public static <T extends BaseGameActivity> TInteractiveSprite getDefaultComponentGraphics(T activity) {
		return new TInteractiveSprite(0, 0, mDefaultComponentTextureRegion, activity.getVertexBufferObjectManager() );
	}
	
	public static Font getDefaultComponentFont() {
		return mDefaultComponentFont;
	}
	
	public <T extends TComponent> String validateComponent(T component) {
		String result = "";
		if(component.id == null) {
			result += "[ERROR] ID of this component is null.\r";
		} else if(component.id == "#problem=<unknown_id>") {
			result += "[ERROR] ID wasn't assigned to this component.\r";
		}
		if(component.opacity>1.0f||component.opacity<0.0f) {
			result += "[WARNNING] Invalid component opacity. Expected value was in range <0,1> and it was x = "+((Object)component.opacity).toString()+"\r";
		}
		if(component.opacity>1.0f||component.opacity<0.0f) {
			result += "[WARNNING] Invalid component opacity delivered from parent. Expected value was in range <0,1> and it was x = "+((Object)component.parentOpacity).toString()+"\r";
		}
		if(result == "") {
			result = "Component was validated sucessfully. No errors nor warnnings were reported during this process.";
		}
		return result;
	}
	
}
