package com.schoolmanagement.entity.concretes;

import com.schoolmanagement.entity.abstracts.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "admins")
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Admin extends User {

    private boolean built_in; //silinemez

}
