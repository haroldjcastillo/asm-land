package com.github.haroldjcastillo.asm.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import com.github.haroldjcastillo.asm.core.removing.Foo;
import com.github.haroldjcastillo.asm.core.removing.RemoveDebugAdapter;

class RemoveDebugAdapterTest {
	
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
				ClassReader cr = new ClassReader(b);
				ClassWriter cw = new ClassWriter(cr, 0);
				ClassVisitor cv = new RemoveDebugAdapter(cw, "bar", "()V");
				cr.accept(cv, 0);
				return cw.toByteArray();
			}
		});

		final Foo foo = new Foo();
		try {
			foo.bar();
		} catch (NoSuchMethodError e) {
			assertNotNull(e);
		}
	}

}
