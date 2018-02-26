package com.example.extremetic_tac_toe.tinyandui;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleLayoutGameActivity;

import com.example.extremetic_tac_toe.locale.logging.Logger;

import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;

public class TFence extends TComponent implements TSet {

	public HashMap<String, TComponent> mStack = new HashMap<String, TComponent>();
	protected Object parent = null;
	protected Scene mScene;
	protected BaseGameActivity mActivity;
	protected String mName = "?";
	
	protected TFence() {
		
	}
	
	public TFence( TContainer parent, String pName, boolean registerAtParent ) {
		super(TCore.ComponentClassID.T_FENCE);
		mScene = parent.mScene;
		mActivity = parent.mActivity;
		mName = pName;
		id = pName;
		if(registerAtParent) parent.add(this);
	}
	
	public TFence( TFence parent, String pName, boolean registerAtParent ) {
		super(TCore.ComponentClassID.T_FENCE);
		mScene = parent.mScene;
		mActivity = parent.mActivity;
		mName = pName;
		id = pName;
		if(registerAtParent) parent.add(pName, this);
	}
	
	public TFence( TContainer parent, String pName ) {
		this( parent, pName, true );
	}
	
	public TFence( TFence parent, String pName ) {
		this( parent, pName, true );
	}
	
	public String getName() {
		return mName;
	}
	
	public void setName(String pName) {
		mName = pName;
	}
	
	public int getComponentNumber() {
		return mStack.size();
	}
	
	public TComponent getComponent(String componentID) {
		return mStack.get(componentID);
	}
	
	public TComponent get(String componentID) {
		return mStack.get(componentID);
	}
	
	public <T extends TComponent> void addComponent(String componentID, T pComponent) {
		pComponent.onInit(mActivity, mScene);
		mStack.put(componentID, pComponent);
	}
	
	public <T extends TComponent> void add(String componentID, T pComponent) {
		addComponent(componentID, pComponent);
	}
	
	public <T extends TComponent> void addComponent(T pComponent) {
		pComponent.onInit(mActivity, mScene);
		mStack.put(pComponent.id, pComponent);
	}
	
	public <T extends TComponent> void add(T pComponent) {
		addComponent(pComponent.id, pComponent);
	}
	
	public void removeComponent(String componentID) {
		mStack.get(componentID).onRemove(mActivity, mScene);
		mStack.remove(componentID);
	}
	
	public void removeAllComponents() {
		int size = mStack.size();
		Iterator it = mStack.entrySet().iterator();
	    while (it.hasNext()) {
	        HashMap.Entry pairs = (HashMap.Entry)it.next();
	        ((TComponent)pairs.getValue()).onRemove(mActivity, mScene);
	    }
	}
	
	public void sdebug() {
		int size = mStack.size();
		Iterator it = mStack.entrySet().iterator();
		Logger.post(Logger.DEBUG, "All keyz: ", "");
	    while (it.hasNext()) {
	        HashMap.Entry pairs = (HashMap.Entry)it.next();
	       	Logger.post(Logger.DEBUG, "key = "+pairs.getKey().toString(), "");
	    }
	}
	
	public void runPassAnimation(TPassAnimation animation, TFence target) {

		new Thread(){
			private TPassAnimation mAnimation = null;
			private TFence mSrc, mTgt;
			public Thread init(TPassAnimation pAnimation, TFence pSrc, TFence pTgt) {
				mAnimation = pAnimation;
				mSrc = pSrc;
				mTgt = pTgt;
				return this;
			}
			@Override
			public void run() {
				mAnimation.init(mSrc, mTgt);
				final int steps = mAnimation.getStepsNumber();
				TSynchronizer sync = new TSynchronizer();
				sync.newMeasurement();
				for(int it=0;it<steps;++it) {
					sync.newMeasurement();
					while(sync.getFieldChange(Calendar.MILLISECOND)<((mAnimation.getAnimationDurationTime())/mAnimation.getStepsNumber())) {
						sync.removeLast();
						sync.newMeasurement();
					}
					sync.clear();
					sync.newMeasurement();
					mAnimation.aniamte(mSrc, mTgt, it+1);
				}
				mAnimation.deinit(mSrc, mTgt);
			}
		}.init(animation, this, target).start();
	}
	
