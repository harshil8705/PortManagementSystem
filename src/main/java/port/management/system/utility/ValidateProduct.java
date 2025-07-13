package port.management.system.utility;

import org.springframework.stereotype.Component;
import port.management.system.model.Container;
import port.management.system.model.Product;
import port.management.system.exception.ApiException;

@Component
public class ValidateProduct {

    public boolean isProductValidBeforeAssignment(Product product, Container container) {

        if (product.getContainer() != null && product.getContainer().getContainerId().equals(container.getContainerId())) {

            throw new ApiException("The Product with productId : " + product.getProductId() + " is already assigned to the Container with containerId : " + container.getContainerId());

        }

        if (product.getContainer() != null) {

            throw new ApiException("The Product with productId : " + product.getProductId() + " is already assigned to the Container with containerId : " + product.getContainer().getContainerId());

        }

        if (!product.getCurrentProductPort().equals(container.getPort())){

            throw new ApiException("The Port for the Product with productId : " + product.getProductId() + " and the Container with containerId : " + container.getContainerId() + " is not same.");

        }

        if (container.getShip() != null) {

            throw new ApiException("The Container with containerId : " + container.getContainerId() + " is already assigned to the ship. Hence Product can't be added to this Container.");

        }

        if (product.isPaymentPending()) {

            throw new ApiException("Cannot assign the Product to the Container as the Payment for the Product is pending.");

        }

        if (product.getProductWeightInKg() >
                (container.getMaxWeightCapacity() -
                        container.getProducts().stream().mapToDouble(Product::getProductWeightInKg).sum())) {

            throw new ApiException("Maximum Container Weight Capacity Reached. Now Maximum of : " + (container.getMaxWeightCapacity() -
                    container.getProducts().stream().mapToDouble(Product::getProductWeightInKg).sum()) + "kg's can be assigned to the Container.");

        }

        return true;

    }

}
