package com.smelser;

import java.io.IOException;
import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.smelser.pages.OutletLaptopPage;
import com.smelser.pages.MyPage;
import com.smelser.utils.CookieToJSON;
import com.smelser.utils.Gmail;
import com.smelser.utils.PageManager;

public class Main {

	static final boolean SEND_EMAIL = true;
	static final Logger LOG = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException {
		
		LOG.trace("Starting up Lenovo application.");
		
		final MyWebClient myClient = new MyWebClient();
	    final WebClient webClient = myClient.getWebClient();
	    final CookieManager cm = myClient.getCookieManager();
	    final PageManager pm = new PageManager();

	    while(!Thread.currentThread().interrupted()){
	    	
	    	try{
	    	
			    pm.clearValidators();
			    final MyPage laptops = new OutletLaptopPage(pm, webClient);
			    
			    laptops.doPage(null);
			    
			    LOG.info("On Page: "+pm.getPage().getUrl().toString());
			    
			    String cookies = CookieToJSON.toJSON(cm.getCookies());
			    
			    //if we have validators, then items should be in the cart
			    if(pm.hasValidators()){
			    	LOG.info("Found an item and added to cart");
			    	if(SEND_EMAIL){
			    		Gmail.sendAttached(pm.getValidatorsAsString("================"),cookies);
			    	}
			    	
			    	LOG.info(pm.getValidatorsAsString("================"));
			    	LOG.trace(cookies);
			    }else{
			    	LOG.info("No items added to cart");
			    }
			    
			    webClient.closeAllWindows();
	    	}catch(Exception e){
	    		LOG.warn("Failed to load page");
	    	}
		    
		    Thread.sleep(30000);
	    }
	    
	    
	    
	    LOG.trace("Application closing");
	    
	}

}
