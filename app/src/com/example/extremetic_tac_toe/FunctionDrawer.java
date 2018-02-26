package com.example.extremetic_tac_toe;

import java.util.Vector;

import org.andengine.input.touch.TouchEvent;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;

public class FunctionDrawer {

    protected static XYDisplay curDisplay = new XYDefault();

    protected static boolean functionLookingMode = false;
    protected static double sh = 0.0;
    protected static double stepsNumber = 50;
    protected static double sw = 0.0;
    protected static boolean swapXYaxis = false;
    protected static boolean useAutoStep = false;
    // protected static double graphing_step = 1;
    // protected static double function_graphing_step = 4;
    // protected static double function_graphing_step_curve = 5;
    protected static double x_axis_delta = 0.0;

    protected static double XAxisLabels = 7;

    protected static double y_axis_delta = 0.0;

    protected static double YAxisLabels = 7;

    protected static double zoom = 1.0;

    final static int MATH_ERROR = -9999;

    final static int MATH_UNKNOWN = -9998;

    private float additional_work_area_h = 0;

    boolean draw_ttinfo = true;

    Vector<Function> functions = new Vector<Function>();
    int last_dragx = -9999;
    int last_dragy = -9999;
    int last_wheelx = -9999;
    double lookx = 0.0;

    double looky = 0.0;
    String tt_str = "";
    double tt_x = -9999;

    // int fingerPointerX = -1;
    // int fingerPointerY = -1;
    // int fingerPointerFunctionId = -1;
    // boolean fingerPointerState = false;

    int tt_xbck = -9999;

    double tt_y = -9999;

    public FunctionDrawer() {

    }

    protected static class XYDefault implements XYDisplay {
	@Override
	public String getName() {
	    return "default";
	}

	@Override
	public double getStep() {
	    if (FunctionDrawer.useAutoStep) {
		return FunctionDrawer.sw / 50;
	    }
	    return FunctionDrawer.sw / FunctionDrawer.stepsNumber;
	}

	@Override
	public double getX(final double x, final double y) {
	    return x;
	}

	@Override
	public double getY(final double x, final double y) {
	    return y;
	}

	@Override
	public void init() {

	}
    }

    protected static interface XYDisplay {
	public String getName();

	public double getStep();

	public double getX(double x, double y);

	public double getY(double x, double y);

	public void init();
    }

    protected static class XYNormalNotProportional implements XYDisplay {
	public double scaleX = 3.0;
	public double scaleY = 7.0;
	public boolean swapXY = false;

	@Override
	public String getName() {
	    return "normalnprop";
	}

	@Override
	public double getStep() {
	    if (FunctionDrawer.useAutoStep) {
		return FunctionDrawer.sw / 50;
	    }
	    return FunctionDrawer.sw / FunctionDrawer.stepsNumber;
	}

	@Override
	public double getX(final double x, final double y) {
	    if (this.swapXY) {
		return (this.scaleY * (y - FunctionDrawer.sh / 2) + FunctionDrawer.sh / 2) / FunctionDrawer.sh
			* FunctionDrawer.sw;
	    }
	    return this.scaleX * (x - FunctionDrawer.sw / 2) + FunctionDrawer.sw / 2;
	}

	@Override
	public double getY(final double x, final double y) {
	    if (this.swapXY) {
		return (this.scaleX * (x - FunctionDrawer.sw / 2) + FunctionDrawer.sw / 2) / FunctionDrawer.sw
			* FunctionDrawer.sh;
	    }
	    return this.scaleY * (y - FunctionDrawer.sh / 2) + FunctionDrawer.sh / 2;
	}

	@Override
	public void init() {

	}
    }

    protected static class XYRadial implements XYDisplay {
	@Override
	public String getName() {
	    return "radial";
	}

	@Override
	public double getStep() {
	    if (FunctionDrawer.useAutoStep) {
		return FunctionDrawer.sw / 50;
	    }
	    return FunctionDrawer.sw / FunctionDrawer.stepsNumber;
	}

	@Override
	public double getX(final double x, final double y) {
	    return y / FunctionDrawer.sh * Math.cos(Math.toRadians(x * 360 / FunctionDrawer.sw)) * FunctionDrawer.sw
		    + FunctionDrawer.sw / 2;
	}

