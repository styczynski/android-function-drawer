package com.example.extremetic_tac_toe.tinyandui;

import com.example.extremetic_tac_toe.locale.logging.Logger;

public class TDriveOutPassAnimation implements TPassAnimation {

	public static final short ANIMATION_TO_LEFT = 1;
	public static final short ANIMATION_TO_RIGHT = 2;
	public static final short ANIMATION_TO_TOP = 3;
	public static final short ANIMATION_TO_BOTTOM = 4;
	
	private short mAnimationType = ANIMATION_TO_RIGHT;
	private int mDurationTime = 5000;
	
	public TDriveOutPassAnimation(final short animationType, int animationDuration) {
		mAnimationType = animationType;
		mDurationTime = animationDuration;
	}
	
	@Override
	public void init(TFence source, TFence target) {
		source.setVisible(true);
		source.block(true);
		source.flush();
		switch(mAnimationType) {
			case ANIMATION_TO_RIGHT:
				target.move(-500, 0);
				break;
			case ANIMATION_TO_LEFT:
				target.move(500, 0);
				break;
			case ANIMATION_TO_TOP:
				target.move(0, -1000);
				break;
			case ANIMATION_TO_BOTTOM:
				target.move(0, 1000);
				break;	
		}
		target.block(true);
		target.setVisible(true);
		target.flush();
	}
	
	@Override
	public void deinit(TFence source, TFence target) {
		source.setVisible(false);
		switch(mAnimationType) {
			case ANIMATION_TO_RIGHT:
				source.move(-500, 0);
				break;
			case ANIMATION_TO_LEFT:
				source.move(500, 0);
				break;
			case ANIMATION_TO_TOP:
				source.move(0, -1000);
				break;
			case ANIMATION_TO_BOTTOM:
				source.move(0, 1000);
				break;	
		}
		source.block(false);
		source.flush();
		target.block(false);
		target.setVisible(true);
		target.flush();
	}
	
	@Override
	public void aniamte(TFence source, TFence target, int step) {
		switch(mAnimationType) {
			case ANIMATION_TO_RIGHT:
				source.move(500f/((float)getStepsNumber()), 0);
				source.flush();
				target.move(500f/((float)getStepsNumber()), 0);
				target.flush();
				break;
			case ANIMATION_TO_LEFT:
				source.move(-500f/((float)getStepsNumber()), 0);
				source.flush();
				target.move(-500f/((float)getStepsNumber()), 0);
				target.flush();
				break;
			case ANIMATION_TO_TOP:
				source.move(0, 1000f/((float)getStepsNumber()));
				source.flush();
				target.move(0, 1000f/((float)getStepsNumber()));
				target.flush();
				break;
			case ANIMATION_TO_BOTTOM:
				source.move(0, -1000f/((float)getStepsNumber()));
				source.flush();
				target.move(0, -1000f/((float)getStepsNumber()));
				target.flush();
				break;
		}
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
