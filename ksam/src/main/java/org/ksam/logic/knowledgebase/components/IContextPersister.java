package org.ksam.logic.knowledgebase.components;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public interface IContextPersister {

    public Map<String, Integer> updateContext(List<Entry<String, Object>> context);
}
