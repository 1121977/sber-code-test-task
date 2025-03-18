package ru.securecodereview.core.dao;

import ru.securecodereview.core.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientDao {
    Optional<Client> findById(long id);
    //List<Client> findAll();

    long insert(Client client);

    List<Client> findByName(String name);
    //void update(Client client);
    //long insertOrUpdate(Client client);

}
