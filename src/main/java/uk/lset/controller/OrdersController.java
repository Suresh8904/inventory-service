package uk.lset.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.lset.dto.UpdateOrderStatus;
import uk.lset.entities.Orders;
import uk.lset.request.OrderRequest;
import uk.lset.service.OrdersService;

import java.util.List;

@RestController
@RequestMapping()
public class OrdersController {


    private final OrdersService ordersService;

    @Autowired
    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PostMapping("/orders/addNewOrder/{productId}")
    public ResponseEntity<Orders> addNewOrder(@RequestBody OrderRequest orderRequest, @PathVariable String productId, @RequestParam int quantity) {
        Orders newOrder = ordersService.addNewOrder(orderRequest, productId, quantity);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    @GetMapping("/orders/allOrders")
    public ResponseEntity<List<Orders>> getAllOrders() {
        List<Orders> orders = ordersService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<Orders> getOrderById(@PathVariable String orderId) {
        Orders orders = ordersService.getOrderById(orderId);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("orders/updateStatusOrder/{orderId}")
    public ResponseEntity<Orders> updateStatusOrder(@PathVariable String orderId, @RequestBody UpdateOrderStatus updateOrderStatus) {
            Orders updatedStatusOrder = ordersService.updateOrderStatus(updateOrderStatus,orderId);
            return ResponseEntity.ok(updatedStatusOrder);

    }

}

