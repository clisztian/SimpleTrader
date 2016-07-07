package ch.mdfa.timeseries;



//Holds Time series data and object V

public class TimeSeriesEntry<V> {
	public TimeSeriesEntry(String timeStamp2, V value2) {
		this.timeStamp = timeStamp2;
		this.value = value2;
	}
	private String timeStamp;
	private V value;
	
	public V getValue()
	{return value;}
	
	public String getTimeStamp()
	{return timeStamp;}
}
