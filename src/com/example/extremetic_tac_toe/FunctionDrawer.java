package com.example.extremetic_tac_toe;

import java.util.Vector;

import org.andengine.input.touch.TouchEvent;

import expr.Expr;
import expr.Parser;
import expr.SyntaxException;
import expr.Variable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


public class FunctionDrawer {

	public FunctionDrawer() {
		
    }
	
	protected static double sw = 0.0;
	protected static double sh = 0.0;
	protected static double stepsNumber = 50;
	protected static boolean useAutoStep = false;
	protected static double XAxisLabels = 7;
	protected static double YAxisLabels = 7;
	protected static boolean swapXYaxis = false;
	
	protected static interface XYDisplay {
		public void init();
		public double getX(double x, double y);
		public double getY(double x, double y);
		public double getStep();
		public String getName();
	}
	
	protected static class XYDefault implements XYDisplay {	
		@Override
		public void init() {
			
		}
		@Override
		public String getName() {
			return "default";
		}
		@Override
		public double getStep() {
			if(useAutoStep) {
				return sw/50;
			}
			return sw/stepsNumber;
		}
		@Override
		public double getX(double x, double y) {
			return x;
		}
		@Override
		public double getY(double x, double y) {
			return y;
		}
	}
	
	
	protected static class XYNormalNotProportional implements XYDisplay {
		public double scaleX = 3.0;
		public double scaleY = 7.0;
		public boolean swapXY = false;
		
		@Override
		public void init() {
			
		}
		@Override
		public String getName() {
			return "normalnprop";
		}
		@Override
		public double getStep() {
			if(useAutoStep) {
				return sw/50;
			}
			return sw/stepsNumber;
		}
		@Override
		public double getX(double x, double y) {
			if(swapXY) {
				return (scaleY*(y-sh/2)+sh/2)/sh*sw;
			}
			return scaleX*(x-sw/2)+sw/2;
		}
		@Override
		public double getY(double x, double y) {
			if(swapXY) {
				return (scaleX*(x-sw/2)+sw/2)/sw*sh;
			}
			return scaleY*(y-sh/2)+sh/2;
		}
	}
	
	protected static class XYSwap implements XYDisplay {
		@Override
		public void init() {
			
		}
		@Override
		public String getName() {
			return "swap";
		}
		@Override
		public double getStep() {
			if(useAutoStep) {
				return sw/50;
			}
			return sw/stepsNumber;
		}
		@Override
		public double getX(double x, double y) {
			return (y/sh)*sw;
		}
		@Override
		public double getY(double x, double y) {
			return (x/sw)*sh;
		}
	}
	
	protected static class XYRadial implements XYDisplay {
		@Override
		public void init() {
			
		}
		@Override
		public String getName() {
			return "radial";
		}
		@Override
		public double getStep() {
			if(useAutoStep) {
				return sw/50;
			}
			return sw/stepsNumber;
		}
		@Override
		public double getX(double x, double y) {
			return (y/sh)*Math.cos(Math.toRadians(x*360/sw))*sw+sw/2;
		}
		@Override
		public double getY(double x, double y) {
			return (y/sh)*Math.sin(Math.toRadians(x*360/sw))*sh+sh/2;
		}
	}
	
	protected static XYDisplay makeXYDisplay(String name) {
		if(name.equals("default")) {
			return new XYDefault();
		} else if(name.equals("swap")) {
			return new XYSwap();
		} else if(name.equals("radial")) {
			return new XYRadial();
		} else if(name.equals("normalnprop")) {
			return new XYNormalNotProportional();
		}
		return new XYDefault();
	}
	
	protected static XYDisplay curDisplay = new XYDefault();
	
	private double getStep() {
		return curDisplay.getStep();
	}
	
	//protected static double graphing_step = 1;
	//protected static double function_graphing_step = 4;
	//protected static double function_graphing_step_curve = 5;
	protected static double x_axis_delta = 0.0;
	protected static double y_axis_delta = 0.0;
	protected static double zoom = 1.0;
	final static int MATH_ERROR = -9999;
	final static int MATH_UNKNOWN = - 9998;
	
	protected static boolean functionLookingMode = false;
	double lookx = 0.0;
	double looky = 0.0;
	
	//int fingerPointerX = -1;
	//int fingerPointerY = -1;
	//int fingerPointerFunctionId = -1;
	//boolean fingerPointerState = false;
	
