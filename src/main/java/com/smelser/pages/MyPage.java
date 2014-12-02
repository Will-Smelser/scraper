package com.smelser.pages;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.smelser.pages.event.PageEventHandler;

public interface MyPage {
	public void doPage(HtmlElement el) throws FailingHttpStatusCodeException, MalformedURLException, IOException;
	public void addSelector(PageEventHandler handler);
}
