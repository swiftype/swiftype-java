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

public class EnginesApi {
	private static final String ENGINES_PATH = "engines";

	/**
	 * @return	List of all your engines
	 */
	public Engine[] getAll() {
		try {
			final JSONArray enginesJson = new JSONArray(Client.get(ENGINES_PATH));
			final Engine[] engines = new Engine[enginesJson.length()];
			for (int i = 0; i < engines.length; ++i) {
				engines[i] = Engine.fromJson(enginesJson.getJSONObject(i));
			}
			return engines;
		} catch (JSONException e) {
			throw new IllegalStateException(e.getMessage());
		}
	}

	/**
	 * @param engineId		Slug or id of an engine
	 * @return				Engine matching the specified engineId
	 */
	public Engine get(final String engineId) {
		return toEngine(Client.get(enginePath(engineId)));
	}

	/**
	 * @param name		Name of the engine
	 * @return			Created engine
	 */
	public Engine create(final String name) {
		final String response = Client.post(ENGINES_PATH, "{\"engine\": {\"name\": \"" + name + "\"}}");
		return toEngine(response);
	}

	/**
	 * @param engineId	Slug or id of an engine
	 * @return			Success of deletion
	 */
	public boolean destroy(final String engineId) {
		try {
			Client.delete(enginePath(engineId));
			return true;
		} catch (WebServiceException e) {
			return false;
		}
	}

	/**
	 * @param engineId	Slug or id of an engine
	 * @param query		Query terms for the search
	 * @return			Search results per DocumentType
	 */
	public Map<String, SearchResult> search(final String engineId, final String query) {
		return search(engineId, query, SearchOptions.DEFAULT);
	}

	/**
	 * @param engineId	Slug or id of an engine
	 * @param query		Query terms for the search
	 * @param options	Options for the search, for detail take a look at our @see <a href="https://swiftype.com/documentation/searching">Searching Documentation</a>
	 * @return			Search results per DocumentType
	 */
	public Map<String, SearchResult> search(final String engineId, final String query, final SearchOptions options) {
		return toSearchResults(Client.post(enginePath(engineId) + "/search", options.withQuery(query)));
	}

	/**
	 * @param engineId	Slug or id of an engine
	 * @param query		Query for the suggestions
	 * @return			Suggest results per DocumentType
	 */
	public Map<String, SuggestResult> suggest(final String engineId, final String query) {
		return suggest(engineId, query, SearchOptions.DEFAULT);
	}

	/**
	 * @param engineId	Slug or id of an engine
	 * @param query		Query for the suggestions
	 * @param options	Options for the suggest, for details take a look at our @see <a href="https://swiftype.com/documentation/autocomplete">Autocomplete Documentation</a>
	 * @return			Suggest results per DocumentType
	 */
	public Map<String, SuggestResult> suggest(final String engineId, final String query, final SearchOptions options) {
		return toSuggestResults(Client.post(enginePath(engineId) + "/suggest", options.withQuery(query)));
	}

	static String enginePath(final String engineId) {
		return ENGINES_PATH + "/" + engineId;
	}

	private Engine toEngine(final String response){
		try {
			final JSONObject json = new JSONObject(response);
			return Engine.fromJson(json);
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
			throw new IllegalStateException();
		}
	}
}
