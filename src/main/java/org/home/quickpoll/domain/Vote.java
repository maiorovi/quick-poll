package org.home.quickpoll.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "vote")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "option_id")
    private Option option;

    public Long getOptionId() {
        return getOption().getId();
    }
}
