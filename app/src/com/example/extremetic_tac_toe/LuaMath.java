package com.example.extremetic_tac_toe;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jme.JmePlatform;

public final class LuaMath extends TwoArgFunction {
	
	private static Function postedFunct = null;
	
	public LuaMath() {}
	
	class LuaFuctionDebug extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue arg0) {
			System.out.println("[JLua.debug] "+arg0.tostring());
			return null;
		}
	}
	
	class LuaFuctionEval extends OneArgFunction {
		@Override
		public LuaValue call(LuaValue arg0) {
			try {
				return LuaDouble.valueOf(LatexParser.calculate(arg0.tostring().toString()));
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	class LuaFuctionSin extends OneArgFunction {
		@Override public LuaValue call(LuaValue arg0) { return LuaDouble.valueOf( Math.sin(Math.toRadians(arg0.tofloat())) ); }
	}
	
	class LuaFuctionCos extends OneArgFunction {
		@Override public LuaValue call(LuaValue arg0) { return LuaDouble.valueOf( Math.cos(Math.toRadians(arg0.tofloat())) ); }
	}
	
	class LuaFuctionTan extends OneArgFunction {
		@Override public LuaValue call(LuaValue arg0) { return LuaDouble.valueOf( Math.tan(Math.toRadians(arg0.tofloat())) ); }
	}
	
	class LuaFuctionPow extends TwoArgFunction {
		@Override public LuaValue call(LuaValue arg0, LuaValue arg1) { return LuaDouble.valueOf( Math.pow(arg0.tofloat(), arg1.tofloat()) ); }
	}
	
	class LuaFuctionSignum extends OneArgFunction {
		@Override public LuaValue call(LuaValue arg0) { return LuaDouble.valueOf( Math.signum(arg0.tofloat()) ); }
	}
	
	class LuaFuctionAbs extends OneArgFunction {
		@Override public LuaValue call(LuaValue arg0) { return LuaDouble.valueOf( Math.abs(arg0.tofloat()) ); }
	}
	
	class LuaFuctionCeil extends OneArgFunction {
		@Override public LuaValue call(LuaValue arg0) { return LuaDouble.valueOf( Math.ceil(arg0.tofloat()) ); }
	}
	
	class LuaFuctionFloor extends OneArgFunction {
		@Override public LuaValue call(LuaValue arg0) { return LuaDouble.valueOf( Math.floor(arg0.tofloat()) ); }
	}

	class LuaFuctionRound extends OneArgFunction {
		@Override public LuaValue call(LuaValue arg0) { return LuaDouble.valueOf( Math.round(arg0.tofloat()) ); }
	}
	
	class LuaFuctionRnd extends ZeroArgFunction {
		@Override public LuaValue call() { return LuaDouble.valueOf( Math.random() ); }
	}
	
	class LuaFuctionRndEx extends TwoArgFunction {
		@Override public LuaValue call(LuaValue arg0, LuaValue arg1) { return LuaDouble.valueOf( Math.random()*(arg1.tofloat()-arg0.tofloat())+arg0.tofloat() ); }
	}
	
	class LuaFuctionCompare extends ThreeArgFunction {
		@Override public LuaValue call(LuaValue arg0, LuaValue arg1, LuaValue arg2) { return LuaBoolean.valueOf( DoubleComparator.equals(arg0.todouble(), arg1.todouble(), arg2.todouble()) ); }
	}
	
	class LuaFuctionEqual extends TwoArgFunction {
		@Override public LuaValue call(LuaValue arg0, LuaValue arg1) { return LuaBoolean.valueOf( DoubleComparator.equals(arg0.todouble(), arg1.todouble()) ); }
	}
	
	 @Override
     public LuaValue call(LuaValue modname, LuaValue env) {
	     LuaValue library = tableOf();
	     library.set("debug", new LuaFuctionDebug());
	     library.set("eval", new LuaFuctionEval());
	     library.set("sin", new LuaFuctionSin());
	     library.set("cos", new LuaFuctionCos());
	     library.set("tan", new LuaFuctionTan());
	     library.set("pow", new LuaFuctionPow());
	     library.set("sgn", new LuaFuctionSignum());
	     library.set("abs", new LuaFuctionAbs());
	     library.set("ceil", new LuaFuctionCeil());
	     library.set("floor", new LuaFuctionFloor());
	     library.set("round", new LuaFuctionRound());
	     library.set("rnd", new LuaFuctionRnd());
	     library.set("rndex", new LuaFuctionRndEx());
	     library.set("compare", new LuaFuctionCompare());
	     library.set("equal", new LuaFuctionEqual());
	     env.set("jmath", library);
	     return library;
	 }

}
