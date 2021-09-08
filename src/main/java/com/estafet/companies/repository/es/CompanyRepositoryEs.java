package com.estafet.companies.repository.es;

import com.estafet.companies.model.es.EsCompany;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public interface CompanyRepositoryEs extends ElasticsearchRepository<EsCompany, Long>
{
    Page<EsCompany> findByName(String name, Pageable pageable);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"company.name\": \"?0\"}}]}}")
    Page<EsCompany> findByNameUsingCustomQuery(String name, Pageable pageable);
}