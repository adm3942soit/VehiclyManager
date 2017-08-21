package com.adonis.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import static com.adonis.install.InstallConstants.DEFAULT_TIMEZONE;


@SuppressWarnings("serial")
@MappedSuperclass
@Getter
@Setter
public class Audit implements Serializable, Cloneable {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",
			locale = JsonFormat.DEFAULT_LOCALE, timezone = DEFAULT_TIMEZONE)
	@Column(name = "CREATED")
	private Date created;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",
			locale = JsonFormat.DEFAULT_LOCALE, timezone = DEFAULT_TIMEZONE)
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
