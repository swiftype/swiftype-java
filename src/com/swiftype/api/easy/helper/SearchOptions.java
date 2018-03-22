package com.swiftype.api.easy.helper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * SearchOptions representing options for searches or autocompletes. For more details
 * @see <a href="https://swiftype.com/documentation/searching">Search Documentation</a>
 *
 */
public class SearchOptions {
	public static final SearchOptions DEFAULT = new Builder().build();

	private final JSONObject options;

	private SearchOptions(final Builder builder) {
		options = new JSONObject();
		mapToJson(options, "fetch_fields", builder.fetchFields);
		mapToJson(options, "search_fields", builder.searchFields);
		mapToJson(options, "filters", builder.filters);
		mapToJson(options, "functional_boosts", builder.functionalBoosts);
		mapToJson(options, "sort_field", builder.sortField);
		mapToJson(options, "sort_direction", builder.sortDirection);
		mapToJson(options, "facets", builder.facets);

		arrayToJson(options, "document_types", builder.documentTypes);

		intToJson(options, "page", builder.page);
		intToJson(options, "per_page", builder.per_page);
	}

	private SearchOptions(final JSONObject options) {
		this.options = options;
	}

	public static SearchOptions from(final String optionString) {
		JSONObject options;
		try {
			options = new JSONObject(optionString);
		} catch (JSONException e) {
			return DEFAULT;
		}
		return new SearchOptions(options);
	}

	public JSONObject toJson() {
		return options;
	}

	public String withQuery(final String query) {
		try {
			final JSONObject json = options.length() == 0 ? new JSONObject() : new JSONObject(options, JSONObject.getNames(options));
			json.put("q", query);
			return json.toString();
		} catch (JSONException e) {
			return "";
		}
	}

	@Override
	public String toString() {
		return options.toString();
	}

	public static class Builder {
		private final Map<String, String[]> fetchFields = new HashMap<String, String[]>();
		private final Map<String, String[]> searchFields = new HashMap<String, String[]>();
		private final Map<String, Map<String, String[]>> filters = new HashMap<String, Map<String, String[]>>();
		private final Map<String, Map<String, String>> functionalBoosts = new HashMap<String, Map<String, String>>();
		private final Map<String, String> sortField = new HashMap<String, String>();
		private final Map<String, String> sortDirection = new HashMap<String, String>();
		private final Map<String, String[]> facets = new HashMap<String, String[]>();
		private String[] documentTypes;
		private int page = -1;
		private int per_page = -1;

		/**
		 * @param documentType	DocumentType containing the specified fields
		 * @param fields		Fields you want to get returned for the specified DocumentType
		 * @return				Builder to add more options. If your finished create the options with {@link build()}.
		 */
		public Builder fetchFields(final String documentType, final String ... fields) {
			fetchFields.put(documentType, fields);
			return this;
		}

		/**
		 * @param documentType	DocumentType containing the specified fields
		 * @param fields		Fields used for searching. Each field can contain a weight
		 * 						for the field by adding '^(weight)' after the field name, e.g. 'title^2'
		 * @return				Builder to add more options. If your finished create the options with {@link build()}.
		 */
		public Builder searchFields(final String documentType, final String ... fields) {
			searchFields.put(documentType, fields);
			return this;
		}

		/**
		 * @param documentType	DocumentType these filter will be applied to
		 * @param field			Field to filter
		 * @param filter		Filter conditions for the specified field. For more details
		 * 						@see <a href="https://swiftype.com/documentation/searching#filters">Filter Documentation</a>
		 * @return
		 */
		public Builder filter(final String documentType, final String field, final String... filter) {
			Map<String, String[]> documentTypeFilters = filters.get(documentType);
			if (documentTypeFilters == null) {
				documentTypeFilters = new HashMap<String, String[]>();
				filters.put(documentType, documentTypeFilters);
			}
			documentTypeFilters.put(field, filter);
			return this;
		}

		/**
		 * @param documentTypes	DocumentTypes, which should be searched
		 * @return				Builder to add more options. If your finished create the options with {@link build()}.
		 */
		public Builder documentTypes(final String ... documentTypes) {
			this.documentTypes = documentTypes;
			return this;
		}

