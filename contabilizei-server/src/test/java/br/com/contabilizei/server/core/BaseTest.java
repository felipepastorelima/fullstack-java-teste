package br.com.contabilizei.server.core;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.BeforeClass;

import com.google.common.io.Files;

public abstract class BaseTest {
	
	@BeforeClass
	public static void setUp() throws IOException {
		/*
		 *  Por falha do Eclipse ou do Maven o arquivo de persistência
		 *  dos testes não é substituído automaticamente, por isto foi feito
		 *  desta forma
		 */
		Files.copy(new File("target/test-classes/META-INF/persistence.xml"), new File("target/classes/META-INF/persistence.xml"));
		new DatabaseClearer().clear();
	}

	@After
	public void tearDown() throws Exception {
		new DatabaseClearer().clear();
	}

}
