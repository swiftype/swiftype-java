package com.swiftype.api.easy.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Record {
	private final Map<String, String> fields;
	private final Map<String, String> highlightedFields;

	private Record(final Map<String, String> fields,
				  final Map<String, String> highlightedFields) {
		this.fields = fields;
		this.highlightedFields = highlightedFields;
	}

	public Map<String, String> getFields() {
		return fields;
	}

	public Map<String, String> getHighlightedFields() {
		return highlightedFields;
	}

	public static Record fromJson(final JSONObject json) {
		JSONObject highlights;
		try {
			highlights = json.getJSONObject("highlight");
		} catch (JSONException e) {
			highlights = new JSONObject();
		} finally {
			json.remove("highlight");
		}

		return new Record(extractFields(json), extractFields(highlights));
	}

	public static List<Record> fromJsonArray(final JSONArray json) {
		final List<Record> records = new ArrayList<Record>(json.length());
		try {
			for (int i = 0; i < json.length(); ++i) {
				records.add(Record.fromJson(json.getJSONObject(i)));
			}
		} catch (JSONException e) {
			return null;
		}
		return records;
	}

	private static Map<String, String> extractFields(final JSONObject json) {
		final Map<String, String> fields = new HashMap<String, String>(json.length());
		for (@SuppressWarnings("unchecked")	final Iterator<String> keys = json.keys(); keys.hasNext();) {
			final String key = keys.next();
			fields.put(key, json.optString(key));
		}
		return fields;
	}
}
