package ua.dp.dkruglikov.expert.system;

import java.io.IOException;
import java.util.Arrays;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Main {

    public static void main(String[] args) throws IOException {
        String timeLine = "386.8 391 424.7 444.6 452.3 466.4 463.6 458.7 432.5 427.9 401.8 423.8 445.4 443.7 415.9 431.7"
                + " 409.7 474.3 471 465.4 461.4 447.6 445.1 438.7 438.9 450.5 454.8 446.3 436 446.9 446.6 453.5 461.2 470.7 460.4 452.7"
                + " 441 405 385.2 380.2 358.5 354.6 354.8 356.3";
        double[] actual = {409.7, 474.3, 471, 465.4, 461.4, 447.6};
        System.out.println("ACTUAL: " + Arrays.toString(actual));
        String[] resources = {
            "average",
            "naive",
            "seasonal-naive",
            "drift",
            "croston",
            "exponential-smooting",
            "neural-network",
            "holt-winters",
            "structural"};
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            for (String resource : resources) {
                HttpPut request = new HttpPut("http://127.0.0.1:12080/" + resource);
                request.setEntity(new StringEntity(timeLine));
                try (CloseableHttpResponse response = client.execute(request)) {
                    System.out.println(resource.toUpperCase() + ": "
                        + Arrays.toString(parseResponse(EntityUtils.toString(response.getEntity()))));
                }
            }
        }
    }

    private static double[] parseResponse(String response) {
        return Arrays.stream(response.substring(1, response.length() - 1).split(","))
                .mapToDouble(Double::parseDouble)
                .toArray();
    }
}
