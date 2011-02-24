package sk.com.gwt.client;


public class StockData implements java.io.Serializable{
	
	private static final long serialVersionUID = -4261228897187955772L;
	private String symbol;
	private String date;
	private double change;
	private double price;
	private String changePercentage; 
	private String name;
	private double open;
	private double high;
	private double low;
	private double close;
	private double volume;
	private String stockXchange;
   

	public StockData(){
		
	}

	public StockData(String symbol, String date, double open, double high,
			double low, double close, double volume, String stockXchange) {
		super();
		this.symbol = symbol;
		this.date = date;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
		this.stockXchange = stockXchange;
	}

	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getChangePercentage() {
		return changePercentage;
	}

	public void setChangePercentage(String changePercentage) {
		this.changePercentage = changePercentage;
	}

	public String getSymbol() {
		return symbol;
	}
	
	public double getChange() {
		return change;
	}

	public void setChange(double change) {
		this.change = change;
	}
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public String getStockXchange() {
		return stockXchange;
	}

	public void setStockXchange(String stockXchange) {
		this.stockXchange = stockXchange;
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	//Print in this order Date LastTradePrice change ChangePercentage Name Open High Low Close Volume Stock Exchange
	public String toString (){
			
		return  symbol+" "+name+" "+price+" "+change+" "+changePercentage+" "+ close+ " "+ open+" "+ volume +" "+high+" "+low;
	}
	
	

}