	@Override
	public double getY(final double x, final double y) {
	    return y / FunctionDrawer.sh * Math.sin(Math.toRadians(x * 360 / FunctionDrawer.sw)) * FunctionDrawer.sh
		    + FunctionDrawer.sh / 2;
	}

	@Override
	public void init() {

	}
    }

    protected static class XYSwap implements XYDisplay {
	@Override
	public String getName() {
	    return "swap";
	}

	@Override
	public double getStep() {
	    if (FunctionDrawer.useAutoStep) {
		return FunctionDrawer.sw / 50;
	    }
	    return FunctionDrawer.sw / FunctionDrawer.stepsNumber;
	}

	@Override
	public double getX(final double x, final double y) {
	    return y / FunctionDrawer.sh * FunctionDrawer.sw;
	}

	@Override
	public double getY(final double x, final double y) {
	    return x / FunctionDrawer.sw * FunctionDrawer.sh;
	}

	@Override
	public void init() {

	}
    }

    public static int darkerColour(final int col, final double op) {
	int r = Color.red(col), g = Color.green(col), b = Color.blue(col);
	r *= op;
	g *= op;
	b *= op;
	return Color.rgb(r, g, b);
    }

    public static void functline(double x1, double y1, double x2, double y2, final Canvas canvas, final Paint paint) {
	final double nx1 = FunctionDrawer.curDisplay.getX(x1, y1), ny1 = FunctionDrawer.curDisplay.getY(x1, y1),
		nx2 = FunctionDrawer.curDisplay.getX(x2, y2), ny2 = FunctionDrawer.curDisplay.getY(x2, y2);
	x1 = nx1;
	x2 = nx2;
	y1 = ny1;
	y2 = ny2;

	final Paint linep = new Paint(paint) {
	    {
		this.setStyle(Paint.Style.STROKE);
		this.setAntiAlias(true);
		this.setStrokeWidth(1.5f);
		this.setColor(paint.getColor());
	    }
	};

	final Paint borderp = new Paint(paint) {
	    {
		this.setStyle(Paint.Style.STROKE);
		this.setAntiAlias(true);
		this.setStrokeWidth(3.0f);
		this.setStrokeCap(Cap.ROUND);
		this.setColor(FunctionDrawer.darkerColour(paint.getColor(), 0.65));
	    }
	};

	canvas.drawLine((float) x1, (float) y1, (float) x2, (float) y2, borderp);
	canvas.drawLine((float) x1, (float) y1, (float) x2, (float) y2, linep);

    }

    public static void line(double x1, double y1, double x2, double y2, final Canvas canvas, final Paint paint) {
	final double nx1 = FunctionDrawer.curDisplay.getX(x1, y1), ny1 = FunctionDrawer.curDisplay.getY(x1, y1),
		nx2 = FunctionDrawer.curDisplay.getX(x2, y2), ny2 = FunctionDrawer.curDisplay.getY(x2, y2);
	x1 = nx1;
	x2 = nx2;
	y1 = ny1;
	y2 = ny2;
	final float strkw = paint.getStrokeWidth();
	paint.setStrokeWidth(1);
	final Paint shadowp = new Paint(paint) {
	    {
		this.setStyle(Paint.Style.STROKE);
		this.setAntiAlias(true);
		this.setStrokeWidth(1.0f);
		this.setStrokeCap(Cap.ROUND);
		this.setColor(FunctionDrawer.darkerColour(paint.getColor(), 0.65));

	    }
	};

	canvas.drawLine((float) x1, (float) y1, (float) x2, (float) y2, shadowp);
	canvas.drawLine((float) x1 + 1, (float) y1 + 1, (float) x2 + 1, (float) y2 + 1, paint);
	paint.setStrokeWidth(strkw);
    }

    public static void outtextxy(double x, double y, final String str, final Canvas canvas, final Paint paint) {
	final double nx = FunctionDrawer.curDisplay.getX(x, y), ny = FunctionDrawer.curDisplay.getY(x, y);
	x = nx;
	y = ny;
	canvas.drawText(str, (float) x, (float) y, paint);
    }

