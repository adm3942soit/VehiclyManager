package com.adonis.data.persons;


import com.adonis.data.Audit;
import com.adonis.data.payments.CreditCard;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

/**
 * A domain object example. In a real application this would probably be a JPA
 * entity or DTO.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "persons", schema = "")
@Getter
@Setter
@NoArgsConstructor
public class Person extends Audit
{

	@NotNull(message="Please enter first name")
	@Column(name = "FIRST_NAME", nullable = false)
	private String firstName;

	@NotNull(message="Please enter last name")
	@Column(name = "LAST_NAME", nullable = false)
	private String lastName;

	@Column(name = "NAME", nullable = false)
	private String name;

	@Email
	@NotNull(message="Please enter email")
	@Column(name = "EMAIL", unique = true, nullable = false)
	private String email;

	@Column(name = "LOGIN", nullable = true, unique = true)
	private String login;

//	@NotNull(message="Please select a password")
	@Length(min=5, max=10, message="Password should be between 5 - 10 charactes")
	@Column(name = "PASSWORD", nullable = true)
	private String password;

	@Past
	@Column(name = "BIRTH_DATE", nullable = true)
	private java.util.Date birthDate;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ADDRESS")
	private Address address;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CARD")
	private CreditCard card;

	@Column(name = "PHONE_NUMBER", nullable = true)
	private String phoneNumber;

	@Column(name = "GENDER", nullable = true)
	private String gender;

	@Column(name = "PICTURE", nullable = true)
	private String picture;

	@Lob
	@Column(name = "NOTES", length = 1000, nullable = true)
	private String notes;


}
