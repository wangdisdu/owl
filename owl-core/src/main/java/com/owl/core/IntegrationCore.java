package com.owl.core;

import cn.hutool.core.util.ReflectUtil;
import com.owl.api.IntegrationBuilder;
import com.owl.api.IntegrationConnection;
import com.owl.api.IntegrationContext;
import com.owl.api.annotation.IntegrationMeta;
import com.owl.api.reflect.IntegrationScanner;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class IntegrationCore {

    public static List<IntegrationMeta> scan() {
        List<IntegrationMeta> list = new ArrayList<>();
        ServiceLoader<IntegrationBuilder> builders = ServiceLoader.load(IntegrationBuilder.class);
        for (IntegrationBuilder builder : builders) {
            IntegrationMeta meta = IntegrationScanner.scan(builder.getClass());
            list.add(meta);
        }
        return list;
    }

    public static IntegrationConnection connect(IntegrationContext context, BuilderConfig config) throws Exception {
        IntegrationBuilder builder = build(context, config);
        return builder.connect();
    }

    public static IntegrationBuilder build(IntegrationContext context, BuilderConfig config) throws Exception {
        IntegrationBuilder builder = ReflectUtil.newInstance(config.getBuilder());
        builder.build(context, builder.configure(config.getConfig()));
        return builder;
    }
}
