package com.carnetvaccin.app.backend;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class AbstractServiceTest {

    protected static EntityManagerFactory entityManagerFactory;
    protected static EntityManager entityManager;

    @BeforeAll
    static void setUp() {
        entityManagerFactory = Persistence.createEntityManagerFactory("test-pu");
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterEach
    public void cleanDatabase() {
        startTransaction();
//        entityManager.getTransaction().begin();
        entityManager.createQuery("DELETE FROM VaccinUtilisateur").executeUpdate();
        entityManager.createQuery("DELETE FROM Vaccin").executeUpdate();
        entityManager.getTransaction().commit();
    }

    @AfterAll
    static void tearDown() {
        if (entityManager != null) {
            entityManager.close();
        }
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    public void startTransaction() {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
    }
}