package com.pcshop.tests;

import com.pcshop.dto.ProductDTO;
import com.pcshop.entities.Category;
import com.pcshop.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct() {
        Product product = new Product(
                1L, "Phone", "good Phone", 800.0,
                "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/14-big.jpg",
                Instant.parse("2020-10-20T03:00:00Z")
        );
        product.getCategories().add(new Category(2L, "Electronics"));
        return product;
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }

}
