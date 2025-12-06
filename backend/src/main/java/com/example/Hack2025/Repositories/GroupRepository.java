package com.example.Hack2025.Repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.example.Hack2025.Entities.Group;

import java.util.Optional;

@Repository
@Transactional
public class GroupRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Group> getGroupById(Integer id) {
        Group group = entityManager.find(Group.class, id);
        return Optional.ofNullable(group);
    }
}
