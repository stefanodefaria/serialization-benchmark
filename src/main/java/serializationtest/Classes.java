package serializationtest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

public class Classes {

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = HouseAddress.class, name = "house"),
            @JsonSubTypes.Type(value = FarmAddress.class, name = "farm")
    })
    static abstract class Address implements Serializable {
        enum AddressType {
            house,
            farm
        }

        abstract AddressType getType();
    }

    static class FarmAddress extends Address {
        private String road;
        private int kilometer;

        public FarmAddress() {

        }

        @JsonCreator
        FarmAddress(@JsonProperty("road") String road, @JsonProperty("kilometer") int kilometer) {
            this.road = road;
            this.kilometer = kilometer;
        }

        public void setRoad(String road) {
            this.road = road;
        }

        public void setKilometer(int kilometer) {
            this.kilometer = kilometer;
        }

        public String getRoad() {
            return road;
        }

        public int getKilometer() {
            return kilometer;
        }

        @Override
        AddressType getType() {
            return AddressType.farm;
        }
    }


    static class HouseAddress extends Address {
        private String street;
        private int houseNumber;

        HouseAddress() {
        }

        @JsonCreator
        public HouseAddress(@JsonProperty("street") String street, @JsonProperty("houseNumber") int houseNumber) {
            this.street = street;
            this.houseNumber = houseNumber;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public void setHouseNumber(int houseNumber) {
            this.houseNumber = houseNumber;
        }

        public String getStreet() {
            return street;
        }

        public int getHouseNumber() {
            return houseNumber;
        }

        @Override
        AddressType getType() {
            return AddressType.house;
        }
    }
}
