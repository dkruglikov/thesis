package ua.dp.dkruglikov.expert.system;

import java.io.IOException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Main {
    
    public static void main(String[] args) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPut request = new HttpPut("http://127.0.0.1:12080/average");
            request.setEntity(new StringEntity("0 1 2 3"));
            try (CloseableHttpResponse response = client.execute(request)) {
                System.out.println(EntityUtils.toString(response.getEntity()));
            }
        }
    }
}
