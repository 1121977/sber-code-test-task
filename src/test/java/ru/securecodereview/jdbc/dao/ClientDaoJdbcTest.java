package ru.securecodereview.jdbc.dao;

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

@Testcontainers
class ClientDaoJdbcTest {

//    static ClientDaoJdbc clientDaoJdbc;

/*
    @BeforeAll
    static void beforeAll() {
        Properties taskProperties = new Properties();
        try(InputStream taskPropertiesStream = ClientDaoJdbcTest.class.getClassLoader().getResourceAsStream("task.properties")){
            taskProperties.load(taskPropertiesStream);
            String url = taskProperties.getProperty("sql.url");
            String user = taskProperties.getProperty("sql.user");
            String password = taskProperties.getProperty("sql.password");
//            clientDaoJdbc =
        } catch (IOException err){
            err.printStackTrace();
        }
    }
*/

    @Container
    private final PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("testDataBase")
            .withUsername("owner")
            .withPassword("secret")
            .withClasspathResourceMapping("00_createTables.sql", "/docker-entrypoint-initdb.d/00_createTables.sql", BindMode.READ_ONLY)
            .withClasspathResourceMapping("01_insertData.sql", "/docker-entrypoint-initdb.d/01_insertData.sql", BindMode.READ_ONLY);

@ParameterizedTest(name = "SQL Injection")
@ValueSource(strings = {"name2", "injection\' OR 1 = 1 --"})
void selectTest(String search) {
        ClientDao clientDao = new ClientDaoJdbc(new DataSourceImpl(getConnectionProperties(), postgresqlContainer.getJdbcUrl()));
        List<Client> clientList = clientDao.findByName(search);
        assertTrue(clientList.contains(new Client(2, "name2")));
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