    public static void point(double x, double y, final Canvas canvas, final Paint paint) {
	final double nx = FunctionDrawer.curDisplay.getX(x, y), ny = FunctionDrawer.curDisplay.getY(x, y);
	x = nx;
	y = ny;
	canvas.drawPoint((float) x, (float) y, paint);
    }

    protected static XYDisplay makeXYDisplay(final String name) {
	if (name.equals("default")) {
	    return new XYDefault();
	} else if (name.equals("swap")) {
	    return new XYSwap();
	} else if (name.equals("radial")) {
	    return new XYRadial();
	} else if (name.equals("normalnprop")) {
	    return new XYNormalNotProportional();
	}
	return new XYDefault();
    }

    protected static String valToStr(double value) {
	boolean inverted = false;
	if (value < 0.0) {
	    inverted = true;
	    value = -value;
	}

	if (value < 1.0) {
	    final double finalValue = Math.round(value * 1000.0) / 1000.0;
	    if (inverted) {
		return new String("-").concat(((Object) finalValue).toString());
	    }
	    return ((Object) finalValue).toString();
	} else if (value >= 1.0 && value < 100) {
	    final double finalValue = (int) Math.round(value);
	    if (inverted) {
		return new String("-").concat(((Object) finalValue).toString());
	    }
	    return ((Object) finalValue).toString();
	} else if (value >= 100 && value < 1000) {
	    final double finalValue = (int) Math.round(value) / 100;
	    if (inverted) {
		return new String("-").concat(((Object) finalValue).toString().concat("H"));
	    }
	    return ((Object) finalValue).toString().concat("H");
	} else if (value >= 1000 && value < 100000) {
	    final double finalValue = (int) Math.round(value) / 1000;
	    if (inverted) {
		return new String("-").concat(((Object) finalValue).toString().concat("E"));
	    }
	    return ((Object) finalValue).toString().concat("E");
	} else if (value >= 100000 && value < 1000000) {
	    final double finalValue = (int) Math.round(value) / 100000;
	    if (inverted) {
		return new String("-").concat(((Object) finalValue).toString().concat("HE"));
	    }
	    return ((Object) finalValue).toString().concat("HE");
	} else if (value >= 1000000 && value < 1000000000) {
	    final double finalValue = (int) Math.round(value) / 1000000;
	    if (inverted) {
		return new String("-").concat(((Object) finalValue).toString().concat("M"));
	    }
	    return ((Object) finalValue).toString().concat("M");
	} else if (value >= 1000000000) {
	    final double finalValue = (int) Math.round(value) / 1000000000;
	    if (inverted) {
		return new String("-").concat(((Object) finalValue).toString().concat("B"));
	    }
	    return ((Object) finalValue).toString().concat("B");
	}
	final double finalValue = Math.round(value * 1000.0) / 1000.0;
	return ((Object) finalValue).toString();
    }

    public void addFunction(final Function funct) {
	this.functions.add(funct);
    }

    public void drawAllFunctions(Canvas canvas, Paint paint) {
	FunctionDrawer.curDisplay.init();
	final int len = this.functions.size();
	for (int it = 0; it < len; ++it) {
	    final Function cfunct = this.functions.get(it);
	    if (cfunct.object_type == Function.ENTRY_TYPE_CURVE_OBJECT) {
		this.drawCurveFunction(cfunct, canvas, paint);
	    } else if (cfunct.object_type == Function.ENTRY_TYPE_LUA_DRAWER) {
		LuaFunctionPlugin.canvas = canvas;
		LuaFunctionPlugin.paint = paint;
		cfunct.drawLua();
		canvas = LuaFunctionPlugin.canvas;
		paint = LuaFunctionPlugin.paint;
	    } else {
		if (!cfunct.isDisabled && cfunct.object_type != Function.ENTRY_TYPE_VARIABLE) {
		    this.drawFunction(cfunct, canvas, paint);
		}
	    }
	    this.drawLookingMode(canvas, paint);
	}
	/*
	 * if(fingerPointerState) {
	 * paint.setColor(functions.get(fingerPointerFunctionId).color);
	 * canvas.drawCircle(fingerPointerX, fingerPointerY, 15, paint); }
	 */
    }

