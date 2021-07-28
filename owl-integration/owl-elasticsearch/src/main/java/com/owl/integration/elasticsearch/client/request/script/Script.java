package com.owl.integration.elasticsearch.client.request.script;

import com.owl.integration.elasticsearch.client.RequestSerializer;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * https://www.elastic.co/guide/en/elasticsearch/reference/current/modules-scripting-expression.html
 */
public class Script implements RequestSerializer {
    public static final String InlineType = "inline";
    public static final String SourceType = "source";
    public static final String IdType = "id";
    public static final String FileType = "file";
    public static final String PainlessLang = "painless";
    public static final String ExpressionLang = "expression";

    protected String script;
    protected String type;
    protected String lang;
    protected Map<String, Object> params = new LinkedHashMap<>();

    public Script setScript(String script) {
        this.script = script;
        return this;
    }

    public Script setType(String type) {
        this.type = type;
        return this;
    }

    public Script setLang(String lang) {
        this.lang = lang;
        return this;
    }

    public Script setParams(Map<String, Object> params) {
        this.params = params;
        return this;
    }

    public Script addParam(String name, Object value) {
        this.params.put(name, value);
        return this;
    }

    public String getScript() {
        return script;
    }

    public String getType() {
        return type;
    }

    public String getLang() {
        return lang;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    /*{
          "script": {
            "lang": "painless",
            "source": "doc['my_field'] * multiplier",
            "params": {
              "multiplier": 2
            }
          }
    }*/
    public Map<String, Object> serialize() {
        Map<String, Object> source = new LinkedHashMap<>();
        source.put(this.type, script);
        if (this.lang != null) {
            source.put("lang", this.lang);
        }
        if (this.params.size() > 0) {
            source.put("params", this.params);
        }
        return source;
    }

}
