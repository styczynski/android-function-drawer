package com.example.extremetic_tac_toe;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class FunctionSmallViewer {

	Function function = null;
	
	public FunctionSmallViewer(Function funct) {
		function = funct;
	}
	
	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.GRAY);
		canvas.drawPaint(paint);
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(2);
		
		if(!function.isGood()) {
			return;
		}
		
		final int w = canvas.getWidth();
		final int h = canvas.getHeight();
		
		final double factor = 210;
		final double factordiv = 10;
		double small = Math.PI/factordiv;
		double large = factor*Math.PI;
		int cycles = 4;
		int n = 100;
		double delta = 20;
		double period = small;
		double fa = 0, fb = 0, fc = 0;
		boolean periodic = false;
		
		while(period<large) {
			System.out.println(((Object)period).toString()+"/"+((Object)large).toString());
			for(double x=small; x<large; x+=period) {
				fa = function.eval(x);
				periodic = true;
				for(int i=1;i<cycles;++i) {
					fb = function.eval(x+i*period);
					fc = function.eval(x + i*period + period/2);
					if(Math.abs(fb-fa)>delta||Math.abs(fc-fa)<delta) {
						periodic = false;
					}
				}
				if(periodic) {
					break;
				}
			}
			if(periodic) {
				break;
			}
			period = period+delta;
		}
	
		
		if(!periodic) {
			System.out.println("#2 Probe!");
			n = 150;
			large = 1000;
			double x = 2;
			double signSlope = Math.signum(function.evalDifferentiate(x, 1));
			for(int i=1;i<n;++i) {
				if(signSlope==Math.signum(function.evalDifferentiate(x, 1)) && (Math.abs(function.evalDifferentiate(x, 2)))<(x*x)/large || Math.abs(function.evalDifferentiate(x, 2))>large/(x*x)) {
					break;
				}
				System.out.println("X = "+((Object)x).toString());
				signSlope = Math.signum(function.evalDifferentiate(x, 1));
				x = 2*x;
			}
			period = x;
		}
		
		final double begx = -period;
		final double endx = period;
		final double graphing_interval = 0.001;//Math.abs(endx-begx)/w;
		System.out.println("Begx/endx: "+((Object)begx).toString()+"; "+((Object)endx).toString());
		
		final double undefmin = 9999999;
		final double undefmax = -9999999;
		double min = undefmin;
		double max = undefmax;
		double zoomx = 0, zoomy = 0;
		double temp;
		for(double x=begx;x<endx;x+=graphing_interval) {
			temp = function.eval(x);
			if(temp>max||max==undefmax) {
				max = temp;
			}
			if(temp<min||min==undefmin) {
				min = temp;
			}
		}
		if(max==undefmax) {
			max = -min;
		}
		if(min==undefmin) {
			min = -max;
		}
		while(Math.abs(Math.abs(max)-Math.abs(min))<10) {
			min*=2;
			max*=2;
		}
		System.out.println("Min = "+((Object)min).toString());
		System.out.println("Max = "+((Object)max).toString());
		
		zoomx = Math.abs(w/(endx-begx));
		zoomy = Math.abs(h/(Math.abs(max)-Math.abs(min)));
		double lastVal = -99999;
		double val = 0;
		boolean outOfBox = false;
		paint.setColor(function.color);
		int paintnum = 0;
		final int drawsteplim = 25;
		for(double x=begx;x<endx;x+=graphing_interval) {
			++paintnum;
			val = (zoomy*function.eval(x)-min);
			if(val>h&&(!outOfBox)) {
				val = h;
				canvas.drawLine((float)((x-graphing_interval-begx)*zoomx), (float)lastVal, (float)(zoomx*(x-begx)), (float)val, paint);
				outOfBox = true;
			}
			if(val<h&&outOfBox) {
				outOfBox = false;
				if(val>=h/2) {
					lastVal = h;
				} else {
					lastVal = 0;
				}
			}
			if(x>begx+1&&(!outOfBox)&&(paintnum>=drawsteplim)) {
				if(lastVal!=-99999) {
					canvas.drawLine((float)((x-drawsteplim*graphing_interval-begx)*zoomx), (float)lastVal, (float)(zoomx*(x-begx)), (float)val, paint);
				}
				lastVal = val;
				paintnum = 0;
			}
			
		}

	}
	
}
