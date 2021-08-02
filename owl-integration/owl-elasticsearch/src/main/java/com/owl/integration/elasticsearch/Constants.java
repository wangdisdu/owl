package com.owl.integration.elasticsearch;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

public class Constants {
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    public static final String INDEX = "_index";
    public static final String TYPE = "_type";
    public static final String SOURCE = "_source";
    public static final String ID = "_id";
    public static final String UID = "_uid";

    public static final Set<String> META_COLUMNS = ImmutableSet.of(UID, ID, TYPE, INDEX);
}
