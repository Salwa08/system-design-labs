package ma.xproce.loadbalancerdemo101.productservice;


import org.springframework.web.bind.annotation .*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class ProductController {

    private final AtomicInteger requestCount = new AtomicInteger (0);
    private final String instanceId =
            System.getenv().getOrDefault("INSTANCE_ID", "unknown");

    @GetMapping ("/products/{id}")
    public ProductResponse getProduct (@PathVariable String id)
            throws InterruptedException {
        Thread. sleep(50); //simulates real server work, e.g. a database call
        int count = requestCount.incrementAndGet ();
        return new ProductResponse (id, "Sample Product" + id,
                instanceId, count);

    }

    @GetMapping("/health")
    public String health() {
        return "OK from instance " + instanceId;

    }
}