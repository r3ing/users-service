package com.globallogic.users_service.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Represents a phone entity associated with a user in the system.
 * Each phone includes detailed information such as the phone number, city code,
 * country code, and its association to a user.
 *
 * This entity is mapped to the "phones" table in the database.
 * It uses JPA annotations for ORM.
 */
@Entity
@Table(name = "phones")
@Data
public class Phone {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private Long number;

  @Column(nullable = false, name = "city_code")
  private Integer cityCode;

  @Column(nullable = false, name = "contry_code")
  private String countryCode;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id")
  private User user;

}
