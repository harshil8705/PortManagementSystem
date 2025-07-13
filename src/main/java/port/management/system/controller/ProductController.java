package port.management.system.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import port.management.system.configuration.AppConstants;
import port.management.system.dto.*;
import port.management.system.service.ProductServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    //    Refactor Method.
    @PostMapping("/public/products/source-port-id/{sourcePortId}/destination-port-id/{destinationPortId}")
    public ResponseEntity<ProductResponse2> addNewProductsBySourceAndDestinationPortId(
            @PathVariable Long sourcePortId,
            @PathVariable Long destinationPortId,
            @Valid @RequestBody List<ProductDTO2> productDTO2List
    ) {

        return new ResponseEntity<>(productService.addNewProductsBySourceAndDestinationPortId(sourcePortId, destinationPortId, productDTO2List), HttpStatus.OK);

    }

    @PutMapping("/public/products/product-id/{productId}")
    public ResponseEntity<ProductResponse3> updateProductById(@PathVariable Long productId,
                                                              @RequestBody ProductDTO2 productDTO2) {

        return new ResponseEntity<>(productService.updateProductById(productId, productDTO2), HttpStatus.OK);

    }

    @GetMapping("/admin/products/current-product-port-id/{currentProductPortId}")
    public ResponseEntity<ProductResponse> getAllProductsByCurrentProductPortId(
            @PathVariable Long currentProductPortId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder
    ) {

        return new ResponseEntity<>(productService.getAllProductsByCurrentProductPortId(currentProductPortId, pageNumber, pageSize, sortBy, sortOrder), HttpStatus.FOUND);

    }

    @GetMapping("/admin/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder
    ) {

        return new ResponseEntity<>(productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder), HttpStatus.FOUND);

    }

    @GetMapping("/admin/products/product-name/{productName}")
    public ResponseEntity<ProductResponse> getAllProductsByName(
            @PathVariable String productName,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder
    ) {

        return new ResponseEntity<>(productService.getAllProductsByName(productName, pageNumber, pageSize, sortBy, sortOrder), HttpStatus.FOUND);

    }

    @GetMapping("/public/products/product-id/{productId}")
    public ResponseEntity<ProductResponse3> getProductById(@PathVariable Long productId) {

        return new ResponseEntity<>(productService.getProductById(productId), HttpStatus.FOUND);

    }

    @DeleteMapping("/admin/products/product-id/{productId}")
    public ResponseEntity<String> removeProductById(@PathVariable Long productId) {

        return new ResponseEntity<>(productService.removeProductById(productId), HttpStatus.OK);

    }

    @PutMapping("/admin/products/product-id/{productId}/container-id/{containerId}")
    public ResponseEntity<ProductResponse3> addProductToContainer(@PathVariable Long productId,
                                                                  @PathVariable Long containerId) {

        return new ResponseEntity<>(productService.addProductToContainer(productId, containerId), HttpStatus.OK);

    }

    @GetMapping("/admin/products/container-id/{containerId}")
    public ResponseEntity<ProductResponse> getAllProductsByContainer(
            @PathVariable Long containerId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION, required = false) String sortOrder
    ) {

        return new ResponseEntity<>(productService.getAllProductsByContainer(containerId, pageNumber, pageSize, sortBy, sortOrder), HttpStatus.FOUND);

    }

    @PutMapping("/admin/products/container/product-id/{productId}")
    public ResponseEntity<ProductResponse3> unassignProductFromContainer(@PathVariable Long productId) {

        return new ResponseEntity<>(productService.unassignProductFromContainer(productId), HttpStatus.OK);

    }

    @GetMapping("/public/track/product/status/product-id/{productId}")
    public ResponseEntity<TrackProductResponse> trackProductStatusByProductId(@PathVariable Long productId) {

        return new ResponseEntity<>(productService.trackProductStatusByProductId(productId), HttpStatus.OK);

    }

}