	private float additional_work_area_h = 0;
 
	Vector<Function> functions = new Vector<Function>();
	
	public Vector<Function> getFunctions() {
		return functions;
	}
	
	public void setFunctions(Vector<Function> arg) {
		functions = arg;
	}
	
	public void addFunction(Function funct) {
		functions.add(funct);
	}
	
	public static void outtextxy(double x, double y, String str, Canvas canvas, Paint paint) {
		double nx = curDisplay.getX(x, y),
		ny = curDisplay.getY(x, y);
		x=nx;
		y=ny;
		canvas.drawText(str, (float)x, (float)y, paint);
	}
	
	public static void point(double x, double y, Canvas canvas, Paint paint) {
		double nx = curDisplay.getX(x, y),
		ny = curDisplay.getY(x, y);
		x=nx;
		y=ny;
		canvas.drawPoint((float)x, (float)y, paint);
	}
	
	public static void line(double x1, double y1, double x2, double y2, Canvas canvas, final Paint paint) {
		double nx1 = curDisplay.getX(x1, y1),
		ny1 = curDisplay.getY(x1, y1),
		nx2 = curDisplay.getX(x2, y2),
		ny2 = curDisplay.getY(x2, y2);
		x1=nx1;
		x2=nx2;
		y1=ny1;
		y2=ny2;
		final float strkw = paint.getStrokeWidth();
		paint.setStrokeWidth(1);
		Paint shadowp = new Paint(paint) {{
			setStyle(Paint.Style.STROKE);
			setAntiAlias(true);
			setStrokeWidth(1.0f);
			setStrokeCap(Cap.ROUND);
			setColor(darkerColour(paint.getColor(), 0.65));
			
		}};
		
		canvas.drawLine((float)x1, (float)y1, (float)x2, (float)y2, shadowp);
		canvas.drawLine((float)x1+1, (float)y1+1, (float)x2+1, (float)y2+1, paint);
		paint.setStrokeWidth(strkw);
	}
	
	public static int darkerColour(int col, double op) {
		int r=Color.red(col), g=Color.green(col), b=Color.blue(col);
		r*=op;
		g*=op;
		b*=op;
		return Color.rgb(r, g, b);
	}
	
	public static void functline(double x1, double y1, double x2, double y2, Canvas canvas, final Paint paint) {
		double nx1 = curDisplay.getX(x1, y1),
		ny1 = curDisplay.getY(x1, y1),
		nx2 = curDisplay.getX(x2, y2),
		ny2 = curDisplay.getY(x2, y2);
		x1=nx1;
		x2=nx2;
		y1=ny1;
		y2=ny2;
		
		Paint linep = new Paint(paint) {{
			setStyle(Paint.Style.STROKE);
			setAntiAlias(true);
			setStrokeWidth(1.5f);
			setColor(paint.getColor());
		}};
		
		Paint borderp = new Paint(paint) {{
			setStyle(Paint.Style.STROKE);
			setAntiAlias(true);
			setStrokeWidth(3.0f);
			setStrokeCap(Cap.ROUND);
			setColor(darkerColour(paint.getColor(), 0.65));
		}};
		
		canvas.drawLine((float)x1, (float)y1, (float)x2, (float)y2, borderp);
		canvas.drawLine((float)x1, (float)y1, (float)x2, (float)y2, linep);
		
	}

	public void setAdditionalWorkAreaHeight(float arg) {
		additional_work_area_h = arg;
	}

	public void setLastPos(int x, int y){
		last_dragx = x;
		last_dragy = y;
	}
	
	private double divBy(double arg1, double arg2) {
		return (arg1-(arg1%(arg2/5)));
	}
	
