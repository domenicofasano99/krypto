package com.bok.krypto.core;

import com.bok.krypto.integration.external.CoinMarketDTO;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CoinMarketAPI {

    @Value("${coinmarket.api-key}")
    private String apiKey;

    @Value("${coinmarket.endpoint}")
    private String endpoint;

    /*

    @Value("${coinmarket.start}")
    private String start;

    @Value("${coinmarket.limit}")
    private String limit;

    @Value("${coinmarket.convert}")
    private String convert;

     */

    public CoinMarketDTO fetch() {
        List<NameValuePair> paratmers = new ArrayList<>();
        paratmers.add(new BasicNameValuePair("start", "1"));
        paratmers.add(new BasicNameValuePair("limit", "10"));
        paratmers.add(new BasicNameValuePair("convert", "USD"));

        try {
            return makeAPICall(endpoint, paratmers);
        } catch (IOException e) {
            log.error("Error: cannont access content - " + e.toString());
        } catch (URISyntaxException e) {
            log.error("Error: Invalid URL " + e.toString());
        }
        return null;
    }

    private CoinMarketDTO makeAPICall(String uri, List<NameValuePair> parameters)
            throws URISyntaxException, IOException {
        String response_content = "";

        URIBuilder query = new URIBuilder(uri);
        query.addParameters(parameters);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(query.build());

        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.addHeader("X-CMC_PRO_API_KEY", apiKey);

        try (CloseableHttpResponse response = client.execute(request)) {
            HttpEntity entity = response.getEntity();
            response_content = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        }
        Gson gson = new Gson();
        return gson.fromJson(response_content, CoinMarketDTO.class);
    }

}