package com.github.haroldjcastillo.asm.test;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;

import com.github.haroldjcastillo.asm.core.parsing.ClassPrinter;

public class ParsingTest {
	
	@Test
	public void consumePrinter() throws IOException {
		final ClassPrinter cp = new ClassPrinter();
		final ClassReader cr = new ClassReader("java.lang.Runnable");
		cr.accept(cp, 0);
	}

}
