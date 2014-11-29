package com.smelser.utils;

import java.util.List;
import java.util.Set;

import com.gargoylesoftware.htmlunit.util.Cookie;

public class CookieToJSON {
	public static String toJSON(Set<Cookie> set){
		StringBuilder cookiesOut = new StringBuilder("[\n");
	    int i = 1;
	    String comma = "";
	    for(Cookie cookie : set){
	    	String line = null;
	    	if(cookie.getExpires() == null){
	    		line = "\"session\": true,";
	    	}else{
	    		line = "\"session\": true,\n\"expirationDate\": "+
	    				(cookie.getExpires().getTime()/1000.0)+",\n";
	    	}
	    		
	    	cookiesOut.append(comma+"{\n");
	    	cookiesOut.append("\"domain\": \""+cookie.getDomain()+"\",\n");
	    	cookiesOut.append(line);
	    	cookiesOut.append("\"hostOnly\": false,\n");
	    	cookiesOut.append("\"httpOnly\": "+cookie.isHttpOnly()+",\n");
	    	cookiesOut.append("\"name\": \""+cookie.getName()+"\",\n");
	    	cookiesOut.append("\"path\": \""+cookie.getPath()+"\",\n");
	    	cookiesOut.append("\"secure\": "+cookie.isSecure()+",\n");
	    	cookiesOut.append("\"session\": false,\n");
	    	cookiesOut.append("\"storeId\": 0,\n");
	    	cookiesOut.append("\"value\": \""+cookie.getValue().replace("\"", "\\\"")+"\",\n");
	    	cookiesOut.append("\"id\": "+i+"}\n");
	    	i++;
	    	comma = ",";
	    }
	    cookiesOut.append("]");
	    
	    return cookiesOut.toString();
	}
}
