package org.ksam.logic.analyzer.components;

import java.util.List;
import java.util.Map.Entry;

public interface IContextAnalyzer {

    void updateContext(List<Entry<String, Object>> context);

    List<String> getRequiredVars();

}
