package com.smelser.pages.event.laptops;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.smelser.pages.MyPage;
import com.smelser.pages.event.EmptyEventHandler;
import com.smelser.pages.event.PageEventHandler;

public class SortHandler extends EmptyEventHandler implements PageEventHandler{
	
	private static final Logger LOG = LoggerFactory.getLogger(SortHandler.class);
	
	private final String SELECTOR = "#facet_result_sort";
	
	private HtmlPage page;
	private MyPage caller;
	
	public SortHandler(HtmlPage page, MyPage caller){
		this.page = page;
		this.caller = caller;
	}
	
	public boolean foundNode(HtmlElement node) {
		
		HtmlSelect select = (HtmlSelect) node;
		HtmlOption option = select.getOptionByText("PRICE(Low)");
		
		page = select.setSelectedAttribute(option, true);
		ResultsHandler.waitTillContentLoaded(page);
		return true;
	}

	public void afterSelector() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		ResultsHandler.waitTillContentLoaded(page);
		LOG.trace("Sort Handler Complete");
	}

	public void beforeSelector() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		ResultsHandler.waitTillContentLoaded(page);
	}

	public HtmlPage getPage() {
		return this.page;
	}

	public String getSelector() {
		return SELECTOR;
	}
}
