package project.smoothsaver.service;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import project.smoothsaver.dtos.SallingResponse;
import project.smoothsaver.entity.SallingStore;
import project.smoothsaver.entity.ShoppingCart;
import project.smoothsaver.repository.SallingStoreRepository;
import project.smoothsaver.repository.ShoppingCartRepository;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SallingService {

    @Value("${app.api-key-salling}")
    private String API_KEY;

    private final static String URL = "https://api.sallinggroup.com/v1/food-waste/";

    public static final Logger logger = LoggerFactory.getLogger(SallingService.class);
    private final WebClient client;
     SallingStoreRepository sallingStoreRepository;
     ShoppingCartRepository cartRepository;


    @Autowired
    public SallingService(SallingStoreRepository sallingStoreRepository) {
        this.client = WebClient.create();
        this.sallingStoreRepository = sallingStoreRepository;
    }
    //Use this constructor for testing, to inject a mock client
    public SallingService(WebClient client) {
        this.client = client;
    }


    public List<SallingResponse> getItemsOnSaleZip(String zip) {
        String err;
        try {

            List<SallingStore> stores = sallingStoreRepository.findSallingStoreByZip(zip);
            if(!stores.isEmpty()) {
                return stores.stream().map(SallingResponse::new).collect(Collectors.toList());
            }

            List<SallingResponse> response =  client.get()
                        .uri(new URI(URL + "?zip=" +  zip))
                    .header("Authorization", "Bearer " + API_KEY)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<SallingResponse>>() {})
                    .block();


            String id = response.get(0).getStore().getId();
            SallingStore DBResponse = sallingStoreRepository.findSallingStoreById(id);
            if(DBResponse == null) {
                sallingStoreRepository.saveAll(response.stream().map(
                        SallingStore::new).collect(Collectors.toList()));
            }

          return response;
        }  catch (WebClientResponseException e){
            //This is how you can get the status code and message reported back by the remote API
            logger.error("Error response body: " + e.getResponseBodyAsString());
            logger.error("WebClientResponseException", e);
            err = "Internal Server Error, due to a failed request to external service. You could try again" +
                    "( While you develop, make sure to consult the detailed error message on your backend)";
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, err);
        }
        catch (Exception e) {
            logger.error("Exception", e);
            err = "Internal Server Error - You could try again" +
                    "( While you develop, make sure to consult the detailed error message on your backend)";
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, err);
        }
    }

//

    public Page<SallingResponse.ItemOnSale> getItemOnSaleById(String id, Pageable pageable) {
        String err;
        try {

            SallingStore DBResponse = sallingStoreRepository.findSallingStoreById(id);

            if(!(DBResponse == null)) {
                SallingResponse response = new SallingResponse(DBResponse);
                int totalElements = response.getClearances().size();
                // Calculate the indices for the sublist
                int start = pageable.getPageNumber() * pageable.getPageSize();
                int end = Math.min(start + pageable.getPageSize(), totalElements);
                return new PageImpl<>(response.getClearances().subList(start, end), pageable, totalElements);
            }

            SallingResponse response = client.get()
                    .uri(new URI(URL + id))
                    .header("Authorization", "Bearer " + API_KEY)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(SallingResponse.class)
                    .block();

            int totalElements = response.getClearances().size();
            int start = pageable.getPageNumber() * pageable.getPageSize();
            int end = Math.min(start + pageable.getPageSize(), totalElements);

            // Return a sublist of clearances (ItemOnSale)
            return new PageImpl<>(response.getClearances().subList(start, end), pageable, totalElements);

        }  catch (WebClientResponseException e){
            //This is how you can get the status code and message reported back by the remote API
            logger.error("Error response body: " + e.getResponseBodyAsString());
            logger.error("WebClientResponseException", e);
            err = "Internal Server Error, due to a failed request to external service. You could try again" +
                    "( While you develop, make sure to consult the detailed error message on your backend)";
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, err);
        }
        catch (Exception e) {
            logger.error("Exception", e);
            err = "Internal Server Error - You could try again" +
                    "( While you develop, make sure to consult the detailed error message on your backend)";
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, err);
        }
    }

    public List<SallingResponse> getStoresCity(String city) {
        String url = "https://api.sallinggroup.com/v2/stores/?city=";
        String err;
        try {
            List<SallingResponse> response =  client.get()
                    .uri(new URI(url + city))
                    .header("Authorization", "Bearer " + API_KEY)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();
            return response;

        }  catch (WebClientResponseException e){
            //This is how you can get the status code and message reported back by the remote API
            logger.error("Error response body: " + e.getResponseBodyAsString());
            logger.error("WebClientResponseException", e);
            err = "Internal Server Error, due to a failed request to external service. You could try again" +
                    "( While you develop, make sure to consult the detailed error message on your backend)";
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, err);
        }
        catch (Exception e) {
            logger.error("Exception", e);
            err = "Internal Server Error - You could try again" +
                    "( While you develop, make sure to consult the detailed error message on your backend)";
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, err);
        }
    }

