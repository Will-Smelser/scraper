package com.smelser.utils;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class PageManager {
	private HtmlPage page;

	public PageManager(HtmlPage page){
		this.page = page;
	}
		
	final public void setPage(HtmlPage page){
		this.page = page;
	}
	
	final public HtmlPage getPage(){
		return this.page;
	}
}
 