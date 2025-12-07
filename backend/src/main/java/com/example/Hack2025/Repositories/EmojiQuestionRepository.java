package com.example.Hack2025.Repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.example.Hack2025.Entities.EmojiQuestion;

import java.util.List;

@Repository
@Transactional
public class EmojiQuestionRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<EmojiQuestion> getAllQuestions() {
        String query = "SELECT q FROM EmojiQuestion q";
        return entityManager.createQuery(query, EmojiQuestion.class).getResultList();
    }

    public EmojiQuestion getRandomQuestion() {
        String query = "SELECT q FROM EmojiQuestion q ORDER BY RAND()";
        List<EmojiQuestion> questions = entityManager.createQuery(query, EmojiQuestion.class)
                .setMaxResults(1)
                .getResultList();
        return questions.isEmpty() ? null : questions.get(0);
    }

    public EmojiQuestion getQuestionById(Integer id) {
        return entityManager.find(EmojiQuestion.class, id);
    }

    public void createQuestion(EmojiQuestion question) {
        entityManager.persist(question);
    }
}
