package com.owl.integration.elasticsearch.monitor;

import com.fasterxml.jackson.databind.JsonNode;
import com.owl.api.monitor.Category;
import com.owl.api.monitor.Instance;
import com.owl.api.monitor.Json;
import com.owl.api.monitor.JsonUtil;
import com.owl.api.monitor.Time;
import com.owl.api.monitor.Value;

public class ClusterStats {
    @Category
    private String category = "cluster";

    @Json(path = {"cluster_uuid"})
    private String id;

    @Time
    @Json(path = {"timestamp"})
    private long timestamp;

    @Instance
    @Json(path = {"cluster_name"})
    private String name;

    @Value(alias = "集群状态", unit = "state")
    private int clusterStatus;

    @Value(alias = "索引数量", unit = "count")
    @Json(path = {"indices", "count"})
    private long indicesCount;

    @Value(alias = "分片数量", unit = "count")
    @Json(path = {"indices", "shards", "total"})
    private long shardsTotal;

    @Value(alias = "主分片数量", unit = "count")
    @Json(path = {"indices", "shards", "primaries"})
    private long shardsPrimaries;

    @Value(alias = "数据总量", unit = "count")
    @Json(path = {"indices", "docs", "count"})
    private long docsCount;

    @Value(alias = "删除数据量", unit = "count")
    @Json(path = {"indices", "docs", "deleted"})
    private long docsDeleted;

    @Value(alias = "存储使用", unit = "bytes")
    @Json(path = {"indices", "store", "size_in_bytes"})
    private long storeBytes;

    @Value(alias = "fielddata缓存", unit = "bytes")
    @Json(path = {"indices", "fielddata", "memory_size_in_bytes"})
    private long fielddataBytes;

    @Value(alias = "查询缓存", unit = "bytes")
    @Json(path = {"indices", "query_cache", "memory_size_in_bytes"})
    private long queryCacheBytes;

    @Value(alias = "segments数量", unit = "bytes")
    @Json(path = {"indices", "segments", "count"})
    private long segmentsCount;

    @Value(alias = "segments缓存", unit = "bytes")
    @Json(path = {"indices", "segments", "memory_in_bytes"})
    private long segmentsBytes;

    @Value(alias = "节点数量", unit = "count")
    @Json(path = {"nodes", "count", "total"})
    private long nodesCount;

    @Value(alias = "CPU使用", unit = "percent")
    @Json(path = {"nodes", "process", "cpu", "percent"})
    private long cpuPercent;

    @Value(alias = "线程数量", unit = "count")
    @Json(path = {"nodes", "jvm", "threads", "count"}, path2 = {"nodes", "jvm", "threads"})
    private long threadsCount;

    @Value(alias = "最大内存", unit = "bytes")
    @Json(path = {"nodes", "jvm", "mem", "heap_max_in_bytes"})
    private long heapMaxBytes;

    @Value(alias = "使用内存", unit = "bytes")
    @Json(path = {"nodes", "jvm", "mem", "heap_used_in_bytes"})
    private long heapUsedBytes;

    @Value(alias = "内存使用", unit = "percent")
    private long heapUsedPercent;

    @Value(alias = "磁盘总量", unit = "bytes")
    @Json(path = {"nodes", "fs", "total_in_bytes"})
    private long fsTotalBytes;

    @Value(alias = "磁盘剩余", unit = "bytes")
    @Json(path = {"nodes", "fs", "free_in_bytes"})
    private long fsFreeBytes;

    @Value(alias = "磁盘可用", unit = "bytes")
    @Json(path = {"nodes", "fs", "available_in_bytes"})
    private long fsAvailableBytes;

    public static ClusterStats fromJson(JsonNode json) {
        ClusterStats stats = JsonUtil.decode(json, new ClusterStats());
        stats.setClusterStatus(ClusterStatus.valueOf(json.get("status").asText()).ordinal());
        stats.setHeapUsedPercent(stats.getHeapUsedBytes() / (stats.getHeapMaxBytes() / 100L));
        return stats;
    }

    public String getCategory() {
        return category;
    }

    public ClusterStats setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getId() {
        return id;
    }

