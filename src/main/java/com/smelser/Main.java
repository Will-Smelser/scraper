package com.smelser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.smelser.pages.OutletLaptopPage;
import com.smelser.pages.MyPage;
import com.smelser.utils.CookieToJSON;
import com.smelser.utils.Gmail;
import com.smelser.utils.PageManager;

public class Main {

	static final Logger LOG = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException {
		
		LOG.info("Starting up Lenovo application.");

	    ExecutorService pool = Executors.newFixedThreadPool(1);
	    Runnable outletLaptopPage = new OutletLaptopPage();
	    
	    pool.execute(outletLaptopPage);

	    pool.shutdown();
	    while(!pool.awaitTermination(24L,TimeUnit.HOURS)){
	    	LOG.info("Application still running...");
	    }
	    
	    LOG.info("Application closing");
	    
	}

}
