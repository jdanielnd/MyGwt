package sk.com.gwt.server;

import java.util.List;

import sk.com.gwt.client.StockData;
import sk.com.gwt.client.StockQuoteService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;


public class StockQuoteServiceImpl extends RemoteServiceServlet implements StockQuoteService {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2878639994039629830L;


	public StockData getQuote(String symbol){
		DataAcquisition da=new DataAcquisition(symbol);
		return da.getQuotes().get(0);
		}
	
	
	public List<StockData> getQuotes(List<String> quotesSymbols){
		DataAcquisition da= new DataAcquisition(quotesSymbols);
		return da.getQuotes();		
		
	}
	    

}
