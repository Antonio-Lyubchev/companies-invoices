package com.estafet.companies.controller;

import com.estafet.companies.configuration.ObjectMapperConfiguration;
import com.estafet.companies.exception.InvalidInputException;
import com.estafet.companies.model.Invoice;
import com.estafet.companies.service.CompanyService;
import com.estafet.companies.service.InvoiceService;
import com.estafet.companies.utils.JSONParser;
import com.estafet.companies.utils.ModelMapperUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(ModelMapperUtils.class)
@WebMvcTest(InvoiceController.class)
public class InvoiceControllerIT
{
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JSONParser parser;

    @MockBean
    private InvoiceService invoiceService;
    @MockBean
    private CompanyService companyService;

    private List<Invoice> testInvoiceList;
    private Invoice testNewInvoice;

    @BeforeAll
    public void setup() throws IOException
    {
        Resource invoicesJsonFileResource = new ClassPathResource("JSON/AddInvoices.json");
        byte[] invoicesByteArray = invoicesJsonFileResource.getInputStream().readAllBytes();

        // New parser before context initializes the one for the class
        JSONParser parser = new JSONParser();
        parser.setObjectMapper(new ObjectMapperConfiguration().objectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));
        testInvoiceList = parser.fromJsonToList(invoicesByteArray, Invoice.class);

        testNewInvoice = new Invoice(testInvoiceList.get(0).getDateIssued(), testInvoiceList.get(0).getDateDue(), testInvoiceList.get(0).getCompany(), testInvoiceList.get(0).getProducts());
    }

    @Test
    void getAllInvoices() throws Exception
    {
        when(invoiceService.getAllInvoicesAsList()).thenReturn(testInvoiceList);

        ResultActions result = mockMvc.perform(get("/invoices"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].dateIssued", containsInAnyOrder(testInvoiceList.stream()
                        .map(Invoice::getDateIssued)
                        .map(d -> d.format(DateTimeFormatter.ISO_DATE_TIME)).toArray())))
                .andExpect(jsonPath("$[*].dateDue", containsInAnyOrder(testInvoiceList.stream()
                        .map(Invoice::getDateDue)
                        .map(d -> d.format(DateTimeFormatter.ISO_DATE_TIME)).toArray())))
                .andExpect(jsonPath("$[*].invoiceId", containsInAnyOrder(testInvoiceList.stream().map(Invoice::getInvoiceId).toArray())))
                .andExpect(jsonPath("$[*].companyTaxId", containsInAnyOrder(testInvoiceList.stream().map(Invoice::getCompany).toArray())));

        // Test entire response (including products)
        assertEquals(result.andReturn().getResponse().getContentAsString(), parser.fromObjectListToJsonString(testInvoiceList));

        mockMvc.perform(get("/invoices"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(parser.fromObjectListToJsonString(testInvoiceList)));
    }

    @Test
    void getInvoiceById() throws Exception
    {
        Invoice invoiceForTest = testInvoiceList.get(1);
        when(invoiceService.getInvoice(invoiceForTest.getInvoiceId())).thenReturn(invoiceForTest);

        ResultActions result = mockMvc.perform(get("/invoices/" + invoiceForTest.getInvoiceId()));
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dateIssued", is(invoiceForTest.getDateIssued().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.dateDue", is(invoiceForTest.getDateDue().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.company.name", is(invoiceForTest.getCompany().getName())))
                .andExpect(jsonPath("$.company.taxId", is(invoiceForTest.getCompany().getTaxNumber())))
                .andExpect(jsonPath("$.company.address", is(invoiceForTest.getCompany().getAddress())))
                .andExpect(jsonPath("$.company.representative", is(invoiceForTest.getCompany().getRepresentative())));

        // Test entire response (including products)
        //TODO: test is innacurate because of the DTOs, needs reimplementing
        //assertEquals(result.andReturn().getResponse().getContentAsString(), parser.fromObjectToJsonString(invoiceForTest));
    }

    @Test
    void addInvoice() throws InvalidInputException
    {
        when(invoiceService.addInvoice(Mockito.any(Invoice.class))).thenReturn(testNewInvoice.getInvoiceId());

        // TODO: needs company when preparing invoice
/*        mockMvc.perform(put("/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONParser.fromObjectToJsonString(testNewInvoice)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(testNewInvoice.getInvoiceId())));*/
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
