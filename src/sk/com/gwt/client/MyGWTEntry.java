package sk.com.gwt.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Image;

public class MyGWTEntry implements EntryPoint {

	// Widgets
	private HorizontalPanel horizontalPanel;
	private FlexTable stocksFlexTable;
	private VerticalPanel mainVerticalPanel;
	private TextBox symbolTextBox;
	private Button addStockButton;
	private Label lastUpdateLabel;
	private List<String> stockSymbols = new ArrayList<String>();
	private static final int REFRESH_INTERVAL = 50000;

	// StockQuoteService from server
	private StockQuoteServiceAsync stockQuoteService;

	public void onModuleLoad() {
		RootPanel rootPanel = RootPanel.get("stocksList");
		stockQuoteService = GWT.create(StockQuoteService.class);
		if (stockQuoteService == null) {
			Window.alert("Cannot create the stock service");
		}
		mainVerticalPanel = new VerticalPanel();
		rootPanel.add(mainVerticalPanel, 48, 91);
		mainVerticalPanel.setSize("605px", "102px");

		stocksFlexTable = new FlexTable();
		// stocksFlexTable.setStyleName("watchList");
		// Table Header looks like this
		// symbol+" "+name+" "+price+" "+change+" "+changePercentage+" "+ close+
		// " "+ open+" "+ volume +" "+high+" "+low;
		stocksFlexTable.setText(0, 0, "Symbol");
		stocksFlexTable.setText(0, 1, "Name");
		stocksFlexTable.setText(0, 2, "Last Price");
		stocksFlexTable.setText(0, 3, "Change");
		stocksFlexTable.setText(0, 4, "% Change");
		stocksFlexTable.setText(0, 5, "Close");
		stocksFlexTable.setText(0, 6, "Open");
		stocksFlexTable.setText(0, 7, "Volume");
		stocksFlexTable.setText(0, 8, "High");
		stocksFlexTable.setText(0, 9, "Low");
		stocksFlexTable.setText(0, 10, "Remove");

		// Add styles to elements in the stock list table.
		stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
		stocksFlexTable.addStyleName("watchList");

		stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
		stocksFlexTable.addStyleName("watchList");
		stocksFlexTable.getCellFormatter().addStyleName(0, 1,
				"watchListNumericColumn");
		stocksFlexTable.getCellFormatter().addStyleName(0, 2,
				"watchListNumericColumn");
		stocksFlexTable.getCellFormatter().addStyleName(0, 3,
				"watchListNumericColumn");
		stocksFlexTable.getCellFormatter()
				.addStyleName(0, 4, "watchListHeader");
		stocksFlexTable.getCellFormatter().addStyleName(0, 5,
				"watchListNumericColumn");
		stocksFlexTable.getCellFormatter().addStyleName(0, 6,
				"watchListNumericColumn");
		stocksFlexTable.getCellFormatter().addStyleName(0, 7,
				"watchListNumericColumn");
		stocksFlexTable.getCellFormatter().addStyleName(0, 8,
				"watchListNumericColumn");
		stocksFlexTable.getCellFormatter().addStyleName(0, 9,
				"watchListNumericColumn");
		stocksFlexTable.getCellFormatter().addStyleName(0, 10,
				"watchListRemoveColumn");

		stocksFlexTable.setVisible(false);
		mainVerticalPanel.add(stocksFlexTable);
		stocksFlexTable.setSize("570px", "25px");

		horizontalPanel = new HorizontalPanel();
		mainVerticalPanel.add(horizontalPanel);
		horizontalPanel.setSize("319px", "45px");

		symbolTextBox = new TextBox();
		symbolTextBox.addKeyPressHandler(new KeyPressHandler() { // Handling the
																	// Enter key
																	// when
																	// pressed
					public void onKeyPress(KeyPressEvent event) {
						if (event.getCharCode() == KeyCodes.KEY_ENTER) {
							addStock();
						}
					}
				});
		horizontalPanel.add(symbolTextBox);
		symbolTextBox.setWidth("165px");

		horizontalPanel.addStyleName("addPanel");

		addStockButton = new Button("New button");
		addStockButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addStock();
			}
		});
		addStockButton.setText("Add Stock");
		horizontalPanel.add(addStockButton);
		symbolTextBox.setFocus(true);

		lastUpdateLabel = new Label("Last Update : ");
		rootPanel.add(lastUpdateLabel, 48, 460);
		lastUpdateLabel.setSize("571px", "40px");

		Label lblStockWatcherGwt = new Label("STOCK WATCHER: Gwt Project");
		rootPanel.add(lblStockWatcherGwt, 224, 47);
		lblStockWatcherGwt.setSize("308px", "26px");

		Image image = new Image("code_logo.gif");
		rootPanel.add(image, 10, 6);
		image.setSize("186px", "48px");

		Label lblPoweredBy = new Label("Powered By");
		lblPoweredBy.setStyleName("h1");
		rootPanel.add(lblPoweredBy, 10, 551);
		lblPoweredBy.setSize("86px", "21px");

		Image image_1 = new Image("Yhoofin.gif");
		rootPanel.add(image_1, 98, 551);
		image_1.setSize("200px", "21px");

		Label lblSk = new Label("SK");
		rootPanel.add(lblSk, 594, 551);

		// Add a Timer to update Stocks values in an interval of time
		Timer timer = new Timer() {
			public void run() {
				updatestocks();
			}
		};
		timer.scheduleRepeating(REFRESH_INTERVAL);
	}

	protected void addStock() {

		final String symbol = symbolTextBox.getText().toUpperCase().trim();
		symbolTextBox.setFocus(true);

		/**
		 * Verify the entered symbol
		 */
		// Stock code must be between 1 and 10 chars that are numbers, letters,
		// or dots.
		if (!symbol.matches("^[0-9A-Z\\.]{1,10}$")) {
			Window.alert("'" + symbol + "' is not a valid symbol.");
			symbolTextBox.selectAll();
			return;
		}

		// TODO Don't add the stock if it's already in the table.
		if (stockSymbols.contains(symbol)) {
			Window.alert(symbol + " is already in the table");
			symbolTextBox.selectAll();
			return;
		}

		symbolTextBox.setText("");

		/**
		 * Get the stock quote corresponding to the symbol via rpc and with
		 * StockQuoteService
		 */
		if (stockQuoteService != null) {
			stockQuoteService.getQuote(symbol, new AsyncCallback<StockData>() {

				@Override
				/**
				 * add the symbol to the symbols table
				 * add the stock data to the FlexTable
				 */
				public void onSuccess(StockData result) {
					if (!stocksFlexTable.isVisible())
						stocksFlexTable.setVisible(true);
					// Add to the Table
					stockSymbols.add(symbol);
					addStockToTable(result, symbol);
				}

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Could not get the corresponding quote");
					return;

				}
			});
		} else {
			Window.alert("stockQuoteService null");
		}
	}

	protected void addStockToTable(StockData stock, final String symbol) {
		int row = stocksFlexTable.getRowCount();
		// Add the stock to the table.

		System.out.println("stockSymbols table " + stockSymbols + " rowCount="
				+ row);

		// symbol+" "+name+" "+price+" "+change+" "+changePercentage+" "+ close+
		// " "+ open+" "+ volume +" "+high+" "+low;

		stocksFlexTable.setText(row, 0, stock.getSymbol());
		stocksFlexTable.setText(row, 1, stock.getName());
		stocksFlexTable.setText(row, 2, "" + stock.getPrice());

		/**
		 * Changing style for the changing events
		 */
		if (stock.getChange() < -0.1f) {
			stocksFlexTable.getCellFormatter().addStyleName(row, 3,
					"negativeChange");
			stocksFlexTable.getCellFormatter().addStyleName(row, 4,
					"negativeChange");

		} else if (stock.getChange() > 0.1f) {
			stocksFlexTable.getCellFormatter().addStyleName(row, 3,
					"positiveChange");
			stocksFlexTable.getCellFormatter().addStyleName(row, 4,
					"positiveChange");
		} else {
			stocksFlexTable.getCellFormatter().addStyleName(row, 3, "noChange");
			stocksFlexTable.getCellFormatter().addStyleName(row, 4, "noChange");
		}

		stocksFlexTable.setText(row, 3, "" + stock.getChange());
		stocksFlexTable.setText(row, 4, stock.getChangePercentage());
		stocksFlexTable.setText(row, 5, "" + stock.getClose());
		stocksFlexTable.setText(row, 6, "" + stock.getOpen());
		stocksFlexTable.setText(row, 7, "" + stock.getVolume());
		stocksFlexTable.setText(row, 8, "" + stock.getHigh());
		stocksFlexTable.setText(row, 9, "" + stock.getLow());

		// TODO Add a button to remove this stock from the table.
		Button removeStockButton = new Button("x");
		removeStockButton.addStyleDependentName("remove");
		removeStockButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				int removedIndex = stockSymbols.indexOf(symbol);
				if (!stockSymbols.isEmpty())
					stockSymbols.remove(removedIndex);
				stocksFlexTable.removeRow(removedIndex + 1);
			}
		});
		stocksFlexTable.setWidget(row, 10, removeStockButton);
	}

	public void updatestocks() {

		if (stockQuoteService != null && !stockSymbols.isEmpty()) {
			stockQuoteService.getQuotes(stockSymbols,
					new AsyncCallback<List<StockData>>() {
						@Override
						public void onFailure(Throwable caught) {
							
							Window.alert("Cannot get update data" + caught);
						}

						@Override
						public void onSuccess(List<StockData> result) {
							updateStockFlextable(result);
							lastUpdateLabel.setText("Last Update : "
									+ DateTimeFormat.getMediumDateTimeFormat()
											.format(new Date()));
						}
					});
		} else {
			System.out.println("Cannot update stocks values.");
		}
	}

	protected void updateStockFlextable(List<StockData> result) {
		// TODO Auto-generated method stub
		// Make sure the stock is still in the stock table.
		for (StockData stock : result) {

			if (!stockSymbols.contains(stock.getSymbol()))
				return;

			// Get symbol's row
			int row = stockSymbols.indexOf(stock.getSymbol()) + 1;

			stocksFlexTable.setText(row, 0, stock.getSymbol());
			stocksFlexTable.setText(row, 1, stock.getName());
			stocksFlexTable.setText(row, 2, "" + stock.getPrice());

			if (stock.getChange() < -0.1f) {
				stocksFlexTable.getCellFormatter().addStyleName(row, 3,
						"negativeChange");
				stocksFlexTable.getCellFormatter().addStyleName(row, 4,
						"negativeChange");

			} else if (stock.getChange() > 0.1f) {
				stocksFlexTable.getCellFormatter().addStyleName(row, 3,
						"positiveChange");
				stocksFlexTable.getCellFormatter().addStyleName(row, 4,
						"positiveChange");
			} else {
				stocksFlexTable.getCellFormatter().addStyleName(row, 3,
						"noChange");
				stocksFlexTable.getCellFormatter().addStyleName(row, 4,
						"noChange");
			}
			stocksFlexTable.setText(row, 3, "" + stock.getChange());
			stocksFlexTable.setText(row, 4, stock.getChangePercentage());
			stocksFlexTable.setText(row, 5, "" + stock.getClose());
			stocksFlexTable.setText(row, 6, "" + stock.getOpen());
			stocksFlexTable.setText(row, 7, "" + stock.getVolume());
			stocksFlexTable.setText(row, 8, "" + stock.getHigh());
			stocksFlexTable.setText(row, 9, "" + stock.getLow());

		}
	}
}
