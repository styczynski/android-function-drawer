package com.example.extremetic_tac_toe.tinyandui;

import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleLayoutGameActivity;

import android.view.KeyEvent;

public abstract class TComponent {
	
	public TCore.ComponentClassID CLASS_ID = TCore.ComponentClassID.T_DEFAULT;
	
	private static final int stdZIndex = 100;
	
	protected float posX = 0;
	protected float posY = 0;
	protected int indexZ = stdZIndex;
	protected int parentIndexZ = 0;
	protected float parentPosX = 0;
	protected float parentPosY = 0;
	protected float w = 0;
	protected float h = 0;
	protected boolean parentVisible = true;
	protected boolean visible = true;
	protected float opacity = 1.0f;
	protected float parentOpacity = 1.0f;
	protected boolean blocked = false;
	protected boolean parentBlocked = false;
	
	protected TSkin skin = new TSkin();
	
	//public abstract void createFrom(TComponent comp);
	
	private boolean toReload = false;
	public void reload() {
		toReload = true;
	}
	
	public void setSkin(TSkin arg0) {
		skin = arg0;
		reload();
	}
	
	public void whenZIndexChanged() {
		
	}
	
	protected void elementarySkin(TSkin arg0) {
		String temp = "";
		
		temp = arg0.getValue("w");
		if(temp!=null) {
			w = Integer.parseInt(temp);
		}
		temp = arg0.getValue("h");
		if(temp!=null) {
			h = Integer.parseInt(temp);
		}
		temp = arg0.getValue("posx");
		if(temp!=null) {
			posX = Integer.parseInt(temp);
		}
		temp = arg0.getValue("posy");
		if(temp!=null) {
			posY = Integer.parseInt(temp);
		}
		temp = arg0.getValue("opacity");
		if(temp!=null) {
			opacity = Integer.parseInt(temp);
		}
		temp = arg0.getValue("visible");
		if(temp!=null) {
			visible = (Integer.parseInt(temp) == 0);
		}
	}
	
	protected boolean oldGeneralVisibleState = true;
	
	protected String id = "#problem=<unknown_id>";	
	
	
	public void updateElement(BaseGameActivity activity, Scene scene) {
		oldGeneralVisibleState = parentVisible && visible;
		if(toReload) {
			toReload = false;
			this.onRemove(activity, scene);
			this.onInit(activity, scene);
			this.flush(activity, scene);
		}
	}
	
	public boolean isVisibleChanged() {
		return oldGeneralVisibleState != (parentVisible && visible);
	}
	
	public TComponent(TCore.ComponentClassID ccid) {
		CLASS_ID = ccid;
		float[] defaultBounds = TCore.getDefaultComponentBounds(CLASS_ID);
		w = defaultBounds[0];
		h = defaultBounds[1];
	}
	
	public TComponent() {
		CLASS_ID = TCore.ComponentClassID.T_DEFAULT;
	}
	
	public void assignID(String newID) {
		id = newID;
	}
	
	public boolean isBlocked() {
		return blocked;
	}
	
	public void block(boolean state) {
		blocked = state;
	}
	
	public float getOpacity() {
		return opacity;
	}
	
	public void setOpacity(float pOpacity) {
		opacity = pOpacity;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean state) {
		visible = state;
	}
	
	public float getPosX() {
		return posX;
	}
	
	public float getW() {
		return w;
	}
	
	public float getH() {
		return h;
	}
	
	public void setW(float pW) {
		w = pW;
	}
	
	public void setH(float pH) {
		h = pH;
	}
	
	public void setZIndex(int pZ) {
		indexZ = pZ+stdZIndex;
		whenZIndexChanged();
	}
	
	public int getZIndex() {
		return indexZ-stdZIndex;
	}
	
	public void setPosX(float pX) {
		posX = pX;
	}
	
	public float getPosY() {
		return posY;
	}
	
	public void setPosY(float pY) {
		posY = pY;
	}

	public void move(float pAX, float pAY) {
		posX += pAX;
		posY += pAY;
	}
	
	public void setPos(float pX, float pY) {
		posX = pX;
		posY = pY;
	}
	
	public abstract void onKey(KeyEvent event, BaseGameActivity activity, Scene scene);
	
	public abstract void onInit(BaseGameActivity activity, Scene scene);
	
	public abstract void onRemove(BaseGameActivity activity, Scene scene);
	
	public abstract void flush(BaseGameActivity activity, Scene scene);
	
	public void onTouchEvent(TouchEvent pSceneTouchEvent, float X, float Y, int componentID) {
		
	}
	
	public void flush() {
		flush(null, null);
	}
	
	public String toString() {
		String ret = "";
		ret+="@Component:{"+id+"} [x="+((Object)posX).toString()+"; y="+((Object)posY).toString()+"; parent.x="+((Object)parentPosX).toString()+"; parent.y="+((Object)parentPosY).toString()+"; w="+w+"; h="+((Object)h).toString()+";]";
		return ret;
	}
	
	public void validateOpacity() {
		if(opacity < 0) {
			opacity = 0;
		}
		if(opacity > 1.0f) {
			opacity = 1.0f;
		}
		if(parentOpacity < 0) {
			parentOpacity = 0;
		}
		if(parentOpacity > 1.0f) {
			parentOpacity = 1.0f;
		}
	}
}
