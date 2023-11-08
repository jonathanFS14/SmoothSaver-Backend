package project.smoothsaver.api;

import org.springframework.web.bind.annotation.*;
import project.smoothsaver.dtos.SallingResponse;
import project.smoothsaver.service.SallingService;

import java.util.List;

@RestController
@RequestMapping("/api/salling")
@CrossOrigin(origins = "*")
public class SallingController {

    private final SallingService service;

    public SallingController(SallingService service) {
        this.service = service;
    }

    @GetMapping("/{zip}")
    public List<SallingResponse> getItemsOnSaleByZip(@PathVariable String zip) {
        return service.getItemsOnSale(zip);
    }

}



