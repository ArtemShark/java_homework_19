package org.example.menu;

import org.example.clientsDAO.ClientsDaoImpl;
import org.example.model.Client;
import java.time.LocalDate;
import java.util.List;

import java.util.Scanner;

public class ClientsManager implements Manager {
    private ClientsDaoImpl clientsDao;
    private Scanner scanner;

    public ClientsManager(ClientsDaoImpl clientsDao) {
        this.clientsDao = clientsDao;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void manage() {
        while (true) {
            System.out.println("\nУправление клиентами:");
            System.out.println("1. Показать всех клиентов");
            System.out.println("2. Добавить нового клиента");
            System.out.println("3. Изменить информацию клиента");
            System.out.println("4. Удалить клиента");
            System.out.println("5. Показать минимальную скидку для клиента");
            System.out.println("6. Показать максимальную скидку для клиента");
            System.out.println("7. Показать клиентов с минимальной скидкой и величину скидки");
            System.out.println("8. Показать клиентов с максимальной скидкой и величину скидки");
            System.out.println("9. Показать среднюю величину скидки");
            System.out.println("10. Показать самого молодого клиента");
            System.out.println("11. Показать самого возрастного клиента");
            System.out.println("12. Показать клиентов, у которых день рождения в этот день");
            System.out.println("13. Показать клиентов, у которых не заполнен контактный почтовый адрес");
            System.out.println("14. Вернуться в предыдущее меню");
            System.out.print("Ваш выбор: ");

            int action = scanner.nextInt();
            scanner.nextLine();

            switch (action) {
                case 1:
                    showAll();
                    break;
                case 2:
                    addNew();
                    break;
                case 3:
                    update();
                    break;
                case 4:
                    delete();
                    break;
                case 5:
                    showMinDiscount();
                    break;
                case 6:
                    showMaxDiscount();
                    break;
                case 7:
                    showClientsWithMinDiscount();
                    break;
                case 8:
                    showClientsWithMaxDiscount();
                    break;
                case 9:
                    showAverageDiscount();
                    break;
                case 10:
                    showYoungestClient();
                    break;
                case 11:
                    showOldestClient();
                    break;
                case 12:
                    showClientsWithBirthdayToday();
                    break;
                case 13:
                    showClientsWithEmailMissing();
                    break;
                case 14:
                    return;
                default:
                    System.out.println("Неверный ввод, попробуйте еще раз.");
                    break;
            }
        }
    }

    @Override
    public void showAll() {
        List<Client> clientsList = clientsDao.findAll();
        if (clientsList.isEmpty()) {
            System.out.println("Список клиентов пуст.");
        } else {
            for (Client client : clientsList) {
                System.out.println(client.getClientId() + ": " + client.getFullName() + ", Email: " + client.getEmail() + ", Телефон: " + client.getPhone() + ", Скидка: " + client.getDiscount() + "%");
            }
        }
    }

    @Override
    public void addNew() {
        System.out.println("Добавление нового клиента:");
        System.out.print("Введите полное имя: ");
        String fullName = scanner.nextLine();
        System.out.print("Введите дату рождения (ГГГГ-ММ-ДД): ");
        String birthDate = scanner.nextLine();
        System.out.print("Введите телефон: ");
        String phone = scanner.nextLine();
        System.out.print("Введите email: ");
        String email = scanner.nextLine();
        System.out.print("Введите процент скидки: ");
        int discount = scanner.nextInt();
        scanner.nextLine();

        Client client = new Client(0, fullName, LocalDate.parse(birthDate), phone, email, discount);
        clientsDao.save(client);
        System.out.println("Новый клиент успешно добавлен.");
    }

    @Override
    public void update() {
        System.out.print("Введите ID клиента, которого хотите обновить: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Обновление клиента ID: " + id);
        System.out.print("Введите новое полное имя: ");
        String fullName = scanner.nextLine();
        System.out.print("Введите новую дату рождения (ГГГГ-ММ-ДД): ");
        String birthDate = scanner.nextLine();
        System.out.print("Введите новый телефон: ");
        String phone = scanner.nextLine();
        System.out.print("Введите новый email: ");
        String email = scanner.nextLine();
        System.out.print("Введите новый процент скидки: ");
        int discount = scanner.nextInt();
        scanner.nextLine();

        Client client = new Client(id, fullName, LocalDate.parse(birthDate), phone, email, discount);
        clientsDao.update(client);
        System.out.println("Информация о клиенте успешно обновлена.");
    }

    @Override
    public void delete() {
        System.out.print("Введите ID клиента, которого хотите удалить: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        clientsDao.delete(id);
        System.out.println("Клиент успешно удален.");
    }

    public void showMinDiscount() {
        int minDiscount = clientsDao.findMinDiscount();
        System.out.println("Минимальная скидка для клиента: " + minDiscount + "%");
    }

    public void showMaxDiscount() {
        int maxDiscount = clientsDao.findMaxDiscount();
        System.out.println("Максимальная скидка для клиента: " + maxDiscount + "%");
    }

    public void showClientsWithMinDiscount() {
        int minDiscount = clientsDao.findMinDiscount();
        List<Client> clients = clientsDao.findClientsWithMinDiscount(minDiscount);
        if (clients.isEmpty()) {
            System.out.println("Нет клиентов с минимальной скидкой (" + minDiscount + "%).");
        } else {
            System.out.println("Клиенты с минимальной скидкой (" + minDiscount + "%):");
            for (Client client : clients) {
                System.out.println(client.getClientId() + ": " + client.getFullName() + ", Email: " + client.getEmail() + ", Телефон: " + client.getPhone());
            }
        }
    }

    public void showClientsWithMaxDiscount() {
        int maxDiscount = clientsDao.findMaxDiscount();
        List<Client> clients = clientsDao.findClientsWithMaxDiscount(maxDiscount);
        if (clients.isEmpty()) {
            System.out.println("Нет клиентов с максимальной скидкой (" + maxDiscount + "%).");
        } else {
            System.out.println("Клиенты с максимальной скидкой (" + maxDiscount + "%):");
            for (Client client : clients) {
                System.out.println(client.getClientId() + ": " + client.getFullName() + ", Email: " + client.getEmail() + ", Телефон: " + client.getPhone());
            }
        }
    }

    public void showAverageDiscount() {
        double averageDiscount = clientsDao.findAverageDiscount();
        System.out.println("Средняя величина скидки для клиентов: " + averageDiscount + "%");
    }

    public void showYoungestClient() {
        Client youngestClient = clientsDao.findYoungestClient();
        if (youngestClient != null) {
            System.out.println("Самый молодой клиент: " + youngestClient.getFullName() + ", Дата рождения: " + youngestClient.getBirthDate() + ", Email: " + youngestClient.getEmail() + ", Телефон: " + youngestClient.getPhone());
        } else {
            System.out.println("Нет информации о самом молодом клиенте.");
        }
    }

    public void showOldestClient() {
        Client oldestClient = clientsDao.findOldestClient();
        if (oldestClient != null) {
            System.out.println("Самый возрастной клиент: " + oldestClient.getFullName() + ", Дата рождения: " + oldestClient.getBirthDate() + ", Email: " + oldestClient.getEmail() + ", Телефон: " + oldestClient.getPhone());
        } else {
            System.out.println("Нет информации о самом возрастном клиенте.");
        }
    }

    public void showClientsWithBirthdayToday() {
        List<Client> clients = clientsDao.findClientsWithBirthdayToday();
        if (clients.isEmpty()) {
            System.out.println("Сегодня нет дней рождений среди клиентов.");
        } else {
            System.out.println("Клиенты, у которых сегодня день рождения:");
            for (Client client : clients) {
                System.out.println(client.getClientId() + ": " + client.getFullName() + ", Дата рождения: " + client.getBirthDate() + ", Email: " + client.getEmail() + ", Телефон: " + client.getPhone());
            }
        }
    }

    public void showClientsWithEmailMissing() {
        List<Client> clients = clientsDao.findClientsWithEmailMissing();
        if (clients.isEmpty()) {
            System.out.println("Нет клиентов без контактного почтового адреса.");
        } else {
            System.out.println("Клиенты без контактного почтового адреса:");
            for (Client client : clients) {
                System.out.println(client.getClientId() + ": " + client.getFullName() + ", Дата рождения: " + client.getBirthDate() + ", Телефон: " + client.getPhone());
            }
        }
    }
}
