package edu.neu.coe.info6205.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;


public class Timer {

    public <T> double repeat(int n, Supplier<T> function) {
        for (int i = 0; i < n; i++) {
            function.get();
            lap();
        }
        pause();
        final double result = meanLapTime();
        resume();
        return result;
    }
    
    public <T, U> double repeat(int n, Supplier<T> supplier, Function<T, U> function) {
        return repeat(n, false, supplier, function, null, null);
    }

    public <T, U> double repeat(int n, boolean warmup, Supplier<T> supplier, Function<T, U> function, UnaryOperator<T> preFunction, Consumer<U> postFunction) {
        // TO BE IMPLEMENTED : note that the timer is running when this method is called and should still be running when it returns.
    	if (warmup) {
    	    for (int i = 0; i < n; i++) {
    	        T t = supplier.get();
    	        if (preFunction != null) {
    	            t = preFunction.apply(t);
    	        }
    	        U u = function.apply(t);
    	        if (postFunction != null) {
    	            postFunction.accept(u);
    	        }
    	    }
    	}
        if (!running) {
            resume();
        }
        long totalTicks = 0L;
        for (int i = 0; i < n; i++) {
            T t = supplier.get();
            if (preFunction != null) {
                t = preFunction.apply(t);
            }
            long start = getClock();
            U u = function.apply(t);
            long end = getClock();
            if (!warmup) {
            totalTicks += (end - start);
            }
            if (postFunction != null) {
                postFunction.accept(u);
            }
            lap();
        }
        pause();
        double result = toMillisecs(totalTicks) / n;
        resume();
        return result;
 
    }

    public double stop() {
        pauseAndLap();
        return meanLapTime();
    }


    public double meanLapTime() {
        if (running) throw new TimerException();
        return toMillisecs(ticks) / laps;
    }


    public void pauseAndLap() {
        lap();
        ticks += getClock();
        running = false;
    }


    public void resume() {
        if (running) throw new TimerException();
        ticks -= getClock();
        running = true;
    }


    public void lap() {
        if (!running) throw new TimerException();
        laps++;
    }


    public void pause() {
        pauseAndLap();
        laps--;
    }


    public double millisecs() {
        if (running) throw new TimerException();
        return toMillisecs(ticks);
    }

    @Override
    public String toString() {
        return "Timer{" +
                "ticks=" + ticks +
                ", laps=" + laps +
                ", running=" + running +
                '}';
    }

    /**
     * Construct a new Timer and set it running.
     */
    public Timer() {
        resume();
    }

    private long ticks = 0L;
    private int laps = 0;
    private boolean running = false;

    // NOTE: Used by unit tests
    private long getTicks() {
        return ticks;
    }

    // NOTE: Used by unit tests
    private int getLaps() {
        return laps;
    }

    // NOTE: Used by unit tests
    private boolean isRunning() {
        return running;
    }


    private static long getClock() {
        // TO BE IMPLEMENTED 
    	 return System.nanoTime();
        // END SOLUTION
    }


    private static double toMillisecs(long ticks) {
        // TO BE IMPLEMENTED 
    	return ticks / 1_000_000.0;
        // END SOLUTION
    }

    final static LazyLogger logger = new LazyLogger(Timer.class);

    static class TimerException extends RuntimeException {
        public TimerException() {
        }

        public TimerException(String message) {
            super(message);
        }

        public TimerException(String message, Throwable cause) {
            super(message, cause);
        }

        public TimerException(Throwable cause) {
            super(cause);
        }
    }
}