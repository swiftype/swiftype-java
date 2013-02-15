package com.swiftype.api.easy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.swiftype.api.easy.helper.SwiftypeConfig;

public class DomainsApiTest {
	private static final String DOMAIN_ID = "domain_id1";
	private static final String URL = "http://www.example.com";

	private DomainsApi api;

	@Before
	public void setUp() throws Exception {
		final SwiftypeConfig config = SwiftypeConfig.INSTANCE;
		config.setApiHost("127.0.0.1:9292");
		api = new DomainsApi("engine_id");
	}

	@Test
	public void testGetAll() {
		final Domain[] domains = api.getAll();
		assertEquals(2, domains.length);
	}

	@Test
	public void testGet() {
		final Domain domain = api.get(DOMAIN_ID);
		assertEquals(DOMAIN_ID, domain.getId());
	}

	@Test
	public void testCreate() {
		final Domain domain = api.create(URL);
		assertEquals(domain.getSubmittedUrl(), URL);
	}

	@Test
	public void testDestroy() {
		assertTrue(api.destroy(DOMAIN_ID));
	}

	@Test
	public void testRecrawl() {
		final Domain domain = api.recrawl(DOMAIN_ID);
		assertEquals(domain.getId(), DOMAIN_ID);
	}

	public void testCrawlUrl() {
		api.crawlUrl(DOMAIN_ID, URL);
	}
}
