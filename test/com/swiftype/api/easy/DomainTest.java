package com.swiftype.api.easy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.swiftype.api.easy.helper.SwiftypeConfig;

public class DomainTest {
	private Domain domain;

	@Before
	public void setUp() throws Exception {
		SwiftypeConfig.INSTANCE.setApiHost("127.0.0.1:9292");
		domain = new DomainsApi("engine_id").get("domain_id");
	}

	@Test
	public void testRefresh() {
		final String id = domain.getId();
		assertEquals(id, domain.refresh().getId());
	}

	@Test(expected=IllegalStateException.class)
	public void testDestroy() {
		assertTrue(domain.destroy());
		domain.destroy();
	}
}
