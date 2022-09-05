package com.gebel.threelayerarchitecture.dao.db.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name = "car")
public class CarEntity {
	
	@Id
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "color_id", nullable = false)
	private ColorEntity color;

	@ManyToOne
	@JoinColumn(name = "driver_id", nullable = true)
	private DriverEntity driver;

}