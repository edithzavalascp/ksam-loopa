package org.loopasv.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelQuoteExample {
  private String type;

  public ModelQuoteExample() {}

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }


  @Override
  public String toString() {
    return "Quote{" + "type='" + type + '\'' + '}';
  }
}
