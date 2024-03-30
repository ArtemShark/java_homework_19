package org.example.clientsDAO;

import org.example.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientsDao {
    List<Client> findAll();
    Optional<Client> findById(int id);
    void save(Client client);
    void update(Client client);
    void delete(int id);

    int findMinDiscount();
    int findMaxDiscount();
    List<Client> findClientsWithMinDiscount(int minDiscount);
    List<Client> findClientsWithMaxDiscount(int maxDiscount);
    double findAverageDiscount();

    Client findYoungestClient();
    Client findOldestClient();
    List<Client> findClientsWithBirthdayToday();
    List<Client> findClientsWithEmailMissing();

}
