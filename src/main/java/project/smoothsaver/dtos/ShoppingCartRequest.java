package project.smoothsaver.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShoppingCartRequest {
    private int cartId;
    private String itemDescription;
    private int quantity;
    private String storeId;
}
