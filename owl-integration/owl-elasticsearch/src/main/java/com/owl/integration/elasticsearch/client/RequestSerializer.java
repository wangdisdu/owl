package com.owl.integration.elasticsearch.client;

import java.io.Serializable;
import java.util.Map;

public interface RequestSerializer extends Serializable {
    Map<String, Object> serialize();
}
