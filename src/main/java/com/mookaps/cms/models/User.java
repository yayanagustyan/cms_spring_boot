package com.mookaps.cms.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_users")
@Entity
public class User {

    // use strategy IDENTITY for auto-increment, so it continuing from last db id
    // use access WRITE_ONLY for hidden on response, but keep readable on request

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private String name;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String phone_number;
    private String photo_1;
    private String photo_2;
    private String gender;
    private String address;
    private String active;
    private String status;
    private String level;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String reset_token;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String created_at;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String updated_at;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String token;

}
