package com.github.haroldjcastillo.asm.test;

import com.github.haroldjcastillo.agent.instrumentation.Agent;
import com.github.haroldjcastillo.asm.core.generating.Comparable;
import com.github.haroldjcastillo.asm.core.generating.CustomClassLoader;
import com.github.haroldjcastillo.asm.core.generating.Mesurable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.instrument.Instrumentation;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.objectweb.asm.Opcodes.*;

class TraceClassVisitorTest {

	private static Instrumentation inst;

	@BeforeAll
	public static void beforeEach() {
		inst = Agent.inst;
		assertNotNull(inst);
	}

	@Test
	void test() {
		final CustomClassLoader classLoader = new CustomClassLoader();
		final ClassWriter cw = new ClassWriter(0);
		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter);
		final TraceClassVisitor cv = new TraceClassVisitor(cw, printWriter);
		cv.visit(V1_5, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE, getName(Comparable.class), null,
				getName(Object.class), new String[] { getName(Mesurable.class) });
		cv.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "LESS", "I", null, new Integer(-1)).visitEnd();
		cv.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "EQUAL", "I", null, new Integer(0)).visitEnd();
		cv.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "GREATER", "I", null, new Integer(1)).visitEnd();
		cv.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "compareTo", "(Ljava/lang/Object;)I", null, null).visitEnd();
		cv.visitEnd();
		final byte[] classArray = cw.toByteArray();
		assertTrue(classArray.length > 0);
		final Class<?> c = classLoader.defineClass(Comparable.class.getCanonicalName(), classArray);
		assertNotNull(c);
		assertTrue(!stringWriter.toString().isEmpty());
	}

	public String getName(final Class<?> clss) {
		return clss.getCanonicalName().replace(".", "/");
	}

}
