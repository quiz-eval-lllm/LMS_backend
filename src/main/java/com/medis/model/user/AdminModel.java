package com.medis.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;

@Setter
@Getter
//@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "admin")
public class AdminModel extends UserModel {

}
