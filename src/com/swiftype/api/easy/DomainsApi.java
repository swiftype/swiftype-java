package com.swiftype.api.easy;

import javax.xml.ws.WebServiceException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.swiftype.api.easy.helper.Client;

public class DomainsApi {
	private final String engineId;

	public DomainsApi(final String engineId) {
		this.engineId = engineId;
	}

	/**
	 * @return	List of all domains for an engine
	 */
	public Domain[] getAll() {
		try {
			final JSONArray domainsJson = new JSONArray(Client.get(domainsPath()));
			final Domain[] domains = new Domain[domainsJson.length()];
			for (int i = 0; i < domains.length; ++i) {
				domains[i] = Domain.fromJson(engineId, domainsJson.getJSONObject(i));
			}
			return domains;
		} catch (JSONException e) {
			throw new IllegalStateException(e.getMessage());
		}
	}

	/**
	 * @param domainId	Id of the wanted domain
	 * @return			Specified domain
	 */
	public Domain get(final String domainId) {
		return toDomain(Client.get(domainPath(domainId)));
	}

	/**
	 * @param url	Start URL used for crawling the domain
	 * @return		Domain belonging to the specified URL
	 */
	public Domain create(final String url) {
		final String response = Client.post(domainsPath(), "{\"domain\": {\"submitted_url\": \"" + url + "\"} }");
		return toDomain(response);
	}

	/**
	 * @param domainId	Id of the domain to delete
	 * @return			Success of deletion
	 */
	public boolean destroy(final String domainId) {
		try {
			Client.delete(domainPath(domainId));
			return true;
		} catch (WebServiceException e) {
			return false;
		}
	}

	/**
	 * @param domainId	Id of the domain
	 * @return			Asynchronously recrawled domain
	 */
	public Domain recrawl(final String domainId) {
		return toDomain(Client.put(domainPath(domainId) + "/recrawl", ""));
	}

	/**
	 * @param domainId	Id of the domain
	 * @param url		URL to add or update on this domain
	 */
	public void crawlUrl(final String domainId, final String url) {
		Client.put(domainPath(domainId) + "/crawl_url", "{\"url\": \"" + url + "\"}");
	}

	String domainsPath() {
		return EnginesApi.enginePath(engineId) + "/domains";
	}

	String domainPath(final String domainId) {
		return domainsPath() + "/" + domainId;
	}

	private Domain toDomain(final String response){
		try {
			final JSONObject json = new JSONObject(response);
			return Domain.fromJson(engineId, json);
		} catch (JSONException e) {
			throw new IllegalStateException(e.getMessage());
		}
	}
}
