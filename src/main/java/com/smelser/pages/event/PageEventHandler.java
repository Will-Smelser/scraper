package com.smelser.pages.event;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public interface PageEventHandler {
	public boolean foundNode(HtmlElement node);
	public void afterNode();
	public void beforeNode();
	public void afterSelector();
	public void beforeSelector();
	public String getSelector();
}