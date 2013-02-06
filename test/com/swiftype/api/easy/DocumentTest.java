package com.swiftype.api.easy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.swiftype.api.easy.helper.SwiftypeConfig;

public class DocumentTest {
	private static final String ENGINE_ID = "engine_id1";
	private static final String DOCUMENT_TYPE_ID = "document_type_id1";
	private static final String DOCUMENT_ID = "id1";

	private Document document;

	@Before
	public void setUp() throws Exception {
		final SwiftypeConfig config = SwiftypeConfig.INSTANCE;
		config.setApiHost("127.0.0.1:9292");
		document = new DocumentsApi(ENGINE_ID, DOCUMENT_TYPE_ID).get(DOCUMENT_ID);
	}

	@Test
	public void testRefresh() {
		final String id = document.getId();
		assertEquals(id, document.refresh().getId());
	}

	@Test
	public void testUpdate() throws JSONException {
		final String newId = "newId";
		final JSONObject fields = new JSONObject("{\"external_id\": \"" + newId + "\"}");
		assertNotNull(document.update(fields));
	}

	@Test(expected=IllegalStateException.class)
	public void testDestroy() {
		assertTrue(document.destroy());
		document.destroy();
	}
}
