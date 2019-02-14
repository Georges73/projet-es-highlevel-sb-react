package be.es.sbreact.achille.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.stereotype.Repository;


import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import be.es.sbreact.achille.model.Products;
//import org.elasticsearch.client.RequestOptions;

@Repository
public class ProductDao {

	private final String INDEX = "amazon_products";
	private final String TYPE = "products";
	private RestHighLevelClient restHighLevelClient;
	private ObjectMapper objectMapper;

	public ProductDao(ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
		this.objectMapper = objectMapper;
		this.restHighLevelClient = restHighLevelClient;
	}

	public Products insertProduct(Products product) {
		product.setId(UUID.randomUUID().toString());
		Map dataMap = objectMapper.convertValue(product, Map.class);
		IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, product.getId()).source(dataMap);
		try {
			IndexResponse response = restHighLevelClient.index(indexRequest);
		} catch (ElasticsearchException e) {
			e.getDetailedMessage();
		} catch (java.io.IOException ex) {
			ex.getLocalizedMessage();
		}
		return product;
	}

	public Map<String, Object> getProductById(String id) {
		GetRequest getRequest = new GetRequest(INDEX, TYPE, id);
		GetResponse getResponse = null;
		try {
			getResponse = restHighLevelClient.get(getRequest);
		} catch (java.io.IOException e) {
			e.getLocalizedMessage();
		}
		Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
		return sourceAsMap;
	}

	String product = "hello";

	// ----------------------------------------------------------------------------------------------------------------------

	public List<Products> getProductByTitle(String title) throws IOException {

		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices(INDEX);
		searchRequest.types(TYPE);

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(QueryBuilders.prefixQuery("title", title));
		searchSourceBuilder.from(0);
		searchSourceBuilder.size(5);

		HighlightBuilder highlightBuilder = new HighlightBuilder();
		HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field("title");
		highlightTitle.highlighterType("unified");
		highlightBuilder.field(highlightTitle);
		searchSourceBuilder.highlighter(highlightBuilder);

		searchRequest.source(searchSourceBuilder);

		SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
		

		/*
		 * SearchHits hits = searchResponse.getHits();
		 * 
		 * SearchHit[] searchHits = hits.getHits(); for (SearchHit hit : searchHits) {
		 * String index = hit.getIndex(); String id = hit.getId(); float score =
		 * hit.getScore();
		 * 
		 * System.err.println(index); }
		 */

		return getSearchResult(searchResponse);

	}

	// *****************************************************************************************************

	private List<Products> getSearchResult(SearchResponse searchResponse) {

		SearchHit[] searchHit = searchResponse.getHits().getHits();

		List<Products> product = new ArrayList<>();

		for (SearchHit hit : searchHit) {
			System.err.println(hit.getScore());

			
			/*
			 * Map<String, HighlightField> highlightFields = hit.getHighlightFields();
			 * HighlightField highlight = highlightFields.get("title"); Text[] fragments =
			 * highlight.fragments(); String fragmentString = fragments[0].string();
			 * System.err.println(fragmentString);
			 */
			 

			product.add(objectMapper.convertValue(hit.getSourceAsMap(), Products.class));
		}
		return product;
	}

	// *****************************************************************************************************

	public List<Products> findAll() throws Exception {

		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchRequest.source(searchSourceBuilder);

		SearchResponse searchResponse = restHighLevelClient.search(searchRequest);

		return getSearchResult(searchResponse);

	}

	// *****************************************************************************************************
	public Map<String, Object> updateProductById(String id, Products product) {
		UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, id).fetchSource(true); // Fetch Object after its
																							// update
		Map<String, Object> error = new HashMap<>();
		error.put("Error", "Unable to update product");
		try {
			String productJson = objectMapper.writeValueAsString(product);
			updateRequest.doc(productJson, XContentType.JSON);
			UpdateResponse updateResponse = restHighLevelClient.update(updateRequest);
			Map<String, Object> sourceAsMap = updateResponse.getGetResult().sourceAsMap();
			return sourceAsMap;
		} catch (JsonMappingException e) {
			e.getMessage();
		} catch (java.io.IOException e) {
			e.getLocalizedMessage();
		}
		return error;
	}

	public void deleteProductById(String id) {
		DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, id);
		try {
			DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest);
		} catch (java.io.IOException e) {
			e.getLocalizedMessage();
		}
	}

}