package sk.com.gwt.client;

import java.util.List;


import com.google.gwt.user.client.rpc.AsyncCallback;

public interface StockQuoteServiceAsync {

	void getQuote(String symbol, AsyncCallback<StockData> callback);

	void getQuotes(List<String> symbols, AsyncCallback<List<StockData>> callback);

}
