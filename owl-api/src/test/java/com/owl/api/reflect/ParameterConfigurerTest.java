package com.owl.api.reflect;

import com.owl.api.annotation.TestConfig;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParameterConfigurerTest {

    @Test
    public void test1() {
        Map<String, Object> map = new HashMap<>();
        map.put("name1", "v1");
        map.put("f2", 2);
        map.put("f3", 3L);
        map.put("f4", 4.4F);
        map.put("f5", 5.5D);
        Map<String, String> f6 = new HashMap<>();
        f6.put("f61", "61");
        f6.put("f62", "62");
        map.put("f6", f6);
        List<String> f7 = new ArrayList<>();
        f7.add("f71");
        f7.add("f72");
        map.put("f7", f7);
        List<Double> f8 = new ArrayList<>();
        f8.add(8.1);
        f8.add(8.1);
        map.put("f8", f8);
        TestConfig config = new TestConfig();
        ParameterConfigurer.config(config, map);

        Assert.assertEquals(map.get("name1"), config.getF1());
        Assert.assertEquals(map.get("f2"), config.getF2());
        Assert.assertEquals(map.get("f3"), config.getF3());
        Assert.assertEquals(map.get("f4"), config.getF4());
        Assert.assertEquals(map.get("f5"), config.getF5());
        Assert.assertEquals(((Map<String, String>)map.get("f6")).get("f61"), config.getF6().get("f61"));
        Assert.assertEquals(((Map<String, String>)map.get("f6")).get("f62"), config.getF6().get("f62"));
        Assert.assertEquals(((List<String>)map.get("f7")).get(0), config.getF7().get(0));
        Assert.assertEquals(((List<String>)map.get("f7")).get(1), config.getF7().get(1));
        Assert.assertEquals(((List<Double>)map.get("f8")).get(0), config.getF8().get(0));
        Assert.assertEquals(((List<Double>)map.get("f8")).get(1), config.getF8().get(1));

    }
}