		/**
		 * @param documentType	DocumentType, which Documents are functionally boosted
		 * @param field			Field to boost
		 * @param boost			Type of the functional boost you want to apply. For details
		 * 						@see <a href="https://swiftype.com/documentation/searching#functional_boosts">Functional Boost Documentation</a>
		 * @return				Builder to add more options. If your finished create the options with {@link build()}.
		 */
		public Builder functionalBoosts(final String documentType, final String field, final FunctionalBoost boost) {
			Map<String, String> boosts = functionalBoosts.get(documentType);
			if (boosts == null) {
				boosts = new HashMap<String, String>();
				functionalBoosts.put(documentType, boosts);
			}
			boosts.put(field, boost.toString().toLowerCase());
			return this;
		}

		/**
		 * @param page	The result page you want to see
		 * @return		Builder to add more options. If your finished create the options with {@link build()}.
		 */
		public Builder page(final int page) {
			this.page = page;
			return this;
		}

		/**
		 * @param per_page	The amount of results you want to see per page
		 * @return			Builder to add more options. If your finished create the options with {@link build()}.
		 */
		public Builder perPage(final int per_page) {
			this.per_page = per_page;
			return this;
		}

		/**
		 * @param documentType	DocumentType you want to sort
		 * @param field			Field to sort in ascending ordering
		 * @return				Builder to add more options. If your finished create the options with {@link build()}.
		 */
		public Builder sortField(final String documentType, final String field) {
			return sortField(documentType, field, Direction.ASC);
		}

		/**
		 * @param documentType	DocumentType you want to sort
		 * @param field			Field to sort on
		 * @param direction		Sorting order
		 * @return				Builder to add more options. If your finished create the options with {@link build()}.
		 */
		public Builder sortField(final String documentType, final String field, final Direction direction) {
			sortField.put(documentType, field);
			return sortDirection(documentType, direction);
		}

		/**
		 * @param documentType	DocumentType you want to change sort order
		 * @param direction		Order to sort the specified DocumentTypes sort field.
		 * 						By default orders on '_score'. For more details
		 * 						@see <a href="https://swiftype.com/documentation/searching#sorting">Sorting Documentation</a>
		 * @return				Builder to add more options. If your finished create the options with {@link build()}.
		 */
		public Builder sortDirection(final String documentType, final Direction direction) {
			sortDirection.put(documentType, direction.toString().toLowerCase());
			return this;
		}

		/**
		 * @param documentType	DocumentType you want to use
		 * @param fields		Fields you want to get counts for each value
		 * @return				Builder to add more options. If your finished create the options with {@link build()}.
		 */
		public Builder facets(final String documentType, final String ... fields) {
			facets.put(documentType, fields);
			return this;
		}

		/**
		 * @return		SearchOptions based on the supplied values to this builder.
		 */
		public SearchOptions build() {
			return new SearchOptions(this);
		}
	}

	/**
	 * Possible sorting orders
	 *
	 */
	public static enum Direction {
		ASC,
		DESC;
	}

	/**
	 * Possible functional boost types
	 *
	 */
	public static enum FunctionalBoost {
		LOGARITHMIC,
		LINEAR,
		EXPONENTIAL;
	}

	private <K, V> void mapToJson(final JSONObject root, final String optionName, final Map<K, V> map) {
		if (!map.isEmpty()) {
			try {
				root.put(optionName, new JSONObject(map));
			} catch (JSONException e) {
				throw new IllegalArgumentException("Illegal arguments for " + optionName + " option!");
			}
		}
	}

	private <T> void arrayToJson(final JSONObject root, final String optionName, final T[] values) {
		if (values != null) {
			try {
				root.put(optionName, new JSONArray(Arrays.asList(values)));
			} catch (JSONException e) {
				throw new IllegalArgumentException("Illegal arguments for " + optionName + " option!");
			}
		}
	}

	private void intToJson(final JSONObject root, final String optionName, final int value) {
		if (value > 0) {
			try {
				root.put(optionName, value);
			} catch (JSONException e) {
				throw new IllegalArgumentException("Illegal arguments for " + optionName + "option!");
			}
		}
	}
}