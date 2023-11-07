package project.smoothsaver.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class SallingResponse {
    private List<Supermarket> supermarkets;

    @Getter
    @Setter
    public static class Supermarket {
        private List<ItemOnSale> clearances;
        private Store store;
    }

    @Getter
    @Setter
    public static class Store {
        private StoreAddress address;
        private String brand;
        private List<Double> coordinates;
        private List<StoreHours> hours;
        private String name;
        private String id;
        private String type;
    }

    @Getter
    @Setter
    public static class StoreAddress {
        private String city;
        private String country;
        private String extra;
        private String street;
        private String zip;
    }

    @Getter
    @Setter
    public static class StoreHours {
        private String date;
        private String type;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        private LocalDateTime open;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        private LocalDateTime close;
        private boolean closed;
    }

    @Getter
    @Setter
    public static class ItemOnSale {
        private Offer offer;
        private Product product;
    }

    @Getter
    @Setter
    public static class Offer {
        private String currency;
        private double discount;
        private String ean;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        private LocalDateTime endTime;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        private LocalDateTime lastUpdate;
        private double newPrice;
        private double originalPrice;
        private double percentDiscount;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        private LocalDateTime startTime;
        private int stock;
        private String stockUnit;
    }

    @Getter
    @Setter
    public static class Product {
        private String description;
        private String ean;
        private String image;
    }
}
