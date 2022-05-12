package com.example.JobScheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@SpringBootApplication
@RestController
public class JobSchedulerApplication {
	private static final Logger LOGGER = Logger.getLogger(JobSchedulerApplication.class.getName());

	private final JobScheduler scheduler;

	public JobSchedulerApplication() {
		scheduler = new JobScheduler();
	}

	public static void main(final String[] args) {
		SpringApplication.run(JobSchedulerApplication.class, args);
	}

	@PostMapping(
			value = "/create",
			consumes = {MediaType.APPLICATION_JSON_VALUE}
	)
	public String createJobs(@RequestBody final Job req) {
		return scheduler.createManager(req);
	}

	@GetMapping(
			value = "/read",
			produces = {MediaType.APPLICATION_JSON_VALUE}
	)
	public List<Job> getJobs() {
		return scheduler.readManager();
	}

	@PutMapping(
			value = "/update",
			consumes = {MediaType.APPLICATION_JSON_VALUE}
	)
	public String updateJobs(@RequestBody final Job req) {
		return scheduler.updateManager(req);
	}

	@DeleteMapping(value = "/delete/{taskId}")
	public String deleteJobs(@PathVariable(value = "taskId") final String taskId) {
		return scheduler.deleteManager(taskId);
	}
}
            