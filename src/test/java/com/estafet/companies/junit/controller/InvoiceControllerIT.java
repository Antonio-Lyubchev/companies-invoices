package com.estafet.companies.junit.controller;

import com.estafet.companies.configuration.ModelMapperConfiguration;
import com.estafet.companies.configuration.ObjectMapperConfiguration;
import com.estafet.companies.controller.InvoiceController;
import com.estafet.companies.dto.InvoiceDto;
import com.estafet.companies.exception.InvalidInputException;
import com.estafet.companies.model.Company;
import com.estafet.companies.model.Invoice;
import com.estafet.companies.service.CompanyService;
import com.estafet.companies.service.InvoiceService;
import com.estafet.companies.utils.JSONParser;
import com.estafet.companies.utils.ModelMapperUtils;
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
    private ModelMapperUtils mockedModelMapperUtils;
    @MockBean
    private InvoiceService mockedInvoiceService;
    @MockBean
    private CompanyService mockedCompanyService;

    private ModelMapperUtils testModelMapper;
    private List<Invoice> testInvoiceList;
    private Invoice testNewInvoice;

    @BeforeAll
    public void setup() throws IOException
    {
        Resource invoicesJsonFileResource = new ClassPathResource("JSON/AddInvoices.json");
        byte[] invoicesByteArray = invoicesJsonFileResource.getInputStream().readAllBytes();

        // New parser before context initializes the one for the class
        JSONParser parser = new JSONParser();
        parser.setObjectMapper(new ObjectMapperConfiguration().objectMapper());

        testModelMapper = new ModelMapperUtils();
        testModelMapper.setModelMapper(new ModelMapperConfiguration().modelMapper());

        List<InvoiceDto> invoiceDtoList = parser.fromJsonToList(invoicesByteArray, InvoiceDto.class);
        testInvoiceList = testModelMapper.convertInvoiceDtoListToEntity(invoiceDtoList);
        // fill IDs to mock correctly
        for (int i = 0; i < testInvoiceList.size(); i++)
        {
            testInvoiceList.get(i).setId((long)i);
            for (int j = 0; j < testInvoiceList.get(i).getProductItems().size(); j++)
            {
                testInvoiceList.get(i).getProductItems().get(j).setId(j);
                testInvoiceList.get(i).getProductItems().get(j).getProduct().setId((long)j);
            }
        }

        testNewInvoice = new Invoice(testInvoiceList.get(0));
    }

    @Test
    void getAllInvoices() throws Exception
    {
        when(mockedInvoiceService.getAllInvoicesAsList()).thenReturn(testInvoiceList);
        when(mockedModelMapperUtils.convertInvoiceListToDto(Mockito.anyList())).thenReturn(testModelMapper.convertInvoiceListToDto(testInvoiceList));

        mockMvc.perform(get("/invoices"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].dateIssued", containsInAnyOrder(testInvoiceList.stream()
                        .map(Invoice::getDateIssued)
                        .map(d -> d.format(DateTimeFormatter.ISO_DATE_TIME)).toArray())))
                .andExpect(jsonPath("$[*].dateDue", containsInAnyOrder(testInvoiceList.stream()
                        .map(Invoice::getDateDue)
                        .map(d -> d.format(DateTimeFormatter.ISO_DATE_TIME)).toArray())))
                .andExpect(jsonPath("$[*].invoiceId", containsInAnyOrder(testInvoiceList.stream().map(Invoice::getId).map(Long::intValue).toArray())))
                .andExpect(jsonPath("$[*].company.taxNumber", containsInAnyOrder(testInvoiceList
                        .stream()
                        .map(Invoice::getCompany)
                        .map(Company::getTaxNumber)
                        .map(Long::intValue)
                        .toArray())));
    }

    @Test
    void getInvoiceById() throws Exception
    {
        Invoice invoiceForTest = testInvoiceList.get(1);
        when(mockedInvoiceService.getInvoice(invoiceForTest.getId())).thenReturn(invoiceForTest);
        when(mockedModelMapperUtils.convertToDto(Mockito.any(Invoice.class))).thenReturn(testModelMapper.convertToDto(invoiceForTest));

        ResultActions result = mockMvc.perform(get("/invoices/" + invoiceForTest.getId()));
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dateIssued", is(invoiceForTest.getDateIssued().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.dateDue", is(invoiceForTest.getDateDue().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.company.name", is(invoiceForTest.getCompany().getName())))
                .andExpect(jsonPath("$.company.taxNumber", is(invoiceForTest.getCompany().getTaxNumber().intValue())))
                .andExpect(jsonPath("$.company.address", is(invoiceForTest.getCompany().getAddress())))
                .andExpect(jsonPath("$.company.representative", is(invoiceForTest.getCompany().getRepresentative())));
    }

    @Test
    void addInvoice() throws InvalidInputException
    {
        when(mockedInvoiceService.addInvoice(Mockito.any(Invoice.class))).thenReturn(testNewInvoice.getId());

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
