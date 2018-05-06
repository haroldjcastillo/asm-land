package com.github.haroldjcastillo.asm.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.V1_5;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassWriter;

import com.github.haroldjcastillo.asm.core.generating.Comparable;
import com.github.haroldjcastillo.asm.core.generating.CustomClassLoader;
import com.github.haroldjcastillo.asm.core.generating.Mesurable;

public class GeneratingTest {
	
	private CustomClassLoader classLoader = new CustomClassLoader();

	@Test
	public void generateClass() {
		final ClassWriter cw = new ClassWriter(0);
		cw.visit(V1_5, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE, getName(Comparable.class), null,
				getName(Object.class), new String[] { getName(Mesurable.class) });
		cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "LESS", "I", null, new Integer(-1)).visitEnd();
		cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "EQUAL", "I", null, new Integer(0)).visitEnd();
		cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "GREATER", "I", null, new Integer(1)).visitEnd();
		cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "compareTo", "(Ljava/lang/Object;)I", null, null).visitEnd();
		cw.visitEnd();
		final byte[] classArray = cw.toByteArray();
		assertTrue(classArray.length > 0);
		final Class<?> c = classLoader.defineClass(Comparable.class.getCanonicalName(), classArray);
		assertNotNull(c);
	}

	public String getName(final Class<?> clss) {
		return clss.getCanonicalName().replace(".", "/");
	}

}