	public void runPassAnimation(TPassAnimation animation) {
		new Thread(){
			private TPassAnimation mAnimation = null;
			private TFence mSrc, mTgt;
			public Thread init(TPassAnimation pAnimation, TFence pSrc, TFence pTgt) {
				mAnimation = pAnimation;
				mSrc = pSrc;
				mTgt = pTgt;
				return this;
			}
			@Override
			public void run() {
				mAnimation.init(mSrc, mTgt);
				final int steps = mAnimation.getStepsNumber();
				TSynchronizer sync = new TSynchronizer();
				sync.newMeasurement();
				for(int it=0;it<steps;++it) {
					sync.newMeasurement();
					while(sync.getFieldChange(Calendar.MILLISECOND)<((mAnimation.getAnimationDurationTime())/mAnimation.getStepsNumber())) {
						sync.removeLast();
						sync.newMeasurement();
					}
					sync.clear();
					sync.newMeasurement();
					mAnimation.aniamte(mSrc, mTgt, it+1);
				}
				mAnimation.deinit(mSrc, mTgt);
			}
		}.init(animation, new TNullFence(), this).start();
	}

	
	@Override
	public void onInit(BaseGameActivity activity, Scene scene) {
		
	}

	@Override
	public void onRemove(BaseGameActivity activity, Scene scene) {
		 removeAllComponents();
	}	
	
	@Override
	public void flush(BaseGameActivity activity, Scene scene) {
		
		Iterator it = mStack.entrySet().iterator();
	    while (it.hasNext()) {
	        HashMap.Entry pairs = (HashMap.Entry)it.next();
	        ((TComponent)pairs.getValue()).parentIndexZ = this.parentIndexZ;
	        ((TComponent)pairs.getValue()).parentPosX = posX+this.parentPosX;
	        ((TComponent)pairs.getValue()).parentPosY = posY+this.parentPosY;
	        ((TComponent)pairs.getValue()).parentVisible = visible&&this.parentVisible;
	        ((TComponent)pairs.getValue()).parentOpacity = opacity*this.parentOpacity;
	        ((TComponent)pairs.getValue()).parentBlocked = blocked&&this.parentBlocked;
	        ((TComponent)pairs.getValue()).flush(activity, mScene);
	    }
	    this.mScene.sortChildren();
	}
	
	@Override
	public void onKey(KeyEvent event, BaseGameActivity activity, Scene scene) {
		Iterator it = mStack.entrySet().iterator();
	    while (it.hasNext()) {
	        HashMap.Entry pairs = (HashMap.Entry)it.next();
	        ((TComponent)pairs.getValue()).onKey(event, activity, mScene);
	    }
	}
	
	@Override
	public void pushNewSetElement(TComponent component) {
		this.add(component);
	}
	
	@Override
	public TComponent recursiveSearch(String id) {
		
		if(id.equals(this.id)) {
			return this;
		}
		
		System.out.println("Recursive search("+id+") in fence...");
		Iterator it = mStack.entrySet().iterator();
	    while (it.hasNext()) {
	        HashMap.Entry pairs = (HashMap.Entry)it.next();
	        if(pairs.getValue()instanceof TSet) {
	        	Object obj = ((TSet)pairs.getValue()).recursiveSearch(id);
	        	if(obj!=null){
	        		System.out.println("OBJECT WAS FOUND!");
	        		return (TComponent)obj; 
	        	}
	        } else {
	        	if(((TComponent)pairs.getValue()).id.equals(id)) {
	        		return (TComponent)pairs.getValue();
	        	}
	        }
	    }
	    System.out.println("OBJECT NOT PRESENT");
	    return null;
	}
}