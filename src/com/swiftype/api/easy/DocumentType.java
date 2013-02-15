package com.swiftype.api.easy;

import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.json.JSONObject;

import com.swiftype.api.easy.helper.SearchOptions;
import com.swiftype.api.easy.helper.SearchResult;
import com.swiftype.api.easy.helper.SuggestResult;

public class DocumentType {
	private String name;
	private String slug;
	private String engineId;
	private Date updatedAt;
	private int documentCount;
	private boolean destroyed;
	private final DocumentTypesApi api;

	private DocumentType(final String name,
						 final String slug,
						 final String engineId,
						 final Date updatedAt,
						 final int documentCount) {
		this.name = name;
		this.slug = slug;
		this.engineId = engineId;
		this.updatedAt = updatedAt;
		this.documentCount = documentCount;
		this.destroyed = false;

		this.api = new DocumentTypesApi(engineId);
	}

	/**
	 * @return	Update the current DocumentType
	 */
	public DocumentType refresh() {
		final DocumentType documentType = api.get(slug);
		updateFields(documentType);
		return this;
	}

	/**
	 * Destroy the current DocumentType
	 *
	 * @return	Success of deletion command
	 */
	public boolean destroy() {
		checkDestroyed();
		destroyed = api.destroy(slug);
		return destroyed;
	}

	/**
	 * @param query		Query terms for the search
	 * @return			Search results for the current DocumentType
	 */
	public SearchResult search(final String query) {
		checkDestroyed();
		return api.search(slug, query);
	}

	/**
	 * @param query		Query terms for the search
	 * @param options	Options for the search, for detail take a look at our @see <a href="https://swiftype.com/documentation/searching">Searching Documentation</a>
	 * @return			Search results for the current DocumentType
	 */
	public SearchResult search(final String query, final SearchOptions options) {
		checkDestroyed();
		return api.search(slug, query, options);
	}

	/**
	 * @param query		Query for the suggestions
	 * @return			Suggestions for this DocumentType
	 */
	public SuggestResult suggest(final String query) {
		checkDestroyed();
		return api.suggest(slug, query);
	}

	/**
	 * @param query		Query for the suggestions
	 * @param options	Options for the suggest, for details take a look at our @see <a href="https://swiftype.com/documentation/autocomplete">Autocomplete Documentation</a>
	 * @return			Suggestions for this DocumentType
	 */
	public SuggestResult suggest(final String query, final SearchOptions options) {
		checkDestroyed();
		return api.suggest(slug, query, options);
	}

	/**
	 * @return	DocumentsApi for this DocumentType
	 */
	public DocumentsApi getDocumentsApi() {
		checkDestroyed();
		return new DocumentsApi(engineId, slug);
	}

	/**
	 * @return	Name of this DocumentType
	 */
	public String getName() {
		checkDestroyed();
		return name;
	}

	/**
	 * @return	Slug for this DocumentType
	 */
	public String getSlug() {
		checkDestroyed();
		return slug;
	}

	/**
	 * @return	Engine id for this DocumentType
	 */
	public String getEngineId() {
		checkDestroyed();
		return engineId;
	}

	/**
	 * @return	Last update for this DocumentType
	 */
	public Date getUpdatedAt() {
		checkDestroyed();
		return updatedAt;
	}

	/**
	 * @return	Document count for this DocumentType
	 */
	public int getDocumentCount() {
		checkDestroyed();
		return documentCount;
	}

	@Override
	public String toString() {
		return "{name=" + name + ", slug=" + slug + ", engineId=" + engineId + ", updatedAt=" + updatedAt + ", documentCount=" + documentCount + "}";
	}

	public static DocumentType fromJson(final JSONObject json) {
		final String name = json.optString("name");
		final String slug = json.optString("slug");
		final String engineId = json.optString("engine_id");
		final Date updatedAt = DatatypeConverter.parseDateTime(json.optString("updated_at")).getTime();
		final int documentCount = json.optInt("document_count");
		return new DocumentType(name, slug, engineId, updatedAt, documentCount);
	}

	private void checkDestroyed() {
		if (destroyed) {
			throw new IllegalStateException("DocumentType is already destroyed or not up-to-date.");
		}
	}

	private void updateFields(final DocumentType documentType) {
		if (documentType == null) {
			destroyed = true;
		} else {
			this.name = documentType.name;
			this.slug = documentType.slug;
			this.engineId = documentType.engineId;
			this.updatedAt = documentType.updatedAt;
			this.documentCount = documentType.documentCount;
			this.destroyed = false;
		}
	}
}
