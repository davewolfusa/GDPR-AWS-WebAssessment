package com.americancsm.gdpr.webassess;

public enum ProductLevelEnum {
	ONE(1,3),
	TWO(4,10),
	THRE(11,15),
	FOUR(16,30),
	FIVE(31,50),
	SIX(51,100),
	SEVEN(101,300),
	EIGHT(301,500),
	NINE(501,1000),
	TEN(1001,10000000);
	
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
		double startingValue = (enumItem.ordinal() + 1.0d) * 10;
		Double relativePosition = ((employeeCount - enumItem.min)/width)*10.0d;
		
		return startingValue + relativePosition;
	}
}
