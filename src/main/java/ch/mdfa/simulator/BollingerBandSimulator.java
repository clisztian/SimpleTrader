package ch.mdfa.simulator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csvreader.CsvReader;

import ch.algotrader.entity.Position;
import ch.algotrader.entity.security.Security;
import ch.algotrader.entity.strategy.CashBalance;
import ch.algotrader.entity.strategy.Strategy;
import ch.algotrader.entity.trade.LimitOrder;
import ch.algotrader.enumeration.Currency;
import ch.algotrader.enumeration.Side;
import ch.algotrader.simulation.Simulator;
import ch.algotrader.simulation.SimulatorImpl;
import ch.mdfa.signal.BollingerBand;

public class BollingerBandSimulator {

	long ONELOT = 100000;
	
	BollingerBand signal;

	double price; 
	String date_stamp;
	
	double currentMasterSignal = 0;
	double previousMasterSignal = 0;

    ArrayList<Double> daily_returns = new ArrayList<Double>();
    private static final Logger LOGGER = LoggerFactory.getLogger("ch.mdfa.simulator.BollingerBandSimulator");
    
    
    public static void main(String args[]) 
    {
    	
    	/*Set BollingerBand parameters*/
    	int BandLength = 20;
    	double StDev = 1.5;
    	
    	/*Set data file name */
    	String dataFile = "data/EUR.USD.csv";
    	BollingerBandSimulator bb30 = new BollingerBandSimulator("EURUSD", "BAND_30", BandLength, StDev);   	
    	
    	/*Simulate and print results to logger*/
    	bb30.simulateStrategy(dataFile);
	
    }
    
    
    /* Constructor that takes in parameters for BollingerBand */
    public BollingerBandSimulator(String SecurityName, String SignalName, int L, double sd) {
    	signal = new BollingerBand(SecurityName, SignalName, L, sd);
    }
    
    
    
    

	
	public void simulateStrategy(String dataFiles)
	{
		
		/* Set strategy settings and define order and simulator*/
		Security eurusd = new Security("EUR",Currency.USD);
		Strategy bollinger = new Strategy("BollingerBand");
		LimitOrder order;
		
		Simulator simulator = new SimulatorImpl();
		simulator.createCashBalance("STRATEGY", Currency.USD, new BigDecimal(1000000.0));
		
		try{
			
		 CsvReader marketDataFeed = new CsvReader(dataFiles);
		 marketDataFeed.readHeaders();

		 while (marketDataFeed.readRecord()) {
			 
			double price = (new Double(marketDataFeed.get("close"))).doubleValue();
			String date_stamp = marketDataFeed.get("ProductName");
		    daily_returns.add(price);
       
            previousMasterSignal = currentMasterSignal; 
      	    currentMasterSignal = signal.computeSignal(date_stamp, price);
      	    
      	    if(currentMasterSignal != previousMasterSignal)
      	    {
      	    	 
      	      if(currentMasterSignal > previousMasterSignal) { //Buy order
      	    	  order = new LimitOrder(Side.BUY, ONELOT, eurusd, bollinger, new BigDecimal(price));
      	    	  simulator.sendOrder(order);
      	      }
      	      else if(currentMasterSignal < previousMasterSignal) { //Sell order
      	    	  order = new LimitOrder(Side.SELL, ONELOT, eurusd, bollinger, new BigDecimal(price));
      	    	  simulator.sendOrder(order);
      	      }
      	      if(currentMasterSignal == 0) { //liquidated position
      	    	    
      	    	    //log cash balance
      	    	    CashBalance bal = simulator.findCashBalanceByStrategyAndCurrency(bollinger.getName(), Currency.USD);
      	    		LOGGER.info(bal.toString());
      	      }
       
      	      //Log current position
      	      Position position = simulator.findPositionByStrategyAndSecurity(bollinger.getName(), eurusd);
   	    	  LOGGER.info(position.toString());	

      	     }
      	      
           }
		}
	    catch (FileNotFoundException e) { e.printStackTrace(); } 
		catch (IOException e) { e.printStackTrace(); }
        
        
	}
		
		
}
