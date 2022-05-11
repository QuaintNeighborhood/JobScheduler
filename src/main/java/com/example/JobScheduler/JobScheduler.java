package com.example.JobScheduler;

import java.util.List;
import java.util.UUID;

public class JobScheduler {
    private final RedisDAO redis;

    public JobScheduler() {
        redis = new RedisDAO();
    }

    public String createManager(final Job req) {
        final String jobId = UUID.randomUUID().toString();
        final long date = req.getDate();
        req.setJobId(jobId);
        final boolean status = redis.addJob(req);
        final String res;
        if (status) {
            res = String.format("Scheduled job with jobId: %s on %d", jobId, date);
        } else {
            res = String.format("Failed to add job with jobId: %s on %d", jobId, date);
        }
        return res;
    }

    public List<Job> readManager() {
        return redis.getJobs();
    }

    public String updateManager(final Job req) {
        final String jobId = UUID.randomUUID().toString();
        final long date = req.getDate();
        req.setJobId(jobId);
        final boolean status = redis.addJob(req);
        final String res;
        if (status) {
            res = String.format("Updated job with jobId: %s to %d", jobId, date);
        } else {
            res = String.format("Failed to update job with jobId: %s on %d", jobId, date);
        }
        return res;
    }

    public String deleteManager(final String jobId) {
        final boolean status = redis.deleteJob(jobId);
        final String res;
        if (status) {
            res = String.format("Deleted job with jobId: %s", jobId);
        } else {
            res = String.format("Failed to delete job with jobId: %s", jobId);
        }
        return res;
    }
}
