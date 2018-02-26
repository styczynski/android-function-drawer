package com.example.extremetic_tac_toe;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URL;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jme.JmePlatform;

import android.graphics.Canvas;
import android.graphics.Paint;

public final class LuaFunctionPlugin extends TwoArgFunction {

	public static Canvas canvas = null;
	public static Paint paint = null;
	protected static boolean limitScope = true;

	/*public float globalx(float x) {
		return ((float)(x/FunctionDrawer.zoom+FunctionDrawer.x_axis_delta-FunctionDrawer.sw/2.0));
	}
	
	public float globaly(float y) {
		return (float)(FunctionDrawer.zoom*(-y)-FunctionDrawer.y_axis_delta+FunctionDrawer.sh/2.0);
	}
	
	public float localex(float x) {
		return ((float)((FunctionDrawer.zoom*x)-FunctionDrawer.x_axis_delta+FunctionDrawer.sw/2.0));
	}
	
	public float localey(float y) {
		return (float)(FunctionDrawer.zoom*-y+FunctionDrawer.y_axis_delta-FunctionDrawer.sh/2.0);
	}*/
	
	public static float globalxp(float x) {
		return (float)(FunctionDrawer.zoom*x+FunctionDrawer.x_axis_delta);
	}
	
	public static float globalyp(float y) {
		return (float)(-FunctionDrawer.zoom*(y-FunctionDrawer.y_axis_delta-FunctionDrawer.sh));
	}
	
	public static float localexp(float x) {
		return (float)((x-FunctionDrawer.x_axis_delta)/FunctionDrawer.zoom+FunctionDrawer.sw/2);
	}
	
	public static float localeyp(float y) {
		return (float)(1/FunctionDrawer.zoom*-y+FunctionDrawer.sh/2+FunctionDrawer.y_axis_delta);
	}
	
	public static float localex(float x, float y) {
		return (float) FunctionDrawer.curDisplay.getX(localexp(x), localeyp(y));
	}
	
	public static float localey(float x, float y) {
		return (float) FunctionDrawer.curDisplay.getY(localexp(x), localeyp(y));
	}
	
	
	private boolean checkPointIsInScope(float x, float y) {
		double nx = FunctionDrawer.curDisplay.getX(x, y);
		double ny = FunctionDrawer.curDisplay.getY(x, y);
		x = (float)nx;
		y = (float)ny;
		if(limitScope) {
			if(x<-2*FunctionDrawer.sw || x>=FunctionDrawer.sw*2) {
				return true;
			}
			if(y<-2*FunctionDrawer.sh || y>=FunctionDrawer.sh*2) {
				return true;
			}
		}
		return false;
	}
	
	private boolean checkPointIsInScope(LuaValue x, LuaValue y) {
		return checkPointIsInScope( localexp(x.tofloat()), localeyp(y.tofloat()) );
	}
	
	public LuaFunctionPlugin() {}
	
	class LuaFuctionGetCanvasCoord extends ZeroArgFunction {
		
		class LuaFuctionCoordGlobalX extends OneArgFunction {
			@Override
			public LuaValue call(LuaValue arg0) {
				return LuaValue.valueOf(globalxp(arg0.tofloat()));
			}
		}
		
		class LuaFuctionCoordGlobalY extends OneArgFunction {
			@Override
			public LuaValue call(LuaValue arg0) {
				return LuaValue.valueOf(globalyp(arg0.tofloat()));
			}
		}
		
		class LuaFuctionCoordLocalX extends OneArgFunction {
			@Override
			public LuaValue call(LuaValue arg0) {
				return LuaValue.valueOf(localexp(arg0.tofloat()));
			}
		}
		
		class LuaFuctionCoordLocalY extends OneArgFunction {
			@Override
			public LuaValue call(LuaValue arg0) {
				return LuaValue.valueOf(localeyp(arg0.tofloat()));
			}
		}
		
		@Override
		public LuaValue call() {
			LuaTable g = new LuaTable();
			g.set(LuaValue.valueOf("left"), LuaValue.valueOf(globalxp((float)(-FunctionDrawer.sw/2))));
			g.set(LuaValue.valueOf("right"), LuaValue.valueOf(globalxp((float)(FunctionDrawer.sw/2))));
			g.set(LuaValue.valueOf("bottom"), LuaValue.valueOf(globalyp((float)(-FunctionDrawer.sh/2))));
			g.set(LuaValue.valueOf("top"), LuaValue.valueOf(globalyp((float)(FunctionDrawer.sh/2))));
			
			g.set(LuaValue.valueOf("localx"), new LuaFuctionCoordLocalX());
			g.set(LuaValue.valueOf("localy"), new LuaFuctionCoordLocalY());
			g.set(LuaValue.valueOf("globalx"), new LuaFuctionCoordGlobalX());
			g.set(LuaValue.valueOf("globaly"), new LuaFuctionCoordGlobalY());
			
			return g;
		}
	}
	
	class LuaFuctionGetSettings extends ZeroArgFunction {
		
		class LuaFuctionSettingsLimitScope extends OneArgFunction {
			@Override
			public LuaValue call(LuaValue arg0) {
				limitScope = arg0.toboolean();
				return null;
			}
		}
		
		@Override
		public LuaValue call() {
			LuaTable g = new LuaTable();
			//g.set(LuaValue.valueOf("graphingStep"), LuaValue.valueOf(FunctionDrawer.function_graphing_step*4.0));
			g.set(LuaValue.valueOf("graphingStep"), LuaValue.valueOf( ((globalxp((float)(FunctionDrawer.sw/2))-globalxp((float)(-FunctionDrawer.sw/2))))/FunctionDrawer.stepsNumber ));
			g.set(LuaValue.valueOf("limitScope"), new LuaFuctionSettingsLimitScope());
			return g;
		}
	}
	
