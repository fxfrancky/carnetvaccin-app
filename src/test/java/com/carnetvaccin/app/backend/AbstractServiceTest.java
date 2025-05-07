package com.carnetvaccin.app.backend;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.startupcheck.IsRunningStartupCheckStrategy;
import org.testcontainers.junit.jupiter.Container;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractServiceTest {

    @Container
    public static final GenericContainer<?> sqliteContainer = new GenericContainer<>("nouchka/sqlite3:latest")
        .withStartupCheckStrategy(new IsRunningStartupCheckStrategy())
        .withStartupTimeout(Duration.ofSeconds(150));

    protected EntityManagerFactory entityManagerFactory;
    protected EntityManager entityManager;

    @BeforeAll
    static void startContainer() {
        sqliteContainer.start();
    }

    @BeforeEach
    void setUp() {
        System.setProperty("javax.persistence.jdbc.url", "jdbc:sqlite::memory:");
        System.setProperty("javax.persistence.jdbc.driver", "org.sqlite.JDBC");

        entityManagerFactory = Persistence.createEntityManagerFactory("test-pu");
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterEach
    void cleanDatabase() {
        entityManager.getTransaction().begin();
        entityManager.createNativeQuery("DELETE FROM vaccin_utilisateur").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM vaccin").executeUpdate();
        entityManager.getTransaction().commit();
    }

    @AfterAll
    static void stopContainer() {
        sqliteContainer.stop();
    }
}