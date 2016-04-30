package com.examples.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.examples.model.Order;
import com.examples.model.Product;

public class ConvertToMap implements Processor {
	
	final String ORDERID="orderid";
	final String CUSTOMERNAME="customername";
	final String PRODUCTNAME="productname";
	final String SKUID="skuid";
	final String PRICE="price";
	final String PRODUCTS="products";
	
	@Override
	public void process(Exchange arg0) throws Exception {
		Order body = arg0.getIn().getBody(Order.class);
		Map<String, Object> obj = new HashMap<String, Object>(3);
		obj.put(ORDERID, body.getOrderid());
		obj.put("customername", body.getCustomername());
		List<Map<String, Object>> productsList = new ArrayList<Map<String, Object>>();
		Map<String, Object> product = null;
		List<Product> products = body.getProducts().getProduct();
		//System.out.println("Size of products for order id "+ body.getOrderid()+ " is "+products.size());
		for (Product prod : products) {
			product = new HashMap<String, Object>(4);
			product.put(PRODUCTNAME, prod.getProductname());		
			product.put(SKUID, prod.getSkuid());
			product.put(PRICE, prod.getPrice());
			product.put(ORDERID, body.getOrderid());
			productsList.add(product);
		}
		obj.put(PRODUCTS, productsList);
		//obj.put("strVal", body.getOrderid()+body.getCustomername()+productsStr);
		arg0.getOut().setBody(obj);
	}

}
