package com.swiftype.api.easy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.swiftype.api.easy.helper.SearchOptions;
import com.swiftype.api.easy.helper.SearchResult;
import com.swiftype.api.easy.helper.SuggestResult;
import com.swiftype.api.easy.helper.SwiftypeConfig;

public class DocumentTypesApiTest {
	private static final String ENGINE_ID = "engine_id1";
	private static final String DOCUMENT_TYPE_ID = "id1";

	private DocumentTypesApi api;

	@Before
	public void setUp() throws Exception {
		final SwiftypeConfig config = SwiftypeConfig.INSTANCE;
		config.setApiHost("127.0.0.1:9292");
		api = new DocumentTypesApi(ENGINE_ID);
	}

	@Test
	public void testGetAll() {
		final DocumentType[] documentTypes = api.getAll();
		assertEquals(2, documentTypes.length);
		assertEquals("doctype1", documentTypes[0].getName());
		assertEquals(ENGINE_ID, documentTypes[0].getEngineId());
	}

	@Test
	public void testGet() {
		final DocumentType documentType = api.get(DOCUMENT_TYPE_ID);
		assertEquals(DOCUMENT_TYPE_ID, documentType.getSlug());
		assertEquals(ENGINE_ID, documentType.getEngineId());
	}

	@Test
	public void testCreate() {
		final DocumentType documentType = api.create(DOCUMENT_TYPE_ID);
		assertEquals(DOCUMENT_TYPE_ID, documentType.getSlug());
		assertEquals(ENGINE_ID, documentType.getEngineId());
	}

	@Test
	public void testDelete() {
		assertTrue(api.destroy(DOCUMENT_TYPE_ID));
	}

	@Test
	public void testSearch() {
		final SearchResult results = api.search(DOCUMENT_TYPE_ID, "query");
		assertEquals(1, results.getInfo().page);
	}

	@Test
	public void testSearchWithOptions() {
		final int page = 2;
		final SearchOptions options = new SearchOptions.Builder().page(page).build();
		final SearchResult results = api.search(DOCUMENT_TYPE_ID, "query", options);
		assertEquals(page, results.getInfo().page);
	}

	@Test
	public void testSuggest() {
		final SuggestResult results = api.suggest(DOCUMENT_TYPE_ID, "query");
		assertEquals(1, results.getResults().size());
	}

	@Test
	public void testSuggestWithOptions() {
		final SearchOptions options = new SearchOptions.Builder().page(2).build();
		final SuggestResult results = api.suggest(DOCUMENT_TYPE_ID, "query", options);
		assertEquals(1, results.getResults().size());
	}
}
