package com.adonis.data.persons;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * A domain object example. In a real application this would probably be a JPA
 * entity or DTO.
 */
@XmlRootElement(name = "person")
public class PersonDTO
{
	@XmlElement(name = "firstName")
	public String firstName;
	@XmlElement(name = "lastName")
	public String lastName;
	@XmlElement(name = "email")
	public String email;
	@XmlElement(name = "login")
	public String login;
	@XmlElement(name = "birthDate")
	public Date birthDate;
//	private AddressDAO address;
	@XmlElement(name = "phoneNumber")
	public String phoneNumber;
	@XmlElement(name = "gender")
	public String gender;
	@XmlElement(name = "notes")
	public String notes;

}
