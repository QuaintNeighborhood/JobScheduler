package com.example.JobScheduler;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Job {
    private String jobId;
    private long date;
    private String jobSpecs;
    private String cron;
}
