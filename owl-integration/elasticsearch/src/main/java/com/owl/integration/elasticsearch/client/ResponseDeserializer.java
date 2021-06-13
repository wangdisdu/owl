package com.owl.integration.elasticsearch.client;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.io.Serializable;

public interface ResponseDeserializer extends Serializable {
    ResponseDeserializer deserialize(JsonNode json) throws IOException;
}
