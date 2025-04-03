package com.example.survey.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.time.LocalDate;

@Entity
@Table(name = "surveys")
@Data
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String streetAddress;
    private String city;
    private String state;
    private String zip;
    private String telephone;
    private String email;

    private LocalDate dateOfSurvey;

    @ElementCollection
    private List<String> likes;
    private String interestSource;
    private String recommendation;
    private String raffle;
    private String comments;
}