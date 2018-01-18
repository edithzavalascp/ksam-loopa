package org.loopasv.logic.autonomicmanager;

import java.util.HashMap;
import org.loopa.analyzer.Analyzer;
import org.loopa.analyzer.IAnalyzer;
import org.loopa.autonomicmanager.AutonomicManagerOneElementOfEach;
import org.loopa.element.functionallogic.enactor.IFunctionalLogicEnactor;
import org.loopa.element.functionallogic.enactor.analyzer.AnalyzerFunctionalLogicEnactor;
import org.loopa.element.functionallogic.enactor.executer.ExecuterFunctionalLogicEnactor;
import org.loopa.element.functionallogic.enactor.knowledgebase.KnowledgeBaseFuncionalLogicEnactor;
import org.loopa.element.functionallogic.enactor.monitor.MonitorFunctionalLogicEnactor;
import org.loopa.element.functionallogic.enactor.planner.PlannerFunctionalLogicEnactor;
import org.loopa.element.sender.messagesender.IMessageSender;
import org.loopa.executer.Executer;
import org.loopa.executer.IExecuter;
import org.loopa.generic.documents.IPolicy;
import org.loopa.generic.documents.Policy;
import org.loopa.knowledgebase.IKnowledgeBase;
import org.loopa.knowledgebase.KnowledgeBase;
import org.loopa.monitor.IMonitor;
import org.loopa.monitor.Monitor;
import org.loopa.planner.IPlanner;
import org.loopa.planner.Planner;
import org.loopasv.logic.analyzer.LoopaSvAnalyzerFunctionalLogicManager;
import org.loopasv.logic.analyzer.LoopaSvAnalyzerMessageSender;
import org.loopasv.logic.effectors.LoopaSvEffectors;
import org.loopasv.logic.executer.LoopaSvExecuterFunctionalLogicManager;
import org.loopasv.logic.executer.LoopaSvExecuterMessageSender;
import org.loopasv.logic.knowledgebase.LoopaSvKnowledgeBaseFunctionalLogicManager;
import org.loopasv.logic.knowledgebase.LoopaSvKnowledgeBaseMessageSender;
import org.loopasv.logic.monitor.LoopaSvMonitorFunctionalLogicManager;
import org.loopasv.logic.monitor.LoopaSvMonitorMessageSender;
import org.loopasv.logic.planner.LoopaSvPlannerFunctionalLogicManager;
import org.loopasv.logic.planner.LoopaSvPlannerMessageSender;
import org.loopasv.logic.sensors.LoopaSvSensors;

public class LoopaSvAutonomicManager {

  public void start() {
    IMessageSender monitorMS = new LoopaSvMonitorMessageSender();
    IMessageSender analyzerMS = new LoopaSvAnalyzerMessageSender();
    IMessageSender plannerMS = new LoopaSvPlannerMessageSender();
    IMessageSender executerMS = new LoopaSvExecuterMessageSender();
    IMessageSender kbMS = new LoopaSvKnowledgeBaseMessageSender();

    IFunctionalLogicEnactor monitorFle =
        new MonitorFunctionalLogicEnactor(new LoopaSvMonitorFunctionalLogicManager());
    IFunctionalLogicEnactor analyzerFle =
        new AnalyzerFunctionalLogicEnactor(new LoopaSvAnalyzerFunctionalLogicManager());
    IFunctionalLogicEnactor plannerFle =
        new PlannerFunctionalLogicEnactor(new LoopaSvPlannerFunctionalLogicManager());
    IFunctionalLogicEnactor executerFle =
        new ExecuterFunctionalLogicEnactor(new LoopaSvExecuterFunctionalLogicManager());
    IFunctionalLogicEnactor kbFle =
        new KnowledgeBaseFuncionalLogicEnactor(new LoopaSvKnowledgeBaseFunctionalLogicManager());

    // Change for read them from properties or JSON
    IPolicy loopaElementsMessMngmtPolicy =
        new Policy("loopaElements", new HashMap<String, String>() {
          {
            /**
             * mssgInFl:1 mssgInAl:2 mssgAdapt:3 mssgOutFl:4 mssgOutAl:5
             */
            put("mssgInFl", "1");
            put("mssgInAl", "2");
            put("mssgAdapt", "3");
            put("mssgOutFl", "4");
            put("mssgOutAl", "5");
          }
        });

    IMonitor m =
        new Monitor("LoopaSv_Monitor", loopaElementsMessMngmtPolicy, monitorFle, monitorMS);
    IAnalyzer a =
        new Analyzer("LoopaSv_Analyzer", loopaElementsMessMngmtPolicy, analyzerFle, analyzerMS);
    IPlanner p =
        new Planner("LoopaSv_Planner", loopaElementsMessMngmtPolicy, plannerFle, plannerMS);
    IExecuter e =
        new Executer("LoopaSv_Executer", loopaElementsMessMngmtPolicy, executerFle, executerMS);
    IKnowledgeBase kb = new KnowledgeBase("LoopaSv_Kb", loopaElementsMessMngmtPolicy, kbFle, kbMS);

    AutonomicManagerOneElementOfEach loopaSvAm =
        new AutonomicManagerOneElementOfEach(m, a, p, e, kb);
    loopaSvAm.start();

    loopaSvAm.getExecuter().addElementRecipient("adaptation", "effectors", new LoopaSvEffectors());
    new LoopaSvSensors(loopaSvAm.getMonitor());
  }

}
