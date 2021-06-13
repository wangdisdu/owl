package com.owl.integration.elasticsearch.client.request.sorter;

import java.util.LinkedHashMap;
import java.util.Map;

public class FieldSorter extends Sorter {
    protected String field;
    protected Boolean ascending;
    protected Object missing;
    protected String unmappedType;
    protected String sortMode;

    public FieldSorter setField(String field) {
        this.field = field;
        return this;
    }

    public FieldSorter setOrder(Boolean ascending) {
        this.ascending = ascending;
        return this;
    }

    public FieldSorter setAsc() {
        return setOrder(true);
    }

    public FieldSorter setDesc() {
        return setOrder(false);
    }

    public FieldSorter reverseOrder() {
        this.ascending = !this.ascending;
        return this;
    }

    public FieldSorter setMissing(Object missing) {
        this.missing = missing;
        return this;
    }

    public FieldSorter setUnmappedType(String unmappedType) {
        this.unmappedType = unmappedType;
        return this;
    }

    public FieldSorter setSortMode(String sortMode) {
        this.sortMode = sortMode;
        return this;
    }

    public String getField() {
        return field;
    }

    public Boolean getAscending() {
        return ascending;
    }

    public Object getMissing() {
        return missing;
    }

    public String getUnmappedType() {
        return unmappedType;
    }

    public String getSortMode() {
        return sortMode;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> opts = new LinkedHashMap<>();
        opts.put("order", this.ascending ? "asc" : "desc");
        if(this.missing != null) opts.put("missing", this.missing);
        if(this.unmappedType != null) opts.put("unmapped_type", this.unmappedType);
        if(this.sortMode != null) opts.put("mode", this.sortMode);
        Map<String, Object> source = new LinkedHashMap<>();
        source.put(this.field, opts);
        return source;
    }
}
