package br.com.contabilizei.server.core;

public class BaseDTO {

	private Long id;

	public BaseDTO() {
		super();
	}
	
	public BaseDTO(Long id) {
		this.id = id;
	}

	public BaseDTO(BaseEntity entity) {
		if (entity == null) {
			return;
		}
		
		this.id = entity.getId();			
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
