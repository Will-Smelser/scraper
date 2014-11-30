package com.smelser.pages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.smelser.pages.event.PageEventHandler;
import com.smelser.pages.event.laptops.ResultsHandler;
import com.smelser.pages.event.laptops.SortHandler;

public class OutletLaptopPage extends PageBase implements PageEventHandler, Page {
	
	static final Logger LOG = LoggerFactory.getLogger(OutletLaptopPage.class);
	
	
	private HtmlPage page;
	

	public OutletLaptopPage(HtmlPage page) {		
		super(page);
		
		this.page = page;
		this.setDefaultHandler(this);

		//this.addSelector(new SortHandler(page, this));
		this.addSelector(new ResultsHandler(page, this));
	}	

	public boolean foundNode(HtmlElement node, String selector) {
		throw new IllegalStateException("Not implemented");
	}
	
	
	public void setPage(HtmlPage page){
		this.page = page;
	}
	
	public HtmlPage getPage(){
		return page;
	}


	public void beforeSelector() {
		ResultsHandler.waitTillContentLoaded(page);
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
