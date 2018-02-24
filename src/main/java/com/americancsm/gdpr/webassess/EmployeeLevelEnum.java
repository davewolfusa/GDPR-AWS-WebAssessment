package com.americancsm.gdpr.webassess;

public enum EmployeeLevelEnum {
	ONE(1,100),
	TWO(101,500),
	THREE(501,1000),
	FOUR(1001,5000),
	FIVE(5001,10000),
	SIX(10001,50000),
	SEVEN(50001,100000),
	EIGHT(100001,500000),
	NINE(1000001,3000000),
	TEN(3000001,10000000);
	
	private int min;
	private int max;
	
	private EmployeeLevelEnum(int min, int max) {
		this.min = min;
		this.max = max;
	}
	private int getMin() { return this.min; }
	private int getMax() { return this.max; }
	
	public static EmployeeLevelEnum create(int employeeCount) {
		EmployeeLevelEnum result = TEN;
		for (EmployeeLevelEnum enumInstance : EmployeeLevelEnum.values()) {
			if (enumInstance.getMin() <= employeeCount && enumInstance.getMax() >= employeeCount) {
				result = enumInstance;
			}
		}
		return result;
	}
	
	public static Double computeRanking(int employeeCount) {
		EmployeeLevelEnum enumItem = EmployeeLevelEnum.create(employeeCount);
		double width = enumItem.max - enumItem.min + 1.0d; 
		double startingValue = (enumItem.ordinal() + 0.0d) * 10.0d;
		Double relativePosition = ((employeeCount - enumItem.min)/width)*10.0d;
		
		return startingValue + relativePosition;
	}
}
