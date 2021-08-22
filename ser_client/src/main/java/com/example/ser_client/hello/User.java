package com.example.ser_client.hello;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "demo_user")
public class User {
    @Id
    @Column(length = 32)
    private Long userId;

    private String username;

    private Double total;

    private Integer totalTime;
}
