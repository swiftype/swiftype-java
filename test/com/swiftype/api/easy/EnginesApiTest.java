package com.swiftype.api.easy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.swiftype.api.easy.Engine;
import com.swiftype.api.easy.EnginesApi;
import com.swiftype.api.easy.helper.SearchOptions;
import com.swiftype.api.easy.helper.SearchResult;
import com.swiftype.api.easy.helper.SuggestResult;
import com.swiftype.api.easy.helper.SwiftypeConfig;

public class EnginesApiTest {
	private static final String ENGINE_ID = "engine1";

	private EnginesApi api;

	@Before
	public void setUp() throws Exception {
		SwiftypeConfig config = SwiftypeConfig.INSTANCE;
		config.setApiHost("127.0.0.1:9292");
		api = new EnginesApi();
	}

	@Test
	public void testGetAll() {
		Engine[] engines = api.getAll();
		assertEquals(2, engines.length);
		assertEquals(ENGINE_ID, engines[0].getSlug());
	}

	@Test
	public void testGet() {
		Engine engine = api.get(ENGINE_ID);
		assertEquals(ENGINE_ID, engine.getSlug());
	}

	@Test
	public void testCreate() {
		Engine engine = api.create(ENGINE_ID);
		assertEquals(ENGINE_ID, engine.getSlug());
		assertEquals(0, engine.getDocumentCount());
	}

	@Test
	public void testDestroy() {
		assertTrue(api.destroy(ENGINE_ID));
	}

	@Test
	public void testSearch() {
		final Map<String, SearchResult> results = api.search(ENGINE_ID, "query");
		for (final SearchResult documentTypeResult : results.values()) {
			assertEquals(1, documentTypeResult.getInfo().page);
		}
		assertTrue(results.size() > 1);
	}

	@Test
	public void testSearchWithOptions() {
		final int page = 2;
		final SearchOptions options = new SearchOptions.Builder().page(page).build();
		final Map<String, SearchResult> results = api.search(ENGINE_ID, "query", options);
		for (final SearchResult documentTypeResult : results.values()) {
			assertEquals(page, documentTypeResult.getInfo().page);
		}
		assertTrue(results.size() > 1);
	}

	@Test
	public void testSuggest() {
		final Map<String, SuggestResult> results = api.suggest(ENGINE_ID, "query");
		for (final SuggestResult documentTypeResult : results.values()) {
			assertEquals(1, documentTypeResult.getResults().size());
		}
		assertTrue(results.size() > 1);
	}

	@Test
	public void testSuggestWithOptions() {
		final SearchOptions options = new SearchOptions.Builder().page(2).build();
		final Map<String, SuggestResult> results = api.suggest(ENGINE_ID, "query", options);
		for (final SuggestResult documentTypeResult : results.values()) {
			assertEquals(1, documentTypeResult.getResults().size());
		}
		assertTrue(results.size() > 1);
	}
}
