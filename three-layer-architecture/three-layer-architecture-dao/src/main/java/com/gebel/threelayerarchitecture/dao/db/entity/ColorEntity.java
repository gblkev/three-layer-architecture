package com.gebel.threelayerarchitecture.dao.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name = "color")
public class ColorEntity {
	
	@Id
	private Long id;
	
	@Column(name = "hexa_code", nullable = false)
	private String hexaCode;

}