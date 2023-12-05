package project.smoothsaver.api;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import project.smoothsaver.service.OpenAiService;
import project.smoothsaver.api.OpenAiController;
import project.smoothsaver.dtos.MyResponse;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OpenAiControllerTest {

    private OpenAiController controller;
    private OpenAiService service;
    private HttpServletRequest request;
    private ConcurrentHashMap<String, Bucket> bucketsField;

    @BeforeEach
    void setUp() {
        service = mock(OpenAiService.class);
        controller = new OpenAiController(service);
        bucketsField = new ConcurrentHashMap<>();
        request = mock(HttpServletRequest.class);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
    }

    @Test
    void testRateLimiting() {
        final String about = "test";
        // Exceed the number of requests allowed per IP
        // Assuming BUCKET_CAPACITY is 3 as per OpenAiController.BUCKET_CAPACITY
        for (int i = 0; i < 3; i++) {
            assertDoesNotThrow(() -> controller.getMealSuggestion(about, request));
        }

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            controller.getMealSuggestion(about, request);
        });

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, exception.getStatusCode());
    }

    @Test
    void testGetMealSuggestionReturnsCorrectResponse() {
        final String about = "test";
        final String expectedResponse = "Expected response";
        MyResponse mockResponse = new MyResponse(expectedResponse);
        when(service.makeRequest(anyString(), anyString())).thenReturn(mockResponse);
        MyResponse actualResponse = controller.getMealSuggestion(about, request);
        assertEquals(expectedResponse, actualResponse.getAnswer());
    }
    
    @Test
    void testCreateNewBucket() {
        String key = "testKey";
        bucketsField.put(key, controller.createNewBucket());
        Bucket bucket = bucketsField.get(key);
        assertNotNull(bucket, "Newly created bucket should not be null.");

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        assertTrue(probe.isConsumed(), "Bucket should allow consuming one token.");
    }

    @Test
    void testIfSystemMessageIsIncorporatedInResponse() {
        OpenAiService service = mock(OpenAiService.class);
        OpenAiController controller = new OpenAiController(service);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        MyResponse response = new MyResponse(OpenAiController.SYSTEM_MESSAGE);
        when(service.makeRequest(anyString(), eq(OpenAiController.SYSTEM_MESSAGE))).thenReturn(response);
        MyResponse returnedResponse = controller.getMealSuggestion("special pizza", request);
        assertEquals(OpenAiController.SYSTEM_MESSAGE, returnedResponse.getAnswer());
    }
}