package org.ksam.logic.message.adaptation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeAdaptationMessageBody {

  private Map<String, String> body;

  public MeAdaptationMessageBody(String type, String adaptId, List<String> monitorsToAdd,
      List<String> monitorsToRemove) {
    super();
    this.body = new HashMap<>();
    this.body.put("type", type);
    this.body.put("adaptId", adaptId);
    this.body.put("monitorsToAdd",
        monitorsToAdd.toString().substring(1, monitorsToAdd.toString().length() - 1));
    this.body.put("monitorsToRemove",
        monitorsToAdd.toString().substring(1, monitorsToAdd.toString().length() - 1));
  }

  public Map<String, String> getMessageBody() {
    return this.body;
  }
}
