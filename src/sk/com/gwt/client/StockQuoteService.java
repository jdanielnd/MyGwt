package sk.com.gwt.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;

@RemoteServiceRelativePath("stockquote")
public interface StockQuoteService extends RemoteService {
	public StockData getQuote(String symbol);
	
	public List<StockData> getQuotes(List<String> symbols);

}
