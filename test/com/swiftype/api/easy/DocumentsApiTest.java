package com.swiftype.api.easy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.swiftype.api.easy.Document;
import com.swiftype.api.easy.DocumentsApi;
import com.swiftype.api.easy.helper.SwiftypeConfig;


public class DocumentsApiTest {
	private static final String ENGINE_ID = "engine_id1";
	private static final String DOCUMENT_TYPE_ID = "document_type_id1";
	private static final String DOCUMENT_ID = "external_id1";

	private DocumentsApi api;

	@Before
	public void setUp() throws Exception {
		final SwiftypeConfig config = SwiftypeConfig.INSTANCE;
		config.setApiHost("127.0.0.1:9292");
		api = new DocumentsApi(ENGINE_ID, DOCUMENT_TYPE_ID);
	}

	@Test
	public void testGetAll() {
		final Document[] documents = api.getAll();
		assertEquals(2, documents.length);
		assertEquals(ENGINE_ID, documents[0].getEngineId());
		assertEquals(DOCUMENT_TYPE_ID, documents[0].getDocumentTypeId());
	}

	@Test
	public void testGetAllPagination() {
		final Document[] documents = api.getAll(1, 10);
		assertEquals(2, documents.length);
		assertEquals(ENGINE_ID, documents[0].getEngineId());
		assertEquals(DOCUMENT_TYPE_ID, documents[0].getDocumentTypeId());
	}

	@Test
	public void testGet() {
		final Document document = api.get(DOCUMENT_ID);
		assertEquals(ENGINE_ID, document.getEngineId());
		assertEquals(DOCUMENT_TYPE_ID, document.getDocumentTypeId());
		assertEquals(DOCUMENT_ID, document.getExternalId());
	}

	@Test
	public void testCreate() throws JSONException {
		final JSONObject jsonDocument = buildDocumentJSON(DOCUMENT_ID);
		final Document document = api.create(jsonDocument);
		assertEquals(ENGINE_ID, document.getEngineId());
		assertEquals(DOCUMENT_TYPE_ID, document.getDocumentTypeId());
		assertEquals(DOCUMENT_ID, document.getExternalId());
	}

	@Test
	public void testBulkCreate() throws JSONException {
		final JSONObject[] jsonDocuments = {buildDocumentJSON("id1"), buildDocumentJSON("id2")};
		final boolean[] stati = api.create(jsonDocuments);
		for (int i = 0; i < stati.length; ++i) {
			assertTrue(stati[i]);
		}
		assertEquals(2, stati.length);
	}

	@Test
	public void testCreateOrUpdate() throws JSONException {
		final JSONObject documentJson = buildDocumentJSON(DOCUMENT_ID);
		documentJson.put("fields", "{\"field1\": \"value1\"}");
		final Document document = api.createOrUpdate(documentJson);
		assertEquals(DOCUMENT_ID, document.getExternalId());
	}

	@Test
	public void testBulkCreateOrUpdate() throws JSONException {
		final JSONObject[] documentJsons = {buildDocumentJSON("id1"), buildDocumentJSON("id2")};
		final boolean[] stati = api.createOrUpdate(documentJsons);
		for (int i = 0; i < stati.length; ++i) {
			assertTrue(stati[i]);
		}
		assertEquals(2, stati.length);
	}

	@Test
	public void testUpdate() throws JSONException {
		final String field = "field1";
		final String value = "value1";
		final JSONObject fields = new JSONObject("{\"" + field + "\": \"" + value + "\" }");
		final Document document = api.update(DOCUMENT_ID, fields);
		assertEquals(DOCUMENT_ID, document.getExternalId());
		assertEquals(value, document.getAllFields().opt(field));
	}

	@Test
	public void testBulkUpdate() throws JSONException {
		final JSONObject[] documentJsons = {buildDocumentJSON("id1"), buildDocumentJSON("id2")};
		final boolean[] stati = api.update(documentJsons);
		for (int i = 0; i < stati.length; ++i) {
			assertTrue(stati[i]);
		}
		assertEquals(2, stati.length);
	}

	@Test
	public void testDestroy() {
		assertTrue(api.destroy(DOCUMENT_ID));
	}

	@Test
	public void testBulkDestroy() {
		final boolean[] stati = api.destroy("id1", "id2");
		for (int i = 0; i < stati.length; ++i) {
			assertTrue(stati[i]);
		}
		assertEquals(2, stati.length);
	}

	private JSONObject buildDocumentJSON(final String externalId) throws JSONException {
		return new JSONObject("{\"external_id\": \"" + externalId + "\" }");
	}
}