package sk.com.gwt.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Iterator;

import sk.com.gwt.client.StockData;


public class DataAcquisition {
	private String mainAddress= "http://finance.yahoo.com/d/quotes.csv?";
	private String stocksList="s=";
	private String defaultOptions="&f=d1l1c1p2nohgpvx"; //Date Last Trade Price change Change% Name Open High Low Close Volume Stock Exchange
	private String address;
	private List<String> symbols;



	public DataAcquisition(List<String> symbols){
		this.symbols=symbols;

		for (String stock : symbols){
			stocksList+=stock;
			stocksList+="+";
		}
		//Removing the last "+" symbol
		stocksList= stocksList.substring(0, stocksList.length()-1);
		System.out.println(stocksList);
		address=mainAddress.concat(stocksList).concat(defaultOptions);
		System.out.println(address);

	}

	public DataAcquisition(String symbol){
		this.symbols= new ArrayList<String>();
		this.symbols.add(symbol);		
		stocksList+=symbol;
		address=mainAddress.concat(stocksList).concat(defaultOptions);
		System.out.println(address);

	}

	public  List<StockData> getQuotes(){
		List<StockData> stocks=null;
		if (address!=null){
			try {
				URL url = new URL(address);
				BufferedReader buff = new BufferedReader(new InputStreamReader(url.openStream()));
				//Parsing the Stream containing the CSV files.
				stocks= parseCSV(buff);
				printData(stocks);

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {

				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		return stocks;

	}

	private void printData(List<StockData> parseCSV) {

		System.out.println("symbol  name  price  change   changePercentage   close  open   volume  high  low");
		for (StockData stock : parseCSV){
			System.out.println(stock);
		}

	}

	private  List<StockData> parseCSV(BufferedReader buffer) throws IOException, ParseException{
		String line;
		String open, high, low, close, volume;
		List<StockData> stocks=new ArrayList<StockData>();
		Iterator<String> it = this.symbols.iterator();

		while ((line =buffer.readLine())!=null ){

			StringTokenizer st = new StringTokenizer(line, ",");
			StockData sd=new StockData();

			sd.setSymbol(it.next());
			String date= st.nextToken(); 
			sd.setDate(date); 
			System.out.println("Date in the string is "+date);
			////Symbol Date LastTradePrice change ChangePercentage Name Open High Low Close Volume Stock Exchange
		    String lasttradePrice = st.nextToken();
		    if (!lasttradePrice.equals("N/A"))
		    	sd.setPrice(Double.parseDouble(lasttradePrice));
		    
			String change= st.nextToken();
			if (!(change.equals("N/A")))
				sd.setChange(Double.parseDouble(change));
			
			String changePercent = st.nextToken();
			System.out.println("Change Percentage = "+changePercent);
			sd.setChangePercentage(changePercent);
			
			sd.setName(st.nextToken());
			
			open=st.nextToken();
			high=st.nextToken();
			low=st.nextToken();
			close=st.nextToken();
			volume=st.nextToken();

			if (!(open.equals("N/A"))) 
				sd.setOpen(Double.parseDouble(open));
			if (!(high.equals("N/A")))
				sd.setHigh(Double.parseDouble(high));
			if (!(low.equals("N/A")))
				sd.setLow(Double.parseDouble(low));
			if (!(close.equals("N/A")))
				sd.setClose(Double.parseDouble(close));
			if (!(volume.equals("N/A")))
				sd.setVolume(Double.parseDouble(volume));
			stocks.add(sd);
		}
	return stocks;
	}

	public static void main(String[] args){

		List<String> stocks = new ArrayList<String>();
		stocks.add("AAPL"); stocks.add("FAS");
		DataAcquisition da= new DataAcquisition(stocks);
		da.getQuotes();
		System.out.println("");
		DataAcquisition da1=new DataAcquisition("GOOG");
		da1.getQuotes();
	}
}
