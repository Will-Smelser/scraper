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
import com.smelser.pages.Page;
import com.smelser.utils.CookieToJSON;
import com.smelser.utils.Gmail;

public class Main {

	static final boolean SEND_EMAIL = true;
	static final Logger LOG = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException {
		
		LOG.trace("Starting up Lenovo application.");

	    while(!Thread.currentThread().interrupted()){
	    	
	    	final MyWebClient myClient = new MyWebClient();
		    final WebClient webClient = myClient.getWebClient();
		    final CookieManager cm = myClient.getCookieManager();
		    HtmlPage page = webClient.getPage("http://outlet.lenovo.com/outlet_us/laptops/#facet-1=1,2,3,4&facet-3=16,22");
		    
		    final Page laptops = new OutletLaptopPage(page); 
		    laptops.doPage();
		    laptops.waitOnComplete();
		    
		    page = laptops.getPage();
		    
		    LOG.info(page.getUrl().toString());
		    
		    
		    String cookies = CookieToJSON.toJSON(cm.getCookies());
		    
		    LOG.info("On Page: "+page.getUrl());
		    
		    if(page.getUrl().toString().toLowerCase().contains("cart")){
		    	LOG.info("Found an item and added to cart");
		    	if(SEND_EMAIL)
		    		Gmail.sendAttached(cookies);
		    	
		    	LOG.trace(cookies);
		    }else{
		    	LOG.info("No items added to cart");
		    }
		    
		    webClient.closeAllWindows();
		    
		    Thread.sleep(30000);
	    }
	    
	    
	    
	    LOG.trace("Application closing");
	    
	}

}
