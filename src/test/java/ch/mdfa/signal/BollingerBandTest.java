package ch.mdfa.signal;

import org.junit.Assert;
import org.junit.Test;

public class BollingerBandTest {

	@Test
	public void test() {
		
		double currentMasterSignal;
		
		BollingerBand testbb = new BollingerBand("EURUSD", "BB1", 3, 1);
		
		currentMasterSignal = testbb.computeSignal("2016-07-07", 2);
		Assert.assertEquals(0.0, currentMasterSignal, .00001);
		
		currentMasterSignal = testbb.computeSignal("2016-07-08", 4);
		currentMasterSignal = testbb.computeSignal("2016-07-09", 6);
		currentMasterSignal = testbb.computeSignal("2016-07-10", 8);
		
		Assert.assertEquals(6.0, testbb.getLastMidBand(), .00001);
		
	}

}
