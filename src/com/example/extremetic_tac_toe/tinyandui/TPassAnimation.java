package com.example.extremetic_tac_toe.tinyandui;

public interface TPassAnimation {

	public void deinit(TFence source, TFence target);
	
	public void init(TFence source, TFence target);
	
	public void aniamte(TFence source, TFence target, int step);
	
	public int getStepsNumber();
	
	public int getAnimationDurationTime();
}
