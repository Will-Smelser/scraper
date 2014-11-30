package com.smelser.pages.validators;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

import org.eclipse.jetty.util.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.smelser.Main;


public class Laptop implements Validator{
	
	static final Logger LOG = LoggerFactory.getLogger(Laptop.class);
	
	boolean stock = false;
	String title, p, m, hd, price;
	HtmlElement node;
	
	public Laptop(HtmlElement node){
		this.node = node;
		title = ((HtmlElement)node.querySelector(".facetedResults-title a")).getTextContent().trim();
		price = node.querySelector(".pricingSummary-details dd.pricingSummary-details-final-price").getTextContent().trim();

		if(!"OUT OF STOCK".equalsIgnoreCase(node.querySelector(".facet-pricingArea .inventory").getTextContent().trim())){
			stock = true;
		}
		
		for(DomNode el : node.querySelectorAll(".facet-productArea .facetedResults-feature-list dl")){
			if(!el.hasChildNodes()) continue;
			
			String name = el.querySelector("dt").getTextContent();
			String value = el.querySelector("dd").getTextContent();
			if(name != null && value != null){
				name = name.toLowerCase().trim();
				value = value.toLowerCase().trim();
				if(name.contains("processor")){
					p = value;
				}else if(name.contains("memory")){
					m = value;
				}else if(name.contains("hard drive")){
					hd = value;
				}
			}
		}
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("\nLatop: "+title);
		sb.append("\n\tstock:     "+stock);
		sb.append("\n\tprocessor: "+p);
		sb.append("\n\tmemory:    "+m);
		sb.append("\n\tdrive:     "+hd);
		sb.append("\n\tprice:     "+price);
		
		return sb.toString();
	}
	
	private boolean checkProcessor(){
		return p.contains("i7") || p.contains("i5");
	}
	
	private boolean checkMemory(){
		return m.contains("6gb") || m.contains("6 gb") || m.contains("8gb") || m.contains("8 gb");
	}
	
	private boolean checkHd(){
		return hd.contains("ssd") || hd.contains("solid state");
	}

	public boolean valid() {
		Number value;
		try {
			value = NumberFormat.getCurrencyInstance().parse(price);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		
		if(stock && value.longValue() < 500.0 && checkProcessor() && checkMemory() && checkHd()){
			return true;
		}
		return true;
	}

	public void addCart() {
		try {
			((HtmlAnchor)node.querySelector(".facetedResults-footer a")).click();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean addCartRedirected() {
		return true;
	}
}