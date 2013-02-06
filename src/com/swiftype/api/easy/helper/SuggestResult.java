package com.swiftype.api.easy.helper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;


public class SuggestResult {
	private final List<Record> results;
	private final int resultCount;

	private SuggestResult(final List<Record> results, final int resultCount) {
		this.results = results;
		this.resultCount = resultCount;
	}

	public List<Record> getResults() {
		return results;
	}

	public int getResultCount() {
		return resultCount;
	}

	public static Map<String, SuggestResult> fromJson(final JSONObject json) {
		final Map<String, SuggestResult> results = new HashMap<String, SuggestResult>();
		final JSONObject records = json.optJSONObject("records");
		if (records == null) {
			return results;
		}
		final int resultCount = json.optInt("record_count");
		for (@SuppressWarnings("unchecked") final Iterator<String> names = records.keys(); names.hasNext();) {
			final String documentTypeName = names.next();
			final JSONArray documentTypeRecords = records.optJSONArray(documentTypeName);
			if (documentTypeRecords != null) {
				results.put(documentTypeName, new SuggestResult(Record.fromJsonArray(documentTypeRecords), resultCount));
			}
		}
		return results;
	}
}
