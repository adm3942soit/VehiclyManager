package com.adonis.data.persons;


import com.adonis.data.Audit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import java.util.Date;

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

	@Column(name = "FIRST_NAME", nullable = false)
	private String firstName;

	@Column(name = "LAST_NAME", nullable = false)
	private String lastName;

	@Email
	@Column(name = "EMAIL", unique = true, nullable = false)
	private String email;

	@Column(name = "LOGIN", nullable = true, unique = true)
	private String login;

	@Column(name = "PASSWORD", nullable = true)
	private String password;

	@Column(name = "BIRTH_DATE", nullable = true)
	private Date birthDate;

//	@Column(name = "ADDRESS", nullable = true)
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ADDRESS")
	private Address address;

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
