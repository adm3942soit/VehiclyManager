package com.adonis.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@SuppressWarnings("serial")
@MappedSuperclass
@Getter
@Setter
public class Audit implements Serializable, Cloneable {


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
		created = new Date();
		updated = new Date();
	}

	@PreUpdate
	protected void setUpdatedDate() {
		updated = new Date();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (this.id == null) {
			return false;
		}

		if (obj instanceof Audit && obj.getClass().equals(getClass())) {
			return this.id.equals(((Audit) obj).id);
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
