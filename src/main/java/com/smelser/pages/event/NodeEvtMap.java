package com.smelser.pages.event;

public class NodeEvtMap {
	public final String selector;
	public final PageEventHandler handler;
	public NodeEvtMap(String selector, PageEventHandler handler){
		this.selector = selector;
		this.handler = handler;
	}
}