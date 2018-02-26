package com.example.extremetic_tac_toe.tinyandui;

import java.util.Calendar;
import java.util.Vector;

public class TSynchronizer {

	private Vector<Calendar> mes = new Vector<Calendar>();
	
	public TSynchronizer() {
		
	}
	
	public TSynchronizer(TSynchronizer synchronizer) {
		mes = new Vector<Calendar>(synchronizer.mes);
	}
	
	public void newMeasurement() {
		mes.add(Calendar.getInstance());
	}
	
	public void removeLast() {
		mes.remove(mes.size()-1);
	}
	
	public void clear() {
		mes.clear();
	}
	
	public int getChangeMillisec() {
		return getFieldChange(Calendar.MILLISECOND);
	}
	
	public int getChangeSec() {
		return getFieldChange(Calendar.SECOND);
	}
	
	public int getFieldChange(int field) {
		switch(field) {
			case Calendar.MILLISECOND:
				return (mes.lastElement().get(Calendar.MILLISECOND)+mes.lastElement().get(Calendar.SECOND)*1000+mes.lastElement().get(Calendar.MINUTE)*60000)-(mes.get(mes.size()-2).get(Calendar.MILLISECOND)+mes.get(mes.size()-2).get(Calendar.SECOND)*1000+mes.get(mes.size()-2).get(Calendar.MINUTE)*60000);
			case Calendar.SECOND:
				return (int)((mes.lastElement().get(Calendar.MILLISECOND)*0.001+mes.lastElement().get(Calendar.SECOND)+mes.lastElement().get(Calendar.MINUTE)*60+mes.lastElement().get(Calendar.HOUR)*3600)-(mes.get(mes.size()-2).get(Calendar.MILLISECOND)*0.001+mes.get(mes.size()-2).get(Calendar.SECOND)+mes.get(mes.size()-2).get(Calendar.MINUTE)*60+mes.get(mes.size()-2).get(Calendar.HOUR)*3600));	
		}
		return mes.lastElement().get(field)-mes.get(mes.size()-2).get(field);
	}
	
}