    public void drawLookingMode(final Canvas canvas, final Paint paint) {

	if (!FunctionDrawer.functionLookingMode) {
	    return;
	}

	final int len = Core.functions.size();
	for (int it = 0; it < len; ++it) {
	    if (Core.functions.get(it).object_type == Function.ENTRY_TYPE_VARIABLE) {

	    } else if (Core.functions.get(it).object_type == Function.ENTRY_TYPE_LUA_DRAWER) {
		// give x to lua ===> zoom*(lookx-sw/2)+x_axis_delta
		// give y to lua ===> 1/zoom*-(looky-sh/2)+y_axis_delta

		final Function funct = Core.functions.get(it);
		paint.setTextSize(13);

		final String text = funct.luaOnFunctionLooking(
			FunctionDrawer.zoom * (this.lookx - FunctionDrawer.sw / 2) + FunctionDrawer.x_axis_delta,
			FunctionDrawer.zoom * -(this.looky - FunctionDrawer.sh / 2 - FunctionDrawer.y_axis_delta));
		if (!text.equals("")) {
		    final double textw = paint.measureText(text), texth = 14;
		    paint.setColor(android.graphics.Color.WHITE);
		    canvas.drawRect((float) (this.lookx - 10.0), (float) (this.looky + 15.0),
			    (float) (this.lookx - 5.0 + textw), (float) (this.looky - 10.0 - texth), paint);

		    paint.setColor(android.graphics.Color.BLACK);
		    canvas.drawText(text, (float) (this.lookx - 5.0), (float) (this.looky + 15.0), paint);

		    paint.setColor(funct.color);
		    canvas.drawCircle((float) this.lookx, (float) this.looky, 5.0f, paint);

		    return;
		}

	    } else if (Core.functions.get(it).object_type != Function.ENTRY_TYPE_VARIABLE) {
		if (DoubleComparator.equals(FunctionDrawer.sh / 2 + FunctionDrawer.y_axis_delta
			+ 1 / FunctionDrawer.zoom
				* -Core.functions.get(it)
					.eval(FunctionDrawer.zoom * (this.lookx - FunctionDrawer.sw / 2)
						+ FunctionDrawer.x_axis_delta),
			this.looky, 50)) {

		    final Function funct = Core.functions.get(it);
		    final double y = 1 / FunctionDrawer.zoom * -funct.eval(
			    FunctionDrawer.zoom * (this.lookx - FunctionDrawer.sw / 2) + FunctionDrawer.x_axis_delta);

		    final double dispy = y;
		    final double dispx = FunctionDrawer.zoom * (this.lookx - FunctionDrawer.sw / 2)
			    + FunctionDrawer.x_axis_delta;
		    paint.setTextSize(13);

		    final String text = "(" + ((Object) dispx).toString() + "; " + ((Object) dispy).toString() + ")";

		    final double textw = paint.measureText(text), texth = 14;
		    paint.setColor(android.graphics.Color.WHITE);
		    canvas.drawRect((float) (this.lookx - 10.0), (float) (this.looky + 15.0),
			    (float) (this.lookx - 5.0 + textw), (float) (this.looky - 10.0 - texth), paint);

		    paint.setColor(android.graphics.Color.BLACK);
		    canvas.drawText(text, (float) (this.lookx - 5.0), (float) (this.looky + 15.0), paint);

		    paint.setColor(funct.color);
		    canvas.drawCircle((float) this.lookx, (float) (FunctionDrawer.sh / 2 + dispy), 5.0f, paint);

		    return;
		}
	    }
	}

    }

    public Vector<Function> getFunctions() {
	return this.functions;
    }