	class LuaFuctionDrawFunction extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue arg0) {
			double step = ((globalxp((float)(FunctionDrawer.sw/2))-globalxp((float)(-FunctionDrawer.sw/2))))/FunctionDrawer.stepsNumber;
			for(double x=globalxp((float)(0)); x<globalxp((float)(FunctionDrawer.sw)); x+=step) {
				arg0.invoke(LuaValue.valueOf(x));
			}
			return null;
		}
	}
	
	class LuaFuctionDrawPoint extends TwoArgFunction {
		@Override
		public LuaValue call(LuaValue arg0, LuaValue arg1) {
			if(checkPointIsInScope(arg0, arg1)) return null;
			FunctionDrawer.point(localex(arg0.tofloat(),arg1.tofloat()), localey(arg0.tofloat(),arg1.tofloat()), canvas, paint);
			return null;
		}
	}
	
	class LuaFuctionDrawLine extends VarArgFunction {
		@Override
		public LuaValue invoke(Varargs args) {
			if(checkPointIsInScope(args.arg(1), args.arg(2))) return null;
			if(checkPointIsInScope(args.arg(3), args.arg(4))) return null;
			FunctionDrawer.line(localex(args.arg(1).tofloat(),args.arg(2).tofloat()), localey(args.arg(1).tofloat(),args.arg(2).tofloat()), localex(args.arg(3).tofloat(),args.arg(4).tofloat()), localey(args.arg(3).tofloat(),args.arg(4).tofloat()), canvas, paint);
			return null;
		}
	}
	
	class LuaFuctionSetColor extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue arg0) {
			final String str = arg0.tostring().toString();
			if(str.equals("black")) {
				paint.setColor(android.graphics.Color.BLACK);
			} else if(str.equals("blue")) {
				paint.setColor(android.graphics.Color.BLUE);
			} else if(str.equals("cyan")) {
				paint.setColor(android.graphics.Color.CYAN);
			} else if(str.equals("kgray")) {
				paint.setColor(android.graphics.Color.DKGRAY);
			} else if(str.equals("gray")) {
				paint.setColor(android.graphics.Color.GRAY);
			} else if(str.equals("green")) {
				paint.setColor(android.graphics.Color.GREEN);
			} else if(str.equals("ltgray")) {
				paint.setColor(android.graphics.Color.LTGRAY);
			} else if(str.equals("magenta")) {
				paint.setColor(android.graphics.Color.MAGENTA);
			} else if(str.equals("red")) {
				paint.setColor(android.graphics.Color.RED);
			} else if(str.equals("white")) {
				paint.setColor(android.graphics.Color.WHITE);
			} else if(str.equals("yellow")) {
				paint.setColor(android.graphics.Color.YELLOW);
			} else {
				paint.setColor(arg0.toint());
			}
			return null;
		}
	}
	
	class LuaFuctionDebug extends ZeroArgFunction {
		@Override
		public LuaValue call() {
			System.out.println("[DEBUG] FD IS ACTIVE!");
			if(canvas==null) System.out.println("Canvas = NULL");
			if(paint==null) System.out.println("Paint = NULL");
			System.out.println("So what?");
			return null;
		}
	}
	
	class LuaFuctionFill extends ZeroArgFunction {
		@Override
		public LuaValue call() {
			canvas.drawPaint(paint);
			return null;
		}
	}
	
	public static void exec(String str) {
		str = "require \"com/example/extremetic_tac_toe/LuaFunctionPlugin\"\r\n" + 
				"require \"com/example/extremetic_tac_toe/LuaMath\"\r\n" + 
				"fd = juifd;" + str;
		Globals globals = JmePlatform.standardGlobals();
		LuaValue chunk = globals.load(str);
		chunk.call();
	}
	
	/*public static void exec(String str) {
		String script = "assets/test.lua";
	    Globals globals = JmePlatform.standardGlobals();
	    LuaValue chunk = globals.loadfile(script);
	    chunk.call( LuaValue.valueOf(script) ); 
	}*/
	
	 protected static LuaValue parseLuaString(String str) {
		LuaValue lua = JmePlatform.standardGlobals();
		LuaValue exec = (LuaValue)( lua.get("load").invoke(LuaValue.valueOf(str)) );
		exec.call();
		return lua;
	 }
	
	 public static String execOnFunctionLooking(String str, double x, double y) {
		str = "require \"com/example/extremetic_tac_toe/LuaFunctionPlugin\"\r\n" + 
				"require \"com/example/extremetic_tac_toe/LuaMath\"\r\n" + 
				"fd = juifd;" + str;
		LuaValue lua = parseLuaString(str);
		LuaValue funct = lua.get("onFunctionLooking");
		System.out.println(funct.tostring());
		if(!funct.isnil()) {
			return ((LuaFunction)funct).invoke(LuaValue.valueOf(x), LuaValue.valueOf(y)).tojstring();
		}
		return "";
	 }
	
	 @Override
     public LuaValue call(LuaValue modname, LuaValue env) {
	     LuaValue library = tableOf();
	     library.set("drawPoint", new LuaFuctionDrawPoint());
	     library.set("drawLine", new LuaFuctionDrawLine());
	     library.set("setColor", new LuaFuctionSetColor());
	     library.set("fill", new LuaFuctionFill());
	     library.set("debug", new LuaFuctionDebug());
	     library.set("drawFunction", new LuaFuctionDrawFunction());
	     library.set("getSettings", new LuaFuctionGetSettings());
	     //LuaFuctionGetCanvasCoord
	     library.set("getCoord", new LuaFuctionGetCanvasCoord());
	     library.set("getSettings", new LuaFuctionGetSettings());
	     
	     env.set("juifd", library);
	     return library;
	 }

}