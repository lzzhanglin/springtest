package com.mmall.service;import com.mmall.common.ServerResponse;import java.util.Map;public interface IOrderService {    ServerResponse pay(Long orderNO, Integer userId, String path);    ServerResponse alipayCallback(Map<String, String> params);    ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);    ServerResponse createOrder(Integer userId, Integer shippingId);}