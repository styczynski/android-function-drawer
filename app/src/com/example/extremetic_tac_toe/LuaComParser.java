package com.example.extremetic_tac_toe;
import java.util.Vector;


public class LuaComParser {

	private static boolean isLetter(char x) {
		return (x>='a'&&x<='z')||(x>='A'&&x<='Z');
	}
	
	private static Pair<Integer,String> getWord(String com, int pos) {
		final int len = com.length();
		for(int it=pos;it<len;++it) {
			if(!isLetter(com.charAt(it))) {
				return Pair.makePair(it, com.substring(pos, it));
			}
		}
		return Pair.makePair(len, com.substring(pos, len));
	}
	
	private static Pair<Integer,String> getArg(String com, int pos) {
		final int len = com.length();
		int lev = 0, start=pos;
		for(int it=pos;it<len;++it) {
			if(com.charAt(it)=='[') {
				if(lev==0) {
					start=it;
				}
				++lev;
			} else if(com.charAt(it)==']') {
				--lev;
				if(lev==0) {
					return Pair.makePair(it, com.substring(start+1, it));
				}
			} else {
				
			}
		}
		return Pair.makePair(pos, "");
	}
	
	public static void parse(String com) {
		final int len = com.length();
		for(int it=0;it<len;++it) {
			if(com.charAt(it)=='$') {
				boolean move = true;
				Pair<Integer, String> token = getWord(com, it+1);
				System.out.println("Token = ["+token.second+"]");
				
				String tempvarg = ".";
				Vector<String> vargs = new Vector<String>();
				while(tempvarg.length()>0) {
					Pair<Integer, String> targ0 = getArg(com, it+1);
					it = targ0.first;
					tempvarg = targ0.second;
					if(tempvarg.length()>0) vargs.add(tempvarg);
				}
				String args[] = new String[vargs.size()];
				vargs.copyInto(args);
				
				if(!Console.onCommandToken(token.second, args)) {
					move = false;
				}
				if(move) it = token.first;
			}
		}
	}

}
