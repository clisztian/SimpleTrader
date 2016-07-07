package ch.mdfa.signal;

import ch.mdfa.timeseries.TimeSeries;

import org.apache.commons.math.stat.descriptive.moment.*;

public class BollingerBand {

	/* BollingerBand signal values */
	

	int L;          //length of MA
	double StD;     //StDev of envelope
	
	Variance myVar; //variance tracker
	String SecurityName;
	String SignalName;
	
    double PreviousSignal = 0;
	Side CurrentSide = Side.NEUTRAL;

    TimeSeries<Double> PriceSeries;
    TimeSeries<Double> LowBand;
    TimeSeries<Double> UpperBand;
    TimeSeries<Double> MidBand;
	
	
	/*------------------------------------------ 
	 * Bollinger Band 
	 * 
	 *  Input: SecurityName,
	 *         SignalName,
	 *         Length of MA,
	 *         Standard dev multiplier
	 *--------------------------------------------*/
	
	public BollingerBand(String SecurityName, String SignalName, int L, double sd) {
		
		this.L = L;
		this.StD = sd;	
		this.SecurityName = SecurityName;
		this.SignalName = SignalName;
		
		myVar =new Variance();
		PriceSeries = new TimeSeries<Double>();
	    LowBand = new TimeSeries<Double>();
	    UpperBand = new TimeSeries<Double>();	
	    MidBand = new TimeSeries<Double>();		
	}
		
	/*-------------------------------------
	 * 
	 * Sets the new price computes, computes the band, and the signal 
	 * 
	 * Input: TimeStamp
	 *        Last Close price (midprice)
	 * Output: 
	 * 0 is neutral (close)
	 * 1 is buy (long)
	 * -1 is sell (short)
	 * 
	 *-------------------------------------*/
	
	public double computeSignal(String time, double midPrice) {
		
		double CurrentSignal = 0;
		
		// Computes the Bollinger band at new midPrice
		setReturn(time, midPrice);
		
		if(MidBand.size() > 0)
		{
			
			/*Position Entry rules*/
			if(midPrice < LowBand.last().getValue() && CurrentSide == Side.NEUTRAL) {
				CurrentSide = Side.LONG;
				CurrentSignal = 1.0;
			}
			else if(midPrice > UpperBand.last().getValue() && CurrentSide == Side.NEUTRAL) {
				CurrentSide = Side.SHORT;
				CurrentSignal = -1.0;
			}
			else if(midPrice > MidBand.last().getValue() && CurrentSide == Side.LONG) {
				CurrentSide = Side.NEUTRAL;
				CurrentSignal = 0;
			}
			else if(midPrice < MidBand.last().getValue() && CurrentSide == Side.SHORT){
				CurrentSide = Side.NEUTRAL;
				CurrentSignal = 0;				
			}	
		}
		
		return CurrentSignal;
	}
	
	
	/* Sets the new price and Computes the new upper,lower,mid band */
	private void setReturn(String time, double midPrice) {

		PriceSeries.add(time,midPrice);
		if(PriceSeries.size() > L) 
		{
			double mean = 0;
			double[] samples = new double[L];
        	for(int j = 0; j < L; j++)
        	{
        		samples[j] = PriceSeries.get(PriceSeries.size() - 1 - j).getValue();
        		mean = mean + samples[j];
        	}        	
        	mean = mean/L;
        	double var = myVar.evaluate(samples, 0, L);
        	
        	LowBand.add(time, mean - StD*var);
        	UpperBand.add(time, mean + StD*var);
        	MidBand.add(time, mean);
		}
	}
	
	public int getL() {
		return L;
	}
	
}
