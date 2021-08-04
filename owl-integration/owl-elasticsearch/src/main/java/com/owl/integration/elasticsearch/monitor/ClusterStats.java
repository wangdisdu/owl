package com.owl.integration.elasticsearch.monitor;

import com.fasterxml.jackson.databind.JsonNode;
import com.owl.api.monitor.Category;
import com.owl.api.monitor.Instance;
import com.owl.api.monitor.Time;
import com.owl.api.monitor.Value;

public class ClusterStats {
    @Time
    private long timestamp;
    @Instance
    private String name;
    @Category
    private String category = "cluster";
    @Value
    private int clusterStatus;
    @Value
    private long indicesCount;
    @Value
    private long docsCount;
    @Value
    private long storeBytes;
    @Value
    private long nodesCount;
    @Value
    private long cpuPercent;
    @Value
    private long memTotalBytes;
    @Value
    private long memUsedBytes;
    @Value
    private long fsTotalBytes;
    @Value
    private long fsFreeBytes;

    public static ClusterStats fromJson(JsonNode json) {
        ClusterStats stats = new ClusterStats();
        stats.setTimestamp(json.get("timestamp").asLong());
        stats.setName(json.get("cluster_name").asText());
        stats.setClusterStatus(ClusterStatus.valueOf(json.get("status").asText()).ordinal());
        stats.setIndicesCount(json.get("indices").get("count").asLong());
        stats.setDocsCount(json.get("indices").get("docs").get("count").asLong());
        stats.setStoreBytes(json.get("indices").get("store").get("size_in_bytes").asLong());
        stats.setNodesCount(json.get("nodes").get("count").get("total").asLong());
        stats.setCpuPercent(json.get("nodes").get("process").get("cpu").get("percent").asLong());
        stats.setMemTotalBytes(json.get("nodes").get("os").get("mem").get("total_in_bytes").asLong());
        stats.setMemUsedBytes(json.get("nodes").get("jvm").get("mem").get("heap_used_in_bytes").asLong());
        stats.setFsTotalBytes(json.get("nodes").get("fs").get("total_in_bytes").asLong());
        stats.setFsFreeBytes(json.get("nodes").get("fs").get("free_in_bytes").asLong());
        return stats;
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

    public String getCategory() {
        return category;
    }

    public ClusterStats setCategory(String category) {
        this.category = category;
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

    public long getDocsCount() {
        return docsCount;
    }

    public ClusterStats setDocsCount(long docsCount) {
        this.docsCount = docsCount;
        return this;
    }

    public long getStoreBytes() {
        return storeBytes;
    }

    public ClusterStats setStoreBytes(long storeBytes) {
        this.storeBytes = storeBytes;
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

    public long getMemTotalBytes() {
        return memTotalBytes;
    }

    public ClusterStats setMemTotalBytes(long memTotalBytes) {
        this.memTotalBytes = memTotalBytes;
        return this;
    }

    public long getMemUsedBytes() {
        return memUsedBytes;
    }

    public ClusterStats setMemUsedBytes(long memUsedBytes) {
        this.memUsedBytes = memUsedBytes;
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
}
