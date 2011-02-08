package org.jboss.seam.jms.example.sample;

import java.io.Serializable;

public class Order implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String product;

	public Order(){}
	
	public Order(String product)
	{
		this.product = product;
	}
	
	public String getProduct() 
	{
		return product;
	}

	public void setProduct(String product)
	{
		this.product = product;
	}
}
