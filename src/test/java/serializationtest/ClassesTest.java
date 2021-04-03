package serializationtest;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.databind.SmileMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.*;
import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static serializationtest.Classes.*;

public class ClassesTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ObjectMapper MSGPACK_MAPPER = new ObjectMapper(new MessagePackFactory());
    private static final SmileMapper SMILE_MAPPER = new SmileMapper();
    private static final Kryo KRYO = new Kryo();
    private static final int ITERATIONS = 1000 * 1000;

    @BeforeAll
    static void warmup() throws Exception {

        Instant warmupStart = Instant.now();
        for (int i = 0; i < 1000; i++) {
            serializeJson(MAPPER, i);
            serializeJson(MSGPACK_MAPPER, i);
            serializeJson(SMILE_MAPPER, i);
            serializationKryo(i);
            serializeJava(i);
        }
        System.out.println("warmup:" + Duration.between(warmupStart, Instant.now()).toMillis() + "ms");
    }

    @Test
    @Order(1)
    void testLengths() throws Exception {
        int msgpack = serializeJson(MSGPACK_MAPPER, 0);
        int smile = serializeJson(SMILE_MAPPER, 1);
        int jackson = serializeJson(MAPPER, 2);
        int java = serializeJava(3);
        int kryo = serializationKryo(4);

        System.out.println("msgpack: " + msgpack + " bytes");
        System.out.println("smile: " + smile + " bytes");
        System.out.println("jackson: " + jackson + " bytes");
        System.out.println("kyio: " + kryo + " bytes");
        System.out.println("java: " + java + " bytes");
    }

    @Test
    @Order(2)
    public void testSmile() throws Exception {

        Instant start = Instant.now();
        for (int i = 0; i < ITERATIONS; i++) {
            serializeJson(SMILE_MAPPER, i);
        }
        System.out.println("smile: " + Duration.between(start, Instant.now()).toMillis() + "ms");
    }

    @Test
    @Order(3)
    public void testMsgPack() throws Exception {

        Instant start = Instant.now();
        for (int i = 0; i < ITERATIONS; i++) {
            serializeJson(MSGPACK_MAPPER, i);
        }
        System.out.println("msgpack: " + Duration.between(start, Instant.now()).toMillis() + "ms");
    }

    @Test
    @Order(4)
    public void testJackson() throws Exception {

        Instant start2 = Instant.now();
        for (int i = 0; i < ITERATIONS; i++) {
            serializeJson(MAPPER, i);
        }
        System.out.println("jackson: " + Duration.between(start2, Instant.now()).toMillis() + "ms");
    }

    @Test
    @Order(5)
    public void testJava() throws Exception {

        Instant start3 = Instant.now();
        for (int i = 0; i < ITERATIONS; i++) {
            serializeJava(i);
        }
        System.out.println("java: " + Duration.between(start3, Instant.now()).toMillis() + "ms");
    }

    @Test
    @Order(5)
    public void testKryo() throws Exception {

        Instant start3 = Instant.now();
        for (int i = 0; i < ITERATIONS; i++) {
            serializationKryo(i);
        }
        System.out.println("kryio: " + Duration.between(start3, Instant.now()).toMillis() + "ms");
    }

    private static int serializeJson(ObjectMapper objectMapper, int i) throws IOException {
        FarmAddress farmAddress = new FarmAddress("estrada" + i, Integer.MAX_VALUE - i);
        byte[] serializedFarmAddress = objectMapper.writeValueAsBytes(farmAddress);
        Address deserialized = objectMapper.readerFor(Address.class).readValue(serializedFarmAddress);
        assertEquals(FarmAddress.class, deserialized.getClass());
        assertDeserialization(farmAddress, deserialized);

        HouseAddress houseAddress = new HouseAddress("rua" + i, Integer.MAX_VALUE - i);
        byte[] serializedHouseAddress = objectMapper.writeValueAsBytes(houseAddress);
        Address deserializedHouseAddress = objectMapper.readerFor(Address.class).readValue(serializedHouseAddress);
        assertDeserialization(houseAddress, deserializedHouseAddress);
        return serializedFarmAddress.length + serializedHouseAddress.length;
    }

    private static int serializeJava(int i) throws Exception {

        FarmAddress farmAddress = new FarmAddress("estrada" + i, Integer.MAX_VALUE - i);
        byte[] serialized = jSerialize(farmAddress);
        Address deserialized = jDeserialize(serialized);
        assertDeserialization(farmAddress, deserialized);

        HouseAddress houseAddress = new HouseAddress("rua" + i, Integer.MAX_VALUE - i);
        byte[] serializedHouseAddress = jSerialize(houseAddress);
        Address deserializedHouseAddress = jDeserialize(serializedHouseAddress);
        assertDeserialization(houseAddress, deserializedHouseAddress);

        return serialized.length + serializedHouseAddress.length;
    }

    private static int serializationKryo(int i) throws Exception {
        FarmAddress farmAddress = new FarmAddress("estrada" + i, Integer.MAX_VALUE - i);
        byte[] serialized = kSerialize(farmAddress);
        Address deserialized = kDeserialize(serialized);
        assertDeserialization(farmAddress, deserialized);

        HouseAddress houseAddress = new HouseAddress("rua" + i, Integer.MAX_VALUE - i);
        byte[] serializedHouseAddress = kSerialize(houseAddress);
        Address deserializedHouseAddress = kDeserialize(serializedHouseAddress);
        assertDeserialization(houseAddress, deserializedHouseAddress);

        return serialized.length + serializedHouseAddress.length;
    }

    private static byte[] kSerialize(Address address) {
        Output output = new Output();
        output.setBuffer(new byte[256]);
        KRYO.writeClassAndObject(output, address);
        return output.toBytes();
    }

    private static Address kDeserialize(byte[] serialized) throws Exception {
        Input input = new Input(serialized);
        return (Address) KRYO.readClassAndObject(input);
    }


    private static byte[] jSerialize(Address address) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream ous = new ObjectOutputStream(byteArrayOutputStream);
        ous.writeObject(address);
        return byteArrayOutputStream.toByteArray();
    }

    private static Address jDeserialize(byte[] serialized) throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serialized);
        ObjectInputStream ois = new ObjectInputStream(byteArrayInputStream);
        return (Address) ois.readObject();
    }

    private static void assertDeserialization(Address original, Address deserialized) {
        assertEquals(original.getClass(), deserialized.getClass());
        assertEquals(original.getType(), deserialized.getType());

    }

}