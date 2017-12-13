package com.americancsm.gdpr.webassess.model;

import org.apache.commons.text.WordUtils;

public enum IAASProviderEnum {
	AMAZON_WEB_SERVICES, GOOGLE_CLOUD_PLATFORM, MICROSOFT_AZURE;
    
    public String ProperName() {
    	    return WordUtils.capitalizeFully(this.name().replaceAll("_", " "));
    }
}
