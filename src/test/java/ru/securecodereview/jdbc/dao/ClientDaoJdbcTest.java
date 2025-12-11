package ru.securecodereview.jdbc.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.securecodereview.core.DataSourceImpl;
import ru.securecodereview.core.dao.ClientDao;
import ru.securecodereview.core.model.Client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

@Testcontainers
class ClientDaoJdbcTest {

    ClientDao clientDao;

    @BeforeEach
    void setUp(){
        clientDao = new ClientDaoJdbc(new DataSourceImpl(getConnectionProperties(), postgresqlContainer.getJdbcUrl()));
    }


    @Container
    private final PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("testDataBase")
            .withUsername("owner")
            .withPassword("secret")
            .withClasspathResourceMapping("00_createTables.sql", "/docker-entrypoint-initdb.d/00_createTables.sql", BindMode.READ_ONLY)
            .withClasspathResourceMapping("01_insertData.sql", "/docker-entrypoint-initdb.d/01_insertData.sql", BindMode.READ_ONLY);


    @Test
    @DisplayName("Test with name2")
    void selectName2Test(){
        List<Client> clientList = clientDao.findByName("name2");
        assertTrue(clientList.contains(new Client(2, "name2")));
    }

    @Test
    @DisplayName("Test with injection")
    void selectInjectionTest(){
        List<Client> clientList = clientDao.findByName("injection\' OR 1 = 1 --");
        assertFalse(clientList.contains(new Client(2, "name2")));
    }

    private Properties getConnectionProperties() {
        Properties props = new Properties();
        props.setProperty("user", postgresqlContainer.getUsername());
        props.setProperty("password", postgresqlContainer.getPassword());
        props.setProperty("ssl", "false");
        return props;
    }

    private Connection makeSingleConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(postgresqlContainer.getJdbcUrl(), getConnectionProperties());
        connection.setAutoCommit(false);
        return connection;
    }
}