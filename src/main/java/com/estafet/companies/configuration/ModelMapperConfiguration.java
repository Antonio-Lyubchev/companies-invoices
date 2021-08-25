package com.estafet.companies.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;

public class ModelMapperConfiguration
{
    @Bean
    public ModelMapper modelMapper()
    {
        return new ModelMapper();
    }
}