	public void mouseDragged(TouchEvent pSceneTouchEvent, float mouseSensivity) {
		
		/*final double precA = 20;
		final double precB = 40;
		double prec = precA;
		
		if(fingerPointerState) {			
			prec = precB;
		}
		
		final int len = functions.size();
		int functionPressed = -1;
		double functval = -1;
		for(int it=0;it<len;++it) {
			//1/zoom*-funct.eval(zoom*x+x_axis_delta);
			functval = functions.get(it).eval(zoom*(pSceneTouchEvent.getX())+x_axis_delta);
			fingerPointerY = (int) (1/zoom*(-functval)+sh/2+y_axis_delta);
			if(DoubleComparator.equals(functval, sh/2-pSceneTouchEvent.getY(), prec)) {
				System.out.println("Function {"+functions.get(it).eqaution+"} pressed! :D");
				functionPressed = it;
			}
		}
		
		if(functionPressed!=-1) {
			fingerPointerState = true;
			fingerPointerX = (int) (pSceneTouchEvent.getX()/2);
			fingerPointerY = (int) (1/zoom*-functval+sh/2+y_axis_delta);
			fingerPointerFunctionId = functionPressed;
		} else {
			fingerPointerState = false;
		}
		*/
		
		if(this.functionLookingMode) {
			
			lookx = (double)pSceneTouchEvent.getX();
			looky = (double)pSceneTouchEvent.getY();
			
		} else {
			if(last_dragx == -9999) {
				last_dragx = (int)pSceneTouchEvent.getX();
			}
			if(last_dragy == -9999) {
				last_dragy = (int)pSceneTouchEvent.getY();
			}
			
			int dragx = (int)pSceneTouchEvent.getX();
			int dragy = (int)pSceneTouchEvent.getY();
			boolean redraw = false;
			
			if(last_dragx != -9999) {
				x_axis_delta += (last_dragx - dragx)*zoom;//*mouseSensivity;
				//x_axis_delta = divBy( x_axis_delta, zoom*2.0f );
				redraw = true;
			}
			if(last_dragy != -9999) {
				y_axis_delta -= (last_dragy - dragy)*zoom;//*mouseSensivity;
				//y_axis_delta = divBy( y_axis_delta, zoom*2.0f );
				redraw = true;
			}
			if(redraw) {
				//this.repaint();
			}
			
			last_dragx = dragx;
			last_dragy = dragy;
		}
	}
	
	public void drawLookingMode(Canvas canvas, Paint paint) {
		
		if(!this.functionLookingMode) {
			return;
		}

		final int len = Core.functions.size();
		for(int it=0;it<len;++it) {
			if(Core.functions.get(it).object_type == Function.ENTRY_TYPE_VARIABLE) {
				
			} else if(Core.functions.get(it).object_type == Function.ENTRY_TYPE_LUA_DRAWER) {
				// give x to lua ===> zoom*(lookx-sw/2)+x_axis_delta
				// give y to lua ===> 1/zoom*-(looky-sh/2)+y_axis_delta
				
				final Function funct = Core.functions.get(it);
				paint.setTextSize(13);

				final String text = funct.luaOnFunctionLooking(zoom*(lookx-sw/2)+x_axis_delta, zoom*-((looky-sh/2)-y_axis_delta));
				if(!text.equals("")) {
					final double textw = paint.measureText(text), texth = 14;
					paint.setColor(android.graphics.Color.WHITE);
					canvas.drawRect((float)(lookx-10.0), (float)(looky+15.0), (float)(lookx-5.0+textw), (float)(looky-10.0-texth), paint);
					
					paint.setColor(android.graphics.Color.BLACK);
					canvas.drawText(text, (float)(lookx-5.0), (float)(looky+15.0), paint);
					
					paint.setColor(funct.color);
					canvas.drawCircle((float)(lookx), (float)(looky), 5.0f, paint);
					
					return;
				}
				
			} else if(Core.functions.get(it).object_type != Function.ENTRY_TYPE_VARIABLE) {
				if(DoubleComparator.equals(sh/2+y_axis_delta+1/zoom*-Core.functions.get(it).eval(zoom*(lookx-sw/2)+x_axis_delta), looky, 50)) {
					
					final Function funct = Core.functions.get(it);
					double y = 1/zoom*-funct.eval(zoom*(lookx-sw/2)+x_axis_delta);
					
					final double dispy = y;
					final double dispx = zoom*(lookx-sw/2)+x_axis_delta;
					paint.setTextSize(13);

					final String text = "("+((Object)dispx).toString()+"; "+((Object)dispy).toString()+")";
					
					final double textw = paint.measureText(text), texth = 14;
					paint.setColor(android.graphics.Color.WHITE);
					canvas.drawRect((float)(lookx-10.0), (float)(looky+15.0), (float)(lookx-5.0+textw), (float)(looky-10.0-texth), paint);
					
					paint.setColor(android.graphics.Color.BLACK);
					canvas.drawText(text, (float)(lookx-5.0), (float)(looky+15.0), paint);
					
					paint.setColor(funct.color);
					canvas.drawCircle((float)(lookx), (float)(sh/2+dispy), 5.0f, paint);
					
					return;
				}
			}
		}
		
	}
	
