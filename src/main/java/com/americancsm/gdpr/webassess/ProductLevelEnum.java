package com.americancsm.gdpr.webassess;

public enum ProductLevelEnum {
	ONE(1,4),
	TWO(5,8),
	THREE(9,16),
	FOUR(17,32),
	FIVE(33,64),
	SIX(65,128),
	SEVEN(1129,256),
	EIGHT(257,512),
	NINE(513,1024),
	TEN(1025,10000000);
	
	private int min;
	private int max;
	
	private ProductLevelEnum(int min, int max) {
		this.min = min;
		this.max = max;
	}
	private int getMin() { return this.min; }
	private int getMax() { return this.max; }
	
	public static ProductLevelEnum create(int productTypeCount) {
		ProductLevelEnum result = FIVE;
		for (ProductLevelEnum enumInstance : ProductLevelEnum.values()) {
			if (enumInstance.getMin() <= productTypeCount && enumInstance.getMax() >= productTypeCount) {
				result = enumInstance;
				break;
			}
		}
		return result;
	}
	
	public static Double computeRanking(int employeeCount) {
		ProductLevelEnum enumItem = ProductLevelEnum.create(employeeCount);
		double width = enumItem.max - enumItem.min + 1.0d;
		double startingValue = (enumItem.ordinal() + 0.0d) * 10;
		Double relativePosition = ((employeeCount - enumItem.min)/width)*10.0d;
		
		return startingValue + relativePosition;
	}
}
