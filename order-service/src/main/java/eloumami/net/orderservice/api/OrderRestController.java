package eloumami.net.orderservice.api;


import eloumami.net.orderservice.entities.Order;
import eloumami.net.orderservice.feign.InventoryFeignClient;
import eloumami.net.orderservice.repository.OrderRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderRestController {
    private OrderRepository orderRepository;
    private InventoryFeignClient inventoryFeignClient;

    public OrderRestController(OrderRepository orderRepository, InventoryFeignClient inventoryFeignClient) {
        this.orderRepository = orderRepository;
        this.inventoryFeignClient = inventoryFeignClient;
    }

    @GetMapping("/orders")
    public List<Order> findAllOrders(){
        List<Order> allOrders = orderRepository.findAll();
        allOrders.forEach(o->{
            o.getProductItems().forEach(pi->{
                pi.setProduct(inventoryFeignClient.findProductById(pi.getProductId()));
            });
        });
        return allOrders;
    }
    @GetMapping("/orders/{id}")
    public Order findOrderById(@PathVariable String id){
        Order order = orderRepository.findById(id).get();
        order.getProductItems().forEach(pi->{
            pi.setProduct(inventoryFeignClient.findProductById(pi.getProductId()));
        });
        return order;
    }
}
