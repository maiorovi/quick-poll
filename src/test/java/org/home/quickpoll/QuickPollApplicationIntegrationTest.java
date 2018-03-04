package org.home.quickpoll;

import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.home.quickpoll.domain.Option;
import org.home.quickpoll.domain.Poll;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.util.Sets.newHashSet;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("dev")
@WithMockUser
public class QuickPollApplicationIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createsPoll() {
        final Set<Option> options = Sets.newLinkedHashSet(new Option("option 2"), new Option("option 3"));
        final Poll poll = aPoll(options, "Quick question");

        ResponseEntity<Object> responseEntity = restTemplate.postForEntity("/v1/polls", poll, Object.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getHeaders()).contains(entry("Location", Lists.newArrayList("polls/21")));
    }

    @Test
    public void queryForAPoll() {
        final Set<Option> expectedOptions = newHashSet(Lists.newArrayList(new Option("6"), new Option("8"), new Option("5"), new Option("4")));
        Poll expectedPoll = aPoll(expectedOptions, "How many rings are on the Olympic flag?");
        expectedPoll.setId(20);

        Poll poll = restTemplate.getForObject("/v1/polls/20", Poll.class);

        assertThat(poll).isEqualToComparingOnlyGivenFields(expectedPoll, "question", "options");
    }

    private Poll aPoll(Set<Option> options, String question) {
        return Poll.builder()
                .id(10)
                .question(question)
                .options(options)
                .build();
    }
}