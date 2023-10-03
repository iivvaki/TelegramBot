package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name= "user", schema = "public")
public class User {
    @Id
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "is_enable")
    private boolean isEnable;


}
