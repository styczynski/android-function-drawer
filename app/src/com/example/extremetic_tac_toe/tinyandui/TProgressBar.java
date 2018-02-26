package com.example.extremetic_tac_toe.tinyandui;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleLayoutGameActivity;

import android.view.KeyEvent;

import com.example.extremetic_tac_toe.locale.logging.Logger;


public class TProgressBar extends TExComponent<TProgressBarEventListener> {

	private Sprite skin = null;
	private int progress = 0;
	private int max = 100;
	private int width = 100;
	private int height = 10;
	private boolean runStatus = false;
	
	public void run() {
		runStatus = true;
	}
	
	public void suspend() {
		runStatus = false;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public TProgressBar(String ID, float pX, float pY) {
		super(TCore.ComponentClassID.T_PROGRESS_BAR);
		posX = pX;
		posY = pY;
		assignID(ID);
	}
	
	public int getProgress() {
		return progress;
	}
	
	public void setProgress(int pProgress) {
		Logger.post(Logger.DEBUG, "setProgress("+((Object)pProgress)+"); invoked.", "");
		progress = pProgress;
		checkProgress();
	}
	
	public void increaseProgress(int pProgress) {
		progress += pProgress;
		checkProgress();
	}
	
	public void decreaseProgress(int pProgress) {
		progress -= pProgress;
		checkProgress();
	}
	
	public void clearProgress() {
		progress = 0;
		checkProgress();
	}
	
	public void setMax(int pMax) {
		max = pMax;
		checkProgress();
	}
	
	public int getMax() {
		return max;
	}
	
	@Override
	public void onInit(BaseGameActivity activity, Scene scene) {
		skin = TCore.getDefaultComponentGraphics(activity);
		skin.setColor(1.0f, 0.0f, 0.0f);
		scene.attachChild(skin);
		flush(activity, scene);
	}

	@Override
	public void onRemove(BaseGameActivity activity, Scene scene) {
		scene.detachChild(skin);
	}

	@Override
	public void onKey(KeyEvent event,BaseGameActivity activity, Scene scene) {
		
	}
	
	@Override
	public void flush(BaseGameActivity activity, Scene scene) {
		
		skin.setWidth((progress*(width)/max));
		skin.setHeight(height);
		skin.setPosition(posX+parentPosX, posY+parentPosY);
		skin.setAlpha(Math.min(parentOpacity, opacity));
		skin.setVisible(visible && parentVisible);
		
		updateElement(activity, scene);
	}
	
	private void checkProgress() {
		if(runStatus) {
			if(progress>=max&&progress!=0) {
				final int size = getEventListenersNumber();
				for(int it=0;it<size;++it) {
					getEventListener(it).whenCompleted();
				}
			}
			final int size = getEventListenersNumber();
			for(int it=0;it<size;++it) {
				getEventListener(it).whenProgressChanged();
			}
		}
	}

}
