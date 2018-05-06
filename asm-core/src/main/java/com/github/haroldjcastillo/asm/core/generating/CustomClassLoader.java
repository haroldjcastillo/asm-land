package com.github.haroldjcastillo.asm.core.generating;

public class CustomClassLoader extends ClassLoader {

	public Class<?> defineClass(String name, byte[] b) {
		return super.defineClass(name, b, 0, b.length);
	}

}
