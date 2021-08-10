package com.owl.integration.elasticsearch.monitor;

import cn.hutool.core.util.ArrayUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.owl.api.monitor.Category;
import com.owl.api.monitor.Host;
import com.owl.api.monitor.Instance;
import com.owl.api.monitor.Json;
import com.owl.api.monitor.JsonUtil;
import com.owl.api.monitor.Time;
import com.owl.api.monitor.Value;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class NodeStats {
    private String id;

    @Category
    private String category = "node";

    @Time
    @Json(path = {"timestamp"})
    private long timestamp;

    @Instance
    @Json(path = {"name"})
    private String name;

    @Host
    @Json(path = {"host"})
    private String host;

    @Value(alias = "数据总量", unit = "count")
    @Json(path = {"indices", "docs", "count"})
    private long docsCount;

    @Value(alias = "删除数据量", unit = "count")
    @Json(path = {"indices", "docs", "deleted"})
    private long docsDeleted;

    @Value(alias = "存储使用", unit = "bytes")
    @Json(path = {"indices", "store", "size_in_bytes"})
    private long storeBytes;

    @Value(alias = "写入次数", unit = "count")
    @Json(path = {"indices", "indexing", "index_total"})
    private long indexTotal;

    @Value(alias = "写入耗时", unit = "ms")
    @Json(path = {"indices", "indexing", "index_time_in_millis"})
    private long indexMs;

    @Value(alias = "写入失败", unit = "count")
    @Json(path = {"indices", "indexing", "index_failed"})
    private long indexFailed;

    @Value(alias = "查询次数", unit = "count")
    @Json(path = {"indices", "search", "query_total"})
    private long queryTotal;

    @Value(alias = "查询耗时", unit = "ms")
    @Json(path = {"indices", "search", "query_time_in_millis"})
    private long queryMs;

    @Value(alias = "scroll次数", unit = "count")
    @Json(path = {"indices", "search", "scroll_total"})
    private long scrollTotal;

    @Value(alias = "scroll耗时", unit = "ms")
    @Json(path = {"indices", "search", "scroll_time_in_millis"})
    private long scrollMs;

    @Value(alias = "merge次数", unit = "count")
    @Json(path = {"indices", "merges", "total"})
    private long mergesTotal;

    @Value(alias = "merge耗时", unit = "ms")
    @Json(path = {"indices", "merges", "total_time_in_millis"})
    private long mergesMs;

    @Value(alias = "refresh次数", unit = "count")
    @Json(path = {"indices", "refresh", "total"})
    private long refreshTotal;

    @Value(alias = "refresh耗时", unit = "ms")
    @Json(path = {"indices", "refresh", "total_time_in_millis"})
    private long refreshMs;

    @Value(alias = "flush次数", unit = "count")
    @Json(path = {"indices", "flush", "total"})
    private long flushTotal;

    @Value(alias = "flush耗时", unit = "ms")
    @Json(path = {"indices", "flush", "total_time_in_millis"})
    private long flushMs;

    @Value(alias = "查询缓存", unit = "bytes")
    @Json(path = {"indices", "query_cache", "memory_size_in_bytes"})
    private long queryCacheBytes;

    @Value(alias = "fielddata缓存", unit = "bytes")
    @Json(path = {"indices", "fielddata", "memory_size_in_bytes"})
    private long fielddataBytes;

    @Value(alias = "请求缓存", unit = "bytes")
    @Json(path = {"indices", "request_cache", "memory_size_in_bytes"})
    private long requestCacheBytes;

    @Value(alias = "segments数量", unit = "bytes")
    @Json(path = {"indices", "segments", "count"})
    private long segmentsCount;

    @Value(alias = "segments缓存", unit = "bytes")
    @Json(path = {"indices", "segments", "memory_in_bytes"})
    private long segmentsBytes;

    @Value(alias = "terms缓存", unit = "bytes")
    @Json(path = {"indices", "segments", "terms_memory_in_bytes"})
    private long segmentsTermsBytes;

    @Value(alias = "storedFields缓存", unit = "bytes")
    @Json(path = {"indices", "segments", "stored_fields_memory_in_bytes"})
    private long segmentsStoredFieldsBytes;

    @Value(alias = "docValues缓存", unit = "bytes")
    @Json(path = {"indices", "segments", "doc_values_memory_in_bytes"})
    private long segmentsDocValuesBytes;

    @Value(alias = "cpu负载", unit = "count")
    @Json(path = {"os", "cpu", "load_average", "1m"}, path2 = {"os", "load_average"})
    private long cpuLoad;

    @Value(alias = "cpu使用", unit = "percent")
    @Json(path = {"process", "cpu", "percent"})
    private long cpuPercent;

    @Value(alias = "线程数量", unit = "count")
    @Json(path = {"jvm", "threads", "count"})
    private long threadsCount;

    @Value(alias = "最大内存", unit = "bytes")
    @Json(path = {"jvm", "mem", "heap_max_in_bytes"})
    private long heapMaxBytes;

    @Value(alias = "使用内存", unit = "bytes")
    @Json(path = {"jvm", "mem", "heap_used_in_bytes"})
    private long heapUsedBytes;

    @Value(alias = "内存使用", unit = "percent")
    @Json(path = {"jvm", "mem", "heap_used_percent"})
    private long heapUsedPercent;

    @Value(alias = "非堆内存使用", unit = "bytes")
    @Json(path = {"jvm", "mem", "non_heap_used_in_bytes"})
    private long nonHeapUsedBytes;

    @Value(alias = "youngGc次数", unit = "count")
    @Json(path = {"jvm", "gc", "collectors", "young", "collection_count"})
    private long youngGcCount;

    @Value(alias = "youngGc耗时", unit = "ms")
    @Json(path = {"jvm", "gc", "collectors", "young", "collection_time_in_millis"})
    private long youngGcMs;

    @Value(alias = "oldGc次数", unit = "count")
    @Json(path = {"jvm", "gc", "collectors", "old", "collection_count"})
    private long oldGcCount;

    @Value(alias = "oldGc耗时", unit = "ms")
    @Json(path = {"jvm", "gc", "collectors", "old", "collection_time_in_millis"})
    private long oldGcMs;

    @Value(alias = "写入队列", unit = "count")
    @Json(path = {"thread_pool", "write", "queue"}, required = false)
    private long writeQueue;

    @Value(alias = "写入拒绝", unit = "count")
    @Json(path = {"thread_pool", "write", "rejected"}, required = false)
    private long writeRejected;

    @Json(path = {"thread_pool", "bulk", "queue"}, required = false)
    private long bulkQueue;
    @Json(path = {"thread_pool", "bulk", "rejected"}, required = false)
    private long bulkRejected;
    @Json(path = {"thread_pool", "index", "queue"}, required = false)
    private long indexQueue;
    @Json(path = {"thread_pool", "index", "rejected"}, required = false)
    private long indexRejected;

    @Value(alias = "GET队列", unit = "count")
    @Json(path = {"thread_pool", "get", "queue"})
    private long getQueue;

    @Value(alias = "GET拒绝", unit = "count")
    @Json(path = {"thread_pool", "get", "rejected"})
    private long getRejected;

    @Value(alias = "查询队列", unit = "count")
    @Json(path = {"thread_pool", "search", "queue"})
    private long searchQueue;

    @Value(alias = "查询拒绝", unit = "count")
    @Json(path = {"thread_pool", "search", "rejected"})
    private long searchRejected;

    @Value(alias = "磁盘总量", unit = "bytes")
    @Json(path = {"fs", "total", "total_in_bytes"})
    private long fsTotalBytes;

    @Value(alias = "磁盘剩余", unit = "bytes")
    @Json(path = {"fs", "total", "free_in_bytes"})
    private long fsFreeBytes;

    @Value(alias = "磁盘可用", unit = "bytes")
    @Json(path = {"fs", "total", "available_in_bytes"})
    private long fsAvailableBytes;

    @Value(alias = "io次数", unit = "count")
    @Json(path = {"fs", "io_stats", "total", "operations"})
    private long ioTotalCount;

    @Value(alias = "ioRead次数", unit = "count")
    @Json(path = {"fs", "io_stats", "total", "read_operations"})
    private long ioReadCount;

    @Value(alias = "ioReadKB", unit = "kilobytes")
    @Json(path = {"fs", "io_stats", "total", "read_kilobytes"})
    private long ioReadKb;

    @Value(alias = "ioWrite次数", unit = "kilobytes")
    @Json(path = {"fs", "io_stats", "total", "write_operations"})
    private long ioWriteCount;

    @Value(alias = "ioWriteKB", unit = "kilobytes")
    @Json(path = {"fs", "io_stats", "total", "write_kilobytes"})
    private long ioWriteKb;

    public static NodeStats[] fromJson(JsonNode json) {
        Map<String, NodeStats> result = new LinkedHashMap<>();
        Iterator<Map.Entry<String, JsonNode>> nodes = json.get("nodes").fields();
        while (nodes.hasNext()) {
            Map.Entry<String, JsonNode> entry = nodes.next();
            String id = entry.getKey();
            JsonNode node = entry.getValue();
            NodeStats stats = JsonUtil.decode(node, new NodeStats());
            stats.setId(id);
            if (!node.get("thread_pool").has("write")) {
                stats.setWriteQueue(stats.bulkQueue + stats.indexQueue);
                stats.setWriteRejected(stats.bulkRejected + stats.indexRejected);
            }
            result.put(id, stats);
        }
        return ArrayUtil.toArray(result.values(), NodeStats.class);
    }

    public String getId() {
        return id;
    }

    public NodeStats setId(String id) {
        this.id = id;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public NodeStats setCategory(String category) {
        this.category = category;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public NodeStats setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getName() {
        return name;
    }

    public NodeStats setName(String name) {
        this.name = name;
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

    public long getDocsDeleted() {
        return docsDeleted;
    }

    public NodeStats setDocsDeleted(long docsDeleted) {
        this.docsDeleted = docsDeleted;
        return this;
    }

    public long getStoreBytes() {
        return storeBytes;
    }

    public NodeStats setStoreBytes(long storeBytes) {
        this.storeBytes = storeBytes;
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

    public long getIndexFailed() {
        return indexFailed;
    }

    public NodeStats setIndexFailed(long indexFailed) {
        this.indexFailed = indexFailed;
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

    public long getScrollTotal() {
        return scrollTotal;
    }

    public NodeStats setScrollTotal(long scrollTotal) {
        this.scrollTotal = scrollTotal;
        return this;
    }

    public long getScrollMs() {
        return scrollMs;
    }

    public NodeStats setScrollMs(long scrollMs) {
        this.scrollMs = scrollMs;
        return this;
    }

    public long getMergesTotal() {
        return mergesTotal;
    }

    public NodeStats setMergesTotal(long mergesTotal) {
        this.mergesTotal = mergesTotal;
        return this;
    }

    public long getMergesMs() {
        return mergesMs;
    }

    public NodeStats setMergesMs(long mergesMs) {
        this.mergesMs = mergesMs;
        return this;
    }

    public long getRefreshTotal() {
        return refreshTotal;
    }

    public NodeStats setRefreshTotal(long refreshTotal) {
        this.refreshTotal = refreshTotal;
        return this;
    }

    public long getRefreshMs() {
        return refreshMs;
    }

    public NodeStats setRefreshMs(long refreshMs) {
        this.refreshMs = refreshMs;
        return this;
    }

    public long getFlushTotal() {
        return flushTotal;
    }

    public NodeStats setFlushTotal(long flushTotal) {
        this.flushTotal = flushTotal;
        return this;
    }

    public long getFlushMs() {
        return flushMs;
    }

    public NodeStats setFlushMs(long flushMs) {
        this.flushMs = flushMs;
        return this;
    }

    public long getQueryCacheBytes() {
        return queryCacheBytes;
    }

    public NodeStats setQueryCacheBytes(long queryCacheBytes) {
        this.queryCacheBytes = queryCacheBytes;
        return this;
    }

    public long getFielddataBytes() {
        return fielddataBytes;
    }

    public NodeStats setFielddataBytes(long fielddataBytes) {
        this.fielddataBytes = fielddataBytes;
        return this;
    }

    public long getRequestCacheBytes() {
        return requestCacheBytes;
    }

    public NodeStats setRequestCacheBytes(long requestCacheBytes) {
        this.requestCacheBytes = requestCacheBytes;
        return this;
    }

    public long getSegmentsCount() {
        return segmentsCount;
    }

    public NodeStats setSegmentsCount(long segmentsCount) {
        this.segmentsCount = segmentsCount;
        return this;
    }

    public long getSegmentsBytes() {
        return segmentsBytes;
    }

    public NodeStats setSegmentsBytes(long segmentsBytes) {
        this.segmentsBytes = segmentsBytes;
        return this;
    }

    public long getSegmentsTermsBytes() {
        return segmentsTermsBytes;
    }

    public NodeStats setSegmentsTermsBytes(long segmentsTermsBytes) {
        this.segmentsTermsBytes = segmentsTermsBytes;
        return this;
    }

    public long getSegmentsStoredFieldsBytes() {
        return segmentsStoredFieldsBytes;
    }

    public NodeStats setSegmentsStoredFieldsBytes(long segmentsStoredFieldsBytes) {
        this.segmentsStoredFieldsBytes = segmentsStoredFieldsBytes;
        return this;
    }

    public long getSegmentsDocValuesBytes() {
        return segmentsDocValuesBytes;
    }

    public NodeStats setSegmentsDocValuesBytes(long segmentsDocValuesBytes) {
        this.segmentsDocValuesBytes = segmentsDocValuesBytes;
        return this;
    }

    public long getCpuLoad() {
        return cpuLoad;
    }

    public NodeStats setCpuLoad(long cpuLoad) {
        this.cpuLoad = cpuLoad;
        return this;
    }

    public long getCpuPercent() {
        return cpuPercent;
    }

    public NodeStats setCpuPercent(long cpuPercent) {
        this.cpuPercent = cpuPercent;
        return this;
    }

    public long getThreadsCount() {
        return threadsCount;
    }

    public NodeStats setThreadsCount(long threadsCount) {
        this.threadsCount = threadsCount;
        return this;
    }

    public long getHeapMaxBytes() {
        return heapMaxBytes;
    }

    public NodeStats setHeapMaxBytes(long heapMaxBytes) {
        this.heapMaxBytes = heapMaxBytes;
        return this;
    }

    public long getHeapUsedBytes() {
        return heapUsedBytes;
    }

    public NodeStats setHeapUsedBytes(long heapUsedBytes) {
        this.heapUsedBytes = heapUsedBytes;
        return this;
    }

    public long getHeapUsedPercent() {
        return heapUsedPercent;
    }

    public NodeStats setHeapUsedPercent(long heapUsedPercent) {
        this.heapUsedPercent = heapUsedPercent;
        return this;
    }

    public long getNonHeapUsedBytes() {
        return nonHeapUsedBytes;
    }

    public NodeStats setNonHeapUsedBytes(long nonHeapUsedBytes) {
        this.nonHeapUsedBytes = nonHeapUsedBytes;
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

    public long getWriteQueue() {
        return writeQueue;
    }

    public NodeStats setWriteQueue(long writeQueue) {
        this.writeQueue = writeQueue;
        return this;
    }

    public long getWriteRejected() {
        return writeRejected;
    }

    public NodeStats setWriteRejected(long writeRejected) {
        this.writeRejected = writeRejected;
        return this;
    }

    public long getBulkQueue() {
        return bulkQueue;
    }

    public NodeStats setBulkQueue(long bulkQueue) {
        this.bulkQueue = bulkQueue;
        return this;
    }

    public long getBulkRejected() {
        return bulkRejected;
    }

    public NodeStats setBulkRejected(long bulkRejected) {
        this.bulkRejected = bulkRejected;
        return this;
    }

    public long getIndexQueue() {
        return indexQueue;
    }

    public NodeStats setIndexQueue(long indexQueue) {
        this.indexQueue = indexQueue;
        return this;
    }

    public long getIndexRejected() {
        return indexRejected;
    }

    public NodeStats setIndexRejected(long indexRejected) {
        this.indexRejected = indexRejected;
        return this;
    }

    public long getGetQueue() {
        return getQueue;
    }

    public NodeStats setGetQueue(long getQueue) {
        this.getQueue = getQueue;
        return this;
    }

    public long getGetRejected() {
        return getRejected;
    }

    public NodeStats setGetRejected(long getRejected) {
        this.getRejected = getRejected;
        return this;
    }

    public long getSearchQueue() {
        return searchQueue;
    }

    public NodeStats setSearchQueue(long searchQueue) {
        this.searchQueue = searchQueue;
        return this;
    }

    public long getSearchRejected() {
        return searchRejected;
    }

    public NodeStats setSearchRejected(long searchRejected) {
        this.searchRejected = searchRejected;
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

    public long getFsAvailableBytes() {
        return fsAvailableBytes;
    }

    public NodeStats setFsAvailableBytes(long fsAvailableBytes) {
        this.fsAvailableBytes = fsAvailableBytes;
        return this;
    }

    public long getIoTotalCount() {
        return ioTotalCount;
    }

    public NodeStats setIoTotalCount(long ioTotalCount) {
        this.ioTotalCount = ioTotalCount;
        return this;
    }

    public long getIoReadCount() {
        return ioReadCount;
    }

    public NodeStats setIoReadCount(long ioReadCount) {
        this.ioReadCount = ioReadCount;
        return this;
    }

    public long getIoReadKb() {
        return ioReadKb;
    }

    public NodeStats setIoReadKb(long ioReadKb) {
        this.ioReadKb = ioReadKb;
        return this;
    }

    public long getIoWriteCount() {
        return ioWriteCount;
    }

    public NodeStats setIoWriteCount(long ioWriteCount) {
        this.ioWriteCount = ioWriteCount;
        return this;
    }

    public long getIoWriteKb() {
        return ioWriteKb;
    }

    public NodeStats setIoWriteKb(long ioWriteKb) {
        this.ioWriteKb = ioWriteKb;
        return this;
    }
}
