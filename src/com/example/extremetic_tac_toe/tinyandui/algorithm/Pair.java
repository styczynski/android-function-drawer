package com.example.extremetic_tac_toe.tinyandui.algorithm;

public class Pair <T1,T2> {
	public T1 first = null;
	public T2 second = null;

	public Pair() {
		first = null;
		second = null;
	}
	
	public Pair(T1 arg0, T2 arg1) {
		first = arg0;
		second = arg1;
	}
	
	public Pair(Pair<T1,T2> arg) {
		first = arg.first;
		second = arg.second;
	}
	
	public Pair<T1, T2> setFirst(T1 arg) {
		first = arg;
		return this;
	}
	
	public Pair<T1, T2> setSecond(T2 arg) {
		second = arg;
		return this;
	}
	
	public T1 getFirst() {
		return first;
	}
	
	public T2 getSecond() {
		return second;
	}
	
	public Pair<T1, T2> clone() {
		return Pair.makePair(first, second);
	}
	
	public <C1,C2> boolean compareTo(Pair<C1,C2> arg) {
		return Pair.comparePairs(this, arg);
	}
	
	public static <C1,C2> Pair<C1,C2> makePair(C1 arg0, C2 arg1) {
		Pair<C1,C2> ret = new Pair<C1,C2>();
		ret.first = arg0;
		ret.second = arg1;
		return ret;
	}
	
	public static <C1,C2,C3,C4> boolean comparePairs(Pair<C1,C2> arg0, Pair<C3,C4> arg1) {
		return( arg0.first.equals(arg1.first) && arg0.second.equals(arg1.second) );
	}
	
}
