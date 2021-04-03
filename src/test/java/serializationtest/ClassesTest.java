package serializationtest;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.databind.SmileMapper;
import org.junit.jupiter.api.Test;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.io.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static serializationtest.Classes.*;

@SuppressWarnings({"unused", "JUnit3StyleTestMethodInJUnit4Class"})
public class ClassesTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ObjectMapper MSGPACK_MAPPER = new ObjectMapper(new MessagePackFactory());
    private static final SmileMapper SMILE_MAPPER = new SmileMapper();
    private static final Kryo KRYO = new Kryo();
    private static final Random RANDOM = new Random();

    @Test
    void benchmark() throws RunnerException {
        Options options = new OptionsBuilder()
                .include(this.getClass().getName() + ".*")
                .mode(Mode.AverageTime)
                .warmupTime(TimeValue.seconds(1))
                .warmupIterations(1)
                .threads(1)
                .measurementIterations(3)
                .forks(1)
                .shouldFailOnError(true)
                .build();

        new Runner(options).run();
    }

    @Test
    void testLengths() throws Exception {
        int msgpack = serializeJson(MSGPACK_MAPPER, 1);
        int smile = serializeJson(SMILE_MAPPER, 1);
        int jackson = serializeJson(MAPPER, 1);
        int java = serializeJava(1);
        int kryo = serializationKryo(1);

        System.out.println("msgpack: " + msgpack + " bytes");
        System.out.println("smile: " + smile + " bytes");
        System.out.println("jackson: " + jackson + " bytes");
        System.out.println("kyio: " + kryo + " bytes");
        System.out.println("java: " + java + " bytes");
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testJackson() throws Exception {
        serializeJson(MAPPER, null);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testSmile() throws Exception {
        serializeJson(SMILE_MAPPER, null);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testMsgPack() throws Exception {
        serializeJson(MSGPACK_MAPPER, null);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testJava() throws Exception {
        serializeJava(null);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testKryo() {
        serializationKryo(null);
    }

    private static int serializeJson(ObjectMapper objectMapper, Integer value) throws IOException {
        int i = value != null ? value : RANDOM.nextInt();
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

    private static int serializeJava(Integer value) throws Exception {
        int i = value != null ? value : RANDOM.nextInt();
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

    private static int serializationKryo(Integer value) {
        int i = value != null ? value : RANDOM.nextInt();
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

    private static Address kDeserialize(byte[] serialized) {
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