    public void mouseDragged(final TouchEvent pSceneTouchEvent, final float mouseSensivity) {

	/*
	 * final double precA = 20; final double precB = 40; double prec =
	 * precA;
	 * 
	 * if(fingerPointerState) { prec = precB; }
	 * 
	 * final int len = functions.size(); int functionPressed = -1; double
	 * functval = -1; for(int it=0;it<len;++it) {
	 * //1/zoom*-funct.eval(zoom*x+x_axis_delta); functval =
	 * functions.get(it).eval(zoom*(pSceneTouchEvent.getX())+x_axis_delta);
	 * fingerPointerY = (int) (1/zoom*(-functval)+sh/2+y_axis_delta);
	 * if(DoubleComparator.equals(functval, sh/2-pSceneTouchEvent.getY(),
	 * prec)) { System.out.println("Function {"+functions.get(it).eqaution+
	 * "} pressed! :D"); functionPressed = it; } }
	 * 
	 * if(functionPressed!=-1) { fingerPointerState = true; fingerPointerX =
	 * (int) (pSceneTouchEvent.getX()/2); fingerPointerY = (int)
	 * (1/zoom*-functval+sh/2+y_axis_delta); fingerPointerFunctionId =
	 * functionPressed; } else { fingerPointerState = false; }
	 */

	if (FunctionDrawer.functionLookingMode) {

	    this.lookx = pSceneTouchEvent.getX();
	    this.looky = pSceneTouchEvent.getY();

	} else {
	    if (this.last_dragx == -9999) {
		this.last_dragx = (int) pSceneTouchEvent.getX();
	    }
	    if (this.last_dragy == -9999) {
		this.last_dragy = (int) pSceneTouchEvent.getY();
	    }

	    final int dragx = (int) pSceneTouchEvent.getX();
	    final int dragy = (int) pSceneTouchEvent.getY();
	    boolean redraw = false;

	    if (this.last_dragx != -9999) {
		FunctionDrawer.x_axis_delta += (this.last_dragx - dragx) * FunctionDrawer.zoom;// *mouseSensivity;
		// x_axis_delta = divBy( x_axis_delta, zoom*2.0f );
		redraw = true;
	    }
	    if (this.last_dragy != -9999) {
		FunctionDrawer.y_axis_delta -= (this.last_dragy - dragy) * FunctionDrawer.zoom;// *mouseSensivity;
		// y_axis_delta = divBy( y_axis_delta, zoom*2.0f );
		redraw = true;
	    }
	    if (redraw) {
		// this.repaint();
	    }

	    this.last_dragx = dragx;
	    this.last_dragy = dragy;
	}
    }

    public void paintComponent(final Canvas canvas, final Paint paint, final int w, final int h) {

	paint.setColor(android.graphics.Color.BLACK);
	FunctionDrawer.sw = w;
	FunctionDrawer.sh = h;
	// super.paintComponent(g);

	// sw = this.getWidth();
	// sh = this.getHeight();

	// Graphics2D g2d = (Graphics2D) g;

	// canvas.drawText("DRAWN!", (int)(Math.random()*350),
	// (int)(Math.random()*350), paint);
	// if(true) return;

	this.drawXYAxis(canvas, paint);
	this.drawAllFunctions(canvas, paint);

	/*
	 * if(!tt_str.equals("") && draw_ttinfo) { //g2d.drawOval(tt_x-tt_xbck,
	 * tt_y, 5, 5); Ellipse2D.Double circle = new Ellipse2D.Double(tt_x-3.5,
	 * tt_y-3.5, 7, 7); g2d.fill(circle); g2d.drawString(tt_str,
	 * (int)(tt_x-2*tt_xbck), (int)(tt_y+2*tt_xbck)); }
	 */
    }

    public void setAdditionalWorkAreaHeight(final float arg) {
	this.additional_work_area_h = arg;
    }

    public void setFunctions(final Vector<Function> arg) {
	this.functions = arg;
    }

    public void setLastPos(final int x, final int y) {
	this.last_dragx = x;
	this.last_dragy = y;
    }

