package com.estafet.companies.controller;

import com.estafet.companies.exception.EntityNotFoundException;
import com.estafet.companies.exception.InvalidInputException;
import com.estafet.companies.model.Invoice;
import com.estafet.companies.model.InvoiceProduct;
import com.estafet.companies.service.CompanyService;
import com.estafet.companies.service.InvoiceService;
import com.estafet.companies.utils.JSONParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InvoiceController.class)
public class InvoiceControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvoiceService invoiceService;
    @MockBean
    private CompanyService companyService;

    private static List<Invoice> testInvoiceList;

    private static Invoice testNewInvoice;

    @BeforeAll
    public static void setup()
    {
        List<InvoiceProduct> testSampleProducts = Arrays.asList(
                new InvoiceProduct("product1", new BigDecimal("3.14"), 10),
                new InvoiceProduct("product2", new BigDecimal("15.32"), 2),
                new InvoiceProduct("product3", new BigDecimal("150"), 6)
        );

        // Cut off nanoseconds
        final LocalDateTime testLocalDateTime = LocalDateTime.now().withNano(0);

        testInvoiceList = Arrays.asList(
                new Invoice(testLocalDateTime, testLocalDateTime.plusDays(30), "tax1", testSampleProducts),
                new Invoice(testLocalDateTime, testLocalDateTime.plusDays(30), "tax2", testSampleProducts),
                new Invoice(testLocalDateTime, testLocalDateTime.plusDays(30), "tax3", testSampleProducts)
        );

        testNewInvoice = new Invoice(testLocalDateTime, testLocalDateTime.plusDays(30), "tax4", testSampleProducts);
    }

    @Test
    void getAllInvoices() throws Exception
    {
        when(invoiceService.getAllInvoicesAsList()).thenReturn(testInvoiceList);

        ResultActions result = mockMvc.perform(get("/invoices"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].dateIssued", containsInAnyOrder(testInvoiceList.stream().map(Invoice::getDateIssued).map(LocalDateTime::toString).toArray())))
                .andExpect(jsonPath("$[*].dateDue", containsInAnyOrder(testInvoiceList.stream().map(Invoice::getDateDue).map(LocalDateTime::toString).toArray())))
                .andExpect(jsonPath("$[*].invoiceId", containsInAnyOrder(testInvoiceList.stream().map(Invoice::getInvoiceId).toArray())))
                .andExpect(jsonPath("$[*].companyTaxId", containsInAnyOrder(testInvoiceList.stream().map(Invoice::getCompanyTaxId).toArray())));

        // Test entire response (including products)
        assertEquals(result.andReturn().getResponse().getContentAsString(), JSONParser.fromObjectListToJsonString(testInvoiceList));

        mockMvc.perform(get("/invoices"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(JSONParser.fromObjectListToJsonString(testInvoiceList)));
    }

    @Test
    void getInvoiceById() throws InvalidInputException, EntityNotFoundException, Exception
    {
        Invoice invoiceForTest = testInvoiceList.get(1);
        when(invoiceService.getInvoice(invoiceForTest.getInvoiceId())).thenReturn(invoiceForTest);

        ResultActions result = mockMvc.perform(get("/invoices/" + invoiceForTest.getInvoiceId()));
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dateIssued", is(invoiceForTest.getDateIssued().toString())))
                .andExpect(jsonPath("$.dateDue", is(invoiceForTest.getDateDue().toString())))
                .andExpect(jsonPath("$.invoiceId", is(invoiceForTest.getInvoiceId())))
                .andExpect(jsonPath("$.companyTaxId", is(invoiceForTest.getCompanyTaxId())));

        // Test entire response (including products)
        assertEquals(result.andReturn().getResponse().getContentAsString(), JSONParser.fromObjectToJsonString(invoiceForTest));
    }

    @Test
    void addInvoice() throws InvalidInputException, Exception
    {
        when(invoiceService.addInvoice(Mockito.any(Invoice.class))).thenReturn(testNewInvoice.getInvoiceId());

        // TODO: needs company when preparing invoice
        mockMvc.perform(put("/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONParser.fromObjectToJsonString(testNewInvoice)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(testNewInvoice.getInvoiceId())));
    }

    @Test
    void updateInvoice()
    {
    }

    @Test
    void deleteInvoice()
    {
    }

    @Test
    void testAddInvoice()
    {
    }
}
