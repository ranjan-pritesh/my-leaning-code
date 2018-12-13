package com.maxmind.locationservice;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.maxmind.geoip.Location;

public class GetLocationPool implements Callable<CountDetails> {

	List<UserDetails> listofUserIP;
	public File filetoWrite = null;
	CountDetails count;
	BufferedWriter bw = null;
	FileWriter fw = null;
	long found = 0;
	long notFound = 0;
	long invalidIP = 0;
	long filecount = 0;
	long threadid = 0;
	public static final String SEPARATOR = "\t";
	IPLookupService ipLookupService;

	GetLocationPool(List<UserDetails> listofUserIP, long threadid, IPLookupService ipLookupService) {
		this.listofUserIP = listofUserIP;
		this.threadid = threadid;
		this.ipLookupService = ipLookupService;
	}

	@Override
	public CountDetails call() throws Exception {
		System.out.println("started threadid " + threadid);
		count = new CountDetails();
		String str = "/home/pritesh/locationtracker";

		List<String> listofOutput = new ArrayList<>();

		filetoWrite = new File(str + "/result" + "/threadfile" + System.currentTimeMillis() + "_" + threadid + ".txt");

		try {
			fw = new FileWriter(filetoWrite);
			bw = new BufferedWriter(fw);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (UserDetails ud : listofUserIP) {
			if (isValidIp(ud.getUserip())) {
				String res=getLocation(ud);
				listofOutput.add(res);
			} else {
				listofOutput.add(ud.getUsercookie() + SEPARATOR + ud.getUserip() + SEPARATOR + "invalid ip:");
				++count.notfound;
			}
			if (listofOutput.size() % 1000 == 0) {
				WriteinFile(listofOutput);
				listofOutput = new ArrayList<>();
			}

		}

		if (listofOutput.size() > 0) {
			WriteinFile(listofOutput);
			listofOutput = new ArrayList<>();
		}

		try {
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("end threadid " + threadid + " count : " + (count.found + count.notfound));
		// System.out.println("found" + count.getFound() + "invalid" +
		// count.getInvalid() + "not found" + count.getNotfound());
		count.setThreadid(threadid);
		System.out.println();
		return count;
	}

	public String getLocation(UserDetails ud) {
		try {
			StringBuffer result = new StringBuffer();
			String ip = ud.getUserip();
			String user = ud.getUsercookie();
			Location location = ipLookupService.getGeoObject(ip);
			
			if (location != null) {
				result.append(user).append(SEPARATOR).append(ip).append(SEPARATOR).append("Found:").append(SEPARATOR)
						.append(location.countryCode).append(SEPARATOR).append(location.countryName).append(SEPARATOR)
						.append(location.region).append(SEPARATOR).append(location.city).append(SEPARATOR)
						.append(location.postalCode).append(SEPARATOR).append(location.area_code);
				++count.found;
				return result.toString();
			} else {
				String[] ipCheck = ip.split("\\.");
				if (ipCheck.length < 4) {
					ip = ip + ".0";
					ud.setUserip(ip);
					return getLocation(ud);
				}

				result.append(user).append(SEPARATOR).append(ip).append(SEPARATOR).append("NA:");
				++count.notfound;
				return result.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return "null";

	}

	public void WriteinFile(List<String> listofResult) {
		try {
			for (String rs : listofResult) {
				bw.write(rs);
				bw.newLine();
			}
			// System.out.println("write in file |SUCCESS| by threadid: " + threadid);
		} catch (IOException e) {

			e.printStackTrace();

		}
	}

	public boolean isValidIp(String ip) {
		/*
		 * pattern = Pattern.compile(IPADDRESS_PATTERN); matcher = pattern.matcher(ip);
		 */
		return true;

	}

}
