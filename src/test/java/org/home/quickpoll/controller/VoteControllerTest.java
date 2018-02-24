package org.home.quickpoll.controller;

import org.home.quickpoll.controller.v1.VoteController;
import org.home.quickpoll.domain.mapper.VoteMapper;
import org.home.quickpoll.service.VoteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@WebMvcTest(VoteController.class)
public class VoteControllerTest {

    @MockBean
    private VoteService voteService;
    @MockBean
    private VoteMapper voteMapper;

    @Test
    public void name() {

    }
}