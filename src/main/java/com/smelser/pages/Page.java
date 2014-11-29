package com.smelser.pages;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public interface Page {
	public void waitOnComplete();

	public void doPage() throws FailingHttpStatusCodeException, MalformedURLException, IOException;
	
	public HtmlPage getPage();
	
	public void setPage(HtmlPage page);
}
