package net.yorksolutions.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class StoreController {
    private StoreService service;

    @Autowired
    public StoreController(@NonNull StoreService service) {
        this.service = service;
    }

    // not tested
    @GetMapping("/getAllProducts")
    @CrossOrigin
    public List<Product> getAllProducts() {
//        List<Product> products = service.getAllProducts();
        return service.getAllProducts();
    }

    @GetMapping("/addProduct")
    @CrossOrigin
    public void addProduct(@RequestParam UUID token, @RequestParam String name) {
        service.addProduct(token, name);
    }

    public void setService(StoreService service) {
        this.service = service;
    }
}
