package com.smelser.utils;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;

public class WebClientUtil {

	
	public static void setup(WebClient client){
		//reduce the crazzy logging
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
	    java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
	    
	    client.getOptions().setTimeout(120000);
	    client.waitForBackgroundJavaScript(60000);
	    client.getOptions().setRedirectEnabled(true);
	    client.getOptions().setJavaScriptEnabled(true);
	    client.getOptions().setThrowExceptionOnFailingStatusCode(false);
	    client.getOptions().setThrowExceptionOnScriptError(false);
	    client.getOptions().setCssEnabled(false);
	    client.getOptions().setUseInsecureSSL(true);
	    client.setAjaxController(new NicelyResynchronizingAjaxController());
	    
	}

	
}
