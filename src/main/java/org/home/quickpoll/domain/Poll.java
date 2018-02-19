package org.home.quickpoll.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Data
@Setter()
@Entity
@Table(name = "poll")
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "question")
    private String question;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.PRIVATE)
    @Singular
    private Set<Option> options = new HashSet<>();

    public void addOptions(Option option) {
        option.setPoll(this);
        options.add(option);
    }

    public void removeAllOptions() {
        for(Iterator<Option> iterator = options.iterator(); iterator.hasNext();) {
            iterator.next();
            iterator.remove();
        }
    }

    public void addAllOptions(Collection<Option> options) {
        for(Option option : options) {
            addOptions(option);
        }
    }
}
