package com.swiftype.api.easy;

import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.json.JSONObject;

public class Domain {
	private String id;
	private String submittedUrl;
	private String startCrawlUrl;
	private boolean isCrawling;
	private int documentCount;
	private Date updatedAt;
	private DomainsApi api;
	private boolean destroyed;

	private Domain(final String engineId,
				   final String id,
				   final String submittedUrl,
				   final String startCrawlUrl,
				   final boolean isCrawling,
				   final int documentCount,
				   final Date updatedAt) {
		this.id = id;
		this.submittedUrl = submittedUrl;
		this.startCrawlUrl = startCrawlUrl;
		this.isCrawling = isCrawling;
		this.documentCount = documentCount;
		this.updatedAt = updatedAt;

		this.destroyed = false;
		this.api = new DomainsApi(engineId);
	}

	/**
	 * @return	Asynchronously recrawled domain
	 */
	public Domain recrawl() {
		checkDestroyed();
		return api.recrawl(id);
	}

	/**
	 * @param url	URL to add or update on this domain
	 * @return
	 */
	public boolean crawlUrl(final String url) {
		checkDestroyed();
		return api.crawlUrl(id, url);
	}

	/**
	 * Updates the Domain with its current values
	 *
	 * @return	Updated domain
	 */
	public Domain refresh() {
		final Domain domain = api.get(id);
		updateFields(domain);
		return this;
	}

	/**
	 * @return	Success of deleting this domain
	 */
	public boolean destroy() {
		checkDestroyed();
		destroyed = api.destroy(id);
		return destroyed;
	}

	/**
	 * @return	Id of the domain
	 */
	public String getId() {
		checkDestroyed();
		return id;
	}

	/**
	 * @return	Submitted URL for this domain
	 */
	public String getSubmittedUrl() {
		checkDestroyed();
		return submittedUrl;
	}

	/**
	 * @return	URL used to start the crawl for this domain
	 */
	public String getStartCrawlUrl() {
		checkDestroyed();
		return startCrawlUrl;
	}

	/**
	 * @return	Checks if Domain is crawling
	 */
	public boolean isCrawling() {
		refresh();
		checkDestroyed();
		return isCrawling;
	}

	/**
	 * @return	Indexed pages for this domain
	 */
	public int getDocumentCount() {
		checkDestroyed();
		return documentCount;
	}

	/**
	 * @return	Last update for this domain
	 */
	public Date getUpdatedAt() {
		checkDestroyed();
		return updatedAt;
	}

	public static Domain fromJson(final String engineId, final JSONObject json) {
		final String id = json.optString("id");
		final String submittedUrl = json.optString("submitted_url");
		final String startCrawlUrl = json.optString("start_crawl_url");
		final boolean isCrawling = json.optBoolean("crawling");
		final int documentCount = json.optInt("document_count");
		final Date updatedAt = DatatypeConverter.parseDateTime(json.optString("updated_at")).getTime();
		return new Domain(engineId, id, submittedUrl, startCrawlUrl, isCrawling, documentCount, updatedAt);
	}

	private void updateFields(final Domain domain) {
		if (domain == null) {
			destroyed = true;
		} else {
			this.id = domain.id;
			this.submittedUrl = domain.submittedUrl;
			this.startCrawlUrl = domain.startCrawlUrl;
			this.isCrawling = domain.isCrawling;
			this.documentCount = domain.documentCount;
			this.updatedAt = domain.updatedAt;

			this.destroyed = false;
		}
	}

	private void checkDestroyed() {
		if (destroyed) {
			throw new IllegalStateException("Domain is already destroyed or currently not available.");
		}
	}
}
