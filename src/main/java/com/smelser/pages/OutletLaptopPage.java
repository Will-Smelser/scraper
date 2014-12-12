package com.smelser.pages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.smelser.pages.event.laptops.ResultsHandler;
import com.smelser.utils.CookieToJSON;
import com.smelser.utils.Gmail;
import com.smelser.utils.PageManager;
import com.smelser.utils.WebClientUtil;

public class OutletLaptopPage extends PageBase implements MyPage, Runnable {
	
	static final Logger LOG = LoggerFactory.getLogger(OutletLaptopPage.class);
	static final String START_URL = "http://outlet.lenovo.com/outlet_us/laptops/#facet-1=1,2,3,4&facet-3=16,22";
	
	static final boolean SEND_EMAIL = true;
	static final int RELOAD_SLEEP = 30000; //ms
	
	static final BrowserVersion B_VERSION = BrowserVersion.FIREFOX_24;
	
	private HtmlPage page;
	
    private WebClient webClient = new WebClient(B_VERSION);
    private final CookieManager cm = new CookieManager();

	public OutletLaptopPage() {
		super(new PageManager());

		//this.addSelector(new SortHandler(page, this));
		this.addSelector(new ResultsHandler(pm));
		
		WebClientUtil.setup(webClient);
	}	
	
	public void doPage(HtmlElement el) {
		try {
			webClient.closeAllWindows();
			webClient = new WebClient(B_VERSION);
			WebClientUtil.setup(webClient);
			
			this.page = (HtmlPage) webClient.getPage(START_URL);
		} catch (Exception e) {
			LOG.error("Failed to load the page.",e);
		}
		this.pm.setPage(page);
		
		//force wait on all of document
		this.page.asXml();
		
		super.doPage(this.page.getDocumentElement());
	}
	

	public void run() {
		LOG.info("Running Outlet Laptop Page scraper.");
		    
		Thread.currentThread();
		while(!Thread.interrupted()){
			LOG.info("Reloading outlet laptop page.");
			
			pm.clearValidators();
		    this.doPage(null);
		    
		    //if we have validators, then items should be in the cart
		    if(pm.hasValidators()){
		    	String cookies = CookieToJSON.toJSON(cm.getCookies());
		    	
		    	LOG.info("Found an item and added to cart");
		    	if(SEND_EMAIL){
		    		Gmail.sendAttached(pm.getValidatorsAsString("================"),cookies);
		    	}
		    	
		    	LOG.info(pm.getValidatorsAsString("================"));
		    	LOG.trace(cookies);
		    }else{
		    	LOG.info("No items added to cart");
		    }
		    
		    try {
				Thread.sleep(RELOAD_SLEEP);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		
	}

}
