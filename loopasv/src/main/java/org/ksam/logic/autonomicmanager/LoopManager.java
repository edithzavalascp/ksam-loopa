/*******************************************************************************
 * Copyright (c) 2018 Universitat Politécnica de Catalunya (UPC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Contributors: Edith Zavala
 ******************************************************************************/
package org.ksam.logic.autonomicmanager;

import org.ksam.logic.analyzer.components.AnalyzerFleManager;
import org.ksam.logic.autonomicmanager.configuration.AmConfigurationManager;
import org.ksam.logic.executer.components.ExecuterFleManager;
import org.ksam.logic.executer.configuration.ExampleExecuterMessageSender;
import org.ksam.logic.generic.sender.ExampleMessageSender;
import org.ksam.logic.knowledgebase.components.KnowledgeBaseFleManager;
import org.ksam.logic.monitor.components.MonitorFleManager;
import org.ksam.logic.planner.components.PlannerFleManager;
import org.loopa.analyzer.Analyzer;
import org.loopa.analyzer.IAnalyzer;
import org.loopa.autonomicmanager.SimpleAutonomicManager;
import org.loopa.element.functionallogic.enactor.analyzer.AnalyzerFunctionalLogicEnactor;
import org.loopa.element.functionallogic.enactor.executer.ExecuterFunctionalLogicEnactor;
import org.loopa.element.functionallogic.enactor.knowledgebase.KnowledgeBaseFuncionalLogicEnactor;
import org.loopa.element.functionallogic.enactor.monitor.MonitorFunctionalLogicEnactor;
import org.loopa.element.functionallogic.enactor.planner.PlannerFunctionalLogicEnactor;
import org.loopa.executer.Executer;
import org.loopa.executer.IExecuter;
import org.loopa.knowledgebase.IKnowledgeBase;
import org.loopa.knowledgebase.KnowledgeBase;
import org.loopa.monitor.IMonitor;
import org.loopa.monitor.Monitor;
import org.loopa.planner.IPlanner;
import org.loopa.planner.Planner;

public class LoopManager {

    private final static AmConfigurationManager amC = new AmConfigurationManager();

    private SimpleAutonomicManager loop;

    public LoopManager(String loopType) {
	/**
	 * TODO Check loopType and create one based on the type. Currently only one type
	 * "SimpleAutonomicManager" is supported
	 */

	// Create elements
	IMonitor m = new Monitor(amC.getElementId("monitorId"),
		amC.getElementsMessagesCodesPolicy(amC.getElementId("monitorId")),
		new MonitorFunctionalLogicEnactor(new MonitorFleManager()), new ExampleMessageSender());

	IAnalyzer a = new Analyzer(amC.getElementId("analyzerId"),
		amC.getElementsMessagesCodesPolicy(amC.getElementId("analyzerId")),
		new AnalyzerFunctionalLogicEnactor(new AnalyzerFleManager()), new ExampleMessageSender());

	IPlanner p = new Planner(amC.getElementId("plannerId"),
		amC.getElementsMessagesCodesPolicy(amC.getElementId("plannerId")),
		new PlannerFunctionalLogicEnactor(new PlannerFleManager()), new ExampleMessageSender());

	IExecuter e = new Executer(amC.getElementId("executerId"),
		amC.getElementsMessagesCodesPolicy(amC.getElementId("executerId")),
		new ExecuterFunctionalLogicEnactor(new ExecuterFleManager()), new ExampleExecuterMessageSender());

	IKnowledgeBase kb = new KnowledgeBase(amC.getElementId("kbId"),
		amC.getElementsMessagesCodesPolicy(amC.getElementId("kbId")),
		new KnowledgeBaseFuncionalLogicEnactor(new KnowledgeBaseFleManager()), new ExampleMessageSender());

	// Create loop
	this.loop = new SimpleAutonomicManager(amC.getId(), amC.getElementsMessageBodyTypesPolicy(), m, a, p, e, kb);

	// Start loop
	this.loop.start();
    }

    public SimpleAutonomicManager getLoop() {
	return loop;
    }

    public void setLoop(SimpleAutonomicManager loop) {
	this.loop = loop;
    }
}