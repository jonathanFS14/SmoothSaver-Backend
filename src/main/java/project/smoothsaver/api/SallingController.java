package project.smoothsaver.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import project.smoothsaver.dtos.MyResponse;
import project.smoothsaver.dtos.SallingResponse;
import project.smoothsaver.dtos.ShoppingCartRequest;
import project.smoothsaver.entity.SallingStore;
import project.smoothsaver.entity.ShoppingCart;
import project.smoothsaver.service.SallingService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/salling/")
@CrossOrigin(origins = "*")
public class SallingController {

    private final SallingService service;

    public SallingController(SallingService service) {
        this.service = service;
    }

    @GetMapping("zip/{zip}")
    public List<SallingResponse> getStoresWithItemsOnSaleByZip(@PathVariable String zip) {
        return service.getItemsOnSaleZip(zip);
    }

    @GetMapping("id/{id}")
    public Page<SallingResponse.ItemOnSale> getStoreWithItemOnSaleById(@PathVariable String id, Pageable pageable) {
        return service.getItemOnSaleById(id, pageable);
    }

    @GetMapping("city/{city}")
    public List<SallingResponse> getStoresByCity(@PathVariable String city) {
        return service.getStoresCity(city);
    }

//    @GetMapping("cart/items")
//    public List<SallingStore.ItemOnSale> getAllCartItems() {
//        return service.getAllCartItems();
//    }

//    @GetMapping("store/{storeId}")
//    public SallingResponse.Store fetchStoreById(@PathVariable String storeId) {
//        return service.fetchStoreById(storeId);
//    }

//    @GetMapping("store/{storeId}")
//    public String fetchStoreById(@PathVariable String storeId) {
//        return service.getStoreNameById(storeId);
//    }

    @PostMapping("addToCart")
    public ResponseEntity<MyResponse> addItemToCart(@RequestBody ShoppingCartRequest request, Pageable pageable, String storeName) {
        try {
            ShoppingCart cart = new ShoppingCart();
            service.addItemToCart(request.getItemDescription(), request.getStoreId(), request.getQuantity(), cart, pageable, storeName);
            return ResponseEntity.ok().body(new MyResponse("Item added to cart successfully"));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding item to cart");
        }
    }
}



