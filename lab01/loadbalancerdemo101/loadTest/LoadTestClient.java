import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
public class LoadTestClient {
    public static void main(String[] args) throws Exception {
        String targetUrl = args.length > 0 ? args[0]
                : "http://localhost:8081/products/1";
        int totalRequests = args.length > 1 ? Integer.parseInt(args[1]) :
                500;
        int concurrency = args.length > 2 ? Integer.parseInt(args[2]) :
                20;
        HttpClient client = HttpClient.newHttpClient();
        ExecutorService executor =
                Executors.newFixedThreadPool(concurrency);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        AtomicLong totalLatencyMs = new AtomicLong(0);
        long startTime = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(totalRequests);
        for (int i = 0; i < totalRequests; i++) {
            executor.submit(() -> {
                long reqStart = System.currentTimeMillis();
                try {
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(targetUrl))

                            .GET()
                            .build();

                    HttpResponse<String> response = client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString());
                    if (response.statusCode() == 200) {
                        successCount.incrementAndGet();
                    } else {
                        failureCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                } finally {
                    totalLatencyMs.addAndGet(
                            System.currentTimeMillis() - reqStart);
                    latch.countDown();
                }
            });
        }
        latch.await();
        long endTime = System.currentTimeMillis();
        executor.shutdown();
        double durationSeconds = (endTime - startTime) / 1000.0;
        double throughput = totalRequests / durationSeconds;
        double avgLatency = totalLatencyMs.get() / (double) totalRequests;
        System.out.println("=== Load Test Results ===");
        System.out.println("Target: " + targetUrl);
        System.out.println("Total requests: " + totalRequests
                + " (concurrency: " + concurrency + ")");
        System.out.println("Successes: " + successCount.get()
                + ", Failures: " + failureCount.get());
        System.out.printf("Total duration: %.2f sec%n", durationSeconds);
        System.out.printf("Throughput: %.2f requests/sec%n", throughput);
        System.out.printf("Average latency: %.2f ms%n", avgLatency);
    }
}