package com.swiftype.api.easy;

import java.util.Date;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.json.JSONObject;

import com.swiftype.api.easy.helper.SearchOptions;
import com.swiftype.api.easy.helper.SearchResult;
import com.swiftype.api.easy.helper.SuggestResult;


public class Engine {
	private String id;
	private String key;
	private String name;
	private String slug;
	private Date updatedAt;
	private int documentCount;
	private EnginesApi api;
	private boolean destroyed;

	private Engine(final String id,
				   final String key,
				   final String name,
				   final String slug,
				   final Date updatedAt,
				   final int documentCount) {
		this.id = id;
		this.key = key;
		this.name = name;
		this.slug = slug;
		this.updatedAt = updatedAt;
		this.documentCount = documentCount;

		this.api = new EnginesApi();
		this.destroyed = false;
	}

	/**
	 * @param query		Query terms for the search
	 * @return			Search results per DocumentType
	 */
	public Map<String, SearchResult> search(final String query) {
		checkDestroyed();
		return api.search(id, query);
	}

	/**
	 * @param query		Query terms for the search
	 * @param options	Options for the search, for detail take a look at our @see <a href="https://swiftype.com/documentation/searching">Searching Documentation</a>
	 * @return			Search results per DocumentType
	 */
	public Map<String, SearchResult> search(final String query, final SearchOptions options) {
		checkDestroyed();
		return api.search(id, query, options);
	}

	/**
	 * @param query		Query for the suggest
	 * @return			Suggest results per DocumentType
	 */
	public Map<String, SuggestResult> suggest(final String query) {
		checkDestroyed();
		return api.suggest(id, query);
	}

	/**
	 * @param query		Query for the suggest
	 * @param options	Options for the suggest, for details take a look at our @see <a href="https://swiftype.com/documentation/autocomplete">Autocomplete Documentation</a>
	 * @return			Suggest results per DocumentType
	 */
	public Map<String, SuggestResult> suggest(final String query, final SearchOptions options) {
		checkDestroyed();
		return api.suggest(id, query, options);
	}

	/**
	 * Delete this engine
	 *
	 * @return	Success of the delete
	 */
	public boolean destroy() {
		checkDestroyed();
		destroyed = api.destroy(id);
		return destroyed;
	}

	/**
	 * Updates all values of this engine
	 *
	 * @return	Updated engine
	 */
	public Engine refresh() {
		final Engine engine = api.get(id);
		updateFields(engine);
		return this;
	}

	/**
	 * @return	DocumentTypesApi for this engine
	 */
	public DocumentTypesApi getDocumentTypeApi() {
		checkDestroyed();
		return new DocumentTypesApi(id);
	}


	/**
	 * @return	Engine id
	 */
	public String getId() {
		checkDestroyed();
		return id;
	}

	/**
	 * @return	Engine key
	 */
	public String getKey() {
		checkDestroyed();
		return key;
	}

	/**
	 * @return	Engine name
	 */
	public String getName() {
		checkDestroyed();
		return name;
	}

	/**
	 * @return	Engine slug
	 */
	public String getSlug() {
		checkDestroyed();
		return slug;
	}

	/**
	 * @return	Engine updated date
	 */
	public Date getUpdatedAt() {
		checkDestroyed();
		return updatedAt;
	}

	/**
	 * @return	Engine document count
	 */
	public int getDocumentCount() {
		checkDestroyed();
		return documentCount;
	}

	public static Engine fromJson(final JSONObject json) {
		final String id = json.optString("id");
		final String key = json.optString("key");
		final String name = json.optString("name");
		final String slug = json.optString("slug");
		final Date updatedAt = DatatypeConverter.parseDateTime(json.optString("updated_at")).getTime();
		final int documentCount = json.optInt("document_count");
		return new Engine(id, key, name, slug, updatedAt, documentCount);
	}

	private void checkDestroyed() {
		if (destroyed) {
			throw new IllegalStateException("Engine is already destroyed or not up-to-date.");
		}
	}

	private void updateFields(final Engine engine) {
		if (engine == null) {
			destroyed = true;
		} else {
			this.id = engine.id;
			this.key = engine.key;
			this.name = engine.name;
			this.slug = engine.slug;
			this.updatedAt = engine.updatedAt;
			this.documentCount = engine.documentCount;
			this.destroyed = false;
		}
	}
}
