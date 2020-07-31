package ua.dp.dkruglikov.expert.system;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;

public interface Forecast {

    Map<String, Double[]> forecast(Collection<Double> values);

    Map<String, Double[]> forecast(SortedMap<Instant, Double> timeSerie);
}