    public void zoomed(final float zoomf, final float mouseSensivity) {
	/*
	 * int wheelx = arg0.getWheelRotation(); if(last_wheelx != -9999) {
	 * if(wheelx < 0) { zoom *= 1.1; } else { zoom /= 1.1; }
	 * //System.out.println("*= "+((Object)(wheelx)).toString()); }
	 * last_wheelx = wheelx;
	 */
	System.out.println("@# Zoomed by: " + ((Object) (zoomf * mouseSensivity)).toString());
	System.out.println("@# Prev zoom: " + ((Object) FunctionDrawer.zoom).toString());
	System.out.println("@# Next zoom: " + ((Object) (FunctionDrawer.zoom * zoomf * mouseSensivity)).toString());

	if (zoomf < 1.0f) {
	    FunctionDrawer.zoom *= zoomf / mouseSensivity;
	} else {
	    FunctionDrawer.zoom *= zoomf * mouseSensivity;
	}
	FunctionDrawer.zoom = this.zoomCheck(FunctionDrawer.zoom);
	// zoom *= zoomf;
    }

    private double divBy(final double arg1, final double arg2) {
	return arg1 - arg1 % (arg2 / 5);
    }

    private double getStep() {
	return FunctionDrawer.curDisplay.getStep();
    }

    private double zoomCheck(double zoomf) {
	System.out.println("PREV ZOOM VALUE = " + ((Object) zoomf).toString());
	boolean inverse = false;
	if (zoomf > 1.0) {
	    zoomf = 1.0 / zoomf;
	    inverse = true;
	}

	boolean cont = true;
	double floorVal = 0;
	double divider = 1.0;
	while (cont) {
	    zoomf *= 10.0;
	    divider *= 0.1;
	    if (Math.floor(zoomf) > 0) {
		floorVal = (int) Math.floor(zoomf);
		cont = false;
	    }
	}
	if (inverse) {
	    System.out.println("FINAL ZOOM VALUE = " + ((Object) (1.0 / (divider * floorVal))).toString());
	    return 1.0 / (divider * floorVal);
	}
	System.out.println("FINAL ZOOM VALUE = " + ((Object) (divider * floorVal)).toString());
	return divider * floorVal;
    }

    void drawCurveFunction(final Function funct, final Canvas canvas, final Paint paint) {
	final double step = this.getStep(); // function_graphing_step_curve;
	paint.setColor(funct.color);
	double lastsgn = 0, cursgn = 0;
	for (double x = -FunctionDrawer.sw / 2.0; x <= FunctionDrawer.sw / 2.0; x += step) {
	    lastsgn = 0.0;
	    cursgn = 0.0;
	    for (double y = -FunctionDrawer.sh / 2.0; y <= FunctionDrawer.sh / 2.0; y += step) {
		cursgn = Math.signum(funct.probePoint(FunctionDrawer.zoom * x + FunctionDrawer.x_axis_delta,
			-(y - FunctionDrawer.sh / 2 - FunctionDrawer.y_axis_delta) * FunctionDrawer.zoom));
		// System.out.print("TEST FOR ARG
		// ("+((Object)(zoom*x+x_axis_delta)).toString()+",
		// "+((Object)(-(y-sh/2-y_axis_delta)*zoom)).toString()+")
		// result is = "+((Object)cursgn).toString());
		if (DoubleComparator.equals(cursgn, 0) || lastsgn != cursgn && y != -FunctionDrawer.sh / 2) {
		    // System.out.print(" = GOOD");
		    FunctionDrawer.functline(x - step / 10 + FunctionDrawer.sw / 2.0 + FunctionDrawer.x_axis_delta,
			    y - step / 10 + FunctionDrawer.y_axis_delta,
			    x + step / 10 + FunctionDrawer.sw / 2.0 + FunctionDrawer.x_axis_delta,
			    y + step / 10 + FunctionDrawer.y_axis_delta, canvas, paint);
		} else {
		    // System.out.print(" = BAD");
		}
		// System.out.print("\n");
		lastsgn = cursgn;
	    }
	}
	paint.setColor(android.graphics.Color.BLACK);
    }