	public void drawAllFunctions(Canvas canvas, Paint paint) {
		curDisplay.init();
		final int len = functions.size();
		for(int it=0;it<len;++it) {
			final Function cfunct = functions.get(it);
			if(cfunct.object_type==Function.ENTRY_TYPE_CURVE_OBJECT) {
				drawCurveFunction(cfunct, canvas, paint);
			} else if(cfunct.object_type==Function.ENTRY_TYPE_LUA_DRAWER) {
				LuaFunctionPlugin.canvas = canvas;
				LuaFunctionPlugin.paint = paint;
				cfunct.drawLua();
				canvas = LuaFunctionPlugin.canvas;
				paint = LuaFunctionPlugin.paint;
			} else {
				if(!cfunct.isDisabled&&(cfunct.object_type!=Function.ENTRY_TYPE_VARIABLE)) {
					drawFunction(cfunct, canvas, paint);
				}
			}
			drawLookingMode(canvas, paint);
		}
		/*if(fingerPointerState) {
			paint.setColor(functions.get(fingerPointerFunctionId).color);
			canvas.drawCircle(fingerPointerX, fingerPointerY, 15, paint);
		}*/
	}
	
	void drawCurveFunction(Function funct, Canvas canvas, Paint paint) {
		double step = getStep(); //function_graphing_step_curve;
		paint.setColor(funct.color);
		double lastsgn = 0, cursgn = 0;
		for(double x=-sw/2.0;x<=sw/2.0;x+=step) {
			lastsgn = 0.0;
			cursgn = 0.0;
			for(double y=-sh/2.0;y<=sh/2.0;y+=step) {
				cursgn = Math.signum(funct.probePoint(zoom*x+x_axis_delta, -(y-sh/2-y_axis_delta)*zoom));
				//System.out.print("TEST FOR ARG ("+((Object)(zoom*x+x_axis_delta)).toString()+", "+((Object)(-(y-sh/2-y_axis_delta)*zoom)).toString()+") result is = "+((Object)cursgn).toString());
				if(DoubleComparator.equals(cursgn, 0) || (lastsgn!=cursgn && y!=-sh/2)) {
					//System.out.print(" = GOOD");
					functline(x-step/10+sw/2.0+x_axis_delta, y-step/10+y_axis_delta, x+step/10+sw/2.0+x_axis_delta, y+step/10+y_axis_delta, canvas, paint);
				} else {
					//System.out.print(" = BAD");
				}
				//System.out.print("\n");
				lastsgn = cursgn;
			}
		}
		paint.setColor(android.graphics.Color.BLACK);
	}
	
	void drawFunction(Function funct, Canvas canvas, Paint paint) {
		
		double lasty = 0.0;
		double y = 0.0;
		double step = getStep(); //function_graphing_step;
		paint.setColor(funct.color);
		for(double x=0.0;x<sw/2;x+=step) {
			lasty = y;
			if(funct.eval(zoom*x+x_axis_delta) == MATH_ERROR) {

			} else {
				y = 1/zoom*-funct.eval(zoom*x+x_axis_delta);
				if(sh/2+y+y_axis_delta<-additional_work_area_h) {
					y = MATH_UNKNOWN;
				} else if(sh/2+y+y_axis_delta>sh+additional_work_area_h) {
					y = MATH_UNKNOWN;
				} else {
					if(y!=MATH_UNKNOWN&&lasty!=MATH_UNKNOWN&&x!=0.0) functline(sw/2+x-step, sh/2+lasty+y_axis_delta, sw/2+x, sh/2+y+y_axis_delta, canvas, paint);
				}
			}
		}
		//1/zoom*-funct(...)
		//sh/2+lasty+y_axis_delta
		
		y = 0.0;
		lasty = 0.0;
		for(double x2=0.0;x2>-sw/2;x2-=step) {
			lasty = y;
			if(funct.eval(zoom*x2+x_axis_delta) == MATH_ERROR) {

			} else {
				y = 1/zoom*-funct.eval(zoom*x2+x_axis_delta);
				if(sh/2+y+y_axis_delta<-additional_work_area_h) {
					y = MATH_UNKNOWN;
				} else if(sh/2+y+y_axis_delta>sh+additional_work_area_h) {
					y = MATH_UNKNOWN;
				} else {
					if(y!=MATH_UNKNOWN&&lasty!=MATH_UNKNOWN&&x2!=0.0) functline(sw/2+x2+step, sh/2+lasty+y_axis_delta, sw/2+x2, sh/2+y+y_axis_delta, canvas, paint);
				}
			}
		}
		paint.setColor(android.graphics.Color.BLACK);
	}
	
