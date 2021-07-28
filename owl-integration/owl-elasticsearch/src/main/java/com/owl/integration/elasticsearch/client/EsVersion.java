package com.owl.integration.elasticsearch.client;

import cn.hutool.core.util.StrUtil;

import java.util.Locale;
import java.util.Objects;

public class EsVersion {
    public static final EsVersion V7_0_0 = new EsVersion("7.0.0");

    private final String version;

    private EsVersion(String version) {
        this.version = version;
    }

    public boolean gt(EsVersion other) {
        return compare(other) > 0;
    }

    public boolean lt(EsVersion other) {
        return compare(other) < 0;
    }

    public boolean gte(EsVersion other) {
        return compare(other) >= 0;
    }

    public boolean lte(EsVersion other) {
        return compare(other) <= 0;
    }

    public boolean eq(EsVersion other) {
        return compare(other) == 0;
    }

    public int compare(EsVersion other) {
        return StrUtil.compareVersion(this.version, other.version);
    }

    public static EsVersion fromString(String version) {
        Objects.requireNonNull(version, "version");
        if (!version.matches("\\d+\\.\\d+\\.\\d+")) {
            final String message = String.format(Locale.ROOT, "Wrong version format. "
                    + "Expected ${digit}.${digit}.${digit} but got %s", version);
            throw new IllegalArgumentException(message);
        }
        return new EsVersion(version);
    }
}
