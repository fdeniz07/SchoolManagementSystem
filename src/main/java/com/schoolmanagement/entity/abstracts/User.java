package com.schoolmanagement.entity.abstracts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.schoolmanagement.entity.concretes.UserRole;
import com.schoolmanagement.entity.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable; //Best Practice olarak serilestirme entity siniflarina eklenir.
import java.time.LocalDate;

@MappedSuperclass //Db de user tablosu olusmadan bu sinifin anac sinif olarak kullanilmasini saglar
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder //Alt siniflarin User sinifinin builder özelliklerini kullanabilmesine izin verir

//@SuperBuilder --> ilgili sinifin field'larini bu classdan türetilen siniflara aktarirken (sadece java tarafinda). Eger atada @SuperBuilder varsa child da da eklenir
//@MappedSuperclass --> annotation'u da db de table olusturmamasina ragmen türettigi entitylere field'larini aktariyor ve türetilen entity siniflarinin db de kolonlarinin olusmasini sagliyor
//@Builder(toBuilder = true) --> bize farkli farkli parametreli constructor olusturmamizi saglar. Yeni bir nesne olusturmak yerine varolan nesnenin kopyasini alarak degisiklik yapmamizi saglar. Eklenen class icin
            //Builder DP olusturuyor. Default da degeri false gelir.
            /*
                         Builder neseyi Örnek örnek = Örnek.özellik1().özellik2().build gibi build etmeye yararken, toBuilder olan bir nesnenin üzerinden farklı bir nesne yapmaya yarıyor.
                         Örnek yeni örnek = örnek.toBuilder().özellik1(farklı bir özellik).build
             */
public abstract class User implements Serializable { //Ortak classlar icin bu class'i Base olarak kullanacagiz

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String ssn;

    private String name;

    private String surname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDay;

    private String birthPlace;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Client'den DB'ye giderken yazma islemi olsun, Db'den Client'e giderken Okuma islemi olmasin.
    private String password;                               // Hassas veri oldugu icin okuma islemlerinde kullanilmaz

    @Column(unique = true)
    private String phoneNumber;

    @OneToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UserRole userRole;

    private Gender gender;
}




























