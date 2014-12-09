package com.smelser.pages.event.laptops;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.smelser.pages.event.EmptyEventHandler;
import com.smelser.pages.event.PageEventHandler;
import com.smelser.pages.validators.Laptop;
import com.smelser.pages.validators.Validator;
import com.smelser.utils.PageManager;

public class ResultsHandler extends EmptyEventHandler implements PageEventHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(ResultsHandler.class);
	
	private static final String SELECTOR_RESULTS = ".facetedResults > li";
	private PageManager pm;
	
	private List<Validator> laptops = new ArrayList<Validator>();
	
	private final PageEventHandler cartHandler;
	
	public ResultsHandler(PageManager pm){
		this.pm = pm;
		this.cartHandler = new CartHandler();
	}
	
	public String getSelector(){
		return SELECTOR_RESULTS;
	}
	
	public boolean foundNode(HtmlElement node) {
		try{
			Validator laptop = new Laptop(node);
			if(laptop.valid()){
				
				LOG.info("Found valid laptop");
				LOG.info(laptop.toString());
				
				cartHandler.beforeSelector();
				HtmlAnchor anchor = (HtmlAnchor)node.querySelector(cartHandler.getSelector());
				
				if(anchor != null){
					pm.addValidator(laptop);
					
					cartHandler.beforeNode();
					if(!cartHandler.foundNode(anchor))
						return false;
					cartHandler.afterNode();
				}else
					LOG.warn("Failed to find element.");
				
				cartHandler.afterSelector();
				
			}else
				LOG.info("Skipped: "+laptop.toString().replace("\n", ""));
		}catch(Exception e){
			LOG.error("Exception validating laptop",e);
			LOG.error(node.asXml());
		}
		return true;
	}

	public void beforeSelector() {
		waitTillContentLoaded(this.pm.getPage());
	}
	
	public static void waitTillContentLoaded(HtmlPage page){
		
		int count = 0;
		DomNode results = null;
		
		//wait on initial load
		while(results == null && count < 20){
			LOG.info("Waiting on content...");
			count++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			results = page.querySelector(SELECTOR_RESULTS);
		}
		
		//wait for the results to finish loading
		count = 0;
		int lcount = -1;
		int rcount = page.querySelectorAll(SELECTOR_RESULTS).getLength();
		int constant = 0;
		while((rcount != lcount && count < 20) || constant < 3){
			
			if(rcount != lcount)
				constant = 0;
			else
				constant++;
			
			LOG.info("Waiting on content to stabalize..."+rcount+"-"+lcount);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			lcount = rcount;
			rcount = page.querySelectorAll(SELECTOR_RESULTS).getLength();
			count++;
		}
	}

}