	protected static String valToStr(double value) {
		boolean inverted = false;
		if(value<0.0) {
			inverted = true;
			value = -value;
		}
		
		if(value<1.0) {
			final double finalValue = Math.round( value * 1000.0 ) / 1000.0;
			if(inverted) {
				return new String("-").concat(((Object)finalValue).toString());
			}
			return ((Object)finalValue).toString();
		} else if(value>=1.0&&value<100) {
			final double finalValue = ((int)Math.round(value));
			if(inverted) {
				return new String("-").concat(((Object)finalValue).toString());
			}
			return ((Object)finalValue).toString();
		} else if(value>=100&&value<1000) {
			final double finalValue = ((int)Math.round(value))/100;
			if(inverted) {
				return new String("-").concat(((Object)finalValue).toString().concat("H"));
			}
			return ((Object)finalValue).toString().concat("H");
		} else if(value>=1000&&value<100000) {
			final double finalValue = ((int)Math.round(value))/1000;
			if(inverted) {
				return new String("-").concat(((Object)finalValue).toString().concat("E"));
			}
			return ((Object)finalValue).toString().concat("E");
		} else if(value>=100000&&value<1000000) {
			final double finalValue = ((int)Math.round(value))/100000;
			if(inverted) {
				return new String("-").concat(((Object)finalValue).toString().concat("HE"));
			}
			return ((Object)finalValue).toString().concat("HE");
		} else if(value>=1000000&&value<1000000000) {
			final double finalValue = ((int)Math.round(value))/1000000;
			if(inverted) {
				return new String("-").concat(((Object)finalValue).toString().concat("M"));
			}
			return ((Object)finalValue).toString().concat("M");
		} else if(value>=1000000000) {
			final double finalValue = ((int)Math.round(value))/1000000000;
			if(inverted) {
				return new String("-").concat(((Object)finalValue).toString().concat("B"));
			}
			return ((Object)finalValue).toString().concat("B");
		}
		final double finalValue = Math.round( value * 1000.0 ) / 1000.0;
		return ((Object)finalValue).toString();
	}
	
	void drawXYAxis(Canvas canvas, Paint paint) {
		outtextxy(5,15,"zoom [%] ~= ", canvas, paint);
		String buffer = valToStr(((int)(1/zoom*100)));
		outtextxy(95,15,buffer, canvas, paint);

		line(0, sh/2, sw, sh/2, canvas, paint);
		line(sw/2, 0, sw/2, sh, canvas, paint);

		paint.setColor(android.graphics.Color.RED);
		line(0, sh/2+y_axis_delta, sw, sh/2+y_axis_delta, canvas, paint);
		line(sw/2-x_axis_delta, 0, sw/2-x_axis_delta, sh, canvas, paint);
		paint.setColor(android.graphics.Color.BLACK);

		double step = sw/(XAxisLabels+2.0);
		for(double itx=step;itx<sw/2;itx+=step) {
			paint.setColor(android.graphics.Color.GRAY);
			line(itx+sw/2, 0, itx+sw/2, sh, canvas, paint);
			paint.setColor(android.graphics.Color.BLACK);
			
			line(itx+sw/2, sh/2-5, itx+sw/2, sh/2+5, canvas, paint);
			buffer = valToStr((zoom*(itx+x_axis_delta)));
			outtextxy(itx+sw/2-5, sh/2+15, buffer, canvas, paint);
		}
		for(double itx2=-step;itx2>-sw/2;itx2-=step) {
			paint.setColor(android.graphics.Color.GRAY);
			line(itx2+sw/2, 0, itx2+sw/2, sh, canvas, paint);
			paint.setColor(android.graphics.Color.BLACK);
			
			line(itx2+sw/2, sh/2-5, itx2+sw/2, sh/2+5, canvas, paint);
			buffer = valToStr((zoom*(itx2+x_axis_delta)));
			outtextxy(itx2+sw/2-5, sh/2+15, buffer, canvas, paint);
		}
		
		step = sh/(YAxisLabels+2.0); 
		for(double ity=0.0;ity<sh/2;ity+=step) {
			paint.setColor(android.graphics.Color.GRAY);
			line(0, sh/2+ity, sw, sh/2+ity, canvas, paint);
			paint.setColor(android.graphics.Color.BLACK);
			
			line(sw/2-5, sh/2+ity, sw/2+5, sh/2+ity, canvas, paint);
			buffer = valToStr((zoom*(-ity+y_axis_delta)));
			outtextxy(sw/2+15, sh/2+ity, buffer, canvas, paint);
		}
		for(double ity2=0.0;ity2>=-sh/2;ity2-=step) {
			paint.setColor(android.graphics.Color.GRAY);
			line(0, sh/2+ity2, sw, sh/2+ity2, canvas, paint);
			paint.setColor(android.graphics.Color.BLACK);
			
			line(sw/2-5, sh/2+ity2, sw/2+5, sh/2+ity2, canvas, paint);
			buffer = valToStr((zoom*(-ity2+y_axis_delta)));
			outtextxy(sw/2+15, sh/2+ity2, buffer, canvas, paint);
		}
	}
	
