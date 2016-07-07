package ch.mdfa.signal;


public enum Side {
    LONG, SHORT, NEUTRAL;

    public static final Side of(long position) {
        if (position > 0) {
            return LONG;
        }
        
        if (position < 0) {
            return Side.SHORT;
        }
        
        return NEUTRAL;
    }
    
    public boolean isLong() {
    	return this == LONG;
    }
    
    public boolean isShort() {
    	return this == SHORT;
    }
    
    public boolean isNeutral() {
    	return this == NEUTRAL;
    }
    
    public boolean isOpposite(Side other) {
    	return this.isLong() && other.isShort() || this.isShort() && other.isLong();
    }
}