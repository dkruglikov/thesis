package ua.dp.dkruglikov.expert.system;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Main {

    private static final System.Logger log = System.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException {
        processSinusoid();
    }

    private static void processSinusoid() {
        List<Double> sinusoid = new ArrayList<>(73);
        for (byte i = 0; i < 73; i++) {
            double value = Math.sin(i * Math.PI / 6);
            if ((i & 1) == 1) {
                value *= 1 + 0.1 * Math.random();
            }
            sinusoid.add(value);
        }
        System.out.println("ACTUAL: " + sinusoid.subList(37, 73));
        Forecast forecast = new XTSForecast();
        log.log(
                System.Logger.Level.INFO,
                () -> forecast.forecast(sinusoid.subList(0, 37))
                        .entrySet()
                        .stream()
                        .map(entry -> entry.getKey() + ": " + Arrays.toString(entry.getValue()))
                        .collect(Collectors.joining(System.lineSeparator(), System.lineSeparator(), "")));
    }
    
    private static void processRealData() throws IOException {
        String timestamps = "2020-07-05 00:55;"
                + "2020-07-05 01:55;"
                + "2020-07-05 02:55;"
                + "2020-07-05 03:55;"
                + "2020-07-05 04:24;"
                + "2020-07-05 11:35;"
                + "2020-07-05 12:34;"
                + "2020-07-05 13:12;"
                + "2020-07-05 16:57;"
                + "2020-07-05 17:58;"
                + "2020-07-05 18:58;"
                + "2020-07-05 19:58;"
                + "2020-07-05 20:59;"
                + "2020-07-05 21:58;"
                + "2020-07-05 22:59;"
                + "2020-07-05 23:57;"
                + "2020-07-06 00:58;"
                + "2020-07-06 01:59;"
                + "2020-07-06 02:58;"
                + "2020-07-06 03:58;"
                + "2020-07-06 04:28;"
                + "2020-07-06 07:35;"
                + "2020-07-06 08:35;"
                + "2020-07-06 08:58;"
                + "2020-07-06 08:59;"
                + "2020-07-06 12:59;"
                + "2020-07-06 14:00;"
                + "2020-07-06 14:59;"
                + "2020-07-06 16:00;"
                + "2020-07-06 17:01;"
                + "2020-07-06 18:00;"
                + "2020-07-06 19:00;"
                + "2020-07-06 20:00;"
                + "2020-07-06 20:59;"
                + "2020-07-06 21:59;"
                + "2020-07-06 22:27;"
                + "2020-07-07 05:39;"
                + "2020-07-07 06:10;"
                + "2020-07-07 11:54;"
                + "2020-07-07 12:25;"
                + "2020-07-07 20:57;"
                + "2020-07-07 21:56;"
                + "2020-07-07 22:56;"
                + "2020-07-07 23:57";
        String sunWindSpeed = "386.8;391;424.7;444.6;452.3;466.4;463.6;458.7;432.5;427.9;401.8;423.8;445.4;443.7;415.9;431.7"
                + ";409.7;474.3;471;465.4;461.4;447.6;445.1;438.7;438.9;450.5;454.8;446.3;436;446.9;446.6;453.5;461.2;470.7;460.4;452.7"
                + ";441;405;385.2;380.2;358.5;354.6;354.8;356.3";
        double[] actual = {409.7, 474.3, 471, 465.4, 461.4, 447.6};
        log.log(System.Logger.Level.INFO, () -> System.lineSeparator() + "ACTUAL: " + Arrays.toString(actual));
        List<Instant> timestampData = Arrays.stream(timestamps.split(";"))
                .map(t -> LocalDateTime.parse(t, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .map(ldt -> ldt.toInstant(ZoneOffset.UTC))
                .collect(Collectors.toList());
        List<Double> sunWindSpeedData = Arrays.stream(sunWindSpeed.split(";"))
                .map(Double::valueOf)
                .collect(Collectors.toList());
        SortedMap<Instant, Double> data = new TreeMap<>();
        for (int i = 0; i < timestampData.size(); i++) {
            data.put(timestampData.get(i), sunWindSpeedData.get(i));
        }
        Forecast forecast = new XTSForecast();
        log.log(
                System.Logger.Level.INFO,
                () -> forecast.forecast(data)
                        .entrySet()
                        .stream()
                        .map(entry -> entry.getKey() + ": " + Arrays.toString(entry.getValue()))
                        .collect(Collectors.joining(System.lineSeparator(), System.lineSeparator(), "")));
   }
}
