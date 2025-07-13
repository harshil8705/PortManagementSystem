package port.management.system.chatbot.utility;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import port.management.system.service.OrderServiceImpl;
import port.management.system.service.ProductServiceImpl;

@Component
public class ProductTool {

    private final OrderServiceImpl orderService;
    private final ProductServiceImpl productService;

    public ProductTool(OrderServiceImpl orderService, ProductServiceImpl productService) {

        this.orderService = orderService;
        this.productService = productService;

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

}
