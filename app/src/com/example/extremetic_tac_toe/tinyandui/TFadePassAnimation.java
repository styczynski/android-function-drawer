package com.example.extremetic_tac_toe.tinyandui;

public class TFadePassAnimation implements TPassAnimation {
	
	private int mDurationTime = 5000;
	
	public TFadePassAnimation(int animationDuration) {
		mDurationTime = animationDuration;
	}
	
	@Override
	public void init(TFence source, TFence target) {
		source.setVisible(true);
		source.setOpacity(1.0f);
		source.block(true);
		source.flush();
		target.setVisible(true);
		target.setOpacity(0.0f);
		target.block(true);
		target.flush();
	}
	
	@Override
	public void deinit(TFence source, TFence target) {
		source.setVisible(false);
		source.setOpacity(1.0f);
		source.block(false);
		source.flush();
		target.setOpacity(1.0f);
		target.setVisible(true);
		target.block(false);
		target.flush();
	}
	
	@Override
	public void aniamte(TFence source, TFence target, int step) {
		source.setOpacity(source.getOpacity()-(1.0f/getStepsNumber()));
		source.validateOpacity();
		source.flush();
		target.setOpacity(target.getOpacity()+(1.0f/getStepsNumber()));
		target.validateOpacity();
		target.flush();
	}

	@Override
	public int getStepsNumber() {
		return mDurationTime/2;
	}
	
	@Override
	public int getAnimationDurationTime() {
		return mDurationTime;
	}

	
}
