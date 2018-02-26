package com.example.extremetic_tac_toe.tinyandui;

import java.util.Vector;

import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleLayoutGameActivity;

public abstract class TExComponent<T> extends TComponent {

	protected Vector<T> eventListenerStack = new Vector<T>();
	
	public TExComponent(TCore.ComponentClassID ccid) {
		super(ccid);
	}
	
	public void addEventListener(T listener) {
		eventListenerStack.add(listener);
	}
	
	public void removeEventListener(T listener) {
		eventListenerStack.remove(listener);
	}
	
	public void removeEventListener(int position) {
		eventListenerStack.remove(position);
	}
	
	public int getEventListenersNumber() {
		return eventListenerStack.size();
	}
	
	public T getEventListener(int position) {
		return eventListenerStack.get(position);
	}
	
	public void removeAllEventListeners() {
		eventListenerStack.clear();
	}

	@Override
	public abstract void onInit(BaseGameActivity activity, Scene scene);

	@Override
	public abstract void onRemove(BaseGameActivity activity, Scene scene);
	
}
