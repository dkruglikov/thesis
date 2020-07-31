package ua.dp.dkruglikov.expert.system;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class RobJHyndmanForecast implements Forecast, AutoCloseable {

    private static final String[] RESOURCES = {
        "average",
        "naive",
        "seasonal-naive",
        "drift",
        "croston",
        "exponential-smooting",
        "neural-network",
        "holt-winters",
        "structural"};
    private static final System.Logger log = System.getLogger(RobJHyndmanForecast.class.getName());
    private final CloseableHttpClient client;

    public RobJHyndmanForecast() {
        client = HttpClients.createDefault();
    }
    
    @Override
    public void close() throws Exception {
        client.close();
    }

    @Override
    public Map<String, Double[]> forecast(Collection<Double> values) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, Double[]> forecast(SortedMap<Instant, Double> timeSerie) {
        Map<String, Double[]> result = new LinkedHashMap<>(RESOURCES.length);
        for (String resource : RESOURCES) {
            HttpPut request = new HttpPut("http://127.0.0.1:12080/" + resource);
            try {
                request.setEntity(
                        new StringEntity(
                                timeSerie.values().stream().map(Object::toString).collect(Collectors.joining(" "))));
                try (CloseableHttpResponse response = client.execute(request)) {
                    Double[] forecast = parseResponse(EntityUtils.toString(response.getEntity()));
                    result.put(resource.toUpperCase(), forecast);
                }
            } catch (IOException ex) {
                log.log(System.Logger.Level.ERROR, () -> "Unexpected error from " + resource, ex);
            }
        }
        return result;
    }

    private Double[] parseResponse(String response) {
        return Arrays.stream(response.substring(1, response.length() - 1).split(","))
                .map(Double::valueOf)
                .toArray(Double[]::new);
    }
}
