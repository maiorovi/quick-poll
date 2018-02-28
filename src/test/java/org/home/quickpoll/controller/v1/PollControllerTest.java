package org.home.quickpoll.controller.v1;


import org.assertj.core.util.Sets;
import org.home.quickpoll.controller.v1.PollController;
import org.home.quickpoll.domain.Option;
import org.home.quickpoll.domain.Poll;
import org.home.quickpoll.domain.mapper.PollMapper;
import org.home.quickpoll.dto.OptionDto;
import org.home.quickpoll.dto.PollDto;
import org.home.quickpoll.service.PollService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PollController.class)
public class PollControllerTest {

    private static final String URL_PREFIX ="/v1/polls";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PollMapper pollMapper;

    @MockBean
    private PollService pollService;

    @Test
    public void lookupsPollById() throws Exception {
        final Set<Option> options = Sets.newLinkedHashSet(new Option("option 1"), new Option("option 2"), new Option("option 3"));
        final String question = "What is the sense of life";
        final String url = URL_PREFIX + "/10";

        Poll poll = aPoll(options, question);
        PollDto pollDto = toPollDto(poll);

        given(pollMapper.toPollDto(poll)).willReturn(pollDto);
        given(pollService.getPoll(10L)).willReturn(Optional.of(poll));

        String expecteJson = "{\"question\":\"What is the sense of life\",\"options\":[{\"value\":\"option 2\"},{\"value\":\"option 3\"},{\"value\":\"option 1\"}]}";

        mvc.perform(get(url)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().json(expecteJson));
    }

    @Test
    public void lookupAllPolls() throws Exception {
        final Set<Option> options = Sets.newLinkedHashSet(new Option("option 1"), new Option("option 2"), new Option("option 3"));
        final String question = "What is the sense of life";
        Poll pollFirst = aPoll(options, question);
        Poll pollSecond = aPoll(options, question);

        String expectedJson = "[{\"question\":\"What is the sense of life\",\"options\":[{\"value\":\"option 2\"},{\"value\":\"option 3\"},{\"value\":\"option 1\"}]},{\"question\":\"What is the sense of life\",\"options\":[{\"value\":\"option 2\"},{\"value\":\"option 3\"},{\"value\":\"option 1\"}]}]";

        given(pollService.getAllPolls()).willReturn(newArrayList(pollFirst, pollSecond));
        given(pollMapper.toPollDto(any(Poll.class))).will(invocation -> toPollDto(invocation.getArgumentAt(0, Poll.class)));

        mvc.perform(get(URL_PREFIX).contentType(APPLICATION_JSON))
     .andExpect(status().isOk())
     .andExpect(content().contentType(APPLICATION_JSON_UTF8))
     .andExpect(content().json(expectedJson));
    }

    @Test
    public void lookupAllPollsReturnInternalServerErrorStatusWhenExceptionHappened() throws Exception {
        given(pollService.getAllPolls()).willThrow(Exception.class);

        mvc.perform(get(URL_PREFIX).contentType(APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().json("{\"error\": \"server failed to respond on your request\"}"));
    }

    @Test
    public void lookupByIdReturns404WhenPollwithGivenIdDontExist() throws Exception {
        final String url = URL_PREFIX + "/21";
        given(pollService.getPoll(21L)).willReturn(Optional.empty());

        mvc.perform(get(url))
                .andExpect(status().isNotFound());
    }

    @Test
    public void lookupByPollIdReturns400WhenGivenPollIdIsNotNumeric() throws Exception {
        final String url = URL_PREFIX + "/abc";
        mvc.perform(get(url))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteByIdRequestReturnsNoContentByDefault() throws Exception {
        final String url = URL_PREFIX + "/12";
        Poll poll = aPoll(Sets.newHashSet(), "Radom question");

        given(pollService.getPoll(12L)).willReturn(Optional.of(poll));

        mvc.perform(delete(url))
                .andExpect(status().isNoContent());
    }

    @Test
    public void returns400StatusWhenIdForDeleteRequest() throws Exception {
        final String url = URL_PREFIX + "/ad";
        mvc.perform(delete(url))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void returns404WhenIdForDeleteRequestNotFound() throws Exception {
        given(pollService.getPoll(12L)).willReturn(Optional.empty());

        mvc.perform(delete(URL_PREFIX + "/12"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void addPollReturnsCreatedWhenSuccessful() throws Exception {
        String pollJson = "{\"question\":\"What is the sense of life\",\"options\":[{\"value\":\"option 2\"},{\"value\":\"option 3\"},{\"value\":\"option 1\"}]}";

        Poll poll = Poll.builder().id(1).build();

        given(pollService.createPoll(any(PollDto.class))).willReturn(poll);

        mvc.perform(post(URL_PREFIX).contentType(APPLICATION_JSON)
        .content(pollJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "polls/1"));
    }

    @Test
    public void addPollReturnsBadRequestOnIncorrectJsonInput() throws Exception {
        String pollJson = "{\"question\":\"What is the sense of life\",\"options\":[{value: fds}]}";

        Poll poll = Poll.builder().id(1).build();

        given(pollService.createPoll(any(PollDto.class))).willReturn(poll);

        mvc.perform(post(URL_PREFIX).contentType(APPLICATION_JSON)
                .content(pollJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updatePollReturnsUpdatedPoll() throws Exception {
        final Set<Option> options = Sets.newLinkedHashSet(new Option("option 2"), new Option("option 3"));
        final Poll updatedPoll = aPoll(options,"my question 3");
        updatedPoll.setId(1);

        final String body = "{\"question\": \"my question 3\", \"options\":[{\"value\": \"option 2\"}, {\"value\": \"option 3\"}]}";

        given(pollService.updatePoll(eq(1L), any(PollDto.class))).willReturn(Optional.of(updatedPoll));
        given(pollMapper.toPollDto(updatedPoll)).willReturn(toPollDto(updatedPoll));

        mvc.perform(put(URL_PREFIX+ "/1").contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }

    private Poll aPoll(Set<Option> options, String question) {
        return Poll.builder()
                .id(10)
                .question(question)
                .options(options)
                .build();
    }

    private PollDto toPollDto(Poll poll) {
        Set<OptionDto> optionDtos = poll.getOptions().stream()
                .map(option -> new OptionDto(option.getValue()))
                .collect(Collectors.toSet());

        return PollDto.builder()
                .question(poll.getQuestion())
                .options(optionDtos)
                .build();
    }
}