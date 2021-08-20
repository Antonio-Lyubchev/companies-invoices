package com.estafet.companies.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonInclude(value=Include.NON_NULL, content = Include.NON_NULL)
public class InvoiceRequest {
	 
	private LocalDateTime dateIssued;
	private LocalDateTime dateDue;
	private Company company;
	private List<InvoiceProduct> products;
	
	
	public LocalDateTime getDateIssued() {
		return dateIssued;
	}
	
	public void setDateIssued(LocalDateTime dateIssued) {
		this.dateIssued = dateIssued;
	}
	
	public LocalDateTime getDateDue() {
		return dateDue;
	}
	
	public void setDateDue(LocalDateTime dateDue) {
		this.dateDue = dateDue;
	}
	
	public Company getCompany() {
		return company;
	}
	
	public void setCompany(Company company) {
		this.company = company;
	}
	
	public List<InvoiceProduct> getProducts() {
		return products;
	}
	
	public void setProducts(List<InvoiceProduct> products) {
		this.products = products;
	}
	
	
}
