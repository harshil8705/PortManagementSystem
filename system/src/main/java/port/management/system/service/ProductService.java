package port.management.system.service;

import jakarta.validation.Valid;
import port.management.system.dto.*;

import java.util.List;

public interface ProductService {

    ProductResponse3 updateProductById(Long productId, ProductDTO2 productDTO2);

    ProductResponse getAllProductsByCurrentProductPortId(Long portId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse getAllProductsByName(String productName, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse3 getProductById(Long productId);

    String removeProductById(Long productId);

    ProductResponse3 addProductToContainer(Long productId, Long containerId);

    ProductResponse getAllProductsByContainer(Long containerId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse3 unassignProductFromContainer(Long productId);

    ProductResponse2 addNewProductsBySourceAndDestinationPortId(Long sourcePortId, Long destinationPortId, @Valid List<ProductDTO2> productDTO2List);

    TrackProductResponse trackProductStatusByProductId(Long productId);

}
