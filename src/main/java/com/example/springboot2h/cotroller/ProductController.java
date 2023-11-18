package com.example.springboot2h.cotroller;

import com.example.springboot2h.models.Product;
import com.example.springboot2h.models.ResponseObject;
import com.example.springboot2h.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/Products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    //get detail product
    //Let's return an object with: data, message, status
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> finById(@PathVariable Long id) {
        Optional<Product> foundProduct = productRepository.findById(id); //Bao ngoài bằng Optional để đối tượng này có thể null.
        return foundProduct.isPresent() ?
        ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok","Query product successfully", foundProduct)
        ):
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "Cannot find product with id = " + id, "")
        );
    }

    //insert new product with POST method
    //postman: Raw, JSON
    @PostMapping("/insert")
    public ResponseEntity<ResponseObject> insertProduct(@RequestBody Product newProduct) {
        //2 products must not have the same name
        List<Product> foundProducts = productRepository.findByProductName(newProduct.getProductName().trim());
        if(foundProducts.size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("false", "Product nsmr already taken", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
          new ResponseObject("ok", "Insert product successfully", productRepository.save(newProduct))
        );
    }

    //update , upsert = update if found otherwise insert --> hàm update hoặc insert gọi là upsert
    //Nếu k tìm thấy product để update thìt a insert
    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateProduct(@RequestBody Product newProduct, @PathVariable Long id) {
        Product updateProduct = productRepository.findById(id) //product là cái ta tìm dc
                .map(product ->{  //nếu mà tìm thấy ta sẽ ánh xạ từng bản ghi đó, biểu thức lamda ánh xạ: product ->
                    product.setProductName(newProduct.getProductName());
                    product.setPrice(newProduct.getPrice());
                    product.setYear(newProduct.getYear());
                    product.setUrl(newProduct.getUrl());
                    return productRepository.save(product);
                }).orElseGet(() -> { //néu k tìm thấy product để update thì ta tạo mới
                    newProduct.setId(id);
                    return productRepository.save(newProduct);
                });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update product successfully", updateProduct)
        );
    }

    //Delete a product
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id) {
        boolean exists = productRepository.existsById(id);
        if(exists) {
            productRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Delete product successfully", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "Cannot find product to delete", "")
        );
    }
}
