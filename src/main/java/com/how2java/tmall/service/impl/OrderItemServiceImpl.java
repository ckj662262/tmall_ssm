package com.how2java.tmall.service.impl;

import com.how2java.tmall.mapper.OrderItemMapper;
import com.how2java.tmall.pojo.Order;
import com.how2java.tmall.pojo.OrderItem;
import com.how2java.tmall.pojo.OrderItemExample;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.service.OrderItemService;
import com.how2java.tmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService{
    @Autowired
    OrderItemMapper orderItemMapper;
    @Autowired
    ProductService productService;

    @Override
    public void add(OrderItem oi){
        orderItemMapper.insert(oi);
    }
    @Override
    public void delete(int id){
        orderItemMapper.deleteByPrimaryKey(id);
    }
    @Override
    public void update(OrderItem oi){
        orderItemMapper.updateByPrimaryKeySelective(oi);
    }
    @Override
    public OrderItem get(int id){
        OrderItem oi =  orderItemMapper.selectByPrimaryKey(id);
        setProduct(oi);
        return oi;
    }
    @Override
    public List<OrderItem> list(){
        OrderItemExample example = new OrderItemExample();
        example.setOrderByClause("int desc");
        List<OrderItem> ois = orderItemMapper.selectByExample(example);
//        setProduct(ois);
        return ois;
    }
    @Override
    public void fill(Order o){
        OrderItemExample example = new OrderItemExample();
        example.createCriteria().andOidEqualTo(o.getId());
        example.setOrderByClause("id desc");
        List<OrderItem> ois = orderItemMapper.selectByExample(example);
        setProduct(ois);

        int totalNumber = 0;
        float total = 0;
        for(OrderItem oi:ois){
            total += oi.getNumber() * oi.getProduct().getPromotePrice();
            totalNumber += oi.getNumber();
        }
        o.setTotalNumber(totalNumber);
        o.setTotal(total);
        o.setOrderItems(ois);
    }
    @Override
    public void fill(List<Order> os){
        for(Order o:os)
            fill(o);
    }


    public void setProduct(OrderItem oi){
        Product p = productService.get(oi.getPid());
        productService.setFirstProductImage(p);
        oi.setProduct(p);
    }
    public void setProduct(List<OrderItem> ois){
        for(OrderItem oi:ois)
            setProduct(oi);
    }
    @Override
    public int getSaleCount(int pid) {
        OrderItemExample example =new OrderItemExample();
        example.createCriteria().andPidEqualTo(pid);
        List<OrderItem> ois =orderItemMapper.selectByExample(example);
        int result =0;
        for (OrderItem oi : ois) {
            result+=oi.getNumber();
        }
        return result;
    }
    @Override
    public List<OrderItem> listByUser(int uid) {
        OrderItemExample example =new OrderItemExample();
        example.createCriteria().andUidEqualTo(uid).andOidIsNull();
        List<OrderItem> result =orderItemMapper.selectByExample(example);
        setProduct(result);
        return result;
    }
}
