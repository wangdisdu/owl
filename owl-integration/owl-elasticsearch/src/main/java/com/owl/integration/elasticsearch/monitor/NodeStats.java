package com.owl.integration.elasticsearch.monitor;

import cn.hutool.core.util.ArrayUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.owl.api.monitor.Category;
import com.owl.api.monitor.Host;
import com.owl.api.monitor.Instance;
import com.owl.api.monitor.Time;
import com.owl.api.monitor.Value;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class NodeStats {
    @Time
    private long timestamp;
    private String id;
    @Instance
    private String name;
    @Category
    private String category = "node";
    @Host
    private String host;
    @Value
    private long docsCount;
    @Value
    private long storeBytes;
    @Value
    private long cpuPercent;
    @Value
    private long memTotalBytes;
    @Value
    private long memUsedBytes;
    @Value
    private long memPercent;
    @Value
    private long youngGcCount;
    @Value
    private long youngGcMs;
    @Value
    private long oldGcCount;
    @Value
    private long oldGcMs;
    @Value
    private long fsTotalBytes;
    @Value
    private long fsFreeBytes;
    @Value
    private long fielddataBytes;
    @Value
    private long queryCacheBytes;
    @Value
    private long requestCacheBytes;
    @Value
    private long indexTotal;
    @Value
    private long indexMs;
    @Value
    private long queryTotal;
    @Value
    private long queryMs;
    @Value
    private long writeQueue;
    @Value
    private long searchQueue;

    public static NodeStats[] fromJson(JsonNode json) {
        Map<String, NodeStats> result = new LinkedHashMap<>();
        Iterator<Map.Entry<String, JsonNode>> nodes = json.get("nodes").fields();
        while (nodes.hasNext()) {
            Map.Entry<String, JsonNode> entry = nodes.next();
            String id = entry.getKey();
            JsonNode node = entry.getValue();
            NodeStats stats = new NodeStats();
            stats.setTimestamp(node.get("timestamp").asLong());
            stats.setId(id);
            stats.setName(node.get("name").asText());
            stats.setHost(node.get("host").asText());
            stats.setDocsCount(node.get("indices").get("docs").get("count").asLong());
            stats.setStoreBytes(node.get("indices").get("store").get("size_in_bytes").asLong());
            stats.setCpuPercent(node.get("process").get("cpu").get("percent").asLong());
            stats.setMemTotalBytes(node.get("os").get("mem").get("total_in_bytes").asLong());
            stats.setMemUsedBytes(node.get("jvm").get("mem").get("heap_used_in_bytes").asLong());
            stats.setMemPercent(node.get("jvm").get("mem").get("heap_used_percent").asLong());
            stats.setYoungGcCount(node.get("jvm").get("gc").get("collectors").get("young").get("collection_count").asLong());
            stats.setYoungGcMs(node.get("jvm").get("gc").get("collectors").get("young").get("collection_time_in_millis").asLong());
            stats.setOldGcCount(node.get("jvm").get("gc").get("collectors").get("old").get("collection_count").asLong());
            stats.setOldGcMs(node.get("jvm").get("gc").get("collectors").get("old").get("collection_time_in_millis").asLong());
            stats.setFsTotalBytes(node.get("fs").get("total").get("total_in_bytes").asLong());
            stats.setFsFreeBytes(node.get("fs").get("total").get("free_in_bytes").asLong());
            stats.setFielddataBytes(node.get("indices").get("fielddata").get("memory_size_in_bytes").asLong());
            stats.setQueryCacheBytes(node.get("indices").get("query_cache").get("memory_size_in_bytes").asLong());
            stats.setRequestCacheBytes(node.get("indices").get("request_cache").get("memory_size_in_bytes").asLong());
            stats.setIndexTotal(node.get("indices").get("indexing").get("index_total").asLong());
            stats.setIndexMs(node.get("indices").get("indexing").get("index_time_in_millis").asLong());
            stats.setQueryTotal(node.get("indices").get("search").get("query_total").asLong());
            stats.setQueryMs(node.get("indices").get("search").get("query_time_in_millis").asLong());
            if (node.get("thread_pool").has("write")) {
                stats.setWriteQueue(node.get("thread_pool").get("write").get("queue").asLong());
            } else if (node.get("thread_pool").has("index") && node.get("thread_pool").has("bulk")) {
                stats.setWriteQueue(node.get("thread_pool").get("index").get("queue").asLong()
                        + node.get("thread_pool").get("bulk").get("queue").asLong());
            }
            stats.setSearchQueue(node.get("thread_pool").get("search").get("queue").asLong());
            result.put(id, stats);
        }
        return ArrayUtil.toArray(result.values(), NodeStats.class);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public NodeStats setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getId() {
        return id;
    }

    public NodeStats setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public NodeStats setName(String name) {
        this.name = name;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public NodeStats setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getHost() {
        return host;
    }

    public NodeStats setHost(String host) {
        this.host = host;
        return this;
    }

    public long getDocsCount() {
        return docsCount;
    }

    public NodeStats setDocsCount(long docsCount) {
        this.docsCount = docsCount;
        return this;
    }

    public long getStoreBytes() {
        return storeBytes;
    }

    public NodeStats setStoreBytes(long storeBytes) {
        this.storeBytes = storeBytes;
        return this;
    }

    public long getCpuPercent() {
        return cpuPercent;
    }

    public NodeStats setCpuPercent(long cpuPercent) {
        this.cpuPercent = cpuPercent;
        return this;
    }

    public long getMemTotalBytes() {
        return memTotalBytes;
    }

    public NodeStats setMemTotalBytes(long memTotalBytes) {
        this.memTotalBytes = memTotalBytes;
        return this;
    }

    public long getMemUsedBytes() {
        return memUsedBytes;
    }

    public NodeStats setMemUsedBytes(long memUsedBytes) {
        this.memUsedBytes = memUsedBytes;
        return this;
    }

    public long getMemPercent() {
        return memPercent;
    }

    public NodeStats setMemPercent(long memPercent) {
        this.memPercent = memPercent;
        return this;
    }

    public long getYoungGcCount() {
        return youngGcCount;
    }

    public NodeStats setYoungGcCount(long youngGcCount) {
        this.youngGcCount = youngGcCount;
        return this;
    }

    public long getYoungGcMs() {
        return youngGcMs;
    }

    public NodeStats setYoungGcMs(long youngGcMs) {
        this.youngGcMs = youngGcMs;
        return this;
    }

    public long getOldGcCount() {
        return oldGcCount;
    }

    public NodeStats setOldGcCount(long oldGcCount) {
        this.oldGcCount = oldGcCount;
        return this;
    }

    public long getOldGcMs() {
        return oldGcMs;
    }

    public NodeStats setOldGcMs(long oldGcMs) {
        this.oldGcMs = oldGcMs;
        return this;
    }

    public long getFsTotalBytes() {
        return fsTotalBytes;
    }

    public NodeStats setFsTotalBytes(long fsTotalBytes) {
        this.fsTotalBytes = fsTotalBytes;
        return this;
    }

    public long getFsFreeBytes() {
        return fsFreeBytes;
    }

    public NodeStats setFsFreeBytes(long fsFreeBytes) {
        this.fsFreeBytes = fsFreeBytes;
        return this;
    }

    public long getFielddataBytes() {
        return fielddataBytes;
    }

    public NodeStats setFielddataBytes(long fielddataBytes) {
        this.fielddataBytes = fielddataBytes;
        return this;
    }

    public long getQueryCacheBytes() {
        return queryCacheBytes;
    }

    public NodeStats setQueryCacheBytes(long queryCacheBytes) {
        this.queryCacheBytes = queryCacheBytes;
        return this;
    }

    public long getRequestCacheBytes() {
        return requestCacheBytes;
    }

    public NodeStats setRequestCacheBytes(long requestCacheBytes) {
        this.requestCacheBytes = requestCacheBytes;
        return this;
    }

    public long getIndexTotal() {
        return indexTotal;
    }

    public NodeStats setIndexTotal(long indexTotal) {
        this.indexTotal = indexTotal;
        return this;
    }

    public long getIndexMs() {
        return indexMs;
    }

    public NodeStats setIndexMs(long indexMs) {
        this.indexMs = indexMs;
        return this;
    }

    public long getQueryTotal() {
        return queryTotal;
    }

    public NodeStats setQueryTotal(long queryTotal) {
        this.queryTotal = queryTotal;
        return this;
    }

    public long getQueryMs() {
        return queryMs;
    }

    public NodeStats setQueryMs(long queryMs) {
        this.queryMs = queryMs;
        return this;
    }

    public long getWriteQueue() {
        return writeQueue;
    }

    public NodeStats setWriteQueue(long writeQueue) {
        this.writeQueue = writeQueue;
        return this;
    }

    public long getSearchQueue() {
        return searchQueue;
    }

    public NodeStats setSearchQueue(long searchQueue) {
        this.searchQueue = searchQueue;
        return this;
    }
}
