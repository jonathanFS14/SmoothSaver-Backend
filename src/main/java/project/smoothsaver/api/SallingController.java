package project.smoothsaver.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.smoothsaver.dtos.SallingResponse;
import project.smoothsaver.dtos.ShoppingCartRequest;
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

    @PostMapping("addToCart")
    public ResponseEntity<Void> addItemToCart(@RequestBody ShoppingCartRequest request, Pageable pageable) {
        String storeId = request.getStoreId();
        // Temporarily create a new ShoppingCart instance for testing
        ShoppingCart cart = new ShoppingCart();
        // Optionally set the username or other user-related information on the cart
        // cart.setUserId(username);

        // Call the service method to add the item to the cart
        service.addItemToCart(request.getItemDescription(), storeId, request.getQuantity(), cart, pageable);

        return ResponseEntity.ok().build();
    }

}



