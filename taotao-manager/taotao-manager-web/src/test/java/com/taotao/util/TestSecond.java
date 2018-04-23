package com.taotao.util;

import org.junit.Test;

public class TestSecond {

	@Test
	public void testSecond() {
		for (int i = 0; i < 100; i++) {
			long currentTimeMillis = System.nanoTime();
			System.out.println(currentTimeMillis);
		}
		
	}
}
