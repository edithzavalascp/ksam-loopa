package org.ksam.client;

import java.util.HashMap;

import org.ksam.model.monitor.ModelQuoteExample;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class LoopaSvClient {

  // add this kind of policies
  private HashMap<String, String> endpointIdUrl;

  public ModelQuoteExample callGet() {
    RestTemplate restTemplate = new RestTemplate();
    ModelQuoteExample modelQuoteExample = restTemplate
        .getForObject("http://gturnquist-quoters.cfapps.io/api/random", ModelQuoteExample.class);
    return modelQuoteExample;
  }

  public void callPost(String endpointId, String json) {
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<String>(json, headers);
    restTemplate.postForObject(endpointIdUrl.get(endpointId), entity, String.class);
  }
}
