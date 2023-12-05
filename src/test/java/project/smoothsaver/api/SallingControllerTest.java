package project.smoothsaver.api;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import project.smoothsaver.dtos.SallingResponse;
import project.smoothsaver.entity.SallingStore;
import project.smoothsaver.service.SallingService;
import project.smoothsaver.api.SallingController;
import project.smoothsaver.dtos.ShoppingCartRequest;
import project.smoothsaver.entity.ShoppingCart;
import project.smoothsaver.dtos.MyResponse;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.HttpStatus.OK;

class SallingControllerTest {

    private SallingService service;
    private SallingController controller;
    private Pageable pageable;
    
    @BeforeEach
    void setup() {
        service = mock(SallingService.class);
        controller = new SallingController(service);
        pageable = mock(Pageable.class);
    }


    @Test
    void testGetStoresWithItemsOnSaleByZip() {
        SallingService service = mock(SallingService.class);
        SallingController controller = new SallingController(service);
        List<SallingResponse> expectedResponse = mock(List.class);

        when(service.getItemsOnSaleZip(anyString())).thenReturn(expectedResponse);

        List<SallingResponse> actualResponse = controller.getStoresWithItemsOnSaleByZip("12345");
        assertSame(expectedResponse, actualResponse);
    }

    @Test
    void testGetStoreWithItemOnSaleById() {
        SallingService service = mock(SallingService.class);
        SallingController controller = new SallingController(service);
        Page<SallingResponse.ItemOnSale> expectedResponse = mock(Page.class);
        Pageable pageable = mock(Pageable.class);

        when(service.getItemOnSaleById(anyString(), any(Pageable.class))).thenReturn(expectedResponse);

        Page<SallingResponse.ItemOnSale> actualResponse = controller.getStoreWithItemOnSaleById("storeId", pageable);
        assertSame(expectedResponse, actualResponse);
    }

    @Test
    void testGetStoresByCity() {
        SallingService service = mock(SallingService.class);
        SallingController controller = new SallingController(service);
        List<SallingResponse> expectedResponse = mock(List.class);

        when(service.getStoresCity(anyString())).thenReturn(expectedResponse);

        List<SallingResponse> actualResponse = controller.getStoresByCity("Copenhagen");
        assertSame(expectedResponse, actualResponse);
    }

    @Test
    void testGetCartItems() {
        SallingService service = mock(SallingService.class);
        SallingController controller = new SallingController(service);
        List<SallingStore.ItemOnSale> expectedResponse = mock(List.class);

        when(service.getCartItems(anyInt())).thenReturn(expectedResponse);

        List<SallingStore.ItemOnSale> actualResponse = controller.getCartItems(1);
        assertSame(expectedResponse, actualResponse);
    }

    @Test
    void testGetStoresWithItemsOnSaleByZip_ExceptionHandling() {
        when(service.getItemsOnSaleZip(anyString())).thenThrow(new RuntimeException("Exception for zip"));
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            controller.getStoresWithItemsOnSaleByZip("12345");
        });
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ((ResponseStatusException)exception).getStatusCode());
    }
    
    @Test
    void testGetStoreWithItemOnSaleById_ExceptionHandling() {
        when(service.getItemOnSaleById(anyString(), any(Pageable.class))).thenThrow(new RuntimeException("Exception for id"));
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            controller.getStoreWithItemOnSaleById("storeId", pageable);
        });
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ((ResponseStatusException)exception).getStatusCode());
    }
    
    @Test
    void testGetStoresByCity_ExceptionHandling() {
        when(service.getStoresCity(anyString())).thenThrow(new RuntimeException("Exception for city"));
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            controller.getStoresByCity("Copenhagen");
        });
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ((ResponseStatusException)exception).getStatusCode());
    }
    
    @Test
    void testGetCartItems_ExceptionHandling() {
        when(service.getCartItems(anyInt())).thenThrow(new RuntimeException("Exception for cart"));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.getCartItems(1);
        });
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }
    
    @Test
    void testAddItemToCart_ExceptionHandling() {
        when(service.getCartById(anyInt())).thenThrow(new RuntimeException("Exception adding item"));
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            controller.addItemToCart(new ShoppingCartRequest(), pageable);
        });
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ((ResponseStatusException)exception).getStatusCode());
    }    
}