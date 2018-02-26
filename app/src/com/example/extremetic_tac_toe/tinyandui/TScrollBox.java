package com.example.extremetic_tac_toe.tinyandui;

import java.util.HashMap;
import java.util.Iterator;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.BaseGameActivity;

public class TScrollBox extends TFence implements TGenericEventHandler {
	
	private TScrollBar scrollbar = null;
	private Sprite window_sprite = null;
	private Rectangle touch_area = null;
	private float minw = 0;
	private float minh = 0;
	private float maxw = 9999;
	private float maxh = 9999;
	
	public void setMinW(float pW) {
		minw = pW;
	}
	
	public void setMinH(float pH) {
		minh = pH;
	}
	
	public void setMaxW(float pW) {
		maxw = pW;
	}
	
	public void setMaxH(float pH) {
		maxh = pH;
	}
	
	public TScrollBox(TContainer parent, String pName) {
		super(parent, pName, false);
	}
	
	public TScrollBox(TFence parent, String pName) {
		super(parent, pName, false);
	}
	
	@Override
	public void onInit(BaseGameActivity activity, Scene scene) {
		this.skin.putDefaults(new TSkin().val("img", "taui/uistyle.png"));
		window_sprite = (Sprite) TCore.getSprite(skin.getValue("img"), activity);
		window_sprite.setPosition(posX+parentPosX, posY+parentPosY);
		window_sprite.setWidth(w);
		window_sprite.setHeight(h);
		window_sprite.setZIndex(indexZ+parentIndexZ);
		
		touch_area = new Rectangle(posX+parentPosX, posY+parentPosY, w, h, activity.getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
            	onTouchEvent(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY, 0);
            	return true;
            }
		};
		
		touch_area.setPosition(posX+parentPosX, posY+parentPosY);
		touch_area.setWidth(w);
		touch_area.setHeight(h);
		touch_area.setZIndex(indexZ+parentIndexZ-1);
		
		scene.registerTouchArea(touch_area);
		scene.attachChild(window_sprite);
		
		scrollbar = new TScrollBar(id+":scrollbar", posX+parentPosX-20, posY+parentPosY);
		scrollbar.onInit(activity, scene);
		scrollbar.addEventListener(new TScrollBarEventListener() {
			@Override
			public void whenValueChanged(float oldValue, float newValue) {
				flush(null, mScene);
			}
		});
		
		flush(activity, scene);
	}
	
	float lastY = -999;
	@Override
	public void onTouchEvent(TouchEvent pSceneTouchEvent, float X, float Y, int componentID) {
		System.out.println("ScrollBox pressed!");
		if(blocked||parentBlocked) {
			return;
		}
		if(!visible || !parentVisible) {
			return;
		}
		
		if(lastY==-999) {
			lastY = Y;
			return;
		}
		
		System.out.println("Detla = "+((Object)(((lastY-Y)/h)*100.0f)).toString());
		scrollbar.setValue(scrollbar.getValue()+((lastY-Y)/h)*100.0f);
		System.out.println("newValue = "+((Object)(scrollbar.getValue())).toString());
		lastY = Y;
		allign();
		flush();
		
		if(pSceneTouchEvent.isActionUp()||pSceneTouchEvent.isActionOutside()||pSceneTouchEvent.isActionCancel()) {
			lastY = -999;
		}
	}
	
	
	
	public void allign() {
		
		float maxy = -1;
		float maxx = -1;
		Iterator it = mStack.entrySet().iterator();
	    while (it.hasNext()) {
	        HashMap.Entry pairs = (HashMap.Entry)it.next();
	        TComponent comp = (TComponent)pairs.getValue();
	        if(maxy<comp.posY+comp.h) {
	        	maxy = comp.posY+comp.h;
	        }
	        if(maxx<comp.posX+comp.w) {
	        	maxx = comp.posX+comp.w;
	        }
	    }
	    h = Math.min(Math.max(minh, maxy+130), maxh);
	    w = Math.min(Math.max(minw, maxx+30), maxw);
	}
	
	@Override
	public void flush(BaseGameActivity activity, Scene scene) {
		window_sprite.setPosition(posX+parentPosX, posY+parentPosY);
		window_sprite.setZIndex(indexZ+parentIndexZ);
		window_sprite.setVisible(visible && parentVisible);
		window_sprite.setAlpha(Math.min(opacity, parentOpacity));
		window_sprite.setWidth(w);
		window_sprite.setHeight(h);
		
		touch_area.setPosition(posX+parentPosX, posY+parentPosY);
		touch_area.setZIndex(indexZ+parentIndexZ-1);
		touch_area.setVisible(visible && parentVisible);
		touch_area.setAlpha(Math.min(opacity, parentOpacity));
		touch_area.setWidth(w);
		touch_area.setHeight(h);
		
		//scrollbar.setPos(posX+parentPosX, posY+parentPosY);
		scrollbar.parentPosX = posX+parentPosX+w*0.9f;
		scrollbar.parentPosY = posY+parentPosY+10;
		scrollbar.setH(h);
		scrollbar.setW(50);
		
		float maxy = 0;
		
		Iterator it = mStack.entrySet().iterator();
	    while (it.hasNext()) {
	        HashMap.Entry pairs = (HashMap.Entry)it.next();
	        maxy+=((TComponent)pairs.getValue()).h;
	    }
	    final float kappa = (maxy)/100.0f;
	    it = mStack.entrySet().iterator();
		while (it.hasNext()) {
	        HashMap.Entry pairs = (HashMap.Entry)it.next();
	        
	        ((TComponent)pairs.getValue()).parentIndexZ = this.parentIndexZ+1;
	        ((TComponent)pairs.getValue()).parentPosX = posX+this.parentPosX+10;
	        ((TComponent)pairs.getValue()).parentPosY = posY+this.parentPosY-scrollbar.getValue()*kappa;
	        if(((TComponent)pairs.getValue()).posY-scrollbar.getValue()*kappa<0||((TComponent)pairs.getValue()).posY-scrollbar.getValue()*kappa>h) {
	        	((TComponent)pairs.getValue()).parentVisible = false;
	        } else {
	        	((TComponent)pairs.getValue()).parentVisible = visible&&this.parentVisible;
	        }
	        ((TComponent)pairs.getValue()).parentOpacity = opacity*this.parentOpacity;
	        ((TComponent)pairs.getValue()).parentBlocked = blocked&&this.parentBlocked;
	        ((TComponent)pairs.getValue()).flush(activity, mScene);
	    }
	    this.mScene.sortChildren();
	    
	    scrollbar.flush();
	}
	
	

}
