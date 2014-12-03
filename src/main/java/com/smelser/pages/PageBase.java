package com.smelser.pages;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.smelser.pages.event.EmptyEventHandler;
import com.smelser.pages.event.PageEventHandler;
import com.smelser.utils.PageManager;

public abstract class PageBase implements MyPage {
	
	static final Logger LOG = LoggerFactory.getLogger(PageBase.class);
	
	protected final PageManager pm;
	private final List<PageEventHandler> selectors = new ArrayList<PageEventHandler>();
	
	public PageBase(final PageManager pm){
		this.pm = pm;
	}

	
	public void addSelector(PageEventHandler handler){
		LOG.info("Added css3 selector: "+handler.getSelector());
		selectors.add(handler);
	}
	
	public void doPage(HtmlElement el) {
		
		for(PageEventHandler handler : selectors){
			handler.beforeSelector();
			LOG.info("Executing selector: "+handler.getSelector());
			for(DomNode node : el.querySelectorAll(handler.getSelector())){
				handler.beforeNode();
				if(!handler.foundNode((HtmlElement)node)) break;
				handler.afterNode();
			}
			handler.afterSelector();
		}
	}

}
