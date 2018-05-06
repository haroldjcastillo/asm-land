package com.github.haroldjcastillo.asm.core.transforming;

import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Opcodes.V1_8;

import org.objectweb.asm.ClassVisitor;

public class ChangeVersionAdapter extends ClassVisitor {

	public ChangeVersionAdapter(final ClassVisitor cv) {
		super(ASM4, cv);
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		cv.visit(V1_8, access, name, signature, superName, interfaces);
	}

}
