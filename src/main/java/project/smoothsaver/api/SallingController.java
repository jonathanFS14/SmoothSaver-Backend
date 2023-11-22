package project.smoothsaver.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import project.smoothsaver.dtos.SallingResponse;
import project.smoothsaver.service.SallingService;

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




}



