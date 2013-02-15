package com.swiftype.api.easy.helper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


public class SearchResult {
	private final Info info;
	private final List<Record> results;

	private SearchResult(final Info info, final List<Record> results) {
		this.info = info;
		this.results = results;
	}

	public Info getInfo() {
		return info;
	}

	public List<Record> getResults() {
		return results;
	}

	public static Map<String, SearchResult> fromJson(final JSONObject json) {
		final Map<String, SearchResult> searchResultsPerDocumentType = new HashMap<String, SearchResult>();
		try {
			final JSONObject infoJson = json.getJSONObject("info");
			final JSONObject recordsJson = json.getJSONObject("records");

			for (@SuppressWarnings("unchecked") final Iterator<String> names = infoJson.keys(); names.hasNext();){
				final String documentTypeName = names.next();
				final List<Record> records = Record.fromJsonArray(recordsJson.getJSONArray(documentTypeName));
				final SearchResult result = new SearchResult(Info.fromJson(infoJson.getJSONObject(documentTypeName)), records);
				searchResultsPerDocumentType.put(documentTypeName, result);
			}
		} catch (JSONException e) {
			return null;
		}
		return searchResultsPerDocumentType;
	}

	public static class Info {
		public final String query;
		public final int totalResultCount;
		public final int pageCount;
		public final int page;
		public final int perPage;
		public final JSONObject facets;

		private Info(final String query,
					final int totalResultCount,
					final int pageCount,
					final int page,
					final int perPage,
					final JSONObject facets) {
			this.query = query;
			this.totalResultCount = totalResultCount;
			this.pageCount = pageCount;
			this.page = page;
			this.perPage = perPage;
			this.facets = facets;
		}

		public static Info fromJson(final JSONObject json) {
			final String query = json.optString("query");
			final int totalResultCount = json.optInt("total_result_count");
			final int pageCount = json.optInt("num_pages");
			final int page = json.optInt("current_page");
			final int perPage = json.optInt("per_page");
			final JSONObject facets = json.optJSONObject("facets");
			return new Info(query, totalResultCount, pageCount, page, perPage, facets);
		}
	}
}
