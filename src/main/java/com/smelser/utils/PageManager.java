package com.smelser.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.smelser.pages.validators.Validator;

public class PageManager {
	private HtmlPage page; 
	
	/**
	 * Items which have been validated
	 */
	private List<Validator> validators = new ArrayList<Validator>();
		
	final public void setPage(HtmlPage page){
		this.page = page;
	}
	
	final public HtmlPage getPage(){
		return this.page;
	}
	
	final public void addValidator(Validator v){
		validators.add(v);
	}
	
	final public List<Validator> getValidators(){
		List<Validator> result = new ArrayList<Validator>();
		Collections.copy(result, validators);
		
		return result;
	}
	
	final public void clearValidators(){
		validators.clear();
	}
	
	final public String getValidatorsAsString(String delimiter){
		StringBuilder sb = new StringBuilder("\n");
		for(Validator v: validators){
			sb.append(v.toString()).append("\n").append(delimiter).append("\n");
		}
		return sb.toString();
	}
}
 