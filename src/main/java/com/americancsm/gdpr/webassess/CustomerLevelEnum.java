package com.americancsm.gdpr.webassess;

public enum CustomerLevelEnum {
	ONE(1,100),
	TWO(101,500),
	THREE(501,1000),
	FOUR(1001,5000),
	FIVE(5001,10000),
	SIX(10001,20000),
	SEVEN(20001, 100000),
	EIGHT(100001,1000000),
	NINE(1000001,10000000),
	TEN(10000001,10000000000l);
	
	private long min;
	private long max;
	
	private CustomerLevelEnum(long min, long max) {
		this.min = min;
		this.max = max;
	}
	
	private long getMin() { return this.min; }
	private long getMax() { return this.max; }
	
	public static CustomerLevelEnum create(long customerCount) {
		CustomerLevelEnum result = FIVE;
		for (CustomerLevelEnum enumInstance : CustomerLevelEnum.values()) {
			if (enumInstance.getMin() <= customerCount && enumInstance.getMax() >= customerCount) {
				result = enumInstance;
				break;
			}
		}
		return result;
	}
	
	public static Double computeRanking(int customerCount) {
		CustomerLevelEnum enumItem = CustomerLevelEnum.create(customerCount);
		double width = enumItem.max - enumItem.min + 1.0d; 
		double startingValue = (enumItem.ordinal() + 0d) * 10d;
		Double relativePosition = ((customerCount - enumItem.min)/width)*10d;
		
		return startingValue + relativePosition;
	}
}
