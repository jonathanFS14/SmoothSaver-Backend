package project.smoothsaver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    private String storeId;
    private String storeName;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "shopping_cart_id")
    private List<SallingStore.ItemOnSale> items = new ArrayList<>();

    public ShoppingCart(String storeId, List<SallingStore.ItemOnSale> items) {
        this.storeId = storeId;
        this.items = items;
    }

    public void addItem(SallingStore.ItemOnSale newItem) {
        boolean itemFound = false;
        for (SallingStore.ItemOnSale existingItem : items) {
            if (existingItem.getDescription().equals(newItem.getDescription())) {
                existingItem.setQuantity(existingItem.getQuantity() + newItem.getQuantity());
                itemFound = true;
                break;
            }
        }
        if (!itemFound) {
            items.add(newItem);
        }
    }

    public void removeItem(String itemDescription, int quantityToRemove) {
        for (SallingStore.ItemOnSale item : items) {
            if (item.getDescription().equals(itemDescription)) {
                int newQuantity = item.getQuantity() - quantityToRemove;
                if (newQuantity <= 0) {
                    items.remove(item);
                } else {
                    item.setQuantity(newQuantity);
                }
                break;
            }
        }
    }

    public List<SallingStore.ItemOnSale> getAllItems() {
        return new ArrayList<>(items);
    }
}
