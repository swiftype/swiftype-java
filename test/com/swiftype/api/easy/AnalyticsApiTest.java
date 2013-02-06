package com.swiftype.api.easy;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.swiftype.api.easy.AnalyticsApi;
import com.swiftype.api.easy.AnalyticsApi.DateCount;
import com.swiftype.api.easy.AnalyticsApi.QueryCount;
import com.swiftype.api.easy.helper.SwiftypeConfig;

public class AnalyticsApiTest {
	private AnalyticsApi api;

	@Before
	public void setUp() throws Exception {
		final SwiftypeConfig config = SwiftypeConfig.INSTANCE;
		config.setApiHost("127.0.0.1:9292");
		api = new AnalyticsApi("engine_id");
	}

	@Test
	public void testSearches() {
		final List<DateCount> dateCounts = api.searches();
		assertEquals(1, dateCounts.size());
	}

	@Test
	public void testSearchesPagination() {
		final List<DateCount> dateCounts = api.searches(new Date(), new Date());
		assertEquals(0, dateCounts.size());
	}

	@Test
	public void testAutoSelects() {
		final List<DateCount> dateCounts = api.autoselects();
		assertEquals(1, dateCounts.size());
	}

	@Test
	public void testAutoSelectsPagination() {
		final List<DateCount> dateCounts = api.autoselects(new Date(), new Date());
		assertEquals(0, dateCounts.size());
	}

	@Test
	public void testTopQueries() {
		final List<QueryCount> queryCounts = api.topQueries();
		assertEquals(2, queryCounts.size());
	}

	@Test
	public void testTopQueriesPagination() {
		final List<QueryCount> queryCounts = api.topQueries(2, 10);
		assertEquals(0, queryCounts.size());
	}
}
