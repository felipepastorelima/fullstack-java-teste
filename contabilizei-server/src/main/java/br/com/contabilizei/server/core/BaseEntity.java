package br.com.contabilizei.server.core;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.proxy.HibernateProxyHelper;

@MappedSuperclass
public abstract class BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	public BaseEntity() {
		super();		
	}
	
	public BaseEntity(BaseDTO dto) {
		if (dto == null) {
			return;
		}
		
		this.id = dto.getId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BaseEntity)) {
			return false;
		}
		if (getId() == null || ((BaseEntity) obj).getId() == null) {
			return false;
		}
		if (!getId().equals(((BaseEntity) obj).getId())) {
			return false;
		}
		if (!HibernateProxyHelper.getClassWithoutInitializingProxy(obj).isAssignableFrom(this.getClass())) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return getId() == null ? super.hashCode() : getId().hashCode();
	}
	
}
