package com.owl.integration.elasticsearch.client.request.sorter;

import com.owl.integration.elasticsearch.client.request.script.Script;

import java.util.LinkedHashMap;
import java.util.Map;

public class ScriptSorter extends Sorter {
    protected Script script;
    protected String type;
    protected Boolean ascending;
    protected String sortMode;

    public ScriptSorter setScript(Script script) {
        this.script = script;
        return this;
    }

    public ScriptSorter setType(String type) {
        this.type = type;
        return this;
    }

    public ScriptSorter setOrder(Boolean ascending) {
        this.ascending = ascending;
        return this;
    }

    public ScriptSorter setAsc() {
        this.ascending = true;
        return this;
    }

    public ScriptSorter setDesc() {
        this.ascending = false;
        return this;
    }

    public ScriptSorter reverseOrder() {
        this.ascending = !this.ascending;
        return this;
    }

    public ScriptSorter setSortMode(String sortMode) {
        this.sortMode = sortMode;
        return this;
    }

    public Script getScript() {
        return script;
    }

    public String getType() {
        return type;
    }

    public Boolean getAscending() {
        return ascending;
    }

    public String getSortMode() {
        return sortMode;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> opts = new LinkedHashMap<>();
        opts.put("script", this.script.serialize());
        opts.put("type", this.type);
        opts.put("order", this.ascending ? "asc" : "desc");
        if (this.sortMode != null) {
            opts.put("mode", this.sortMode);
        }
        Map<String, Object> source = new LinkedHashMap<>();
        source.put("_script", opts);
        return source;
    }
}
