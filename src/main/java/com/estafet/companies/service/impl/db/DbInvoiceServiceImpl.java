package com.estafet.companies.service.impl.db;

import com.estafet.companies.configuration.ModelMapperConfiguration;
import com.estafet.companies.configuration.ObjectMapperConfiguration;
import com.estafet.companies.dto.InvoiceDto;
import com.estafet.companies.exception.EntityNotFoundException;
import com.estafet.companies.exception.InvalidInputException;
import com.estafet.companies.model.Invoice;
import com.estafet.companies.repository.InvoiceRepository;
import com.estafet.companies.service.InvoiceService;
import com.estafet.companies.utils.JSONParser;
import com.estafet.companies.utils.ModelMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Primary
@Service
public class DbInvoiceServiceImpl implements InvoiceService
{
    @Autowired
    private InvoiceRepository repository;

    @Override
    public Invoice getInvoice(long invoiceNumber) throws EntityNotFoundException
    {
        Optional<Invoice> invoice = repository.findById(invoiceNumber);
        if (invoice.isEmpty())
        {
            throw new EntityNotFoundException("Invoice with number '" + invoiceNumber + "' not found!");
        }

        return invoice.get();
    }

    @Override
    public long addInvoice(Invoice newInvoice)
    {
        return repository.save(newInvoice).getId();
    }

    @Override
    public void addInvoices(List<Invoice> invoices)
    {
        repository.saveAll(invoices);
    }

    @Override
    public void updateInvoice(long invoiceId, Invoice newInvoice) throws InvalidInputException
    {
        if (newInvoice == null)
        {
            throw new InvalidInputException("Invalid invoice!");
        }

        if (invoiceId != newInvoice.getId())
        {
            throw new InvalidInputException("You cannot change invoice ID of existing invoice!");
        }

        Optional<Invoice> oldInvoice = repository.findById(invoiceId);
        Invoice invoiceToUpdate;

        // copy fields
        invoiceToUpdate = oldInvoice.orElse(newInvoice);

        repository.save(invoiceToUpdate);
    }

    @Override
    public void deleteInvoice(long invoiceId) throws EntityNotFoundException
    {
        try
        {
            repository.deleteById(invoiceId);
        } catch (EmptyResultDataAccessException ex)
        {
            throw new EntityNotFoundException("Invoice with id '" + invoiceId + "' does not exist!");
        }
    }

    @Override
    public List<Invoice> getAllInvoicesAsList()
    {
        return repository.findAll();
    }

    @PostConstruct
    public void insertTestData() throws IOException
    {
        ClassPathResource invoicesJson = new ClassPathResource("/JSON/AddInvoices.json");
        byte[] invoicesByteArray = invoicesJson.getInputStream().readAllBytes();

        JSONParser parser = new JSONParser();
        parser.setObjectMapper(new ObjectMapperConfiguration().objectMapper());
        List<InvoiceDto> invoiceDtos = parser.fromJsonToList(invoicesByteArray, InvoiceDto.class);

        ModelMapperUtils modelMapperUtils = new ModelMapperUtils();
        modelMapperUtils.setModelMapper(new ModelMapperConfiguration().modelMapper());

        List<Invoice> invoiceList = modelMapperUtils.mapList(invoiceDtos, Invoice.class);

        repository.saveAll(invoiceList);
    }
}
