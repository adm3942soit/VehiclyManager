package com.adonis.data.persons;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import static java.lang.System.currentTimeMillis;

@SuppressWarnings("serial")
@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntity implements Serializable, Cloneable {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@Column(name = "CREATED")
	private Date created;

	@Column(name = "UPDATED")
	private Date updated;

	@PrePersist
	protected void setCreatedDate() {
		created = new Date(currentTimeMillis());
		updated = new Date(currentTimeMillis());
	}

	@PreUpdate
	protected void setUpdatedDate() {
		updated = new Date(currentTimeMillis());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (this.id == null) {
			return false;
		}

		if (obj instanceof AbstractEntity && obj.getClass().equals(getClass())) {
			return this.id.equals(((AbstractEntity) obj).id);
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 43 * hash + Objects.hashCode(this.id);
		return hash;
	}
}
