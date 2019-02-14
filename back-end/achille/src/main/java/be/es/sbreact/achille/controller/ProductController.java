package be.es.sbreact.achille.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import be.es.sbreact.achille.dao.MapValidationErrorService;
import be.es.sbreact.achille.dao.ProductDao;
import be.es.sbreact.achille.model.Products;

/**
 * @author Achille
 *
 */
@RestController
//@Controller
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductDao productDao;

	@Autowired
	private MapValidationErrorService validationErrorService;

	// @Autowired
	public ProductController(ProductDao productDao) {
		this.productDao = productDao;
	}

	@GetMapping(value = "/title/{title}", produces = "application/json; charset=utf-8")
	public List<Products> getTitle(@PathVariable String title, Model model) throws IOException {
		System.err.println("----------------------Title-----------------------");

		List<Products> productsFound = productDao.getProductByTitle(title);
		model.addAttribute("products", productsFound);

		return productsFound;
	}

	/** * find all *************************************************** * */

	@GetMapping(value = "/findAllThyme", produces = "application/json; charset=utf-8")
	public String getTitleThyme(Model model) throws Exception {

		List<Products> allproducts = productDao.findAll();
		System.err.println("----------------------findAllThyme-----------------------");
		model.addAttribute("products", allproducts);
		// ModelAndView mav = new ModelAndView("booksPage");

		return "booksPage";
	}

	/**
	 * find book by ID
	 *
	 */
	@GetMapping("/{id}")
	public Map<String, Object> getProductById(@PathVariable String id) {
		return productDao.getProductById(id);
	}

	/**
	 * Insert book(s)
	 *
	 */

	@PostMapping
	public ResponseEntity<?> insertProduct(@RequestBody Products product, BindingResult result) throws Exception {

		ResponseEntity<?> errorMap = validationErrorService.MapvalidationService(result);
		if(errorMap != null) return errorMap;
		
		Products product1 = productDao.insertProduct(product);
		return new ResponseEntity<Products>(product1, HttpStatus.CREATED);
	}

	/**
	 * update book
	 *
	 */

	@PutMapping("/{id}")
	public Map<String, Object> updateProductById(@RequestBody Products product, @PathVariable String id) {
		return productDao.updateProductById(id, product);
	}

	@DeleteMapping("/{id}")
	public void deleteProductkById(@PathVariable String id) {
		productDao.deleteProductById(id);
	}
}
