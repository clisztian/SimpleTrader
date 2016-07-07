package ch.mdfa.timeseries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class TimeSeries<V> extends ArrayList<TimeSeriesEntry<V>> {
	
	private static final long serialVersionUID = 4071035106419033490L;

    @SuppressWarnings("rawtypes")
    public static final TimeSeries EMPTY_SERIES = new TimeSeries<>(0);
	
	public TimeSeries() {
		super();
	}

	public TimeSeries(Collection<? extends TimeSeriesEntry<V>> c) {
		super(c);
	}

	public TimeSeries(int initialCapacity) {
		super(initialCapacity);
	}
	
	public void add(String timeStamp, V value) {
		add(new TimeSeriesEntry<V>(timeStamp, value));
	}

    @SuppressWarnings("unchecked")
    public static final <T> TimeSeries<T> empty() {
        return EMPTY_SERIES;
    }
    
    public TimeSeriesEntry<V> last() {
        return get(size() - 1);
    }

    @SuppressWarnings("unchecked")
	@Override
    public String toString() {
        V it = (V) iterator();
        if (! ((Iterator<TimeSeriesEntry<V>>) it).hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (;;) {
            V e = (V) ((Iterator<TimeSeriesEntry<V>>) it).next();
            sb.append(e);
            if (! ((Iterator<TimeSeriesEntry<V>>) it).hasNext())
                return sb.append(']').toString();
            sb.append('\n').append(' ');
        }
    }

}