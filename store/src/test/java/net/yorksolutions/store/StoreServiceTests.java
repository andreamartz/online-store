package net.yorksolutions.store;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StoreServiceTests {
    @InjectMocks
    @Spy
    StoreService service;

    @Mock
    RestTemplate rest;

    @Mock
    ProductRepository repository;

    @Test
    // test checkAuthorized
    //     case: user is not authorized
    void itShouldThrowUnauthWhenOtherStatusIsUnauth() {
        final UUID token = UUID.randomUUID();
        String url = "http://localhost:8081/isAuthorized?token=" + token;
        // case: user is not authorized
        when(rest.getForEntity(url, Void.class)).thenReturn(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
        final ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> service.checkAuthorized(token));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }

    @Test
    // test checkAuthorized
    void itShouldThrowIntErrWhenOtherStatusIsOther() {
//        final UUID token = UUID.randomUUID();
//        String url = "http://localhost:8081/isAuthorized?token=" + token;
//        when(rest.getForEntity(url, Void.class))  // makes internal network request, not a REAL request
//                .thenReturn(new ResponseEntity<>(HttpStatus.CONFLICT));
//        final ResponseStatusException exception = assertThrows(ResponseStatusException.class,
//                () -> service.checkAuthorized(token));
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
    }

    @Test
    // test checkAuthorized
    void itShouldNotThrowWhenOtherStatusIsOK() {
//        final UUID token = UUID.randomUUID();
//        String url = "http://localhost:8081/isAuthorized?token=" + token;
//        when(rest.getForEntity(url, Void.class))
//                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
//        assertDoesNotThrow(() -> service.checkAuthorized(token));
    }

    //********** getAllProducts **********
//    @Test
//    // test getAllProducts
//    void itShouldThrowNotFoundWhenNoProductsExist() {
//        // var
//
//        // setup
//        when(repository.findAll()).thenReturn(List<Product>)
//        // assert & maybe call
//        service.getAllProducts()
//    }

    @Test
    // test getAllProducts
    void itShouldReturnAllProductsWhenThereIsAtLeastOneProduct() {
        // var

        // setup

        // assert & maybe call
    }

    @Test
    // test addProduct
    void itShouldThrowConflictWhenProductToAddExists() {
        String name = "some product";
        UUID token = UUID.randomUUID();
        HttpStatus expected = HttpStatus.CONFLICT;
//        ResponseStatusException expected = new ResponseStatusException(HttpStatus.CONFLICT);
        // product exists
        doNothing().when(service).checkAuthorized(any());
        when(repository.findByName(name)).thenReturn(Optional.of(new Product("product")));
        final ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> service.addProduct(token, name));
        assertEquals(expected, exception.getStatus());
    }

    @Test
    // test addProduct
    void itShouldAddANewProductWhenProductToAddDoesNotExistAlready() {
        // variables
        String name = "some product";
        UUID token = UUID.randomUUID();
        Product expected = new Product(name);
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);

        // setup
        doNothing().when(service).checkAuthorized(any());
        when(repository.findByName(name)).thenReturn(Optional.empty());
        when(repository.save(captor.capture())).thenReturn(new Product());

        // assertions
        assertDoesNotThrow(() -> service.addProduct(token, name));
        assertEquals(expected, captor.getValue());
    }

    @Test
    // test updateProduct
    void itShouldThrowBadRequestWhenProductToUpdateDoesNotExist() {
//        // variables
//        UUID token = UUID.randomUUID();
//        Long id = 1L;
//        HttpStatus expected = HttpStatus.BAD_REQUEST;
////        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
//
//        // setup
//        doNothing().when(service).checkAuthorized(any());
//        when(repository.existsById(id)).thenReturn(false);
//
//        // assert
//
//
//        // product exists
//
//        when(repository.findByName(name)).thenReturn(Optional.of(new Product("product")));
//        final ResponseStatusException exception = assertThrows(ResponseStatusException.class,
//                () -> service.addProduct(token, name));
//        assertEquals(expected, exception.getStatus());
    }

    @Test
    // test updateProduct
    void itShouldUpdateProductWhenProductToUpdateExists() {
        // variables
        Long id = 1L;
        // setup
        doNothing().when(service).checkAuthorized(any());
        when(repository.existsById(id)).thenReturn(false);

        // assert
    }

    @Test
    // test deleteProduct
    void itShouldThrowBadRequestWhenProductToDeleteDoesNotExist() {
        // var
        UUID token = UUID.randomUUID();
        Long id = 1L;
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        HttpStatus expected = HttpStatus.BAD_REQUEST;

        // setup
        doNothing().when(service).checkAuthorized(any());
        when(repository.existsById(id)).thenReturn(false);

        // assert
        final ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> service.deleteProduct(token, id));
        assertEquals(expected, exception.getStatus());
    }

    @Test
    // test deleteProduct
    void itShouldDeleteAProductWhenProductToDeleteExists() {
        // var
        UUID token = UUID.randomUUID();
        Long id = 1L;
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);

        // setup
        doNothing().when(service).checkAuthorized(any());
        when(repository.existsById(id)).thenReturn(true);
        doNothing().when(repository).deleteById(captor.capture());
        // assert
        assertDoesNotThrow(() -> service.deleteProduct(token, id));
        assertEquals(id, captor.getValue());
    }


}
