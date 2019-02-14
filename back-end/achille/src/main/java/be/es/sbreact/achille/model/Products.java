package be.es.sbreact.achille.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Products {
	
	
	private String id;
    private String title;
    private String description;
    private String manufacturer;
    private float price;
    
    //@CompletionField(maxInputLength = 100)
	//private Completion suggest;
    
    
	public Products(String id, String title, String description,String manufacturer, float price) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.price = price;
		this.manufacturer= manufacturer;
	}
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
	
	
	
	}

