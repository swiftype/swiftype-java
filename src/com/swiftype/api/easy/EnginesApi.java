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

public class EnginesApi {
	private static final String ENGINES_PATH = "engines";

	/**
	 * @return	List of all your engines
	 */
	public Engine[] getAll() {
		final Response response = Client.get(ENGINES_PATH);
		try {
			final JSONArray enginesJson = new JSONArray(response.body);
			final Engine[] engines = new Engine[enginesJson.length()];
			for (int i = 0; i < engines.length; ++i) {
				engines[i] = Engine.fromJson(enginesJson.getJSONObject(i));
			}
			return engines;
		} catch (JSONException e) {
			return null;
		}
	}

	/**
	 * @param engineId		Slug or id of an engine
	 * @return				Engine matching the specified engineId
	 */
	public Engine get(final String engineId) {
		final Response response = Client.get(enginePath(engineId));
		return toEngine(response);
	}

	/**
	 * @param name		Name of the engine
	 * @return			Created engine
	 */
	public Engine create(final String name) {
		final Response response = Client.post(ENGINES_PATH, "{\"engine\": {\"name\": \"" + name + "\"}}");
		return toEngine(response);
	}

	/**
	 * @param engineId	Slug or id of an engine
	 * @return			Success of deletion
	 */
	public boolean destroy(final String engineId) {
		final Response response = Client.delete(enginePath(engineId));
		return response.isSuccess();
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
		final Response response = Client.post(enginePath(engineId) + "/search", options.withQuery(query));
		return toSearchResults(response);
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
		final Response response = Client.post(enginePath(engineId) + "/suggest", options.withQuery(query));
		return toSuggestResults(response);
	}

	static String enginePath(final String engineId) {
		return ENGINES_PATH + "/" + engineId;
	}

	private Engine toEngine(final Response response){
		try {
			final JSONObject json = new JSONObject(response.body);
			return Engine.fromJson(json);
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
