package com.example.JobScheduler;

import java.util.logging.Logger;

public class JobRunner {
    private static final Logger LOGGER = Logger.getLogger(JobRunner.class.getName());

    private final RedisDAO redis;

    public JobRunner() {
        redis = new RedisDAO();
    }

    public void runNextJob() {
        final Job job = redis.getOldestJob();
        if (job != null) {
            final String jobId = job.getJobId();
            // do work
            // just logs and prints the job
            LOGGER.info(
                    String.format("Executed job: jobId: %s, jobSpecs: %s", jobId, job.getJobSpecs())
            );
            System.out.println(job);
        }
    }

    public static void main(String[] args) {
        final JobRunner runner = new JobRunner();
        final PollingThread thread = new PollingThread(runner);
        try {
            thread.start();
        } finally {
            thread.requestStop();
        }
    }
}
