package com.smelser.pages.validators;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;

public class Laptop implements Validator {

	static final Logger LOG = LoggerFactory.getLogger(Laptop.class);
	static final String DATA_FILE = "laptops.csv";
	
	static PrintWriter writer = null;
	static FileReader reader = null;
	
	static Set<String> hashes = new HashSet<String>();

	static {
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(DATA_FILE,true)));
			reader = new FileReader(DATA_FILE);
			
			BufferedReader br = new BufferedReader(reader);
			
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				if(parts.length > 0)
					hashes.add(parts[0].trim());
			}
		} catch (IOException e) {
			LOG.error("Failed to initialize "+DATA_FILE,e);
		}
	}

	private static enum Values {

		I3(175.0), I5(250.0), I7(350), NEW(125.0), USED(0.0), GB16(350.0), GB8(
				150.0), GB6(85.0), GB4(0.0), SSD(125.0), HD7200(50.0), HD5400(
				0.0),

		UNKNOWN(0.0);

		private Number value;

		Values(Number value) {
			this.value = value;
		}
	}

	boolean stock = false;
	String title, p, m, hd, price;
	HtmlElement node;

	public Laptop(HtmlElement node) {
		this.node = node;
		title = ((HtmlElement) node.querySelector(".facetedResults-title a"))
				.getTextContent().trim();
		price = node
				.querySelector(
						".pricingSummary-details dd.pricingSummary-details-final-price")
				.getTextContent().trim();

		if (!"OUT OF STOCK".equalsIgnoreCase(node
				.querySelector(".facet-pricingArea .inventory")
				.getTextContent().trim())) {
			stock = true;
		}

		for (DomNode el : node
				.querySelectorAll(".facet-productArea .facetedResults-feature-list dl")) {
			if (!el.hasChildNodes())
				continue;

			String name = el.querySelector("dt").getTextContent();
			String value = el.querySelector("dd").getTextContent();
			if (name != null && value != null) {
				name = name.toLowerCase().trim();
				value = value.toLowerCase().trim();
				if (name.contains("processor")) {
					p = value;
				} else if (name.contains("memory")) {
					m = value;
				} else if (name.contains("hard drive")) {
					hd = value;
				}
			}
		}
		
		String hash = this.hash();
		if(!hashes.contains(hash))
			write();
		hashes.add(this.hash());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nLatop: " + title);
		sb.append("\n\tstock:     " + stock);
		sb.append("\n\tprocessor: " + p);
		sb.append("\n\tmemory:    " + m);
		sb.append("\n\tdrive:     " + hd);
		sb.append("\n\tprice:     " + price);

		return sb.toString();
	}

	private Number checkProcessor() {
		if (p.contains("i3")) {
			return Values.I3.value;
		} else if (p.contains("i5")) {
			return Values.I5.value;
		} else if (p.contains("i7")) {
			return Values.I7.value;
		} else {
			LOG.info("Failed to match processor type: " + p);
		}
		return Values.UNKNOWN.value;
	}

	private Number checkMemory() {
		if (m.contains("4gb") || m.contains("4 gb")) {
			return Values.GB4.value;
		} else if (m.contains("6gb") || m.contains("6 gb")) {
			return Values.GB6.value;
		} else if (m.contains("8gb") || m.contains("8 gb")) {
			return Values.GB8.value;
		} else if (m.contains("16gb") || m.contains("16 gb")) {
			return Values.GB16.value;
		} else {
			LOG.info("Failed to match memory: " + m);
		}
		return Values.UNKNOWN.value;
	}

	private Number checkHd() {
		Number value = Values.UNKNOWN.value;

		if (hd.contains("ssd") || hd.contains("solid state"))
			value = value.longValue() + Values.SSD.value.longValue();

		if (hd.contains("7200"))
			value = value.longValue() + Values.HD7200.value.longValue();

		if (hd.contains("5400"))
			value = value.longValue() + Values.HD5400.value.longValue();

		return value;
	}

	private Number checkTitle() {
		if (title.contains("new"))
			return Values.NEW.value;
		return Values.USED.value;

	}

	public boolean valid() {
		Number value;
		try {
			value = NumberFormat.getCurrencyInstance().parse(price);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}

		Number cvalue = value();

		LOG.info("Value: " + value + ", Computed Value: " + cvalue);

		return (value.longValue() <= cvalue.longValue() + 10.0);
	}

	private Number value() {
		return checkTitle().longValue() + checkHd().longValue()
				+ checkMemory().longValue() + checkProcessor().longValue();
	}
	
	private void write(){
		StringBuilder laptop = new StringBuilder(clean(title)).append(",")
				.append(clean(p)).append(",")
				.append(clean(m)).append(",")
				.append(clean(hd)).append(",")
				.append(clean(price)).append(",")
				.append(value());
		writer.println(hash()+","+laptop.toString());
		writer.flush();
	}
	
	private String clean(String input){
		return input.replace(",", ";");
	}

	private String hash() {
		StringBuilder laptop = new StringBuilder(title).append(p).append(m)
				.append(hd).append(price).append(value());
		
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("md5");
			
			byte[] hashedBytes = digest.digest(laptop.toString().getBytes("UTF-8"));

			byte[] bytesEncoded = Base64.encodeBase64(hashedBytes);

			return new String(bytesEncoded);
			
		} catch (NoSuchAlgorithmException e) {
			LOG.error("Error creating hash",e);
		} catch (UnsupportedEncodingException e) {
			LOG.error("Error creating hash",e);
		}
		
		return null;

	}
}
