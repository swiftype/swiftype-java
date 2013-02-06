package com.swiftype.api.easy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.swiftype.api.easy.helper.SwiftypeConfig;

public class EngineTest {
	private Engine engine;

	@Before
	public void setUp() throws Exception {
		SwiftypeConfig.INSTANCE.setApiHost("127.0.0.1:9292");
		engine = new EnginesApi().get("engine_id");
	}

	@Test
	public void testRefresh() {
		final String id = engine.getId();
		assertEquals(id, engine.refresh().getId());
	}

	@Test(expected=IllegalStateException.class)
	public void testDestroy() {
		assertTrue(engine.destroy());
		engine.destroy();
	}
}
