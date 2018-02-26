package com.example.extremetic_tac_toe;

public class Pair <T1,T2> {
	
	public T1 first = null;
	public T2 second = null;
	
	public <T3,T4> Pair(Pair<T3,T4> arg0) {
		try {
			first = (T1)arg0.first;
			second = (T2)arg0.second;
		} catch(ClassCastException e) {
			first = null;
			second = null;
		}
	}
	
	public Pair(T1 arg0, T2 arg1) {
		first = arg0;
		second = arg1;
	}
	
	public String getClassName() {
		return this.getClass().getName()+"["+((Object)first).getClass().getName()+";"+((Object)second).getClass().getName()+"]";
	}
	
	public <T3,T4> boolean isTypeOf(Pair<T3,T4> comp) {
		return comp.getClassName().equals(this.getClassName());
	}
	
	public <T3,T4> boolean equals(Pair<T3,T4> comp) {
		if(!this.isTypeOf(comp)) return false;
		if(this.first.equals((T1)comp.first) && this.second.equals((T2)comp.second)) {
			return true;
		}
		return false;
	}
	
	public static <T1,T2> Pair<T1,T2> makePair(T1 first, T2 second) {
		return new Pair<T1, T2>(first, second);
	}
	
	public static <T1,T2,T3,T4> boolean equals(Pair<T1,T2> arg0, Pair<T3,T4> arg1) {
		return arg0.equals(arg1);
	}
	
}
