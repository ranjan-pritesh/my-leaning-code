package com.maxmind.locationservice;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

public class IPLookupService  {


	private LookupService lookUpService;
	private String maxMind="/home/pritesh/GeoIP.dat";

	public IPLookupService() {
		try {
			lookUpService = new LookupService(maxMind, LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE);
		} catch (Exception e) {
			System.out.println("Maxmind properties is not present");
			e.printStackTrace();
		}
	}

	/**
	 * Return null in case the lookup service fails
	 * 
	 * @param ip
	 * @return
	 */
	public  Location getGeoObject(String ip) {
		try {
			return lookUpService.getLocation(ip);
		} catch (Exception e) {
			
		}
		return null;
	}
	
	
}
