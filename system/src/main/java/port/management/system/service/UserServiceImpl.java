package port.management.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import port.management.system.dto.*;
import port.management.system.model.Product;
import port.management.system.model.User;
import port.management.system.exception.ApiException;
import port.management.system.repository.PortRepository;
import port.management.system.repository.ProductRepository;
import port.management.system.repository.UserRepository;
import port.management.system.security.utility.UserUtil;
import port.management.system.utility.BuildProduct;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PortRepository portRepository;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private BuildProduct buildProduct;

    @Override
    public List<ProductResponse3> getAllProductsByUser() {

        User loggedInUser = userUtil.getCurrentUser().getBody();

        if (loggedInUser == null) {

            throw new ApiException("Unable to fetch the User data.");

        }

        List<Product> products = loggedInUser.getProducts();

        if (products.isEmpty()) {

            throw new ApiException("No Products assigned to the User with userId : " + loggedInUser.getUserId() + " till now.");

        }

        return products.stream()
                .map(p -> buildProduct.buildProductResponse3(p))
                .toList();

    }

}
