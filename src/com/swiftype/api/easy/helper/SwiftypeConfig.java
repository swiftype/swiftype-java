package com.swiftype.api.easy.helper;

public enum SwiftypeConfig {
	INSTANCE;

	public final String version = "0.1.0";

	private final String apiBasePath = "/api/v1/";

	private String protocol;
	private String apiHost;
	private String apiKey;

	private SwiftypeConfig() {
		protocol = "http";
		apiHost = "api.swiftype.com";
		apiKey = "PLEASE_SET_YOUR_KEY";
	}

	public String getApiKey() {
		return apiKey;
	}

	public SwiftypeConfig setApiKey(final String apiKey) {
		this.apiKey = apiKey;
		return this;
	}

	public String getApiHost() {
		return apiHost;
	}

	public SwiftypeConfig setApiHost(final String apiHost) {
		this.apiHost = apiHost;
		return this;
	}

	public String getBaseUrl() {
		return protocol + "://" + apiHost + apiBasePath;
	}
}