    void drawFunction(final Function funct, final Canvas canvas, final Paint paint) {

	double lasty = 0.0;
	double y = 0.0;
	final double step = this.getStep(); // function_graphing_step;
	paint.setColor(funct.color);
	for (double x = 0.0; x < FunctionDrawer.sw / 2; x += step) {
	    lasty = y;
	    if (funct.eval(FunctionDrawer.zoom * x + FunctionDrawer.x_axis_delta) == FunctionDrawer.MATH_ERROR) {

	    } else {
		y = 1 / FunctionDrawer.zoom * -funct.eval(FunctionDrawer.zoom * x + FunctionDrawer.x_axis_delta);
		if (FunctionDrawer.sh / 2 + y + FunctionDrawer.y_axis_delta < -this.additional_work_area_h) {
		    y = FunctionDrawer.MATH_UNKNOWN;
		} else if (FunctionDrawer.sh / 2 + y + FunctionDrawer.y_axis_delta > FunctionDrawer.sh
			+ this.additional_work_area_h) {
		    y = FunctionDrawer.MATH_UNKNOWN;
		} else {
		    if (y != FunctionDrawer.MATH_UNKNOWN && lasty != FunctionDrawer.MATH_UNKNOWN && x != 0.0) {
			FunctionDrawer.functline(FunctionDrawer.sw / 2 + x - step,
				FunctionDrawer.sh / 2 + lasty + FunctionDrawer.y_axis_delta, FunctionDrawer.sw / 2 + x,
				FunctionDrawer.sh / 2 + y + FunctionDrawer.y_axis_delta, canvas, paint);
		    }
		}
	    }
	}
	// 1/zoom*-funct(...)
	// sh/2+lasty+y_axis_delta

	y = 0.0;
	lasty = 0.0;
	for (double x2 = 0.0; x2 > -FunctionDrawer.sw / 2; x2 -= step) {
	    lasty = y;
	    if (funct.eval(FunctionDrawer.zoom * x2 + FunctionDrawer.x_axis_delta) == FunctionDrawer.MATH_ERROR) {

	    } else {
		y = 1 / FunctionDrawer.zoom * -funct.eval(FunctionDrawer.zoom * x2 + FunctionDrawer.x_axis_delta);
		if (FunctionDrawer.sh / 2 + y + FunctionDrawer.y_axis_delta < -this.additional_work_area_h) {
		    y = FunctionDrawer.MATH_UNKNOWN;
		} else if (FunctionDrawer.sh / 2 + y + FunctionDrawer.y_axis_delta > FunctionDrawer.sh
			+ this.additional_work_area_h) {
		    y = FunctionDrawer.MATH_UNKNOWN;
		} else {
		    if (y != FunctionDrawer.MATH_UNKNOWN && lasty != FunctionDrawer.MATH_UNKNOWN && x2 != 0.0) {
			FunctionDrawer.functline(FunctionDrawer.sw / 2 + x2 + step,
				FunctionDrawer.sh / 2 + lasty + FunctionDrawer.y_axis_delta, FunctionDrawer.sw / 2 + x2,
				FunctionDrawer.sh / 2 + y + FunctionDrawer.y_axis_delta, canvas, paint);
		    }
		}
	    }
	}
	paint.setColor(android.graphics.Color.BLACK);
    }

