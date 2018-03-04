package org.home.quickpoll.domain.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

@Entity
@Table(name="users")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue
    @Column(name="USER_ID")
    private Long id;

    @Column(name="username")
    @NotEmpty
    private String username;

    @Column(name = "password")
    @NotEmpty
    @JsonIgnore
    private String password;


    @NotEmpty
    @Column(name = "first_name")
    private String firstname;

    @Column(name="last_name")
    @NotEmpty
    private String lastname;

    @Column(name="ADMIN", columnDefinition="char(3)")
    @Type(type="yes_no")
    @NotEmpty
    private boolean admin;
}