	boolean draw_ttinfo = true;
	
	private double zoomCheck(double zoomf) {
		System.out.println("PREV ZOOM VALUE = "+((Object)zoomf).toString());
		boolean inverse = false;
		if(zoomf>1.0) {
			zoomf=1.0/zoomf;
			inverse = true;
		}
		
		boolean cont = true;
		double floorVal = 0;
		double divider = 1.0;
		while(cont) {
			zoomf*=10.0;
			divider*=0.1;
			if(Math.floor(zoomf)>0) {
				floorVal = (int)Math.floor(zoomf);
				cont=false;
			}
		}
		if(inverse) {
			System.out.println("FINAL ZOOM VALUE = "+((Object)(1.0/(divider*floorVal))).toString());
			return 1.0/(divider*floorVal);
		}
		System.out.println("FINAL ZOOM VALUE = "+((Object)(divider*floorVal)).toString());
		return divider*floorVal;
	}
	
	public void zoomed(float zoomf, float mouseSensivity) {
		/*int wheelx = arg0.getWheelRotation();
		if(last_wheelx != -9999) {
			if(wheelx < 0) {
				zoom *= 1.1;
			} else {
				zoom /= 1.1;
			}
			//System.out.println("*= "+((Object)(wheelx)).toString());
		}
		last_wheelx = wheelx;*/
		System.out.println("@# Zoomed by: "+((Object)(zoomf*mouseSensivity)).toString());
		System.out.println("@# Prev zoom: "+((Object)(zoom)).toString());
		System.out.println("@# Next zoom: "+((Object)(zoom*zoomf*mouseSensivity)).toString());

		
		if(zoomf<1.0f) {
			zoom *= (zoomf/mouseSensivity);
		} else {
			zoom *= (zoomf*mouseSensivity);
		}
		zoom = zoomCheck(zoom);
		//zoom *= zoomf;
	}
	
	
    public void paintComponent(Canvas canvas, Paint paint, int w, int h) {
    	    	
    	paint.setColor(android.graphics.Color.BLACK);
    	this.sw = w;
    	this.sh = h;
        //super.paintComponent(g);
        
        //sw = this.getWidth();
        //sh = this.getHeight();
        
        //Graphics2D g2d = (Graphics2D) g;
    	
    	//canvas.drawText("DRAWN!", (int)(Math.random()*350), (int)(Math.random()*350), paint);
    	//if(true) return;
 
        drawXYAxis(canvas, paint);
        drawAllFunctions(canvas, paint);
        
        /*if(!tt_str.equals("") && draw_ttinfo) {
        	//g2d.drawOval(tt_x-tt_xbck, tt_y, 5, 5);
        	Ellipse2D.Double circle = new Ellipse2D.Double(tt_x-3.5, tt_y-3.5, 7, 7);
        	g2d.fill(circle);
        	g2d.drawString(tt_str, (int)(tt_x-2*tt_xbck), (int)(tt_y+2*tt_xbck));
        }*/
    }

    int last_dragx = -9999;
    int last_dragy = -9999;
    int last_wheelx = -9999;
    
    double tt_x = -9999;
    double tt_y = -9999;
    int tt_xbck = -9999;
    String tt_str = "";
   
}
