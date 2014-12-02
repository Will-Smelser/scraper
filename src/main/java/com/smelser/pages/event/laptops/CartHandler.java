package com.smelser.pages.event.laptops;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.smelser.pages.event.EmptyEventHandler;
import com.smelser.pages.event.PageEventHandler;
import com.smelser.pages.validators.Laptop;
import com.smelser.pages.validators.Validator;
import com.smelser.utils.PageManager;

public class CartHandler extends EmptyEventHandler implements PageEventHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(CartHandler.class);
	
	private static final String SELECTOR_ADD_CART = ".facetedResults-footer a";
	
	
	public CartHandler(){
	}
	
	public String getSelector(){
		return SELECTOR_ADD_CART;
	}
	
	public boolean foundNode(HtmlElement node) {
		try{
			HtmlPage p = ((HtmlAnchor)node).click();
			
			//failed to add to cart
			if(!p.getUrl().toString().toLowerCase().contains("cart")){
				LOG.info("Failed to add to cart, skipping.  Result page: "+p.getUrl());
				return true;
			}
			
			Thread.sleep(5000);
			
			return true;
			
		}catch(Exception e){
			LOG.error("Exception clicking add to cart link",e);
			LOG.error(node.asXml());
		}
		return true;
	}

}
