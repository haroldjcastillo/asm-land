package com.github.haroldjcastillo.asm.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import com.github.haroldjcastillo.agent.instrumentation.Agent;
import com.github.haroldjcastillo.asm.core.adding.AddFieldAdapter;
import com.github.haroldjcastillo.asm.core.removing.RemoveDebugAdapter;
import com.github.haroldjcastillo.asm.core.transforming.ChangeVersionAdapter;
import com.github.haroldjcastillo.asm.core.transforming.MultiClassAdapter;

class MultiClassAdapterTest {

	private static Instrumentation inst;

	@BeforeAll
	public static void beforeEach() {
		inst = Agent.inst;
		assertNotNull(inst);
	}

	@Test
	void test() {
		inst.addTransformer(new ClassFileTransformer() {
			public byte[] transform(ClassLoader l, String name, Class<?> c, ProtectionDomain d, byte[] b)
					throws IllegalClassFormatException {
				if (name.equals("com/github/haroldjcastillo/asm/core/removing/Foo")) {
					ClassReader cr = new ClassReader(b);
					ClassWriter cw = new ClassWriter(cr, 0);
					ClassVisitor[] cvArray = { new AddFieldAdapter(cw, ACC_PUBLIC + ACC_STATIC, "lorem", "I"),
							new ChangeVersionAdapter(cw), new RemoveDebugAdapter(cw, "bar", "()V") };
					ClassVisitor cv = new MultiClassAdapter(cvArray);
					cr.accept(cv, 0);
					return cw.toByteArray();
				}
				return b;
			}
		});
	}

}
