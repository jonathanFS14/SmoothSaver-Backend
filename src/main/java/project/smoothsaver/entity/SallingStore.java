package project.smoothsaver.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private List<StoreHours> hours;
    private String name;
    @Id
    private String id;
    private String type;
    @OneToMany(mappedBy = "sallingStore", cascade = CascadeType.ALL)
    private List<ItemOnSale> clearances;

    @Getter
    @Setter
    @Entity
    public static class StoreAddress {

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
    public static class StoreHours {

        @Id
        @Column(name = "sallingStore_id")
        private String id;

        @ManyToOne
        @MapsId
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
    public static class ItemOnSale {

        @Id
        @Column(name = "sallingStore_id")
        private String id;

        @ManyToOne
        @MapsId
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
    }

}