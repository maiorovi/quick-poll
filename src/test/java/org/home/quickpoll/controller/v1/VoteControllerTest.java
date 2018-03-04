package org.home.quickpoll.controller.v1;

import org.home.quickpoll.controller.v1.VoteController;
import org.home.quickpoll.domain.Vote;
import org.home.quickpoll.domain.mapper.VoteMapper;
import org.home.quickpoll.dto.OptionDto;
import org.home.quickpoll.dto.VoteDto;
import org.home.quickpoll.service.VoteService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(value = VoteController.class, secure = false)
public class VoteControllerTest {
    private static final String URL_PREFIX = "/v1/polls/1/votes";

    @MockBean private VoteService voteService;
    @MockBean private VoteMapper voteMapper;

    @Autowired private MockMvc mvc;

    private Vote savedVote;

    @Before
    public void setUp() throws Exception {
        savedVote = new Vote();
    }

    @Test
    public void createsVote() throws Exception {
        String jsonVote = "{\"option\": {\"id\": 1, \"value\": \"option1\"} }";
        given(voteService.createVote(any(VoteDto.class))).willReturn(savedVote);
        savedVote.setId(1L);

        mvc.perform(post(URL_PREFIX).content(jsonVote).contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/polls/1/votes/1"));
    }

    @Test
    public void retrievesAllVotesByPollId() throws Exception {
        given(voteService.pollExists(1l)).willReturn(true);
        final OptionDto option = new OptionDto("option1");
        option.setId(1l);

        final VoteDto voteDto = new VoteDto(1l, option);

        String expectedResponse = "[{ \"id\": 1, \"option\": { \"value\": \"option1\", \"id\": 1}}]";

        given(voteService.getAllVotes(1l)).willReturn(newArrayList(voteDto));

        mvc.perform(get(URL_PREFIX).accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    public void retrieveAllPollsReturns404WhenPollWithGivenIdIsNotExists() throws Exception {
        given(voteService.pollExists(1l)).willReturn(false);

        final String expectedJsonResponse = "{\"title\":\"Resource Not Found\",\"status\":404,\"detail\":\"Can`t vote on a non existent poll\",\"timestamp\":1519509458768,\"developerMessage\":\"org.home.quickpoll.exception.ResourceNotFoundException\",\"errors\":null}";

        mvc.perform(get(URL_PREFIX).accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
        //TODO: Find a way to validate changing timestamp
//                .andExpect(content().json(expectedJsonResponse));
    }
}