# Quickstart

## Setup

1. Create an account at [Swiftype](https://swiftype.com/) and get your API key from your [Account Settings](https://swiftype.com/user/edit).

2. Configure your client:

	    SwiftypeConfig.INSTANCE.setApiKey("YOUR_API_KEY");

3. Create an `Engine` named e.g. `youtube`:

	    final EnginesApi enginesApi = new EnginesApi();
		final Engine engine = enginesApi.create("youtube");

4. Create your `DocumentType`s:

	    final DocumentTypesApi documentTypesApi = new DocumentTypesApi("youtube");
		documentTypesApi.create("videos");
		documentTypesApi.create("channels");

## Indexing

Now you need to create your `Document`s. It's very important to think about the type of each field you create a `Document`. The `DocumentType` the `Document` belongs to will remember each fields type and it is not possible to change it. The type specifies a fields features and you should choose them wisely. For details please have a look at our [Field Types Documentation](https://swiftype.com/documentation/overview#field_types).

Add a `Document` to the `videos` `DocumentType`:

	final DocumentsApi documentsApi = new DocumentsApi("youtube", "videos");
	final JSONObject jsonDocument = new JSONObject("{\"external_id\": \"external_id1\"," +
		" \"fields\": [" +
		"{\"name\": \"title\", \"value\": \"Swiftype Demo\", \"type\": \"string\"}," +
		"{\"name\": \"tags\", \"value\": [\"Swiftype\", \"Search\", \"Full text search\"], \"type\": \"string\"}," +
		"{\"name\": \"url\", \"value\": \"http://www.youtube.com/watch?v=pITuOcGgpBs\", \"type\": \"enum\"}," +
		"{\"name\": \"category\", \"value\": [\"Tutorial\", \"Product\"], \"type\": \"enum\"}," +
		"{\"name\": \"publication_date\", \"value\": \"2012-05-08T12:07Z\", \"type\": \"date\"}," +
		"{\"name\": \"likes\", \"value\": 31, \"type\": \"integer\"}," +
		"{\"name\": \"length\", \"value\": 1.50, \"type\": \"float\"}" +
		"]}");
	final Document document = documentsApi.create(jsonDocument);

Add a `Document` to the `users` `DocumentType`:

	final DocumentsApi documentsApi = new DocumentsApi("youtube", "channels");
	final JSONObject jsonDocument = new JSONObject("{\"external_id\": \"external_id1\"," +
		" \"fields\": [" +
		"{\"name\": \"title\", \"value\": \"Swiftype\", \"type\": \"string\"}," +
		"{\"name\": \"url\", \"value\": \"http://www.youtube.com/user/swiftype\", \"type\": \"enum\"}," +
		"{\"name\": \"video_views\", \"value\": 15678, \"type\": \"integer\"}," +
		"{\"name\": \"video_counts\", \"value\": 6, \"type\": \"integer\"}" +
		"]}");
	final Document document = documentsApi.create(jsonDocument);

## Searching

Now your `Engine` is ready to receive queries. By default, search queries will match any fields that are of type `string` or `text`. You can search each `DocumentType` individually:

	final DocumentTypesApi documentTypesApi = new DocumentTypesApi("youtube");
	final SearchResult videosResult = documentTypesApi.search("videos", "swiftype");

	final SearchResult channelsResult = documentTypeApi.search("channels", "swiftype");

or search all `DocumentType`s on your `Engine` at once:

	final EnginesApi enginesApi = new EnginesApi();
	final Map<String, SearchResult> results = enginesApi.search("youtube", "swiftype");

## Autocomplete

Finally, as with full-text searches, you may perform autocomplete-style (prefix match) searches as well:

	final DocumentTypesApi documentTypesApi = new DocumentTypesApi("youtube");
	final SuggestResult videosResult = documentTypesApi.suggest("videos", "swiftype");

# API Documentation

## Configuration:

Before issuing commands to the API, configure the client with your API key:

	SwiftypeConfig.INSTANCE.setApiKey("YOUR_API_KEY");

You can find your API key in your [Account Settings](https://swiftype.com/user/edit).

## Search

If you want to search for e.g. `swiftype` on your engine, you can use:

	final EnginesApi enginesApi = new EnginesApi();
	final Map<String, SearchResult> results =  enginesApi.search("youtube", "swiftype");

To limit the search to only the `videos` DocumentType:

	final DocumentTypesApi documentTypesApi = new DocumentTypesApi("youtube");
	final SearchResult results = documentTypesApi.search("videos", "swiftype");

Both search methods allow you to specify options as an extra parameter to e.g. filter or sort on fields. For more details on these options pelease have a look at the [Search Options](https://swiftype.com/documentation/searching) and the next section on Options. Here is an example for showing only `videos` that are in the `category` `Tutorial`:

	final SearchOptions options = new SearchOptions.Builder().filters("videos", "category", "Tutorial").build();
	final EnginesApi enginesApi = new EnginesApi();
	final Map<String, SearchResult> results =  enginesApi.search("youtube", "swiftype", options);

### Options

Create a options builder:

	final SearchOptions.Builder optionsBuilder = new SearchOptions.Builder();

After adding all the options you want, you can build a SearchOptions object with:

	final SearchOptions options = optionsBuilder.build();

Get only the `title` and `length` fields on a search or autocomplete:

	optionsBuilder.fetchFields("videos", "title", "length");

To search only for `title`:

	optionsBuilder.searchFields("videos", "title");

To boost search search only `title` and `tags`:

	optionsBuilder.searchFields("videos", "title^4", "tags^2");

To show only `videos` that are of `category` `Product`:

	optionsBuilder.filter("videos", "category", "Product");

To search or autocomplete just for `videos` and `channels`:

	optionsBuilder.documentTypes("videos", "channels");

To rank results higher for more `likes`:

	optionsBuilder.functionalBoosts("videos", "likes", SearchOptions.FunctionalBoost.LOGARITHMIC);

To show the second page of a search result:

	optionsBuilder.page(2);

To sort the results based on `title`:

	optionsBuilder.sortField("videos", "title");

To sort in descending order:

	optionsBuilder.sortField("videos", "title", SearchOptions.Direction.DESC);

To change the sort direction for the search field which defaults to `_score`:

	optionsBuilder.sortDirection("videos", SearchOptions.Direction.ASC);

To count the available `categories`:

	optionsBuilder.facets("videos", "category")

## Autocomplete

Autocompletes have the same functionality as searches. You can autocomplete using all documents:

	final EnginesApi enginesApi = new EnginesApi();
	final Map<String, SuggestResult> results = enginesApi.suggest("youtube", "swi");

or just for one DocumentType:

	final DocumentTypesApi documentTypesApi = new DocumentTypesApi("youtube");
	final SuggestResult results = documentTypesApi.suggest("videos", "swi");

or add options to have more control over the results:

	final SearchOptions options = new SearchOptions.Builder().sortField("videos", "likes").build();
	final EnginesApi enginesApi = new EnginesApi();
	final Map<String, SuggestResult> results = enginesApi.suggest("youtube", "swi", options);

## Engines

Retrieve every `Engine`:

	final EnginesApi enginesApi = new EnginesApi();
	final Engine[] engines = enginesApi.getAll();

Create a new `Engine` with the name `youtube`:

	final EnginesApi enginesApi = new EnginesApi();
	final Engine engine = enginesApi.create("youtube");

Retrieve an `Engine` by `slug` or `id`:

	final EnginesApi enginesApi = new EnginesApi();
	final Engine engine = enginesApi.get("youtube");

To delete an `Engine` you need the `slug` or the `id` field of an `engine`:

	final EnginesApi enginesApi = new EnginesApi();
	enginesApi.destroy("youtube");

## Document Types

Retrieve all `DocumentTypes`s of the `Engine` with the `slug` field `youtube`:

	final DocumentTypesApi documentTypesApi = new DocumentTypesApi("youtube");
	final DocumentType[] documentTypes = documentTypesApi.getAll();

Create a new `DocumentType` for an `Engine` with the name `videos`:

	final DocumentTypesApi documentTypesApi = new DocumentTypesApi("youtube");
	final DocumentType documentType = documentTypesApi.create("videos");

Retrieve an `DocumentType` by `slug` or `id`:

	final DocumentTypesApi documentTypesApi = new DocumentTypesApi("youtube");
	final DocumentType documentType = documentTypesApi.get("videos")

Delete a `DocumentType` using the `slug` or `id` of it:

	final DocumentTypesApi documentTypesApi = new DocumentTypesApi("youtube");
	documentTypesApi.destroy("videos");

## Documents

Retrieve `Document`s of `Engine` `youtube` and `DocumentType` `videos`:

	final DocumentsApi documentsApi = new DocumentsApi("youtube", "videos");
	final Document[] documents = documentsApi.getAll();

Get the second batch of `Document`s with batch size 10:

	final DocumentsApi documentsApi = new DocumentsApi("youtube", "videos");
	final Document[] documents = documentsApi.getAll(2, 10);

Retrieve a specific `Document` using its `id` or `external_id`:

	final DocumentsApi documentsApi = new DocumentsApi("youtube", "videos");
	final Document document = documentsApi.get("external_id1");

Create a new `Document` with mandatory and unique `external_id` and user-defined fields:

	final DocumentsApi documentsApi = new DocumentsApi("youtube", "videos");
	final JSONObject jsonDocument = new JSONObject("{\"external_id\": \"external_id1\"," +
		" \"fields\": [" +
		"{\"name\": \"title\", \"value\": \"Swiftype Demo\", \"type\": \"string\"}," +
		"{\"name\": \"tags\", \"value\": [\"Swiftype\", \"Search\", \"Full text search\"], \"type\": \"string\"}," +
		"{\"name\": \"url\", \"value\": \"http://www.youtube.com/watch?v=pITuOcGgpBs\", \"type\": \"enum\"}," +
		"{\"name\": \"category\", \"value\": [\"Tutorial\", \"Product\"], \"type\": \"enum\"}," +
		"{\"name\": \"publication_date\", \"value\": \"2012-05-08T12:07Z\", \"type\": \"date\"}," +
		"{\"name\": \"likes\", \"value\": 31, \"type\": \"integer\"}," +
		"{\"name\": \"length\", \"value\": 1.50, \"type\": \"float\"}" +
		"]}");
	documentsApi.create(jsonDocument);

Create multiple `Document`s at once and return status for each `Document`< creation:

	final DocumentsApi documentsApi = new DocumentsApi("youtube", "videos");
	final JSONObject jsonDocument1 = new JSONObject("{\"external_id\": \"external_id1\"," +
		" \"fields\": [" +
		"{\"name\": \"title\", \"value\": \"Swiftype Demo\", \"type\": \"string\"}," +
		"{\"name\": \"tags\", \"value\": [\"Swiftype\", \"Search\", \"Full text search\"], \"type\": \"string\"}," +
		"{\"name\": \"url\", \"value\": \"http://www.youtube.com/watch?v=pITuOcGgpBs\", \"type\": \"enum\"}," +
		"{\"name\": \"category\", \"value\": [\"Tutorial\", \"Product\"], \"type\": \"enum\"}," +
		"{\"name\": \"publication_date\", \"value\": \"2012-05-08T12:07Z\", \"type\": \"date\"}," +
		"{\"name\": \"likes\", \"value\": 27, \"type\": \"integer\"}," +
		"{\"name\": \"length\", \"value\": 1.50, \"type\": \"float\"}" +
		"]}");
	final JSONObject jsonDocument2 = new JSONObject("{\"external_id\": \"external_id2\"," +
		" \"fields\": [" +
		"{\"name\": \"title\", \"value\": \"Swiftype Search Wordpress Plugin Demo\", \"type\": \"string\"}," +
		"{\"name\": \"tags\", \"value\": [\"Swiftype\", \"Search\", \"Full text search\", \"WordPress\"], \"type\": \"string\"}," +
		"{\"name\": \"url\", \"value\": \"http://www.youtube.com/watch?v=rukXYKEpvS4\", \"type\": \"enum\"}," +
		"{\"name\": \"category\", \"value\": [\"Tutorial\", \"Wordpress\"], \"type\": \"enum\"}," +
		"{\"name\": \"publication_date\", \"value\": \"2012-08-15T09:07Z\", \"type\": \"date\"}," +
		"{\"name\": \"likes\", \"value\": 2, \"type\": \"integer\"}," +
		"{\"name\": \"length\", \"value\": 2.16, \"type\": \"float\"}" +
		"]}");
	final boolean[] stati = documentsApi.create(new JSONObject[] {jsonDocument1, jsonDocument2});

Update fields of an existing `Document` specified by `id` or `external_id`:

	final DocumentsApi documentsApi = new DocumentsApi("youtube", "videos");
	final JSONObject fields = new JSONObject("{\"likes\": 28, \"category\": [\"Tutorial\", \"Search\"]}");
	final Document document = documentsApi.update("external_id1", fields);

Update multiple `Document`s at once:

	final DocumentsApi documentsApi = new DocumentsApi("youtube", "videos");
	final JSONObject jsonDocument1 = new JSONObject("{\"external_id\": \"external_id1\", \"fields\": {\"likes\": 29}}");
	final JSONObject jsonDocument2 = new JSONObject("{\"external_id\": \"external_id2\", \"fields\": {\"likes\": 4}}");
	final boolean[] stati = documentsApi.update(new JSONObject[] {jsonDocument1, jsonDocument2});

Create or update a `Document`:

	final DocumentsApi documentsApi = new DocumentsApi("youtube", "videos");
	final JSONObject jsonDocument = new JSONObject("{\"external_id\": \"external_id3\"," +
	    " \"fields\": [" +
		"{\"name\": \"title\", \"value\": \"Swiftype Install Type 1: Show results in an overlay\", \"type\": \"string\"}," +
		"{\"name\": \"tags\", \"value\": [\"Swiftype\", \"Search\", \"Full text search\", \"Web\"], \"type\": \"string\"}," +
		"{\"name\": \"url\", \"value\": \"http://www.youtube.com/watch?v=mj2ApIx3frs\", \"type\": \"enum\"}" +
		"]}");
	final Document document = documentsApi.createOrUpdate(jsonDocument);

Create or update multiple `Documents` at once:

	final DocumentsApi documentsApi = new DocumentsApi("youtube", "videos");
	final JSONObject jsonDocument1 = new JSONObject("{\"external_id\": \"external_id4\"," +
	    " \"fields\": [" +
		"{\"name\": \"title\", \"value\": \"Swiftype Install Type 2: Show results on the current page\", \"type\": \"string\"}," +
		"{\"name\": \"tags\", \"value\": [\"Swiftype\", \"Search\", \"Full text search\", \"Web\"], \"type\": \"string\"}," +
		"{\"name\": \"url\", \"value\": \"http://www.youtube.com/watch?v=6uaZXYK2WOE\", \"type\": \"enum\"}" +
		"]}");
	final JSONObject jsonDocument2 = new JSONObject("{\"external_id\": \"external_id5\"," +
	    " \"fields\": [" +
		"{\"name\": \"title\", \"value\": \"Swiftype Install Type 3: Show results on a new page\", \"type\": \"string\"}," +
		"{\"name\": \"tags\", \"value\": [\"Swiftype\", \"Search\", \"Full text search\", \"Web\"], \"type\": \"string\"}," +
		"{\"name\": \"url\", \"value\": \"http://www.youtube.com/watch?v=ebSWAscBPtc\", \"type\": \"enum\"}" +
		"]}");
	final boolean[] stati = documentsApi.createOrUpdate(new JSONObject[] {jsonDocument1, jsonDocument2});

Destroy a `Document`:

	final DocumentsApi documentsApi = new DocumentsApi("youtube", "videos");
	documentsApi.destroy("external_id5");

Destroy multiple `Document`s at once:

	final DocumentsApi documentsApi = new DocumentsApi("youtube", "videos");
	final boolean[] stati = documentsApi.destroy("external_id2", "external_id3", "external_id6");

## Domains

Retrieve all `Domain`s of `Engine` `websites`:

	final DomainsApi domainsApi = new DomainsApi("websites");
	final Domain[] domains = domainsApi.getAll();

Retrieve a specific `Domain` by `id`. You can get the "generated_id" from the returned `Domain` after a create or from one of the `Domain`s after a `domainsApi.getAll()` with `domain.getId()`:

	final DomainsApi domainsApi = new DomainsApi("websites");
	final Domain domain = domainsApi.get("generated_id");

Create a new `Domain` with the URL `https://swiftype.com` and start crawling:

	final DomainsApi domainsApi = new DomainsApi("websites");
	final Domain domain = domainsApi.create("https://swiftype.com");

Delete a `Domain` using its `id`:

	final DomainsApi domainsApi = new DomainsApi("websites");
	final Domain domain = domainsApi.destroy("generated_id");

Initiate a recrawl of a specific `Domain` using its `id`:

	final DomainsApi domainsApi = new DomainsApi("websites");
	final Domain domain = domainsApi.recrawl("generated_id");

Add or update a URL for a `Domain`:

	final DomainsApi domainsApi = new DomainsApi("websites");
	domainsApi.crawlUrl("generated_id", "https://swiftype.com/new/path/about.html");

## Analytics

To get the amount of searches on your `Engine` in the last 14 days use:

	final AnalyticsApi analyticsApi = new AnalyticsApi("youtube");
	final List<DateCount> searches = analyticsApi.searches();

You can also use a specific start and/or end date:

	final AnalyticsApi analyticsApi = new AnalyticsApi("youtube");
	final Date from = ...
	final Date to = ...
	final List<DateCount> searches = analyticsApi.searches(from, to);

To get the amount of autoselects (clicks on autocomplete results) use:

	final AnalyticsApi analyticsApi = new AnalyticsApi("youtube");
	final List<DateCount> autoselects = analyticsApi.autoselects();

As with searches you can also limit by start and/or end date:

	final AnalyticsApi analyticsApi = new AnalyticsApi("youtube");
	final Date from = ...
	final Date to = ...
	final List<DateCount> autoselects = analyticsApi.autoselects(from, to);

If you are interested in the top queries for your `Engine` you can use:

	final AnalyticsApi analyticsApi = new AnalyticsApi("youtube");
	final List<QueryCount> topQueries = analyticsApi.topQueries();

To see more top queries you can paginate through them using:

	final AnalyticsApi analyticsApi = new AnalyticsApi("youtube");
	final List<QueryCount> topQueries = analyticsApi.topQueries(2, 10);

Or you can get the top queries in a specific date range:

	final AnalyticsApi analyticsApi = new AnalyticsApi("youtube");
	final Date from = ...
	final Date to = ...
	final List<QueryCount> topQueries = analyticsApi.topQueries(from, to);

If you want to improve you search results, you should always have a look at search queries, that return no results and perhaps add some `Document`s that match for this query or use our pining feature to add `Document`s for this query:

	final AnalyticsApi analyticsApi = new AnalyticsApi("youtube");
	final List<QueryCount> topNoResultQueries = analyticsApi.topNoResultQueries();

You can also specifiy a date range for no result queries:

	final AnalyticsApi analyticsApi = new AnalyticsApi("youtube");
	final Date from = ...
	final Date to = ...
	final List<QueryCount> topNoResultQueries = analyticsApi.topNoResultQueries(from, to);
