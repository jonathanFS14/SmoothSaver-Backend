package project.smoothsaver.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.smoothsaver.dtos.SallingResponse;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SallingStore {

    @OneToOne(mappedBy = "sallingStore", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private StoreAddress address;
    private String brand;
    @OneToMany(mappedBy = "sallingStore", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private List<StoreHours> hours;
    private String name;
    @Id
    private String id;
    private String type;
    @OneToMany(mappedBy = "sallingStore", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private List<ItemOnSale> clearances;

    @Getter
    @Setter
    @Entity
    @NoArgsConstructor
    public static class StoreAddress {

        public StoreAddress (String city, String country, String extra, String street, String zip){
        this.city = city;
        this.country = country;
        this.extra = extra;
        this.street = street;
        this.zip = zip;
        }

        @Id
        @Column(name = "sallingStore_id")
        private String id;

        @OneToOne
        @MapsId
        @JoinColumn(name = "sallingStore_id")
        private SallingStore sallingStore;

        private String city;
        private String country;
        private String extra;
        private String street;
        private String zip;


    }
    @Getter
    @Setter
    @Entity
    @NoArgsConstructor
    public static class StoreHours {

        public StoreHours (String date, String type, LocalDateTime open, LocalDateTime close, boolean closed){
            this.date = date;
            this.type = type;
            this.open = open;
            this.close = close;
            this.closed = closed;
        }

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @ManyToOne
        @JoinColumn(name = "sallingStore_id")
        private SallingStore sallingStore;

        private String date;
        private String type;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime open;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime close;
        private boolean closed;
    }

    @Getter
    @Setter
    @Entity
    @NoArgsConstructor
    public static class ItemOnSale {

        public ItemOnSale (String currency, double discount, String ean, LocalDateTime endTime, LocalDateTime lastUpdate, double newPrice, double originalPrice, double percentDiscount, LocalDateTime startTime, int stock, String stockUnit, String description, String image){
            this.currency = currency;
            this.discount = discount;
            this.ean = ean;
            this.endTime = endTime;
            this.lastUpdate = lastUpdate;
            this.newPrice = newPrice;
            this.originalPrice = originalPrice;
            this.percentDiscount = percentDiscount;
            this.startTime = startTime;
            this.stock = stock;
            this.stockUnit = stockUnit;
            this.description = description;
            this.image = image;
        }

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @ManyToOne()
        @JoinColumn(name = "sallingStore_id")
        private SallingStore sallingStore;

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
        private String description;
        private String image;
        private int quantity;
    }

    public SallingStore (SallingResponse response){
        this.address = new StoreAddress(response.getStore().getAddress().getCity(), response.getStore().getAddress().getCountry(), response.getStore().getAddress().getExtra(), response.getStore().getAddress().getStreet(), response.getStore().getAddress().getZip());
        this.address.setSallingStore(this);
        this.brand = response.getStore().getBrand();
        this.hours = response.getStore().getHours().stream().map(c -> new StoreHours(c.getDate(), c.getType(), c.getOpen(), c.getClose(), c.isClosed())).toList();
        this.hours.forEach(c -> c.setSallingStore(this));
        this.name = response.getStore().getName();
        this.id = response.getStore().getId();
        this.type = response.getStore().getType();
        this.clearances = response.getClearances().stream().map(c -> new ItemOnSale(c.getOffer().getCurrency(), c.getOffer().getDiscount(), c.getOffer().getEan(), c.getOffer().getEndTime(), c.getOffer().getLastUpdate(), c.getOffer().getNewPrice(), c.getOffer().getOriginalPrice(), c.getOffer().getPercentDiscount(), c.getOffer().getStartTime(), c.getOffer().getStock(), c.getOffer().getStockUnit(), c.getProduct().getDescription(), c.getProduct().getImage())).toList();
        this.clearances.forEach(c -> c.setSallingStore(this));
    }
}