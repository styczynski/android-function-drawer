package com.example.extremetic_tac_toe.tinyandui;

import java.util.Calendar;

public class TDelayer {

	private TSynchronizer sync = new TSynchronizer();
	private int delay = 0;
	
	public TDelayer() {
		this(0);
	}
	
	public TDelayer(int millisec) {
		sync.newMeasurement();
		delay = millisec;
	}
	
	public TDelayer(TDelayer delayer) {
		sync = new TSynchronizer(delayer.sync);
		delay = delayer.delay;
	}
	
	public boolean isRealized() {
		sync.newMeasurement();
		boolean ret = sync.getChangeMillisec()>=delay;
		sync.removeLast();
		return ret;
	}

	public synchronized void waitUntil(int timeSteps) throws InterruptedException {
		while(!isRealized()) {
			wait(timeSteps);
		}
	}
	
	public synchronized void waitUntil(int timeSteps, Runnable toExecute) throws InterruptedException {
		while(!isRealized()) {
			wait(timeSteps);
			toExecute.run();
		}
	}
	
	public synchronized void waitUntil() throws InterruptedException {
		waitUntil(25);
	}
	
	public synchronized void waitUntil(Runnable toExecute) throws InterruptedException {
		waitUntil(25, toExecute);
	}
	
	public void disable() {
		delay = 0;
	}


}
