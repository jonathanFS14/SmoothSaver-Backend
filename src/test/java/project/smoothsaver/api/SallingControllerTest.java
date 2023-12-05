package project.smoothsaver.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import project.smoothsaver.dtos.SallingResponse;
import project.smoothsaver.entity.SallingStore;
import project.smoothsaver.service.SallingService;
import project.smoothsaver.api.SallingController;
import project.smoothsaver.entity.SallingStore;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;

class SallingControllerTest {

    private SallingService service;
    private SallingController controller;
    
    @BeforeEach
    void setup() {
        service = mock(SallingService.class);
        controller = new SallingController(service);
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
}
