package ua.dp.dkruglikov.expert.system;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.json.bind.JsonbBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import ua.dp.dkruglikov.expert.system.dto.TimeSerieDto;

public class XTSForecast implements Forecast, AutoCloseable {

    private static final String[] RESOURCES = {"xts/arima", "xts/neural"};
    private static final System.Logger log = System.getLogger(XTSForecast.class.getName());
    private final CloseableHttpClient client;

    public XTSForecast() {
        client = HttpClients.createDefault();
    }

    @Override
    public void close() throws Exception {
        client.close();
    }

    @Override
    public Map<String, Double[]> forecast(Collection<Double> values) {
        Instant i = Instant.ofEpochMilli(0);
        SortedMap<Instant, Double> timeSerie = new TreeMap<>();
        for (Double value : values) {
            timeSerie.put(i, value);
            i = i.plus(1, ChronoUnit.DAYS);
        }
        return forecast(timeSerie);
    }

    @Override
    public Map<String, Double[]> forecast(SortedMap<Instant, Double> timeSerie) {
        TimeSerieDto dto = new TimeSerieDto();
        dto.setTimestamps(
                timeSerie.keySet()
                        .stream()
                        .map(i -> LocalDateTime.ofInstant(i, ZoneOffset.UTC))
                        .map(ldt -> ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                        .collect(Collectors.joining(";")));
        dto.setValues(
                timeSerie.values()
                        .stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(";")));
        Map<String, Double[]> result = new HashMap<>(RESOURCES.length);
        for (String resource : RESOURCES) {
            HttpPut request = new HttpPut("http://127.0.0.1:12080/" + resource);
            try {
                String jsonDto = JsonbBuilder.create().toJson(dto);
                request.setEntity(new StringEntity(jsonDto));
                try (CloseableHttpResponse response = client.execute(request)) {
                    Double[] forecast = parseResponse(EntityUtils.toString(response.getEntity()));
                    result.put(resource.toUpperCase(), forecast);
                }
            } catch (IOException ex) {
                log.log(System.Logger.Level.ERROR, () -> "Unexpected error from xts", ex);
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
