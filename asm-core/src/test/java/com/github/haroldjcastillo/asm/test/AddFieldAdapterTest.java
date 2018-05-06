package com.github.haroldjcastillo.asm.test;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.security.ProtectionDomain;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import com.github.haroldjcastillo.agent.instrumentation.Agent;
import com.github.haroldjcastillo.asm.core.adding.AddFieldAdapter;
import com.github.haroldjcastillo.asm.core.removing.Foo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class AddFieldAdapterTest {

	private static Instrumentation inst;

	@BeforeAll
	public static void beforeEach() {
		inst = Agent.inst;
		assertNotNull(inst);
	}

	@Test
	@DisplayName("Add field")
	void testA() {
		inst.addTransformer(new ClassFileTransformer() {
			public byte[] transform(ClassLoader l, String name, Class<?> c, ProtectionDomain d, byte[] b)
					throws IllegalClassFormatException {
				if (name.equals("com/github/haroldjcastillo/asm/core/removing/Foo")) {
					ClassReader cr = new ClassReader(b);
					ClassWriter cw = new ClassWriter(cr, 0);
					ClassVisitor cv = new AddFieldAdapter(cw, ACC_PUBLIC + ACC_STATIC, "lorem", "I");
					cr.accept(cv, 0);
					return cw.toByteArray();
				}
				return b;
			}
		});
		final Foo foo = new Foo();
		foo.bar();
	}
	
	@Test
	@DisplayName("Set field")
	public void testB() {
		final Foo foo = new Foo();
		try {
			final Field field = foo.getClass().getField("lorem");
			field.setAccessible(true);
			field.setInt(null, 1);
			assertEquals(1, foo.getClass().getField("lorem").getInt(null));
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException
				| NoSuchFieldException e) {
			fail(e.getMessage());
		}

	}

}