    void drawXYAxis(final Canvas canvas, final Paint paint) {
	FunctionDrawer.outtextxy(5, 15, "zoom [%] ~= ", canvas, paint);
	String buffer = FunctionDrawer.valToStr((int) (1 / FunctionDrawer.zoom * 100));
	FunctionDrawer.outtextxy(95, 15, buffer, canvas, paint);

	FunctionDrawer.line(0, FunctionDrawer.sh / 2, FunctionDrawer.sw, FunctionDrawer.sh / 2, canvas, paint);
	FunctionDrawer.line(FunctionDrawer.sw / 2, 0, FunctionDrawer.sw / 2, FunctionDrawer.sh, canvas, paint);

	paint.setColor(android.graphics.Color.RED);
	FunctionDrawer.line(0, FunctionDrawer.sh / 2 + FunctionDrawer.y_axis_delta, FunctionDrawer.sw,
		FunctionDrawer.sh / 2 + FunctionDrawer.y_axis_delta, canvas, paint);
	FunctionDrawer.line(FunctionDrawer.sw / 2 - FunctionDrawer.x_axis_delta, 0,
		FunctionDrawer.sw / 2 - FunctionDrawer.x_axis_delta, FunctionDrawer.sh, canvas, paint);
	paint.setColor(android.graphics.Color.BLACK);

	double step = FunctionDrawer.sw / (FunctionDrawer.XAxisLabels + 2.0);
	for (double itx = step; itx < FunctionDrawer.sw / 2; itx += step) {
	    paint.setColor(android.graphics.Color.GRAY);
	    FunctionDrawer.line(itx + FunctionDrawer.sw / 2, 0, itx + FunctionDrawer.sw / 2, FunctionDrawer.sh, canvas,
		    paint);
	    paint.setColor(android.graphics.Color.BLACK);

	    FunctionDrawer.line(itx + FunctionDrawer.sw / 2, FunctionDrawer.sh / 2 - 5, itx + FunctionDrawer.sw / 2,
		    FunctionDrawer.sh / 2 + 5, canvas, paint);
	    buffer = FunctionDrawer.valToStr(FunctionDrawer.zoom * (itx + FunctionDrawer.x_axis_delta));
	    FunctionDrawer.outtextxy(itx + FunctionDrawer.sw / 2 - 5, FunctionDrawer.sh / 2 + 15, buffer, canvas,
		    paint);
	}
	for (double itx2 = -step; itx2 > -FunctionDrawer.sw / 2; itx2 -= step) {
	    paint.setColor(android.graphics.Color.GRAY);
	    FunctionDrawer.line(itx2 + FunctionDrawer.sw / 2, 0, itx2 + FunctionDrawer.sw / 2, FunctionDrawer.sh,
		    canvas, paint);
	    paint.setColor(android.graphics.Color.BLACK);

	    FunctionDrawer.line(itx2 + FunctionDrawer.sw / 2, FunctionDrawer.sh / 2 - 5, itx2 + FunctionDrawer.sw / 2,
		    FunctionDrawer.sh / 2 + 5, canvas, paint);
	    buffer = FunctionDrawer.valToStr(FunctionDrawer.zoom * (itx2 + FunctionDrawer.x_axis_delta));
	    FunctionDrawer.outtextxy(itx2 + FunctionDrawer.sw / 2 - 5, FunctionDrawer.sh / 2 + 15, buffer, canvas,
		    paint);
	}

	step = FunctionDrawer.sh / (FunctionDrawer.YAxisLabels + 2.0);
	for (double ity = 0.0; ity < FunctionDrawer.sh / 2; ity += step) {
	    paint.setColor(android.graphics.Color.GRAY);
	    FunctionDrawer.line(0, FunctionDrawer.sh / 2 + ity, FunctionDrawer.sw, FunctionDrawer.sh / 2 + ity, canvas,
		    paint);
	    paint.setColor(android.graphics.Color.BLACK);

	    FunctionDrawer.line(FunctionDrawer.sw / 2 - 5, FunctionDrawer.sh / 2 + ity, FunctionDrawer.sw / 2 + 5,
		    FunctionDrawer.sh / 2 + ity, canvas, paint);
	    buffer = FunctionDrawer.valToStr(FunctionDrawer.zoom * (-ity + FunctionDrawer.y_axis_delta));
	    FunctionDrawer.outtextxy(FunctionDrawer.sw / 2 + 15, FunctionDrawer.sh / 2 + ity, buffer, canvas, paint);
	}
	for (double ity2 = 0.0; ity2 >= -FunctionDrawer.sh / 2; ity2 -= step) {
	    paint.setColor(android.graphics.Color.GRAY);
	    FunctionDrawer.line(0, FunctionDrawer.sh / 2 + ity2, FunctionDrawer.sw, FunctionDrawer.sh / 2 + ity2,
		    canvas, paint);
	    paint.setColor(android.graphics.Color.BLACK);

	    FunctionDrawer.line(FunctionDrawer.sw / 2 - 5, FunctionDrawer.sh / 2 + ity2, FunctionDrawer.sw / 2 + 5,
		    FunctionDrawer.sh / 2 + ity2, canvas, paint);
	    buffer = FunctionDrawer.valToStr(FunctionDrawer.zoom * (-ity2 + FunctionDrawer.y_axis_delta));
	    FunctionDrawer.outtextxy(FunctionDrawer.sw / 2 + 15, FunctionDrawer.sh / 2 + ity2, buffer, canvas, paint);
	}
    }

}
