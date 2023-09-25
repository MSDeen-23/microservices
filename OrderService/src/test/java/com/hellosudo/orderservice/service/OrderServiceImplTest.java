package com.hellosudo.orderservice.service;

import com.hellosudo.orderservice.entity.Order;
import com.hellosudo.orderservice.exception.CustomException;
import com.hellosudo.orderservice.external.client.PaymentService;
import com.hellosudo.orderservice.external.client.ProductService;
import com.hellosudo.orderservice.external.request.PaymentRequest;
import com.hellosudo.orderservice.external.response.OrderResponse;
import com.hellosudo.orderservice.external.response.PaymentResponse;
import com.hellosudo.orderservice.model.OrderRequest;
import com.hellosudo.orderservice.model.PaymentMode;
import com.hellosudo.orderservice.model.ProductResponse;
import com.hellosudo.orderservice.repository.OrderRepository;
import org.checkerframework.common.value.qual.BottomVal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperties;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@SpringBootTest
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    OrderService orderService = new OrderServiceImpl();

    @Value("${microservices.product}")
    private String productServiceUrl;

    @Value("${microservices.product}")
    private String paymentServiceUrl;

    @BeforeEach
    public void setup(){
        ReflectionTestUtils.setField(orderService,"productServiceUrl",productServiceUrl);
        ReflectionTestUtils.setField(orderService,"paymentServiceUrl",paymentServiceUrl);
    }

    @DisplayName("Get order - Success scenario")
    @Test
    void test_When_Order_Success(){
        // Mocking
        // order Mock
        Order order = getMockOrder();
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        // product response mock
        ProductResponse productResponse = getMockProductResponse();
        when(restTemplate.getForObject(productServiceUrl+order.getProductId(), ProductResponse.class))
                .thenReturn(productResponse);

        // payment response mock
        PaymentResponse paymentResponse = getMockPaymentResponse();
        when(restTemplate.getForObject(paymentServiceUrl+"order/"+order.getId(), PaymentResponse.class))
                .thenReturn(paymentResponse);

        // Actual
        OrderResponse orderResponse = orderService.getOrderDetails(1);

        // Verification
        verify(orderRepository,times(1)).findById(anyLong());
        verify(restTemplate, times(1)).getForObject(productServiceUrl+order.getProductId(), ProductResponse.class);
        verify(restTemplate, times(1)).getForObject(paymentServiceUrl+"order/"+order.getId(), PaymentResponse.class);

        // Assert
        assertNotNull(orderResponse);
        assertEquals(order.getId(), orderResponse.getOrderId());

    }

    @DisplayName("Get orders- failure scenario")
    @Test
    void test_When_Get_Order_NOT_FOUND_then_Not_Foung(){
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(null));

        CustomException exception = Assertions.assertThrows(CustomException.class,()-> orderService.getOrderDetails(1));
        assertEquals("NOT_FOUND",exception.getErrorCode());
        assertEquals(404,exception.getStatus());
        verify(orderRepository,times(1))
                .findById(anyLong());
    }


    @DisplayName("Place order -  success scenario")
    @Test
    void test_When_Place_Order_Success(){
        Order order =  getMockOrder();
        OrderRequest orderRequest = getMockOrderRequest();
        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);

        when(productService.reduceQuantity(anyLong(),anyLong()))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));

        when(paymentService.doPayment(any(PaymentRequest.class)))
                .thenReturn(new ResponseEntity<Long>(1L,HttpStatus.OK));
        long orderId = orderService.placeOrder(orderRequest);

        // verification
        verify(orderRepository,times(2))
                .save(any());
        verify(productService,times(1)).reduceQuantity(anyLong(),anyLong());
        verify(paymentService, times(1)).doPayment(any(PaymentRequest.class));
        assertEquals(order.getId(),orderId);
    }

    @Test
    @DisplayName("Placed order -  payment failed scenario")
    void test_when_place_order_payment_fails_then_order_placed(){
        Order order =  getMockOrder();
        OrderRequest orderRequest = getMockOrderRequest();
        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);

        when(productService.reduceQuantity(anyLong(),anyLong()))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));

        when(paymentService.doPayment(any(PaymentRequest.class)))
                .thenThrow(new RuntimeException());

        long orderId = orderService.placeOrder(orderRequest);

        verify(orderRepository,times(2))
                .save(any());
        verify(productService,times(1)).reduceQuantity(anyLong(),anyLong());
        verify(paymentService, times(1)).doPayment(any(PaymentRequest.class));
        assertEquals(order.getId(),orderId);
    }

    private OrderRequest getMockOrderRequest() {
        return  OrderRequest.builder()
                .productId(1)
                .quantity(10)
                .paymentMode(PaymentMode.CASH)
                .totalAmount(100)
                .build();
    }


    private PaymentResponse getMockPaymentResponse() {
        return PaymentResponse.builder()
                .paymentId(1)
                .paymentDate(Instant.now())
                .paymentMode(PaymentMode.CASH)
                .amount(200)
                .orderId(1)
                .status("ACCEPTED")
                .build();
    }

    private ProductResponse getMockProductResponse() {
        return ProductResponse.builder()
                .productId(2)
                .productName("iphone")
                .price(100)
                .quantity(200)
                .build();
    }

    private Order getMockOrder() {
        return Order.builder()
                .orderStatus("PLACED")
                .orderDate(Instant.now())
                .id(1)
                .amount(100)
                .quantity(200)
                .productId(2)
                .build();
    }


}