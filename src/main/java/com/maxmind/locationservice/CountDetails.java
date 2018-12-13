package com.maxmind.locationservice;

public class CountDetails {
	
	long invalid=0;
	long found=0;
	long notfound=0;
	long threadid=0;
	
	public long getInvalid() {
		return invalid;
	}
	public long getFound() {
		return found;
	}
	public long getNotfound() {
		return notfound;
	}
	
	public void setInvalid(long invalid) {
		this.invalid = invalid;
	}
	public void setFound(long found) {
		this.found = found;
	}
	public void setNotfound(long notfound) {
		this.notfound = notfound;
	}
	public void setThreadid(long threadid) {
		this.threadid = threadid;
	}
	public long getThreadid() {
		return threadid;
	}
	
}
