package com.mmall.service;import com.github.pagehelper.PageInfo;import com.mmall.common.ServerResponse;import com.mmall.pojo.Shipping;public interface IShippingService {    ServerResponse add(Integer userID, Shipping shipping);    ServerResponse<String> delete(Integer userId, Integer shippingId);    ServerResponse update(Integer userID, Shipping shipping);    ServerResponse<Shipping> select(Integer userID, Integer shippingId);    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);}