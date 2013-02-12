# Configuration:

Before issuing commands to the API, configure the client with your API key:

	SwiftypeConfig.INSTANCE.setApiKey("YOUR_API_KEY");

You can find your API key in your [Account Settings](https://swiftype.com/user/edit).

# Search

If you want to search for e.g. `action` on your engine, you can use:

	final EnginesApi enginesApi = new EnginesApi();
	final Map<String, SearchResult> results =  enginesApi.search("bookstore", "action");

To limit the search to only the `books` DocumentType:

	final DocumentTypesApi documentTypesApi = new DocumentTypesApi("bookstore");
	final SearchResult results = documentTypesApi.search("books", "action");

Both search methods allow you to specify options as an extra parameter to e.g. filter or sort on fields. For more details on these options pelease have a look at the [Search Options](https://swiftype.com/documentation/searching). Here is an example for showing only books that are in stock:

	final Map<String, String> conditions = new HashMap<String, String>();
	conditions.put("in_stock", true);
	final SearchOptions options = new SearchOptions.Builder().filters("books", conditions).build();
	final EnginesApi enginesApi = new EnginesApi();
	final Map<String, SearchResult> results =  enginesApi.search("bookstore", "action", options);

# Autocomplete

Autocompletes have the same functionality as searches. You can autocomplete using all documents:

	final EnginesApi enginesApi = new EnginesApi();
	final Map<String, SuggestResult> results = enginesApi.suggest("bookstore", "acti");

or just for one DocumentType:

	final DocumentTypesApi documentTypesApi = new DocumentTypesApi("bookstore");
	final SuggestResult results = documentTypesApi.suggest("books", "acti");

or add options to have more control over the results:

	final SearchOptions options = new SearchOptions.Builder().sortField("books", "price").build();
	final EnginesApi enginesApi = new EnginesApi();
	final Map<String, SuggestResult> results = enginesApi.suggest("bookstore", "acti", options);

# Engines

Retrieve every `Engine`:

	final EnginesApi enginesApi = new EnginesApi();
	final Engine[] engines = enginesApi.getAll();

Create a new `Engine` with the name `bookstore`:

	final EnginesApi enginesApi = new EnginesApi();
	final Engine engine = enginesApi.create("bookstore");

Retrieve an `Engine` by `slug` or `id`:

	final EnginesApi enginesApi = new EnginesApi();
	final Engine engine = enginesApi.get("bookstore");

To delete an `Engine` you need the `slug` or the `id` field of an `engine`:

	final EnginesApi enginesApi = new EnginesApi();
	enginesApi.destroy("bookstore");

# Document Types

Retrieve all `DocumentTypes`s of the `Engine` with the `slug` field `bookstore`:

	final DocumentTypesApi documentTypesApi = new DocumentTypesApi("bookstore");
	final DocumentType[] documentTypes = documentTypesApi.getAll();

Create a new `DocumentType` for an `Engine` with the name `books`:

	final DocumentTypesApi documentTypesApi = new DocumentTypesApi("bookstore");
	final DocumentType documentType = documentTypesApi.create("books");

Retrieve an `DocumentType` by `slug` or `id`:

	final DocumentTypesApi documentTypesApi = new DocumentTypesApi("bookstore");
	final DocumentType documentType = documentTypesApi.get("books")

Delete a `DocumentType` using the `slug` or `id` of it:

	final DocumentTypesApi documentTypesApi = new DocumentTypesApi("bookstore");
	documentTypesApi.destroy("books");

# Documents

Retrieve `Document`s of `Engine` `bookstore` and `DocumentType` `books`:

	final DocumentsApi documentsApi = new DocumentsApi("bookstore", "books");
	final Document[] documents = documentsApi.getAll();

Get the second batch of `Document`s with batch size 10:

	final DocumentsApi documentsApi = new DocumentsApi("bookstore", "books");
	final Document[] documents = documentsApi.getAll(2, 10);

Retrieve a specific `Document` using its `id` or `external_id`:

	final DocumentsApi documentsApi = new DocumentsApi("bookstore", "books");
	final Document document = documentsApi.get("id1");

Create a new `Document` with mandatory `external_id` and user-defined fields:

	final DocumentsApi documentsApi = new DocumentsApi("bookstore", "books");
	final JSONObject jsonDocument = new JSONObject("{\"external_id\": \"external_id1\"," +
	    " \"fields\": [{\"name\": \"title\", \"value\": \"Lucene in Action\", \"type\": \"string\"}]}");
	documentsApi.create(jsonDocument);

Create multiple `Document`s at once and return status for each `Document` creation:

	final DocumentsApi documentsApi = new DocumentsApi("bookstore", "books");
	final JSONObject jsonDocument1 = new JSONObject("{\"external_id\": \"external_id1\"," +
	    " \"fields\": [{\"name\": \"title\", \"value\": \"Lucene in Action\", \"type\": \"string\"}]}");
	final JSONObject jsonDocument2 = new JSONObject("{\"external_id\": \"external_id2\"," +
	    " \"fields\": [{\"name\": \"title\", \"value\": \"MongoDB in Action\", \"type\": \"string\"}]}");
	final boolean[] stati = documentsApi.create(new JSONObject[] {jsonDocument1, jsonDocument2});

Update fields of an existing `Document` specified by `id` or `external_id`:

	final DocumentsApi documentsApi = new DocumentsApi("bookstore", "books");
	final JSONObject fields = new JSONObject("{\"in_stock\": false, \"on_sale\": false}");
	final Document document = documentsApi.update("1", fields);

Update multiple `Document`s at once:

	final DocumentsApi documentsApi = new DocumentsApi("bookstore", "books");
	final JSONObject jsonDocument1 = new JSONObject("{\"external_id\": \"2\", \"fields\": {\"in_stock\": false}}");
	final JSONObject jsonDocument2 = new JSONObject("{\"external_id\": \"3\", \"fields\": {\"in_stock\": true}}");
	final boolean[] stati = documentsApi.update(new JSONObject[] {jsonDocument1, jsonDocument2});

Create or update a `Document`:

	final DocumentsApi documentsApi = new DocumentsApi("bookstore", "books");
	final JSONObject jsonDocument = new JSONObject("{\"external_id\": \"1\"," +
	    " \"fields\": [" +
		" {\"name\": \"title\", \"value\": \Information Retrieval\", \"type\": \"string\"}, " +
		" {\"name\": \"author\", \"value\": \Stefan Buttcher\", \"type\": \"string\"}]}");
	final Document document = documentsApi.createOrUpdate(jsonDocument);

Create or update multiple `Documents` at once:

	final DocumentsApi documentsApi = new DocumentsApi("bookstore", "books");
	final JSONObject jsonDocument1 = new JSONObject("{\"external_id\": \"2\"," +
	    " \"fields\": [" +
		" {\"name\": \"title\", \"value\": \Lucene in Action\", \"type\": \"string\"}, " +
		" {\"name\": \"author\", \"value\": \Michael McCandless\", \"type\": \"string\"}]}");
	final JSONObject jsonDocument2 = new JSONObject("{\"external_id\": \"3\"," +
	    " \"fields\": [" +
		" {\"name\": \"title\", \"value\": \MongoDB in Action\", \"type\": \"string\"}, " +
		" {\"name\": \"author\", \"value\": \Kyle Banker\", \"type\": \"string\"}]}");
	final boolean[] stati = documentsApi.createOrUpdate(new JSONObject[] {jsonDocument1, jsonDocument2});

Destroy a `Document`:

	final DocumentsApi documentsApi = new DocumentsApi("bookstore", "books");
	documentsApi.destroy("1");

Destroy multiple `Document`s at once:

	final DocumentsApi documentsApi = new DocumentsApi("bookstore", "books");
	final boolean[] stati = documentsApi.destroy("1", "2", "3");

# Domains

Retrieve all `Domain`s of `Engine` `websites`:

	final DomainsApi domainsApi = new DomainsApi("websites");
	final Domain[] domains = domainsApi.getAll();

Retrieve a specific `Domain` by `id`:

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

# Analytics

To get the amount of searches on your `Engine` in the last 14 days use:

	final AnalyticsApi analyticsApi = new AnalyticsApi("bookstore");
	final List<DateCount> searches = analyticsApi.searches();

You can also use a specific start and/or end date:

	final AnalyticsApi analyticsApi = new AnalyticsApi("bookstore");
	final Date from = ...
	final Date to = ...
	final List<DateCount> searches = analyticsApi.searches(from, to);

To get the amount of autoselects (clicks on autocomplete results) use:

	final AnalyticsApi analyticsApi = new AnalyticsApi("bookstore");
	final List<DateCount> autoselects = analyticsApi.autoselects();

As with searches you can also limit by start and/or end date:

	final AnalyticsApi analyticsApi = new AnalyticsApi("bookstore");
	final Date from = ...
	final Date to = ...
	final List<DateCount> autoselects = analyticsApi.autoselects(from, to);

If you are interested in the top queries for your `Engine` you can use:

	final AnalyticsApi analyticsApi = new AnalyticsApi("bookstore");
	final List<QueryCount> topQueries = analyticsApi.topQueries();

To see more top queries you can paginate through them using:

	final AnalyticsApi analyticsApi = new AnalyticsApi("bookstore");
	final List<QueryCount> topQueries = analyticsApi.topQueries(2, 10);
