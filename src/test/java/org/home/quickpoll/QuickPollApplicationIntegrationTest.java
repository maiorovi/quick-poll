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
public class QuickPollApplicationIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createsPoll() {
        final Set<Option> options = Sets.newLinkedHashSet(new Option("option 2"), new Option("option 3"));
        final Poll poll = aPoll(options, "Quick question");

        ResponseEntity<Object> responseEntity = restTemplate.postForEntity("/poll", poll, Object.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getHeaders()).contains(entry("Location", Lists.newArrayList("poll/3")));
    }

    @Test
    public void queryForAPoll() {
        final Set<Option> expectedOptions = newHashSet(Lists.newArrayList(new Option("Option 1"), new Option("Option 2"), new Option("Option 3")));
        Poll expectedPoll = aPoll(expectedOptions, "What is the sense of life");
        expectedPoll.setId(0);

        Poll poll = restTemplate.getForObject("/poll/1", Poll.class);

        assertThat(poll).isEqualTo(expectedPoll);
    }

    private Poll aPoll(Set<Option> options, String question) {
        return Poll.builder()
                .id(10)
                .question(question)
                .options(options)
                .build();
    }
}