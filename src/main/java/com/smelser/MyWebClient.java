package com.smelser;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;

public class MyWebClient {
	private final WebClient webClient;
	private final CookieManager cm;
	
	public MyWebClient(){
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
	    java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
	    
	    webClient = new WebClient(BrowserVersion.FIREFOX_24);
	    webClient.getOptions().setTimeout(120000);
	    webClient.waitForBackgroundJavaScript(60000);
	    webClient.getOptions().setRedirectEnabled(true);
	    webClient.getOptions().setJavaScriptEnabled(true);
	    webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
	    webClient.getOptions().setThrowExceptionOnScriptError(false);
	    webClient.getOptions().setCssEnabled(false);
	    webClient.getOptions().setUseInsecureSSL(true);
	    webClient.setAjaxController(new NicelyResynchronizingAjaxController());
	    
	    cm = new CookieManager();
	    cm.setCookiesEnabled(true);
        webClient.setCookieManager(cm);
        cm.clearCookies();
	}
	
	public CookieManager getCookieManager(){ return this.cm; }
	
	public WebClient getWebClient(){ return webClient; }
	
}
