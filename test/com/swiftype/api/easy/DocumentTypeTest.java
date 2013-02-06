package com.swiftype.api.easy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.swiftype.api.easy.helper.SearchResult;
import com.swiftype.api.easy.helper.SuggestResult;
import com.swiftype.api.easy.helper.SwiftypeConfig;

public class DocumentTypeTest {
	private DocumentType documentType;

	@Before
	public void setUp() throws Exception {
		SwiftypeConfig.INSTANCE.setApiHost("127.0.0.1:9292");
		documentType = new DocumentTypesApi("engine_id").get("document_type");
	}

	@Test
	public void testRefresh() {
		final String name = documentType.getName();
		assertEquals(name, documentType.refresh().getName());
	}

	@Test(expected=IllegalStateException.class)
	public void testDestroy() {
		assertTrue(documentType.destroy());
		documentType.destroy();
	}

	@Test
	public void testSearch() {
		final SearchResult results = documentType.search("query");
		assertEquals(1, results.getInfo().page);
	}

	@Test
	public void testSuggest() {
		final SuggestResult results= documentType.suggest("query");
		assertEquals(1, results.getResults().size());
	}
}