    public ClusterStats setId(String id) {
        this.id = id;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public ClusterStats setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getName() {
        return name;
    }

    public ClusterStats setName(String name) {
        this.name = name;
        return this;
    }

    public int getClusterStatus() {
        return clusterStatus;
    }

    public ClusterStats setClusterStatus(int clusterStatus) {
        this.clusterStatus = clusterStatus;
        return this;
    }

    public long getIndicesCount() {
        return indicesCount;
    }

    public ClusterStats setIndicesCount(long indicesCount) {
        this.indicesCount = indicesCount;
        return this;
    }

    public long getShardsTotal() {
        return shardsTotal;
    }

    public ClusterStats setShardsTotal(long shardsTotal) {
        this.shardsTotal = shardsTotal;
        return this;
    }

    public long getShardsPrimaries() {
        return shardsPrimaries;
    }

    public ClusterStats setShardsPrimaries(long shardsPrimaries) {
        this.shardsPrimaries = shardsPrimaries;
        return this;
    }

    public long getDocsCount() {
        return docsCount;
    }

    public ClusterStats setDocsCount(long docsCount) {
        this.docsCount = docsCount;
        return this;
    }

    public long getDocsDeleted() {
        return docsDeleted;
    }

    public ClusterStats setDocsDeleted(long docsDeleted) {
        this.docsDeleted = docsDeleted;
        return this;
    }

    public long getStoreBytes() {
        return storeBytes;
    }

    public ClusterStats setStoreBytes(long storeBytes) {
        this.storeBytes = storeBytes;
        return this;
    }

    public long getFielddataBytes() {
        return fielddataBytes;
    }

    public ClusterStats setFielddataBytes(long fielddataBytes) {
        this.fielddataBytes = fielddataBytes;
        return this;
    }

    public long getQueryCacheBytes() {
        return queryCacheBytes;
    }

    public ClusterStats setQueryCacheBytes(long queryCacheBytes) {
        this.queryCacheBytes = queryCacheBytes;
        return this;
    }

    public long getSegmentsCount() {
        return segmentsCount;
    }

    public ClusterStats setSegmentsCount(long segmentsCount) {
        this.segmentsCount = segmentsCount;
        return this;
    }

    public long getSegmentsBytes() {
        return segmentsBytes;
    }

    public ClusterStats setSegmentsBytes(long segmentsBytes) {
        this.segmentsBytes = segmentsBytes;
        return this;
    }

    public long getNodesCount() {
        return nodesCount;
    }

    public ClusterStats setNodesCount(long nodesCount) {
        this.nodesCount = nodesCount;
        return this;
    }

    public long getCpuPercent() {
        return cpuPercent;
    }

    public ClusterStats setCpuPercent(long cpuPercent) {
        this.cpuPercent = cpuPercent;
        return this;
    }

    public long getThreadsCount() {
        return threadsCount;
    }

    public ClusterStats setThreadsCount(long threadsCount) {
        this.threadsCount = threadsCount;
        return this;
    }

    public long getHeapMaxBytes() {
        return heapMaxBytes;
    }

    public ClusterStats setHeapMaxBytes(long heapMaxBytes) {
        this.heapMaxBytes = heapMaxBytes;
        return this;
    }

    public long getHeapUsedBytes() {
        return heapUsedBytes;
    }

    public ClusterStats setHeapUsedBytes(long heapUsedBytes) {
        this.heapUsedBytes = heapUsedBytes;
        return this;
    }

    public long getHeapUsedPercent() {
        return heapUsedPercent;
    }

    public ClusterStats setHeapUsedPercent(long heapUsedPercent) {
        this.heapUsedPercent = heapUsedPercent;
        return this;
    }

    public long getFsTotalBytes() {
        return fsTotalBytes;
    }

    public ClusterStats setFsTotalBytes(long fsTotalBytes) {
        this.fsTotalBytes = fsTotalBytes;
        return this;
    }

    public long getFsFreeBytes() {
        return fsFreeBytes;
    }

    public ClusterStats setFsFreeBytes(long fsFreeBytes) {
        this.fsFreeBytes = fsFreeBytes;
        return this;
    }

    public long getFsAvailableBytes() {
        return fsAvailableBytes;
    }

    public ClusterStats setFsAvailableBytes(long fsAvailableBytes) {
        this.fsAvailableBytes = fsAvailableBytes;
        return this;
    }
}
