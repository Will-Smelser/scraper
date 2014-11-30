package com.smelser.pages;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.util.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.smelser.Main;
import com.smelser.pages.event.EmptyEventHandler;
import com.smelser.pages.event.NodeEvtMap;
import com.smelser.pages.event.PageEventHandler;

public abstract class PageBase extends EmptyEventHandler implements Page {
	
	static final Logger LOG = LoggerFactory.getLogger(PageBase.class);
	
	private final HtmlPage page;
	private final List<NodeEvtMap> selectors = new ArrayList<NodeEvtMap>();
	private PageEventHandler defaultHandler = null;
	
	public PageBase(final HtmlPage page){
		this.page = page;
	}
	
	public void setDefaultHandler(PageEventHandler handler){
		this.defaultHandler = handler;
	}
	
	public void addSelector(String css3selector){
		if(defaultHandler == null) throw new IllegalStateException("No default handler was set.  Call setDefaultHandler() first.");
		selectors.add(new NodeEvtMap(css3selector,defaultHandler));
	}
	
	public void addSelector(String css3selector, PageEventHandler handler){
		LOG.info("Added css3 selector: "+css3selector);
		selectors.add(new NodeEvtMap(css3selector,handler));
	}
	
	public void doPage() throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		
		HtmlElement doc = page.getDocumentElement();
		
		
		for(NodeEvtMap map : selectors){
			map.handler.beforeSelector();
			LOG.info("Executing selector: "+map.selector);
			for(DomNode node : doc.querySelectorAll(map.selector)){
				map.handler.beforeNode();
				if(!map.handler.foundNode((HtmlElement)node,map.selector)) break;
				map.handler.afterNode();
			}
			map.handler.afterSelector();
		}
	}
	
	public void waitForReady(){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
