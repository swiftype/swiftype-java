package com.swiftype.api.easy;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.swiftype.api.easy.helper.Client;
import com.swiftype.api.easy.helper.Client.Response;
import com.swiftype.api.easy.helper.SearchOptions;
import com.swiftype.api.easy.helper.SearchResult;
import com.swiftype.api.easy.helper.SuggestResult;

public class DocumentTypesApi {
	private final String engineId;

	public DocumentTypesApi(final String engineId) {
		this.engineId = engineId;
	}

	/**
	 * @return	List of DocumentTypes for the current engine
	 */
	public DocumentType[] getAll() {
		final Response response = Client.get(documentTypesPath());
		try {
			final JSONArray documentTypesJson = new JSONArray(response.body);
			final DocumentType[] documentTypes = new DocumentType[documentTypesJson.length()];
			for (int i = 0; i < documentTypes.length; ++i) {
				documentTypes[i] = DocumentType.fromJson(documentTypesJson.getJSONObject(i));
			}
			return documentTypes;
		} catch (JSONException e) {
			return null;
		}
	}

	/**
	 * @param documentTypeId	DocumentType slug or id
	 * @return					Specified DocumentType
	 */
	public DocumentType get(final String documentTypeId) {
		return toDocumentType(Client.get(documentTypePath(documentTypeId)));
	}

	/**
	 * @param name	Name for the DocumentType you want to create
	 * @return		Newly created DocumentType
	 */
	public DocumentType create(final String name) {
		return toDocumentType(Client.post(documentTypesPath(), "{\"document_type\": {\"name\": \"" + name + "\"}}"));
	}

	/**
	 * @param documentTypeId	DocumentType slug or id
	 * @return					Success of deletion
	 */
	public boolean destroy(final String documentTypeId) {
		return Client.delete(documentTypePath(documentTypeId)).isSuccess();
	}

	/**
	 * @param documentTypeId	DocumentType slug or id
	 * @param query				Query terms to search for
	 * @return					Search results per DocumentType
	 */
	public Map<String, SearchResult> search(final String documentTypeId, final String query) {
		return search(documentTypeId, query, SearchOptions.DEFAULT);
	}


	/**
	 * @param documentTypeId	DocumentType slug or id
	 * @param query				Query terms to search for
	 * @param options			Options for the search, for detail take a look at our @see <a href="https://swiftype.com/documentation/searching">Searching Documentation</a>
	 * @return					Search results per DocumentType
	 */
	public Map<String, SearchResult> search(final String documentTypeId, final String query, final SearchOptions options) {
		final Response response = Client.post(documentTypePath(documentTypeId) + "/search", options.withQuery(query));
		return toSearchResults(response);
	}

	/**
	 * @param documentTypeId	DocumentType slug or id
	 * @param query				Query for the suggestions
	 * @return					Suggest results per DocumentType
	 */
	public Map<String, SuggestResult> suggest(final String documentTypeId, final String query) {
		return suggest(documentTypeId, query, SearchOptions.DEFAULT);
	}

	/**
	 * @param documentTypeId	DocumentType slug or id
	 * @param query				Query for the suggestions
	 * @param options			Options for the suggest, for details take a look at our @see <a href="https://swiftype.com/documentation/autocomplete">Autocomplete Documentation</a>
	 * @return					Suggest results per DocumentType
	 */
	public Map<String, SuggestResult> suggest(final String documentTypeId, final String query, final SearchOptions options) {
		final Response response = Client.post(documentTypePath(documentTypeId) + "/suggest", options.withQuery(query));
		return toSuggestResults(response);
	}

	String documentTypesPath() {
		return EnginesApi.enginePath(engineId) + "/document_types";
	}

	String documentTypePath(final String documentTypeId) {
		return documentTypesPath() + "/" + documentTypeId;
	}

	private static DocumentType toDocumentType(final Response response){
		try {
			final JSONObject json = new JSONObject(response.body);
			return DocumentType.fromJson(json);
		} catch (JSONException e) {
			return null;
		}
	}

	private Map<String, SearchResult> toSearchResults(final Response response) {
		try {
			final JSONObject json = new JSONObject(response.body);
			return SearchResult.fromJson(json);
		} catch (JSONException e) {
			return null;
		}
	}

	private Map<String, SuggestResult> toSuggestResults(final Response response) {
		try {
			final JSONObject json = new JSONObject(response.body);
			return SuggestResult.fromJson(json);
		} catch (JSONException e) {
			return null;
		}
	}
}

