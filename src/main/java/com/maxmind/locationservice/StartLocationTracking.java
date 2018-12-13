/*
 * author : pritesh ranjan
 * date 5th Nov 2018
 */
package com.maxmind.locationservice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class StartLocationTracking {

	static BufferedWriter bw = null;
	static FileWriter fw = null;
	public static File filetoWrite = null;
	public static long final_found = 0;
	public static long final_notFound = 0;
	public static long final_invalidIP = 0;
	static long readcount = 0;
	static long id = 1;
	private static IPLookupService ipLookupService = new IPLookupService();
	static List<UserDetails> listofUserIP = new ArrayList<UserDetails>();

	public static void main(String[] args) {

		long startTime = System.currentTimeMillis();
		try {
			readFromFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			System.out.println("Total time Taken ::" + (System.currentTimeMillis() - startTime) + " ms.");
		}
	}

	public static void readFromFile() throws FileNotFoundException, IOException {
		String str = "/home/pritesh/locationtracker";

		File file = new File(str + "/listOfuserIpwithCity0inTnP7Nov.txt");

		String tempArray[] = null;
		String finalIp = null;
		String finalUser = null;

		// ExecutorService executor = Executorcount.notfounds.newFixedThreadPool(100);
		ExecutorService es = new ThreadPoolExecutor(100, 100, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(100*4), new ThreadPoolExecutor.CallerRunsPolicy());

		BufferedReader br = new BufferedReader(new FileReader(file));
		List<Future<CountDetails>> futureList = new ArrayList<Future<CountDetails>>();

		String st;
		while ((st = br.readLine()) != null) {
			tempArray = st.split("\\t");
			finalIp = tempArray[3].trim();
			finalUser = tempArray[1].trim();

			UserDetails userDetails=new UserDetails();
			userDetails.setUsercookie(finalUser);
			userDetails.setUserip(finalIp);
			
			readcount++;
			listofUserIP.add(userDetails);

			if (readcount % 20000 == 0) {
				System.out.println("submitting task to executor with id " + id + " size of listofUserIP  >" +listofUserIP.size());
				Future<CountDetails> f = es.submit(new GetLocationPool(listofUserIP, id++, ipLookupService));
				futureList.add(f);
				listofUserIP = new ArrayList<UserDetails>();
			}

		}
		if (listofUserIP.size() > 0) {
			System.out.println("submitting Last task to executor with id " + id + listofUserIP.size());
			Future<CountDetails> f = es.submit(new GetLocationPool(listofUserIP, id++, ipLookupService));
			futureList.add(f);
			listofUserIP = new ArrayList<UserDetails>();
		}
		System.out.println("\n\ncame outside");

		br.close();
		try {
			es.shutdown();
			System.out.println("Shutting down executor");
			/*
			 * es.awaitTermination(1, TimeUnit.MICROSECONDS); es.shutdownNow();
			 */
		} catch (Exception e) {
			System.out.println("error");
		}

		for (Future f : futureList) {
			try {
				CountDetails cd = (CountDetails) f.get();
				final_found += cd.getFound();
				final_invalidIP += cd.getInvalid();
				final_notFound += cd.getNotfound();
				System.out.println("future came for id" + cd.getThreadid());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		System.out.println();
		System.out.println("#######Final Result ##### >>>> found " + final_found + "notfound " + final_notFound
				+ " invalid ip " + final_invalidIP);
		// bw.close();
		return;
	}

}
