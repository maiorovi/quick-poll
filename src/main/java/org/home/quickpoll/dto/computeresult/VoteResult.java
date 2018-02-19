package org.home.quickpoll.dto.computeresult;

import lombok.Data;
import org.home.quickpoll.dto.computeresult.OptionCount;

import java.util.Collection;

@Data
public class VoteResult {
    private int totalVotes;
    private Collection<OptionCount> results;
}
