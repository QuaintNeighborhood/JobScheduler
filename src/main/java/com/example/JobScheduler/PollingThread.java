package com.example.JobScheduler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PollingThread extends Thread {
    private static final Logger LOGGER = Logger.getLogger(PollingThread.class.getName());

    private final int maxRetries = 1;
    private final int pollingDelayMillis = 10000;

    private boolean stopRequested = false;
    private int numRetriesAttempted = 0;

    private final JobRunner runner;

    public PollingThread(final JobRunner runner) {
        this.runner = runner;
    }

    public void requestStop() {
        stopRequested = true;
    }

    @Override
    public void run() {
        try {
            while (!stopRequested && !isMaxRetryAttemptsReached()) {
                runner.runNextJob();
            }
            numRetriesAttempted = 0;
        } catch (final Exception ex) {
            numRetriesAttempted++;
            LOGGER.log(Level.SEVERE, "Error while polling for jobs", ex);
        }
    }

    private boolean isMaxRetryAttemptsReached() {
        return numRetriesAttempted >= maxRetries;
    }
}
