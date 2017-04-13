package com.adonis.data.service;

import com.adonis.data.persons.Person;
import com.adonis.data.persons.PersonDTO;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Nikolajs Arhipovs
 */
public class DTOMapper extends ConfigurableMapper {

    public static final MapperFacade dtoMapper = new DTOMapper();

    @Override
    protected void configureFactoryBuilder(DefaultMapperFactory.Builder factoryBuilder) {
        factoryBuilder.mapNulls(false);
    }

    @Override
    public void configure(MapperFactory factory) {

        factory.getConverterFactory().registerConverter(
                new BidirectionalConverter<Date, Timestamp>() {

                    @Override
                    public Timestamp convertTo(Date s, Type<Timestamp> type) {
                        return new Timestamp(s.getTime());
                    }

                    @Override
                    public Date convertFrom(Timestamp d, Type<Date> type) {
                        return (Date) d;
                    }

                });

        factory.classMap(Person.class, PersonDTO.class)
                .field("firstName", "firstName")
                .field("lastName", "lastName")
                .field("email", "email")
                .field("login", "login")
                .field("birthDate", "birthDate")
                .field("phoneNumber", "phoneNumber")
                .field("gender", "gender")
                .field("notes", "notes")
               // .byDefault()
                .register();


    }

}
