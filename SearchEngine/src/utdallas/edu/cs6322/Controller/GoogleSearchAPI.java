package utdallas.edu.cs6322.Controller;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GoogleSearchAPI {
	
	public static ArrayList<String> googleSearch (String search) {
	    /*String google = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
	    String search = "mexican food in dallas";
	    String charset = "UTF-8";

	    URL url = new URL(google + URLEncoder.encode(search, charset));
	    Reader reader = new InputStreamReader(url.openStream(), charset);
	    GoogleResults results = new Gson().fromJson(reader, GoogleResults.class);

	    // Show title and URL of 1st result.
	    System.out.println(results.getResponseData().getResults().get(0).getTitle());
	    System.out.println(results.getResponseData().getResults().get(0).getUrl());*/
		ArrayList<String> list = new ArrayList<String>();
		
		try{
		
		String google = "http://www.google.com/search?q=";
		//String search = "indian restaurants in dallas";
		String charset = "UTF-8";
		String userAgent = "ExampleBot 1.0 (+http://example.com/bot)"; // Change this to your company's name and bot homepage!

		Elements links = Jsoup.connect(google + URLEncoder.encode(search, charset)).userAgent(userAgent).get().select("li.g>h3>a");

		for (Element link : links) {
		    String title = link.text();
		    String url = link.absUrl("href"); // Google returns URLs in format "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".
		    url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");

		   if (!url.startsWith("http")) {
		        continue; // Ads/news/etc.
		    }

		    System.out.println("Title: " + title);
		    System.out.println("URL: " + url);		    
		    String data=url+" "+title;
		    list.add(data);
		}
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return list;
		
	}
	
	public static void main(String[] args) {
		
		googleSearch("indian restaurants in dallas");
	}

}
