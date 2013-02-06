package com.swiftype.api.easy;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.swiftype.api.easy.helper.Client;
import com.swiftype.api.easy.helper.Client.Response;

public class DocumentsApi {
	private final String documentTypeId;
	private final DocumentTypesApi documentTypeApi;

	public DocumentsApi(final String engineId, final String documentTypeId) {
		this.documentTypeId = documentTypeId;
		this.documentTypeApi = new DocumentTypesApi(engineId);
	}

	/**
	 * @return	List of documents for the specified Engine and DocumentType
	 */
	public Document[] getAll() {
		return toDocuments(Client.get(documentsPath()));
	}

	/**
	 * @param page		Page to retrieve
	 * @param perPage	Documents per page
	 * @return			List of documents for the specified Engine and DocumentType restricted by page and perPage
	 */
	public Document[] getAll(final int page, final int perPage) {
		final String[] pageParam = {"page", Integer.toString(page)};
		final String[] perPageParam = {"per_page", Integer.toString(perPage)};
		return toDocuments(Client.get(documentsPath(), pageParam, perPageParam));
	}

	/**
	 * @param documentId	Id of the document
	 * @return				Document matching the specified id
	 */
	public Document get(final String documentId) {
		return toDocument(Client.get(documentPath(documentId)));
	}

	/**
	 * @param document	JSONObject representing a Document. For detail take a look at the @see <a href="https://swiftype.com/documentation/indexing#documents">Documents Documentation</a>
	 *
	 * Example for a document:
	 *
	 * {
     *  "external_id": "2",
     *  "fields": [
     *      {"name": "title", "value": "my title 1", "type": "string"},
     *       {"name": "page_type", "value": "user", "type": "enum"}
     * ]}
	 *
	 * @return			Newly created document
	 */
	public Document create(final JSONObject document) {
		return toDocument(Client.post(documentsPath(), "{\"document\": " + document + " }"));
	}

	/**
	 * @param documents		Array of JSONObjects. For an example of a JSON Document see {@link #create(JSONObject) create}.
	 * @return				Array of success stati for creating the individual documents
	 */
	public boolean[] create(final JSONObject[] documents) {
		return toBooleans(Client.post(documentsPath() + "/bulk_create", "{\"documents\": " + Arrays.toString(documents) + " }"));
	}

	/**
	 * @param document		Document to create or update. For details on the Document JSON see {@link #create(JSONObject) create}.
	 * @return				Created or updated Document
	 */
	public Document createOrUpdate(final JSONObject document) {
		return toDocument(Client.post(documentsPath() + "/create_or_update", "{\"document\": " + document + " }"));
	}

	/**
	 * @param documents		Array of Documents. For details on the Document JSON see {@link #create(JSONObject) create}.
	 * @return				Success stati for creating or updating the specified documents
	 */
	public boolean[] createOrUpdate(final JSONObject[] documents) {
		return toBooleans(Client.post(documentsPath() + "/bulk_create_or_update", "{\"documents\": " + Arrays.toString(documents) + " }"));
	}

	/**
	 * @param documentId	Id of the Document you want to update
	 * @param fields		Fields to update
	 *
	 * example:
	 *
	 * {"title": "my title 2", "page_type": "user2"}
	 *
	 * @return				Updated Document
	 */
	public Document update(final String documentId, final JSONObject fields) {
		return toDocument(Client.put(documentPath(documentId) + "/update_fields", "{\"fields\": " + fields + " }"));
	}

	/**
	 * @param documents		Documents JSON Objects to update.
	 *
	 * example:
	 *
	 * [
     *   {"external_id": "1", "fields": {"title": "my title 5", "page_type": "user2"}},
     *   {"external_id": "2", "fields": {"title": "my title 4", "page_type": "user2"}}
     * ]
	 *
	 * @return				Success stati for updating the documents
	 */
	public boolean[] update(final JSONObject[] documents) {
		return toBooleans(Client.put(documentsPath() + "/bulk_update", "{\"documents\": " + Arrays.toString(documents) + " }"));
	}

	/**
	 * @param documentId	Id of Document
	 * @return				Success of deletion
	 */
	public boolean destroy(final String documentId) {
		final Response response = Client.delete(documentPath(documentId));
		return response.isSuccess();
	}

	/**
	 * @param documentIds	Ids to delete
	 * @return				Success stati of deletion
	 */
	public boolean[] destroy(final String ... documentIds) {
		final StringBuilder sb = new StringBuilder();
		sb.append("{\"documents\": [");
		for (final String id : documentIds) {
			sb.append("\"" + id + "\",");
		}
		if (documentIds.length > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("] }");
		final Response response = Client.post(documentsPath() + "/bulk_destroy", sb.toString());
		return toBooleans(response);
	}

	private Document[] toDocuments(final Response response) {
		try {
			final JSONArray documentsJson = new JSONArray(response.body);
			final Document[] documents = new Document[documentsJson.length()];
			for (int i = 0; i < documents.length; ++i) {
				documents[i] = Document.fromJson(documentsJson.getJSONObject(i));
			}
			return documents;
		} catch (JSONException e) {
			return null;
		}
	}

	private Document toDocument(final Response response) {
		if (!response.isSuccess()) {
			return null;
		}
		try {
			final JSONObject json = new JSONObject(response.body);
			return Document.fromJson(json);
		} catch (JSONException e) {
			return null;
		}
	}

	private boolean[] toBooleans(final Response response) {
		if (!response.isSuccess()) {
			return null;
		}
		try {
			final JSONArray json = new JSONArray(response.body);
			final boolean[] stati = new boolean[json.length()];
			for (int i = 0; i < stati.length; ++i) {
				stati[i] = json.getBoolean(i);
			}
			return stati;
		} catch (JSONException e) {
			return null;
		}
	}

	String documentsPath() {
		return documentTypeApi.documentTypePath(documentTypeId) + "/documents";
	}

	String documentPath(final String documentId) {
		return documentsPath() + "/" + documentId;
	}
}