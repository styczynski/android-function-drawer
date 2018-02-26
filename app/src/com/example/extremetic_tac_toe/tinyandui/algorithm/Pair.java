package com.example.extremetic_tac_toe.tinyandui.algorithm;

public class Pair<T1, T2> {
    public T1 first = null;
    public T2 second = null;

    public Pair() {
	this.first = null;
	this.second = null;
    }

    public Pair(final Pair<T1, T2> arg) {
	this.first = arg.first;
	this.second = arg.second;
    }

    public Pair(final T1 arg0, final T2 arg1) {
	this.first = arg0;
	this.second = arg1;
    }

    public static <C1, C2, C3, C4> boolean comparePairs(final Pair<C1, C2> arg0, final Pair<C3, C4> arg1) {
	return arg0.first.equals(arg1.first) && arg0.second.equals(arg1.second);
    }

    public static <C1, C2> Pair<C1, C2> makePair(final C1 arg0, final C2 arg1) {
	final Pair<C1, C2> ret = new Pair<C1, C2>();
	ret.first = arg0;
	ret.second = arg1;
	return ret;
    }

    @Override
    public Pair<T1, T2> clone() {
	return Pair.makePair(this.first, this.second);
    }

    public <C1, C2> boolean compareTo(final Pair<C1, C2> arg) {
	return Pair.comparePairs(this, arg);
    }

    public T1 getFirst() {
	return this.first;
    }

    public T2 getSecond() {
	return this.second;
    }

    public Pair<T1, T2> setFirst(final T1 arg) {
	this.first = arg;
	return this;
    }

    public Pair<T1, T2> setSecond(final T2 arg) {
	this.second = arg;
	return this;
    }

}
