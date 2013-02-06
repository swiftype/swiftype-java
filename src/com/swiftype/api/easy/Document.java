package com.swiftype.api.easy;

import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.json.JSONObject;

public class Document {
	private String id;
	private String externalId;
	private String engineId;
	private String documentTypeId;
	private Date updatedAt;
	private JSONObject allFields;
	private boolean destroyed;
	private final DocumentsApi api;

	private Document(final String id,
					 final String externalId,
					 final String engineId,
					 final String documentTypeId,
					 final Date updatedAt,
					 final JSONObject allFields) {
		this.id = id;
		this.externalId = externalId;
		this.engineId = engineId;
		this.documentTypeId = documentTypeId;
		this.updatedAt = updatedAt;
		this.allFields = allFields;

		this.destroyed = false;
		this.api = new DocumentsApi(engineId, documentTypeId);
	}

	/**
	 * @return	Update the current Document
	 */
	public Document refresh() {
		final Document document = api.get(id);
		updateFields(document);
		return this;
	}

	/**
	 * @param fields 	Fields to update
	 *
	 * example:
	 *
	 * {"title": "my title 2", "page_type": "user2"}
	 *
	 * @return 			The current updated Document
	 */
	public Document update(final JSONObject fields) {
		checkDestroyed();
		api.update(id, fields);
		return refresh();
	}

	/**
	 * @return	Success of deletion
	 */
	public boolean destroy() {
		checkDestroyed();
		destroyed = api.destroy(id);
		return destroyed;
	}

	/**
	 * @return	Id of this Document
	 */
	public String getId() {
		checkDestroyed();
		return id;
	}

	/**
	 * @return	External id of this Document
	 */
	public String getExternalId() {
		checkDestroyed();
		return externalId;
	}

	/**
	 * @return	Engine id of this Document
	 */
	public String getEngineId() {
		checkDestroyed();
		return engineId;
	}

	/**
	 * @return	DocumentType id of this Document
	 */
	public String getDocumentTypeId() {
		checkDestroyed();
		return documentTypeId;
	}

	/**
	 * @return	Last update to this Document
	 */
	public Date getUpdatedAt() {
		checkDestroyed();
		return updatedAt;
	}

	/**
	 * @return	All fields this Document has as a JSONObject
	 */
	public JSONObject getAllFields() {
		checkDestroyed();
		return allFields;
	}

	public static Document fromJson(final JSONObject json) {
		final String id = json.optString("id");
		final String externalId = json.optString("external_id");
		final String engineId = json.optString("engine_id");
		final String documentTypeId = json.optString("document_type_id");
		final Date updatedAt = DatatypeConverter.parseDateTime(json.optString("updated_at")).getTime();
		return new Document(id, externalId, engineId, documentTypeId, updatedAt, json);
	}

	private void checkDestroyed() {
		if (destroyed) {
			throw new IllegalStateException("Document is already destroyed or not up-to-date.");
		}
	}

	private void updateFields(final Document document) {
		if (document == null) {
			destroyed = true;
		} else {
			this.id = document.id;
			this.externalId = document.externalId;
			this.engineId = document.engineId;
			this.documentTypeId = document.documentTypeId;
			this.updatedAt = document.updatedAt;
			this.allFields = document.allFields;
			this.destroyed = false;
		}
	}
}
