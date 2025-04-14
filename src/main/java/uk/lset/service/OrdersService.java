package uk.lset.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import uk.lset.dto.ProductInventoryDTO;
import uk.lset.dto.UpdateOrderStatus;
import uk.lset.entities.Inventory;
import uk.lset.entities.OrderStatus;
import uk.lset.entities.Orders;
import uk.lset.entities.Products;
import uk.lset.entities.OrderStatus;
import uk.lset.exception.InsufficientStockException;
import uk.lset.exception.ItemNotFoundException;
import uk.lset.exception.QuantityBadRequestException;
import uk.lset.repository.InventoryRepository;
import uk.lset.repository.OrdersRepository;
import uk.lset.repository.ProductRepository;
import uk.lset.request.OrderRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrdersService {


    private final OrdersRepository ordersRepository;

    @Autowired
    public OrdersService(OrdersRepository ordersRepository, ProductService productService, ProductRepository productRepository,
                         InventoryRepository inventoryRepository, InventoryService inventoryService) {
        this.ordersRepository = ordersRepository;
        this.productService = productService;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.inventoryService = inventoryService;
    }

    private final ProductService productService;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryService inventoryService;


    public Orders addNewOrder(@RequestBody OrderRequest orderRequest, String productId, int quantity) {
        if(!inventoryRepository.existsByProductId(productId)) {
            throw new ItemNotFoundException("Product with id " + productId + " does not exist." );
        } else if (quantity <= 1) {
            throw new QuantityBadRequestException("Quantity must be greater than zero.");
        }
        Products orderProducts = productService.getProductById(productId);
        ProductInventoryDTO productInventory = inventoryService.getProductStock(productId);

        if(productInventory.getQuantityAvailable() < quantity) {
            throw new InsufficientStockException("Not enough stock available." + productInventory.getQuantityAvailable() + " items left.");
        }

        Orders order = new Orders();
        order.setProductId(orderProducts.getProductId());
        order.setProductName(orderProducts.getProductName());
        order.setProductPrice(orderProducts.getPrice());
        order.setQuantity(quantity);
        order.setOrderValue(quantity * orderProducts.getPrice());
        order.setCoder(generateCoder());
        order.setClientName(orderRequest.getClientName());
        order.setEmail(orderRequest.getEmail());
        order.setDeliveryAddress(orderRequest.getDeliveryAddress());
        order.setStatus(OrderStatus.ACCEPTED);
        order.setOrderDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));


        return ordersRepository.save(order);

    }

    private String generateCoder(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        return "O-" + timeStamp + "-" + UUID.randomUUID().toString().substring(0, 10);
    }


    @Transactional(readOnly = true)
    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Orders getOrderById(String id){
        return ordersRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Order with id " + id + " does not exists."));
    }

    @Transactional
    public Orders updateOrderStatus( UpdateOrderStatus updateOrderStatus, String orderId) {
        Orders order = ordersRepository.findById(orderId).orElseThrow(()->new ItemNotFoundException("Order with id " + orderId + " does not exists."));
        order.setStatus(updateOrderStatus.getStatus());
        if(updateOrderStatus.getStatus().toString().equals("SHIPPED")) {
            Inventory inventory = inventoryRepository.findByProductId(order.getProductId()).orElseThrow(()->new ItemNotFoundException("Product with id " + order.getProductId() + " does not exist."));
            inventory.setQuantityAvailable(inventory.getQuantityAvailable() - order.getQuantity());
            inventoryRepository.save(inventory);
        }
        return ordersRepository.save(order);
    }
}
