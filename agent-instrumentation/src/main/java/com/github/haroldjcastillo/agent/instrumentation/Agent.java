package com.github.haroldjcastillo.agent.instrumentation;

import java.lang.instrument.Instrumentation;

public class Agent {
	
	public static volatile Instrumentation inst;
	
	public static void premain(String args, Instrumentation inst) {
		Agent.inst = inst;
	}

}
