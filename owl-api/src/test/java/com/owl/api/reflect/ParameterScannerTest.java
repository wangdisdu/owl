package com.owl.api.reflect;

import com.owl.api.annotation.DataType;
import com.owl.api.annotation.ParameterMeta;
import com.owl.api.annotation.TestConfig;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParameterScannerTest {

    @Test
    public void test1() {
        List<ParameterMeta> list = ParameterScanner.scan(TestConfig.class);
        Assert.assertEquals(8, list.size());
        ParameterMeta f1 = list.get(0);
        Assert.assertEquals(DataType.STRING, f1.getType());
        Assert.assertEquals("name1", f1.getName());
        Assert.assertEquals("display1", f1.getDisplay());
        Assert.assertEquals("description1", f1.getDescription());
        Assert.assertEquals("placeholder1", f1.getPlaceholder());
        Assert.assertEquals("op1", f1.getCandidates()[0]);
        Assert.assertEquals("op2", f1.getCandidates()[1]);
        Assert.assertEquals(false, f1.getRequired());
        Assert.assertEquals(true, f1.getPassword());

        ParameterMeta f2 = list.get(1);
        Assert.assertEquals(DataType.INT, f2.getType());
        Assert.assertEquals("f2", f2.getName());

        ParameterMeta f3 = list.get(2);
        Assert.assertEquals(DataType.LONG, f3.getType());
        Assert.assertEquals("f3", f3.getName());

        ParameterMeta f4 = list.get(3);
        Assert.assertEquals(DataType.FLOAT, f4.getType());
        Assert.assertEquals("f4", f4.getName());

        ParameterMeta f5 = list.get(4);
        Assert.assertEquals(DataType.DOUBLE, f5.getType());
        Assert.assertEquals("f5", f5.getName());

        ParameterMeta f6 = list.get(5);
        Assert.assertEquals(DataType.KV, f6.getType());
        Assert.assertEquals("f6", f6.getName());

        ParameterMeta f7 = list.get(6);
        Assert.assertEquals(DataType.LIST, f7.getType());
        Assert.assertEquals("f7", f7.getName());
        Assert.assertEquals(DataType.STRING, f7.getListParameter().getType());

        ParameterMeta f8 = list.get(7);
        Assert.assertEquals(DataType.LIST, f8.getType());
        Assert.assertEquals("f8", f8.getName());
        Assert.assertEquals(DataType.DOUBLE, f8.getListParameter().getType());
    }
}
