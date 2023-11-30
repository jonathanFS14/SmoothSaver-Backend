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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @GetMapping("cart/{cartId}")
    public List<SallingStore.ItemOnSale> getCartItems(@PathVariable int cartId) {
        return service.getCartItems(cartId);
    }

    @PostMapping("addToCart")
    public ResponseEntity<MyResponse> addItemToCart(@RequestBody ShoppingCartRequest request, Pageable pageable) {
        try {
            ShoppingCart cart = service.getCartById(request.getCartId());
            if (cart == null) {
                cart = new ShoppingCart(request.getStoreId(), new ArrayList<>());
            }

            service.addItemToCart(request.getItemDescription(), request.getStoreId(), request.getQuantity(), cart.getId(), pageable, request.getStoreId());

            ShoppingCart savedCart = service.saveCart(cart);

            return ResponseEntity.ok().body(new MyResponse("Item added to cart successfully", savedCart.getId()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding item to cart", e);
        }
    }

    @DeleteMapping("cart/{cartId}")
    public ResponseEntity<?> removeItemFromCart(@PathVariable int cartId, @RequestBody ShoppingCartRequest request) {
        try {
            service.removeItemFromCart(cartId, request.getItemDescription(), request.getQuantity());
            return ResponseEntity.ok().body("Item removed successfully");
        } catch (Exception e) {
            // Handle exceptions appropriately
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing item");
        }
    }


}



