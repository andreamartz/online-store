package net.yorksolutions.store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
public class StoreControllerTests {
    @LocalServerPort
    int port;

    @Autowired
    StoreController controller;

    @Mock
    StoreService service;

    @BeforeEach
    void setup() {
        controller.setService(service);
    }

    @Test
    void itShouldCallGetAllProductsAndReturnIterable() {
        Product p1 = new Product("name1");
        Product p2 = new Product("name2");
        p1.setId(1L);
        p2.setId(2L);
        Product[] products = new Product[] {p1, p2};
        TestRestTemplate rest = new TestRestTemplate();
        UUID token = UUID.randomUUID();

        // List is an interface that extends Collection;
        // Collection is an interface that extends Iterable
        when(service.getAllProducts(token)).thenReturn(List.of(products));
        String url = "http://localhost:" + port + "/getAllProducts?token=" + token;

        final ResponseEntity<Product[]> response = rest.getForEntity(url, Product[].class);

        assertArrayEquals(products, response.getBody());
    }

    @Test
    void itShouldCallAddProductWithTheTokenAndTheProductName() {
        final TestRestTemplate rest = new TestRestTemplate();
        final var token = UUID.randomUUID();
        final String name = "phone";
        final HttpStatus expected = HttpStatus.ACCEPTED;
        final String url = "http://localhost:" + port + "/addProduct?token=" + token + "&name=" + name;

        doThrow(new ResponseStatusException(HttpStatus.ACCEPTED)).when(service).addProduct(token, name);
        final ResponseEntity<Void> response = rest.getForEntity(url, Void.class);

        assertEquals(expected, response.getStatusCode());
    }

//    @Test
//    void itShouldCallUpdateProductWithTheTokenAndProductIdAndNewProductName() {
//
//    }

//    @Test
//    void itShouldCallDeleteProductWithTokenAndProductId() {
//
//    }
}
