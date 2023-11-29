package project.smoothsaver.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.smoothsaver.entity.SallingStore;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class SallingResponse {


        private List<ItemOnSale> clearances;
        private Store store;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Store {
        private StoreAddress address;
        private String brand;
        private List<Double> coordinates;
        private List<StoreHours> hours;
        private String name;
        private String id;
        private String type;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoreAddress {
        private String city;
        private String country;
        private String extra;
        private String street;
        private String zip;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoreHours {
        private String date;
        private String type;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime open;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime close;
        private boolean closed;
    }
    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemOnSale {
        private Offer offer;
        private Product product;
        private int quantity;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
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
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Product {
        private String description;
        private String ean;
        private String image;
    }
    }

    public SallingResponse(SallingStore store) {
        this.store = new Store();
        this.store.setAddress(new Store.StoreAddress(
                store.getAddress().getCity(),
                store.getAddress().getCountry(),
                store.getAddress().getExtra(),
                store.getAddress().getStreet(),
                store.getAddress().getZip()
        ));
        this.store.setBrand(store.getBrand());
        this.store.setName(store.getName());
        this.store.setId(store.getId());
        this.store.setType(store.getType());

        // Map StoreHours
        this.store.setHours(store.getHours().stream()
                .map(h -> new Store.StoreHours(h.getDate(), h.getType(), h.getOpen(), h.getClose(), h.isClosed()))
                .collect(Collectors.toList()));

        // Map ItemOnSale
        this.clearances = store.getClearances().stream()
                .map(item -> {
                    ItemOnSale.Offer offer = new ItemOnSale.Offer(
                            item.getCurrency(),
                            item.getDiscount(),
                            item.getEan(),
                            item.getEndTime(),
                            item.getLastUpdate(),
                            item.getNewPrice(),
                            item.getOriginalPrice(),
                            item.getPercentDiscount(),
                            item.getStartTime(),
                            item.getStock(),
                            item.getStockUnit()
                    );
                    ItemOnSale.Product product = new ItemOnSale.Product(item.getDescription(), item.getEan(), item.getImage());
                    return new ItemOnSale(offer, product, item.getQuantity());
                })
                .collect(Collectors.toList());
    }



}
