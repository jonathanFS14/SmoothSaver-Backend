package project.smoothsaver.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class SallingResponse {
    List<supermarket> supermarkets;

    @Getter
    @Setter
    public static class supermarket{
    private List<ItemOnSale> clearances;
    private store store;

    @Getter
    @Setter
    public static class store{
        private address address;
        private String name;
        @Getter
        @Setter
        public static class address{
            private String city;
            private String street;
            private String zip;
        }
    }


    @Getter
    @Setter
    public static class ItemOnSale {
        private offer offer;
        private product product;
        @Getter
        @Setter
        public static class offer {
            private double discount;
            private double newPrice;
            private double originalPrice;
            private double percentDiscount;
            private LocalDateTime endTime;
            private int stock;
        }
        @Getter
        @Setter
        public static class product{
            private String description;
            private String image;
        }
    }
    }

}
