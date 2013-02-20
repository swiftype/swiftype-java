package com.swiftype.api.easy.helper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

import javax.xml.ws.WebServiceException;


public class Client {
	private static final String USER_AGENT;
	private static final SwiftypeConfig CONFIG;

	static {
		CONFIG = SwiftypeConfig.INSTANCE;
		USER_AGENT = "Swiftype/Java-" + CONFIG.version;
	}

	public static String get(final String path, final String[] ... params) {
		return requestWithoutBody("GET", path, params);
	}

	public static String post(final String path, final String data) {
		return requestWithBody("POST", path, data);
	}

	public static String put(final String path, final String data) {
		return requestWithBody("PUT", path, data);
	}

	public static String delete(final String path, final String[] ... params) {
		return requestWithoutBody("DELETE", path, params);
	}

	private static String requestWithoutBody(final String method, final String path, final String[] ... params) {
		final URL url = buildUrl(path, params);
		try {
			final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(method);
			connection.setRequestProperty("User-Agent", USER_AGENT);

			final int status = connection.getResponseCode();

			final boolean isSuccess = (status / 100 == 2);
			InputStream in = isSuccess ? connection.getInputStream() : connection.getErrorStream();

			final StringBuilder sb = new StringBuilder();
			if (in != null) {
				final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
					sb.append("\n");
				}
				reader.close();
			}

			if (!isSuccess) {
				errorResponse(status, sb.toString());
			}

			return sb.toString();
		} catch (FileNotFoundException e) {
			throw new WebServiceException(e.getMessage());
		} catch (IOException e) {
			throw new WebServiceException(e.getMessage());
		}
	}

	private static String requestWithBody(final String method, final String path, final String data) {
		final URL url = buildUrl(path);
		try {
			final byte[] dataBytes = data.getBytes("UTF-8");
			final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod(method);
			connection.setRequestProperty("User-Agent", USER_AGENT);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Content-Length", Integer.toString(dataBytes.length));

			final OutputStream out = connection.getOutputStream();
			out.write(dataBytes);
			out.flush();
			final int status = connection.getResponseCode();

			final boolean isSuccess = (status / 100 == 2);
			InputStream in = isSuccess ? connection.getInputStream() : connection.getErrorStream();

			final StringBuilder sb = new StringBuilder();
			if (in != null) {
				final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line;
				while((line = reader.readLine()) != null) {
					sb.append(line);
					sb.append("\n");
				}
				reader.close();
			}

			if (!isSuccess) {
				errorResponse(status, sb.toString());
			}

			return sb.toString();
		} catch (FileNotFoundException e) {
			throw new WebServiceException(e.getMessage());
		} catch (IOException e) {
			throw new WebServiceException(e.getMessage());
		}
	}

	private static URL buildUrl(final String path, final String[] ... params) {
		final StringBuilder sb = new StringBuilder();
		sb.append(CONFIG.getBaseUrl() + path + ".json?auth_token=" + CONFIG.getApiKey());
		try {
			for (String[] param : params) {
				sb.append("&");
				sb.append(URLEncoder.encode(param[0], "UTF-8"));
				sb.append("=");
				sb.append(URLEncoder.encode(param[1], "UTF-8"));
			}
			return new URL(sb.toString());
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("'" + sb.toString() + "' is not a valid URL.");
		} catch (UnsupportedEncodingException e) {
			final StringBuilder paramsSb = new StringBuilder();
			for (final String[] param : params) {
				paramsSb.append(Arrays.toString(param));
				paramsSb.append(", ");
			}
			if (params.length > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
			throw new IllegalArgumentException("'" + paramsSb.toString() + "' is no valid parameter set.");
		}
	}

	private static void errorResponse(final int status, final String body) {
		throw new WebServiceException("Status: " + status + ", Body: " + body);
	}
}
