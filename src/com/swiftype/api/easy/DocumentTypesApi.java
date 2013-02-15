package com.swiftype.api.easy;

import java.util.Map;

import javax.xml.ws.WebServiceException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.swiftype.api.easy.helper.Client;
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
		try {
			final JSONArray documentTypesJson = new JSONArray(Client.get(documentTypesPath()));
			final DocumentType[] documentTypes = new DocumentType[documentTypesJson.length()];
			for (int i = 0; i < documentTypes.length; ++i) {
				documentTypes[i] = DocumentType.fromJson(documentTypesJson.getJSONObject(i));
			}
			return documentTypes;
		} catch (JSONException e) {
			throw new IllegalStateException(e.getMessage());
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
	 * @return					Success of deletion command
	 */
	public boolean destroy(final String documentTypeId) {
		try {
			Client.delete(documentTypePath(documentTypeId));
			return true;
		} catch (WebServiceException e) {
			return false;
		}
	}

	/**
	 * @param documentTypeId	DocumentType slug or id
	 * @param query				Query terms to search for
	 * @return					Search results
	 */
	public SearchResult search(final String documentTypeId, final String query) {
		return search(documentTypeId, query, SearchOptions.DEFAULT);
	}


	/**
	 * @param documentTypeId	DocumentType slug or id
	 * @param query				Query terms to search for
	 * @param options			Options for the search, for detail take a look at our @see <a href="https://swiftype.com/documentation/searching">Searching Documentation</a>
	 * @return					Search results
	 */
	public SearchResult search(final String documentTypeId, final String query, final SearchOptions options) {
		final String response = Client.post(documentTypePath(documentTypeId) + "/search", options.withQuery(query));
		return toSearchResults(response).get(documentTypeId);
	}

	/**
	 * @param documentTypeId	DocumentType slug or id
	 * @param query				Query for the suggestions
	 * @return					Suggest results
	 */
	public SuggestResult suggest(final String documentTypeId, final String query) {
		return suggest(documentTypeId, query, SearchOptions.DEFAULT);
	}

	/**
	 * @param documentTypeId	DocumentType slug or id
	 * @param query				Query for the suggestions
	 * @param options			Options for the suggest, for details take a look at our @see <a href="https://swiftype.com/documentation/autocomplete">Autocomplete Documentation</a>
	 * @return					Suggest results
	 */
	public SuggestResult suggest(final String documentTypeId, final String query, final SearchOptions options) {
		final String response = Client.post(documentTypePath(documentTypeId) + "/suggest", options.withQuery(query));
		return toSuggestResults(response).get(documentTypeId);
	}

	String documentTypesPath() {
		return EnginesApi.enginePath(engineId) + "/document_types";
	}

	String documentTypePath(final String documentTypeId) {
		return documentTypesPath() + "/" + documentTypeId;
	}

	private static DocumentType toDocumentType(final String response){
		try {
			final JSONObject json = new JSONObject(response);
			return DocumentType.fromJson(json);
		} catch (JSONException e) {
			throw new IllegalStateException(e.getMessage());
		}
	}

	private Map<String, SearchResult> toSearchResults(final String response) {
		try {
			final JSONObject json = new JSONObject(response);
			return SearchResult.fromJson(json);
		} catch (JSONException e) {
			throw new IllegalStateException(e.getMessage());
		}
	}

	private Map<String, SuggestResult> toSuggestResults(final String response) {
		try {
			final JSONObject json = new JSONObject(response);
			return SuggestResult.fromJson(json);
		} catch (JSONException e) {
			throw new IllegalStateException(e.getMessage());
		}
	}
}

