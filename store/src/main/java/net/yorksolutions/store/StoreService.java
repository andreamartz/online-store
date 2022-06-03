package net.yorksolutions.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class StoreService {
    private ProductRepository repository;
    private final RestTemplate rest;

    @Autowired
    // for Spring
    public StoreService(@NonNull ProductRepository repository) {
        this.repository = repository;
        this.rest = new RestTemplate();
    }

//     for mockito
    public StoreService(ProductRepository repository, RestTemplate rest) {
        this.repository = repository;
        this.rest = rest;
    }

    public void checkAuthorized(UUID token) {
        String url = "http://localhost:8081/isAuthorized?token=" + token;
        // ResponseEntity - represents the response (in this case the response body has type Void)
        // rest.getForEntity
        // <Void> claas means we're ignoring the response body
        final ResponseEntity<Void> response = rest.getForEntity(url, Void.class);
//         response.getBody() to get the body (when body is of class Void)

        switch (response.getStatusCode()) {
            case OK:
                return;

            case UNAUTHORIZED:
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

            default:
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    public void checkForOwner(UUID token) {
//        String url = "http://localhost:8081/isOwner?token=" + token;
//
////        final ResponseEntity<Void> response = rest.get
//    }

//     https://www.baeldung.com/java-iterable-to-collection#2-iterator-to-collection
//     public List<Product> getAllProducts() {
//        List<Product> productList = new ArrayList<>();
//        List<Product> products = repository.findAll();
//        if (products.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No products found");
//        }
//        products.forEach(productList::add);
//        return productList;
//    }
//     https://www.baeldung.com/java-iterable-to-collection#2-iterator-to-collection
    public List<Product> getAllProducts() {
//    List<Product> productList = new ArrayList<>();
    List<Product> products = repository.findAll();
//    if (products.isEmpty()) {
//        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No products found");
//    }
//    products.forEach(productList::add);
//    return productList;
    return products;
}

    public void addProduct(UUID token, String name) {
        Product newProduct = new Product(name);

        checkAuthorized(token);

        // if product already in table, throw exception
        if (repository.findByName(name).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        // if not, add the product
        else
            repository.save(newProduct);
    }

    public void updateProduct(UUID token, Long id, String newName) {
        checkAuthorized(token);

        // if product not in table, throw exception
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else {
//            repository.
        }
    }

    public void deleteProduct(UUID token, Long id) {
        checkAuthorized(token);

        // if product not in table, throw exception
        if (!repository.existsById(id)) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else {
            repository.deleteById(id);
        }
    }
}
