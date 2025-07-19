package port.management.system.chatbot.utility;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import port.management.system.service.*;

@Component
public class ProductTool {

    private final OrderServiceImpl orderService;
    private final ProductServiceImpl productService;
    private final UserServiceImpl userService;

    public ProductTool(OrderServiceImpl orderService, ProductServiceImpl productService, UserServiceImpl userService, PortServiceImpl portService, CountryServiceImpl countryService, ShipServiceImpl shipService, ContainerServiceImpl containerService) {

        this.orderService = orderService;
        this.productService = productService;
        this.userService = userService;

    }

    @Tool(name = "cancelOrder", description = "Cancel the order of the user. Requires userConfirmed=true to proceed cancellation. If user confirms the cancellation then proceed with {userConfirmed}=true")
    public String cancelOrder(Long productId, boolean userConfirmed) {

        if (productId == null) {

            return "Product ID is required.";

        }

        if (!userConfirmed) {

            return "Are you sure you want to cancel the order with the Product Id : " + productId + "? Please confirm by saying yes.";

        }

        return orderService.cancelOrder(productId);

    }

    @Tool(name = "trackOrder", description = "Track the order of the user based on the provided product ID by the user.")
    public String trackOrder(Long productId) {

        if (productId == null) {

            return "Product ID is required.";

        }

        return productService.trackProductStatusByProductId(productId).toString();

    }

    @Tool(name = "getProductsByUser", description = "Get the List of all the products available if User asks for fetching the list of all the products that are available.")
    public String getProductsByUser() {

        if (userService.getAllProductsByUser().isEmpty()) {

            return "No Products are currently available.";

        }

        return userService.getAllProductsByUser().toString();

    }

}
