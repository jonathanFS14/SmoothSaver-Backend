package project.smoothsaver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ShoppingCart {

    private String storeId;
    private String storeName;
    private List<SallingStore.ItemOnSale> items;

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
