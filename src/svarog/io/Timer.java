package svarog.io;

import java.util.concurrent.TimeUnit;

public class Timer {
	public static double getTime() {
		return (double)System.nanoTime() / (double)1000000000L;
	}
	
	public static void syncFrameRate(float fps, long lastNanos) {
        long targetNanos = lastNanos + (long) (1_000_000_000.0f / fps) - 1_000_000L;  // subtract 1 ms to skip the last sleep call
        try {
        	while (System.nanoTime() < targetNanos) {
        		Thread.sleep(1);
        	}
        }
        catch (InterruptedException ignore) {}
    }
	
	public static long getNanoTime() {
		return System.nanoTime();
	}
	
	public static void sleep(long nanos, long diff) {
    	if(nanos < diff)
			try {
				Thread.sleep(TimeUnit.NANOSECONDS.toMillis(diff - nanos));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
	
	public static boolean getDelay(long startNanos, long nanosNow, double delayInSeconds) {
		if(nanosNow - startNanos > delayInSeconds*1000000000)
			return true;
		else 
			return false;
	}
}
