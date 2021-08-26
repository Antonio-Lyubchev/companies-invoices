package com.estafet.companies.repository;

import com.estafet.companies.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceProductRepository extends JpaRepository<Product, Long>
{
}