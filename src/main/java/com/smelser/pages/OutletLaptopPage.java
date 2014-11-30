package com.smelser.pages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.smelser.pages.event.PageEventHandler;
import com.smelser.pages.event.laptops.ResultsHandler;
import com.smelser.pages.event.laptops.SortHandler;
import com.smelser.utils.PageManager;

public class OutletLaptopPage extends PageBase implements PageEventHandler, Page {
	
	static final Logger LOG = LoggerFactory.getLogger(OutletLaptopPage.class);
	
	
	private PageManager pm;
	

	public OutletLaptopPage(PageManager pm) {		
		super(pm);
		
		this.pm = pm;
		this.setDefaultHandler(this);

		//this.addSelector(new SortHandler(page, this));
		this.addSelector(new ResultsHandler(pm));
	}	


	public void beforeSelector() {
		ResultsHandler.waitTillContentLoaded(pm.getPage());
	}

	public void waitOnComplete() {
		// TODO Auto-generated method stub		
	}

	public String getSelector() {
		return null;
	}

	public boolean foundNode(HtmlElement node) {
		return true;
	}

}
