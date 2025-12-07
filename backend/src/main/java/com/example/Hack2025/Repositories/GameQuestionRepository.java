package com.example.Hack2025.Repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.example.Hack2025.Entities.GameQuestion;

import java.util.List;

@Repository
@Transactional
public class GameQuestionRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<GameQuestion> getAllQuestions() {
        String query = "SELECT q FROM GameQuestion q";
        return entityManager.createQuery(query, GameQuestion.class).getResultList();
    }

    public GameQuestion getRandomQuestion() {
        String query = "SELECT q FROM GameQuestion q ORDER BY RAND()";
        List<GameQuestion> questions = entityManager.createQuery(query, GameQuestion.class)
                .setMaxResults(1)
                .getResultList();
        return questions.isEmpty() ? null : questions.get(0);
    }

    public List<GameQuestion> getRandomQuestions(int count) {
        String query = "SELECT q FROM GameQuestion q ORDER BY RAND()";
        return entityManager.createQuery(query, GameQuestion.class)
                .setMaxResults(count)
                .getResultList();
    }

    public GameQuestion getQuestionById(Integer id) {
        return entityManager.find(GameQuestion.class, id);
    }

    public void createQuestion(GameQuestion question) {
        entityManager.persist(question);
    }
}
