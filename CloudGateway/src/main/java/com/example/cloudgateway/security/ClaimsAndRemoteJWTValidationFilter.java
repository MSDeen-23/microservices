//package com.example.cloudgateway.security;
//
//import com.okta.jwt.AccessTokenVerifier;
//import com.okta.jwt.Jwt;
//import com.okta.jwt.JwtVerificationException;
//import com.okta.jwt.JwtVerifiers;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
//@Component
//public class ClaimsAndRemoteJWTValidationFilter implements GlobalFilter {
//
////    @Value("${okta.oauth2.issuer}")
////    private String issuerUrl;
////    @Value("${okta.oauth2.client-group}")
////    private String adminGroup;
////
////    @Override
////    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
////
////        ServerHttpRequest request = exchange.getRequest();
////        ServerHttpResponse response = exchange.getResponse();
////        if(!request.getMethod().toString().equals("GET")){
////            String bearerToken = request.getHeaders().getFirst("Authorization").substring(7);
////            AccessTokenVerifier jwtVerifier = JwtVerifiers
////                    .accessTokenVerifierBuilder()
////                    .setIssuer(issuerUrl)
////                    .build();
////            try {
////                Jwt jwt = jwtVerifier.decode(bearerToken);
////                List<String> groups = (List<String>) jwt.getClaims().get("groups");
////                if(groups.contains(adminGroup))
////                    return chain.filter(exchange);
////                else {
////                    response.setStatusCode(HttpStatus.FORBIDDEN);
////                    byte[] bytes = ("User with bearer token not a super admin").getBytes(StandardCharsets.UTF_8);
////                    DataBuffer buffer = response.bufferFactory().wrap(bytes);
////                    return response.writeWith(Mono.just(buffer));
////                }
////            } catch (JwtVerificationException e) {
////                e.printStackTrace();
////                response.setStatusCode(HttpStatus.UNAUTHORIZED);
////                byte[] bytes = ("Failed to parse bearer token, check value").getBytes(StandardCharsets.UTF_8);
////                DataBuffer buffer = response.bufferFactory().wrap(bytes);
////                return response.writeWith(Mono.just(buffer));
////            }
////        }
////
////        return chain.filter(exchange);
////
////    }
//}
