package project.smoothsaver.api;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import project.smoothsaver.dtos.MyResponse;
import project.smoothsaver.service.OpenAiService;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/openai")
@CrossOrigin(origins = "*")
public class OpenAiController {

    private OpenAiService service;
//test
    public OpenAiController(OpenAiService service) {
        this.service = service;
    }

    private final int BUCKET_CAPACITY = 3;
    private final int REFILL_AMOUNT = 3;
    private final int REFILL_TIME = 2;

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.classic(BUCKET_CAPACITY, Refill.greedy(REFILL_AMOUNT, Duration.ofMinutes(REFILL_TIME)));
        return Bucket.builder().addLimit(limit).build();
    }

    private Bucket getBucket(String key) {
        return buckets.computeIfAbsent(key, k -> createNewBucket());
    }

    final static String SYSTEM_MESSAGE = "you are a helping assistant that provides meal suggestions based on a list of ingredients";


    @GetMapping("/limited")
    public MyResponse getMealSuggestion(@RequestParam String about, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        Bucket bucket = getBucket(ip);
        if (!bucket.tryConsume(1)) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests, try again later");
        }
        return service.makeRequest(about,SYSTEM_MESSAGE);
    }
}


