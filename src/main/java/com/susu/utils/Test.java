package com.susu.utils;

import com.susu.serializer.Bar;
import com.susu.serializer.Baz;
import com.susu.serializer.Foo;
import com.susu.serializer.taira.Taira;
import com.susu.serializer.taira.TairaData;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * <p>Description:测试类</p>
 * @author sujay
 * @version  0:00 2022/1/24
 */
public class Test {


    public static void main(String[] args) {
        Taira.DEBUG = true;

        // Init object
        Foo foo = new Foo();
        foo.setByteField((byte) 2);
        foo.setIntField(103);
        foo.setCharField('$');
        foo.setDoubleField(123.21);
        foo.setBytesField(new byte[] { 11, 22, 33, 44 });
        foo.setStringField("world");
        foo.setRemainsStringField("Hi Ro Hi Ro Ma U Hi Ka Ri No Na Ka");

        Bar bar = new Bar();
        bar.setFloatVal(123.2f);
        bar.setShortVal((short) 11);
        bar.setLongVal(1242354L);
        bar.setBooleanVal(true);

        Baz[] bazArray = new Baz[3];
        bazArray[0] = new Baz(1);
        bazArray[1] = new Baz(3);
        bazArray[2] = new Baz(5);

        bar.setInnerArrayVal(bazArray);
        foo.setBarField(bar);
        foo.setIntListField(Arrays.asList(3, 5, 9));

        System.out.println("fooObject: " + foo.toString());

        // Taira serialize
        taira(foo);

    }

    /**
     * Use Taira serialize/deserialize 1000 times
     */
    private static void taira(TairaData data) {

        long serializeStart = System.currentTimeMillis();
        byte[] fooBytes = new byte[0];

        for (int i = 0; i < 1000; i++) {
            fooBytes = Taira.DEFAULT.toBytes(data);
        }

        System.out.println("Taira serialize x 1000 time cost: " + (System.currentTimeMillis() - serializeStart));
        System.out.println("Taira serialize data size: " + fooBytes.length);

        Taira deserializeTaira = new Taira(ByteOrder.BIG_ENDIAN);
        long deserializeStart = System.currentTimeMillis();
        Foo foo = null;

        for (int i = 0; i < 1000; i++) {
            foo = deserializeTaira.fromBytes(fooBytes, Foo.class);
        }

        System.out.println("Taira deserialize x 1000 time cost: " + (System.currentTimeMillis() - deserializeStart));
        System.out.println("Taira deserialize result: " + String.valueOf(foo));
    }

}
