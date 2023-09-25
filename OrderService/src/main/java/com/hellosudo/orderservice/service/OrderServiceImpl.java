package com.hellosudo.orderservice.service;

import com.hellosudo.orderservice.entity.Order;
import com.hellosudo.orderservice.exception.CustomException;
import com.hellosudo.orderservice.external.client.PaymentService;
import com.hellosudo.orderservice.external.client.ProductService;
import com.hellosudo.orderservice.external.request.PaymentRequest;
import com.hellosudo.orderservice.external.response.PaymentResponse;
import com.hellosudo.orderservice.model.OrderRequest;
import com.hellosudo.orderservice.external.response.OrderResponse;
import com.hellosudo.orderservice.model.ProductResponse;
import com.hellosudo.orderservice.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${microservices.product}")
    private String productServiceUrl;

    @Value("${microservices.payment}")
    private String paymentServiceUrl;

    @Override
    public long placeOrder(OrderRequest orderRequest) {

       log.info("Placing order Request : {} ",orderRequest);
       productService.reduceQuantity(orderRequest.getProductId(),orderRequest.getQuantity());
       log.info("Creating order with status CREATED");

       Order order = Order.builder()
               .amount(orderRequest.getTotalAmount())
               .orderStatus("CREATED")
               .productId(orderRequest.getProductId())
               .orderDate(Instant.now())
               .quantity(orderRequest.getQuantity())
               .build();

       order = orderRepository.save(order);

       log.info("Calling payment service to complete the payment");

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())
                .build();
        String orderStatus = null;
        try{
            paymentService.doPayment(paymentRequest);
            log.info("Payment done successfully");
            orderStatus = "PLACED";
        }
        catch (Exception e){
            log.error("Error occured in payment changing status");
            orderStatus = "PAYMENT_FAILED";
        }
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
       log.info("Order placed successfully with order id : {} ", order.getId());
       return order.getId();


    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Get order details for Order Id : {}",orderId );
        Order order
                = orderRepository.findById(orderId)
                .orElseThrow(()-> new CustomException("Order not found for the order id :"+orderId,"NOT_FOUND",404));
        log.info("Invoking product service to fetch the product details from the product service : {} ",order.getProductId());
        ProductResponse productResponse =
                restTemplate.getForObject(
                        productServiceUrl+order.getProductId(),
                        ProductResponse.class
                );

        log.info("Getting payment information from payment service for order id : {} ", orderId);
        PaymentResponse paymentResponse
                = restTemplate.getForObject(
                        paymentServiceUrl+"order/"+order.getId(),
                PaymentResponse.class);
        OrderResponse.PaymentDetails paymentDetails
                = OrderResponse.PaymentDetails
                .builder()
                .paymentId(paymentResponse.getPaymentId())
                .paymentStatus(paymentResponse.getStatus())
                .paymentDate(paymentResponse.getPaymentDate())
                .paymentMode(paymentResponse.getPaymentMode())
                .build();

        OrderResponse.ProductDetails productDetails
                = OrderResponse.ProductDetails
                .builder()
                .productName(productResponse.getProductName())
                .productId(productResponse.getProductId())
                .build();

        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .amount(order.getAmount())
                .orderDate(order.getOrderDate())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();


        return orderResponse;

    }
}
