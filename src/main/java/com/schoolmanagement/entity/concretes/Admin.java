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
@SuperBuilder//Türeyen ya da türetilen class ise SuperBuilder yazilmali(hem user hem de student e yazilmali). Ancak single(tek) olan sinifa @Builder yeterlidir.
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true) //ayni nesneden türetilen iki objeyu hash leyip ona göre hizlice karsilastirir
                                                                    //ikinci parametre ise base'den gelen field larida dahil eder
@ToString(callSuper = true) // normalde toString metodu ilgili sinifin field'larini yazririr, callsuper ile basedeki fieldlari da yazdirmaya yarar
public class Admin extends User {

    private boolean built_in; //silinemez

}
