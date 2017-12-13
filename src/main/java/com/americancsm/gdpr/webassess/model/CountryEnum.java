package com.americancsm.gdpr.webassess.model;

import org.apache.commons.text.WordUtils;

public enum CountryEnum {
	// Americas
	UNITED_STATES, BRAZIL, MEXICO, COLUMBIA, ARGENTINA, CANADA, PERU, VENEZUELA, CHILI, GUATEMALA, ECUADOR,
	CUBA, BOLIVIA, HAITI, DOMINICAN_REPUBLIC, HONDURAS, PARAGUAY, EL_SALVADOR, NICARAGUA, COSTA_RICA, PANAMA, 
	PUERTO_RICO, URUGUAY, JAMAICA, TRINIDAD_AND_TOBAGO, GUYANA, SURINAME, GUADELOUPE, BAHAMAS, MARTINIQUE, 
	BELIZE, BARBADOS, FRENCH_GUIANA, SAINT_LUCIA, CURACAO, SAINT_VINCENT_AND_GRENADINES, GRENADA, ARUBA, 
	UNITED_STATES_VIRGIN_ISLANDS, ANTIGUA_AND_BARBUDA, CAYMAN_ISLANDS, SAINT_PIERRE_AND_MIQUELON, MONTSERRAT,
	FALKLAND_ISLANDS,
	
	// Asia
	CHINA, INDIA, INDONESIA, PAKISTAN, BANGLADESH, JAPAN, PHILLIPPINES, VIETNAM, IRAN, TURKEY, THAILAND, MYANMAR,
	SOUTH_KOREA, IRAQ, AFGHANISTAN, SAUDI_ARABIA, UZBEKISTAN, MALAYSIA, NEPAL, YEMEN, NORTH_KOREA, TAIWAN, 
	SRI_LANKA, SYRIA, KAZAKHSTAN, CAMBODIA, AZERBAIJAN, JORDAN, UNITED_ARAB_EMIRATES, TAJIKISTAN, ISREAL, 
	HONG_KONG, LAOS, LEBANON, KYRGYZSTAN, TURKMENISTAN, SINGAPORE, PALESTINE, OMAN, KUWAIT, GEORGIA, MONGOLIA, 
	ARMENIA, QATAR, BAHRAIN, EAST_TIMOR, BHUTAN, 
	
	// Europe
	// EU Countries
	AUSTRIA(true), BELGIUM(true), BULGARIA(true), CROATIA(true), CYPRUS(true), CZECH_REPUBLIC(true), DENMARK(true), 
	ESTONIA(true), FINLAND(true), FRANCE(true), GERMANY(true), GREECE(true), HUNGARY(true), IRELAND(true), 
	ITALY(true), LATVIA(true), LITHUANIA(true), LUXEMBOURG(true), MALTA(true), NETHERLANDS(true), POLAND(true), 
	PORTUGAL(true), ROMANIA(true), SLOVAKIA(true), SLOVENIA(true), SPAIN(true), SWEDEN(true), UNITED_KINGDOM(true), 
	
	// Non-EU European Countries
	RUSSIA, UKRAINE, BELARUS, SERBIA, SWITZERLAND, NORWAY, MOLDOVA, BOSNIA_AND_HERZEGOVINA, ALBANIA, 
	REPUBLIC_OF_MACEDONIA, MONTENEGRO, ICELAND, GUERNSEY_AND_JERSEY, ISLE_OF_MAN, ANDORRA, FARCE_ISLANDS, MONACO, 
	LIECHTENSTEIN, GILBRALTAR, SAN_MARINO, VATICAN_CITY,
	
	// Oceania
	AUSTRAILIA, PAPUA_NEW_GUINEA, NEW_ZEALAND, FIJI, SOLOMON_ISLANDS, FRENCH_POLYNESIA, NEW_CALEDONIA, VANUATU, 
	SAMOA, GUAM, KIRBATI, TONGA, FEDERATED_STATES_OF_MICRONESIA, AMERICA_SAMOA, NORTHERN_MARIANA_ISLANDS, 
	MARSHALL_ISLANDS, PALAU, COOK_ISLANDS, WALLIS_AND_FUTUNA, NAURU, TUVALU, NIUE, TOKELAU,
	
	// Africa
	EGYPT, SOUTH_AFRICA, KENYA, UGANDA, ALGERIA, SUDAN, MOROCCO, ANGOLA, MOZAMBIQUE, GHANA, MADAGASCAR, 
	IVORY_COAST, CAMEROON, NIGER, BURKINA_FASO, MALAWI, MALI, ZAMBIA, ZIMBABWE, SENEGAL, CHAD, SOMALIA, GUINEA, 
	SOUTH_SUDAN, RWANDA, TUNISIA, BENIN, BURUNDI, TOGO, SIERRA_LEONE, LIBYA, CONGO, ERTREA, LIBERIA, 
	CENTRAL_AFRICAN_REPUBLIC; 

	private boolean isEUMemberCountry;
	
    private CountryEnum() {
    	    this.isEUMemberCountry = false;
    }
    
    private CountryEnum(boolean isEUMemberCountry) {
    	    this.isEUMemberCountry = isEUMemberCountry;
    }
    
    public boolean isEUMemberCountry() {
    		return this.isEUMemberCountry;
    }
    
    public String ProperName() {
    	    return WordUtils.capitalizeFully(this.name().replaceAll("_", " "));
    }
}
