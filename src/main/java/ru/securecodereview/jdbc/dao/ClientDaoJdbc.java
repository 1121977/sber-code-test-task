package ru.securecodereview.jdbc.dao;

import ru.securecodereview.core.dao.ClientDao;
import ru.securecodereview.core.model.Client;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientDaoJdbc implements ClientDao {

    private final DataSource dataSource;

    public ClientDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Client> findById(long id) {
        return Optional.empty();
    }

    @Override
    public long insert(Client client) {
        return 0;
    }

    @Override
    public List<Client> findByName(String name) {
        List<Client> resultList = new ArrayList<>();
        StringBuilder sqlCommand = new StringBuilder("select id, name from client where name = ").append('\'' + name + '\'');
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select id, name from client where name = ?");
            preparedStatement.setString(1, name);
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                resultList.add(new Client(resultSet.getLong("id"), resultSet.getString("name")));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return resultList;
    }

}
