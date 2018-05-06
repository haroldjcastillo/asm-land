package com.github.haroldjcastillo.asm.core.removing;

import static org.objectweb.asm.Opcodes.ASM4;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class RemoveDebugAdapter extends ClassVisitor {

	private final String name;
	private final String desc;

	public RemoveDebugAdapter(final ClassVisitor cv, final String name, final String desc) {
		super(ASM4, cv);
		this.name = name;
		this.desc = desc;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		if (this.name.equals(name) && this.desc.equals(desc)) {
			// do not delegate to next visitor -> this removes the method
			return null;	
		}
		return cv.visitMethod(access, name, desc, signature, exceptions);
	}

	@Override
	public void visitSource(String source, String debug) {
	}

	@Override
	public void visitOuterClass(String owner, String name, String desc) {
	}

	@Override
	public void visitInnerClass(String name, String outerName, String innerName, int access) {
	}

}
