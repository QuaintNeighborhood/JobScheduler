package com.example.JobScheduler;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.params.ZAddParams;
import redis.clients.jedis.resps.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RedisDAO {
    private final Jedis jedis;

    private static final String HASH_KEY = "scheduler.hash";
    private static final String SORTED_SET_KEY = "scheduler.sortedset";

    public RedisDAO() {
        this.jedis = new Jedis();
    }

    public RedisDAO(final Jedis jedis) {
        this.jedis = jedis;
    }

    public boolean addJob(
            final Job req
    ) {
        final String jobId = req.getJobId();
        try (final Transaction txn = jedis.multi()) {
            txn.hsetnx(HASH_KEY, jobId, req.getJobSpecs());
            txn.zadd(SORTED_SET_KEY, req.getDate(), jobId, ZAddParams.zAddParams().nx());
            final List<Object> res = txn.exec();
            if (res == null || res.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public List<Job> getJobs() {
        final List<Tuple> listOfJobsWithoutSpecs = jedis.zrangeByScoreWithScores(SORTED_SET_KEY, 0, Double.MAX_VALUE);
        final List<Job> res = new ArrayList<>();
        for (final Tuple tuple : listOfJobsWithoutSpecs) {
            final String jobSpecs = jedis.hget(HASH_KEY, tuple.getElement());
            final Job job = Job.builder()
                    .date((long) tuple.getScore())
                    .jobSpecs(jobSpecs)
                    .build();
            res.add(job);
        }
        return res;
    }

    public boolean updateJob(
            final Job req
    ) {
        final String jobId = req.getJobId();
        if (jedis.hexists(HASH_KEY, jobId)) {
            jedis.hset(HASH_KEY, jobId, req.getJobSpecs());
            jedis.zadd(SORTED_SET_KEY, req.getDate(), jobId, ZAddParams.zAddParams().xx());
        } else {
            return false;
        }
        return true;
    }

    public boolean deleteJob(
            final String jobId
    ) {
        try (final Transaction txn = jedis.multi()) {
            txn.hdel(HASH_KEY, jobId);
            txn.zrem(SORTED_SET_KEY, jobId);
            final List<Object> res = txn.exec();
            if (res == null || res.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
