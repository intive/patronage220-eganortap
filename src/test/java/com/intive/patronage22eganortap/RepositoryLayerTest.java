package com.intive.patronage22eganortap;

import com.intive.patronage22eganortap.domain.Poll;
import com.intive.patronage22eganortap.domain.PossibleAnswer;
import com.intive.patronage22eganortap.domain.Question;
import com.intive.patronage22eganortap.domain.UserAnswer;
import com.intive.patronage22eganortap.repository.PollRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = {
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
})
class RepositoryLayerTest {

    @Autowired
    private PollRepository pollRepository;

    @Test
    void shouldAddPoll() {
        //given
        Poll samplePoll = Poll.builder()
                .name("Sample Poll")
                .description("Sample Description")
                .email("mail@example.com")
                .link("link")
                .status("status")
                .isPublic(true)
                .timer(123)
                .startDate(new Date(10000L))
                .build();

        Question sampleQuestion = Question.builder()
                .content("Sample content")
                .build();

        samplePoll.setQuestions(List.of(sampleQuestion));
        UserAnswer sampleUserAnswer = UserAnswer.builder()
                .sessionId("sessionId")
                .build();

        sampleQuestion.setUserAnswers(Set.of(sampleUserAnswer));

        PossibleAnswer samplePossibleAnswer = PossibleAnswer.builder()
                .content("Some content")
                .build();

        sampleQuestion.setPossibleAnswers(Set.of(samplePossibleAnswer));
        sampleUserAnswer.setPossibleAnswer(samplePossibleAnswer);

        //when
        pollRepository.save(samplePoll);

        //then
        Optional<Poll> foundPoll = pollRepository.findAll().stream().findFirst();
        assertTrue(foundPoll.isPresent());

        Poll presentPoll = foundPoll.get();
        assertEquals(samplePoll, presentPoll);

        Optional<Question> foundQuestion = presentPoll.getQuestions().stream().findFirst();
        assertTrue(foundQuestion.isPresent());

        Question presentQuestion = foundQuestion.get();
        assertEquals(sampleQuestion, presentQuestion);

        Optional<UserAnswer> foundUserAnswer = presentQuestion.getUserAnswers().stream().findFirst();
        assertTrue(foundUserAnswer.isPresent());

        Optional<PossibleAnswer> foundPossibleAnswer = presentQuestion.getPossibleAnswers().stream().findFirst();
        assertTrue(foundPossibleAnswer.isPresent());

        assertEquals(samplePossibleAnswer, foundPossibleAnswer.get());

        UserAnswer presentUserAnswer = foundUserAnswer.get();
        assertEquals(sampleUserAnswer, presentUserAnswer);
        assertEquals(samplePossibleAnswer, presentUserAnswer.getPossibleAnswer());
    }


}