//    public void addItemToCart(String itemDescription, String storeId, int quantity, ShoppingCart cart, Pageable pageable) {
//        try {
//            Page<SallingResponse.ItemOnSale> itemPage = findItemByDescription(storeId, pageable, itemDescription);
//
//            if (!itemPage.isEmpty()) {
//                SallingResponse.ItemOnSale itemOnSaleApi = itemPage.getContent().get(0);
//
//                SallingStore.ItemOnSale itemOnSale = convertToItemOnSale(itemOnSaleApi, storeId);
//                itemOnSale.setQuantity(quantity);
//                cart.addItem(itemOnSale);
//            } else {
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
//            }
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding item to cart");
//        }
//    }

    public void addItemToCart(String itemDescription, String storeName, int quantity, int cartId, Pageable pageable, String zip) {
        try {
            String storeId = getStoreIdFromName(storeName, zip);

            Page<SallingResponse.ItemOnSale> itemPage = findItemByDescription(storeId, pageable, itemDescription);

            if (!itemPage.isEmpty()) {
                SallingResponse.ItemOnSale itemOnSaleApi = itemPage.getContent().get(0);
                SallingStore.ItemOnSale itemOnSale = convertToItemOnSale(itemOnSaleApi, storeId);
                itemOnSale.setQuantity(quantity);

                ShoppingCart cart = cartRepository.findById(cartId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));

                cart.addItem(itemOnSale);

                cartRepository.save(cart);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding item to cart");
        }
    }

    public List<SallingStore.ItemOnSale> getCartItems(int cartId) {
        ShoppingCart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));
        return cart.getAllItems();
    }

    public ShoppingCart getCartById(int cartId) {
        return cartRepository.findById(cartId).orElseThrow(() -> new EntityNotFoundException("Cart not found"));
    }

    public ShoppingCart saveCart(ShoppingCart cart) {
        return cartRepository.save(cart);
    }

    public void removeItemFromCart(int cartId, String itemDescription, int quantityToRemove) {
        ShoppingCart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
        cart.removeItem(itemDescription, quantityToRemove);
        cartRepository.save(cart);
    }


    private SallingStore.ItemOnSale convertToItemOnSale(SallingResponse.ItemOnSale itemOnSaleApi, String storeId) {
        SallingStore.ItemOnSale itemOnSale = new SallingStore.ItemOnSale();
        itemOnSale.setDescription(itemOnSaleApi.getProduct().getDescription());

        SallingResponse.Store store = new SallingResponse.Store();
        String storeName = store.getName(); // Or use reverse lookup method
        store.setName(storeName);

        return itemOnSale;
    }

    private Page<SallingResponse.ItemOnSale> findItemByDescription(String storeId, Pageable pageable, String description) {
        try {
            Page<SallingResponse.ItemOnSale> itemPage = getItemOnSaleById(storeId, pageable);

            if (!itemPage.isEmpty()) {
                List<SallingResponse.ItemOnSale> filteredItems = itemPage.getContent().stream()
                        .filter(item -> item.getProduct().getDescription() != null && item.getProduct().getDescription().contains(description))
                        .collect(Collectors.toList());

                int totalElements = filteredItems.size();
                int start = pageable.getPageNumber() * pageable.getPageSize();
                int end = Math.min(start + pageable.getPageSize(), totalElements);

                return new PageImpl<>(filteredItems.subList(start, end), pageable, totalElements);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No items found in the store");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching items by description");
        }
    }

    private String getStoreIdFromName(String storeName, String zip) {
        List<SallingResponse> itemsOnSale = getItemsOnSaleZip(zip);
        for (SallingResponse item : itemsOnSale) {
            if (item.getStore().getName().equals(storeName)) {
                return item.getStore().getId();
            }
        }
        throw new RuntimeException("Store not found");
    }

//    public SallingResponse.Store fetchStoreById(String storeId) {
//        try {
//            String storeUrl = URL + storeId;
//            SallingResponse.Store store = client.get()
//                    .uri(storeUrl)
//                    .header("Authorization", "Bearer " + API_KEY)
//                    .retrieve()
//                    .bodyToMono(SallingResponse.Store.class)
//                    .block();
//
//            return store;
//
//        } catch (WebClientResponseException e) {
//            throw new ResponseStatusException(e.getStatusCode(), "Error fetching store: " + e.getMessage());
//        }
//    }

//    public List<SallingStore.ItemOnSale> getAllCartItems() {
//        return cart.getAllItems();
//    }

//    public ShoppingCart getCartForUser(String username) {
//        // Check if the user already has a shopping cart
//        ShoppingCart cart = // logic to retrieve the shopping cart from a repository or database
//
//        if (cart == null) {
//            // If no cart exists, create a new one
//            cart = new ShoppingCart();
//            cart.setUserId(username); // Set the user ID or username to the cart
//            // Optionally save the new cart to the repository or database
//            // shoppingCartRepository.save(cart);
//        }
//
//        return cart;
//    }


}
