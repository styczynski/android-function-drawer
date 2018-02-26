package com.example.extremetic_tac_toe.tinyandui;

import java.util.Calendar;
import java.util.Vector;

import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleLayoutGameActivity;

import com.example.extremetic_tac_toe.locale.logging.Logger;
import com.example.extremetic_tac_toe.tinyandui.TCore.ComponentClassID;

import android.os.Handler;
import android.os.Looper;

public abstract class TFence__ extends TComponent {

	public TFence__(ComponentClassID ccid) {
		super(ccid);
		// TODO Auto-generated constructor stub
	}
/*
	protected Vector<TComponent> mStack = new Vector<TComponent>();
	protected Object parent = null;
	protected Scene mScene;
	protected BaseGameActivity mActivity;
	protected String mName = "?";
	
	
	public TFence__( TContainer parent, String pName ) {
		mScene = parent.mScene;
		mActivity = parent.mActivity;
		mName = pName;
		parent.add(this);
	}
	
	public TFence__( TFence parent, String pName ) {
		mScene = parent.mScene;
		mActivity = parent.mActivity;
		mName = pName;
		parent.add(this);
	}
	
	public String getName() {
		return mName;
	}
	
	public void setName(String pName) {
		mName = pName;
	}
	
	public <T extends TComponent> int addComponent(T pComponent) {
		pComponent.onInit(mActivity, mScene);
		mStack.add(pComponent);
		return mStack.size()-1;
	}
	
	public <T extends TComponent> int add(T pComponent) {
		return addComponent(pComponent);
	}
	
	public <T extends TComponent> void removeComponent(T pComponent) {
		pComponent.onRemove(mActivity, mScene);
		mStack.removeElement(pComponent);
	}
	
	public void removeComponent(int pLocation) {
		mStack.get(pLocation).onRemove(mActivity, mScene);
		mStack.remove(pLocation);
	}
	
	public void removeAllComponents() {
		int size = mStack.size();
		for(int it=0;it<size;++it) {
			removeComponent(it);
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

	@Override
	public void onInit(BaseGameActivity activity, Scene scene) {
		
	}

	@Override
	public void onRemove(BaseGameActivity activity, Scene scene) {
		 removeAllComponents();
	}
	
	public void flush() {
		final int size = mStack.size();
		for(int it=0;it<size;++it) {
			mStack.get(it).parentPosX = posX;
			mStack.get(it).parentPosY = posY;
			mStack.get(it).parentVisible = visible;
			mStack.get(it).parentOpacity = opacity;
			mStack.get(it).parentBlocked = blocked;
			mStack.get(it).flush();
		}
	}
	*/
}
