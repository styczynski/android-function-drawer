package com.example.extremetic_tac_toe.tinyandui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleLayoutGameActivity;

import android.view.KeyEvent;

public class TContainer extends TComponent implements TSet {

	protected Vector<TComponent> mStack = null;
	protected BaseGameActivity mActivity;
	protected Scene mScene;
	
	public TContainer(BaseGameActivity pActivity) {
		mStack = new Vector<TComponent>();
		mActivity = pActivity;
		mScene = new Scene();
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
	
	public void updateParentActivity(SimpleLayoutGameActivity pActivity) {
		mActivity = pActivity;
	}
	
	public Scene draw() {
		return mScene;
	}
	
	public void flush() {
		final int size = mStack.size();
		for(int it=0;it<size;++it) {
			mStack.get(it).flush(mActivity, mScene);
		}
	}
	
	public TComponent getComponent(String id) {
		final int size = mStack.size();
		for(int it=0;it<size;++it) {
			if(mStack.get(it).id.equals(id)) {
				return mStack.get(it);
			}
		}
		return null;
	}
	
	@Override
	public void pushNewSetElement(TComponent component) {
		this.add(component);
	}
	
	@Override
	public TComponent recursiveSearch(String id) {
		System.out.println("Recursive search("+id+") in container...");
		final int len = mStack.size();
		for(int it=0;it<len;++it) {
	        if(mStack.get(it) instanceof TSet) {
	        	Object obj = ((TSet)mStack.get(it)).recursiveSearch(id);
	        	if(obj!=null){
	        		System.out.println("OBJECT WAS FOUND!");
	        		return (TComponent)obj; 
	        	}
	        } else {
	        	if(((TComponent)mStack.get(it)).id.equals(id)) {
	        		return (TComponent)mStack.get(it);
	        	}
	        }
	    }
		System.out.println("OBJECT NOT PRESENT!");
	    return null;
	}

	@Override
	public void onKey(KeyEvent event, BaseGameActivity activity, Scene scene) {
		final int size = mStack.size();
		for(int it=0;it<size;++it) {
			mStack.get(it).onKey(event, activity, scene);
		}
	}
	
	
	public void onKey(KeyEvent event) {
		onKey(event, mActivity, mScene);
	}
	
	@Override
	public void onInit(BaseGameActivity activity, Scene scene) {
		
	}

	@Override
	public void onRemove(BaseGameActivity activity, Scene scene) {
		
	}

	@Override
	public void flush(BaseGameActivity activity, Scene scene) {
		
	}
	
}
