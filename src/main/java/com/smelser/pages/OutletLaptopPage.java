package com.smelser.pages;

import java.io.IOException;
import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.smelser.pages.event.PageEventHandler;
import com.smelser.pages.event.laptops.ResultsHandler;
import com.smelser.utils.PageManager;

public class OutletLaptopPage extends PageBase implements MyPage {
	
	static final Logger LOG = LoggerFactory.getLogger(OutletLaptopPage.class);
	static final String START_URL = "http://outlet.lenovo.com/outlet_us/laptops/#facet-1=1,2,3,4&facet-3=16,22";
	
	private PageManager pm;
	
	private HtmlPage page;
	

	public OutletLaptopPage(PageManager pm, WebClient webClient) throws FailingHttpStatusCodeException, MalformedURLException, IOException {		
		super(pm);
		
		this.pm = pm;
		this.page = (HtmlPage) webClient.getPage(START_URL);
		pm.setPage(page);

		//this.addSelector(new SortHandler(page, this));
		this.addSelector(new ResultsHandler(pm));
	}	
	
	public void doPage(HtmlElement el) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		super.doPage(page.getDocumentElement());
	}

}
