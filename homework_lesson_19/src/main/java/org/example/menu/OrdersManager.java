package org.example.menu;

import org.example.orderdeatilsDAO.OrderDetailsDaoImpl;
import org.example.ordersDAO.OrdersDaoImpl;
import org.example.model.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import java.util.Scanner;
public class OrdersManager implements Manager {
    private OrdersDaoImpl ordersDao;
    private Scanner scanner;

    private OrderDetailsManager orderDetailsManager;

    public OrdersManager(OrdersDaoImpl ordersDao, OrderDetailsDaoImpl orderDetailsDao) {
        this.ordersDao = ordersDao;
        this.orderDetailsManager = new OrderDetailsManager(orderDetailsDao);
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void manage() {
        while (true) {
            System.out.println("\nУправление заказами:");
            System.out.println("1. Показать все заказы");
            System.out.println("2. Показать все заказы конкретного десерта");
            System.out.println("3. Показать все заказы конкретного официанта");
            System.out.println("4. Показать все заказы конкретного клиента");
            System.out.println("5. Показать информацию о заказах в конкретную дату");
            System.out.println("6. Показать информацию о заказах в указанном промежутке дат");
            System.out.println("7. Показать количество заказов десертов в конкретную дату");
            System.out.println("8. Показать количество заказов напитков в конкретную дату");
            System.out.println("9. Добавить новый заказ кофе");
            System.out.println("10. Добавить новый заказ десерта");
            System.out.println("11. Изменить заказ");
            System.out.println("12. Удалить заказ");
            System.out.println("13. Удалить заказы конкретного десерта");
            System.out.println("14. Управление деталями заказа");
            System.out.println("15. Показать информацию о клиентах, которые заказывали напитки сегодня");
            System.out.println("16. Показать среднюю сумму заказа в конкретную дату");
            System.out.println("17. Показать максимальную сумму заказа в конкретную дату");
            System.out.println("18. Показать клиента, который совершил максимальную сумму заказа в конкретную дату");
            System.out.println("19. Вернуться в предыдущее меню");
            System.out.print("Ваш выбор: ");

            int action = scanner.nextInt();
            scanner.nextLine();

            switch (action) {
                case 1:
                    showAll();
                    break;
                case 2:
                    System.out.print("Введите ID десерта: ");
                    int dessertId = scanner.nextInt();
                    showAllDessertOrders(dessertId);
                    break;
                case 3:
                    showAllOrdersOfStaff();
                    break;
                case 4:
                    showAllOrdersOfClient();
                    break;
                case 5:
                    showOrdersByDate();
                    break;
                case 6:
                    showOrdersByDateRange();
                    break;
                case 7:
                    showDessertOrdersByDate();
                    break;
                case 8:
                    showCoffeeOrdersByDate();
                    break;
                case 9:
                    addNewCoffeeOrder();
                    break;
                case 10:
                    addNewDessertOrder();
                    break;
                case 11:
                    update();
                    break;
                case 12:
                    delete();
                    break;
                case 13:
                    deleteOrdersByDessert();
                    break;
                case 14:
                    orderDetailsManager.manage();
                    break;
                case 15:
                    showCustomersWithDrinkOrdersToday();
                    break;
                case 16:
                    System.out.print("Введите дату (гггг-мм-дд): ");
                    LocalDate averageDate = LocalDate.parse(scanner.nextLine());
                    showAverageOrderAmountByDate(averageDate);
                    break;
                case 17:
                    System.out.print("Введите дату (гггг-мм-дд): ");
                    LocalDate maxDate = LocalDate.parse(scanner.nextLine());
                    showMaxOrderAmountByDate(maxDate);
                    break;
                case 18:
                    System.out.print("Введите дату (гггг-мм-дд): ");
                    LocalDate customerMaxDate = LocalDate.parse(scanner.nextLine());
                    showCustomerWithMaxOrderAmountByDate(customerMaxDate);
                    break;
                case 19:
                    return;
                default:
                    System.out.println("Неверный ввод, попробуйте еще раз.");
                    break;
            }
        }
    }

    @Override
    public void showAll() {
        List<Order> orders = ordersDao.findAll();
        if (orders.isEmpty()) {
            System.out.println("Список заказов пуст.");
        } else {
            for (Order order : orders) {
                System.out.println("Заказ ID: " + order.getOrderId() + ", Клиент ID: " + order.getClientId() + ", Сотрудник ID: " + order.getStaffId() + ", Дата заказа: " + order.getOrderDate() + ", Общая стоимость: " + order.getTotalPrice());
            }
        }
    }

    @Override
    public void addNew() {
        System.out.println("Добавление нового заказа:");
        System.out.print("Введите ID клиента: ");
        int clientId = scanner.nextInt();
        System.out.print("Введите ID сотрудника: ");
        int staffId = scanner.nextInt();
        System.out.print("Введите общую стоимость заказа: ");
        BigDecimal totalPrice = scanner.nextBigDecimal();

        LocalDate orderDate = LocalDate.now();

        Order order = new Order(0, clientId, staffId, orderDate, totalPrice);
        ordersDao.save(order);
        System.out.println("Новый заказ успешно добавлен.");
    }

    @Override
    public void update() {
        System.out.print("Введите ID заказа, который хотите обновить: ");
        int orderId = scanner.nextInt();
        System.out.print("Введите новый ID клиента: ");
        int clientId = scanner.nextInt();
        System.out.print("Введите новый ID сотрудника: ");
        int staffId = scanner.nextInt();
        System.out.print("Введите новую общую стоимость заказа: ");
        BigDecimal totalPrice = scanner.nextBigDecimal();

        Order order = new Order(orderId, clientId, staffId, LocalDate.now(), totalPrice);
        System.out.println("Заказ успешно обновлен.");
    }


    @Override
    public void delete() {
        System.out.print("Введите ID заказа, который хотите удалить: ");
        int orderId = scanner.nextInt();
        ordersDao.delete(orderId);
        System.out.println("Заказ успешно удален.");
    }

    public void addNewCoffeeOrder() {
        System.out.println("Добавление нового заказа кофе:");
        System.out.print("Введите ID клиента: ");
        int clientId = scanner.nextInt();
        System.out.print("Введите ID сотрудника: ");
        int staffId = scanner.nextInt();
        System.out.print("Введите общую стоимость заказа: ");
        BigDecimal totalPrice = scanner.nextBigDecimal();

        LocalDate orderDate = LocalDate.now();

        Order order = new Order(0, clientId, staffId, orderDate, totalPrice);
        ordersDao.saveCoffeeOrder(order);
        System.out.println("Новый заказ кофе успешно добавлен.");
    }

    public void addNewDessertOrder() {
        System.out.println("Добавление нового заказа десерта:");
        System.out.print("Введите ID клиента: ");
        int clientId = scanner.nextInt();
        System.out.print("Введите ID сотрудника: ");
        int staffId = scanner.nextInt();
        System.out.print("Введите общую стоимость заказа: ");
        BigDecimal totalPrice = scanner.nextBigDecimal();

        LocalDate orderDate = LocalDate.now();

        Order order = new Order(0, clientId, staffId, orderDate, totalPrice);
        ordersDao.saveDessertOrder(order);
        System.out.println("Новый заказ десерта успешно добавлен.");
    }

    private void deleteOrdersByDessert() {
        System.out.print("Введите ID десерта, заказы которого вы хотите удалить: ");
        int dessertId = scanner.nextInt();

        ordersDao.deleteOrdersByDessertId(dessertId);
        System.out.println("Заказы конкретного десерта успешно удалены.");
    }

    public void showAllDessertOrders(int dessertId) {
        List<Order> dessertOrders = ordersDao.findByDessertId(dessertId);
        if (dessertOrders.isEmpty()) {
            System.out.println("Для данного десерта нет заказов.");
        } else {
            System.out.println("Заказы для десерта с ID " + dessertId + ":");
            for (Order order : dessertOrders) {
                System.out.println("Заказ ID: " + order.getOrderId() + ", Клиент ID: " + order.getClientId() + ", Сотрудник ID: " + order.getStaffId() + ", Дата заказа: " + order.getOrderDate() + ", Общая стоимость: " + order.getTotalPrice());
            }
        }
    }

    public void showAllOrdersOfStaff() {
        System.out.print("Введите ID официанта, чьи заказы вы хотите увидеть: ");
        int staffId = scanner.nextInt();
        scanner.nextLine();

        List<Order> orders = ordersDao.findAllOrdersOfStaff(staffId);
        if (orders.isEmpty()) {
            System.out.println("Этот официант еще не принимал заказы.");
        } else {
            System.out.println("Заказы официанта с ID " + staffId + ":");
            for (Order order : orders) {
                System.out.println("Заказ ID: " + order.getOrderId() + ", Дата заказа: " + order.getOrderDate() + ", Общая стоимость: " + order.getTotalPrice());
            }
        }
    }

    public void showAllOrdersOfClient() {
        System.out.print("Введите ID клиента, чьи заказы вы хотите увидеть: ");
        int clientId = scanner.nextInt();
        scanner.nextLine();

        List<Order> orders = ordersDao.findAllOrdersOfClient(clientId);
        if (orders.isEmpty()) {
            System.out.println("У этого клиента еще нету заказов");
        } else {
            System.out.println("Заказы клиента с ID " + clientId + ":");
            for (Order order : orders) {
                System.out.println("Заказ ID: " + order.getOrderId() + ", Дата заказа: " + order.getOrderDate() + ", Общая стоимость: " + order.getTotalPrice());
            }
        }
    }

    public void showOrdersByDate() {
        System.out.print("Введите дату (гггг-мм-дд): ");
        String dateString = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateString);
        List<Order> orders = ordersDao.findOrdersByDate(date);
        if (orders.isEmpty()) {
            System.out.println("Нет заказов на указанную дату.");
        } else {
            System.out.println("Заказы на " + date + ":");
            for (Order order : orders) {
                System.out.println(order);
            }
        }
    }

    public void showOrdersByDateRange() {
        System.out.print("Введите начальную дату (гггг-мм-дд): ");
        String startDateString = scanner.nextLine();
        LocalDate startDate = LocalDate.parse(startDateString);
        System.out.print("Введите конечную дату (гггг-мм-дд): ");
        String endDateString = scanner.nextLine();
        LocalDate endDate = LocalDate.parse(endDateString);
        List<Order> orders = ordersDao.findOrdersByDateRange(startDate, endDate);
        if (orders.isEmpty()) {
            System.out.println("Нет заказов в указанном диапазоне дат.");
        } else {
            System.out.println("Заказы за период с " + startDate + " по " + endDate + ":");
            for (Order order : orders) {
                System.out.println(order);
            }
        }
    }

    public void showDessertOrdersByDate() {
        System.out.print("Введите дату (гггг-мм-дд): ");
        String dateString = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateString);
        int count = ordersDao.countDessertOrdersByDate(date);
        System.out.println("Количество заказов десертов на " + date + ": " + count);
    }

    public void showCoffeeOrdersByDate() {
        System.out.print("Введите дату (гггг-мм-дд): ");
        String dateString = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateString);
        int count = ordersDao.countCoffeeOrdersByDate(date);
        System.out.println("Количество заказов кофе на " + date + ": " + count);
    }

    public void showCustomersWithDrinkOrdersToday() {
        List<Order> drinkOrdersToday = ordersDao.findOrdersWithDrinksToday();
        if (drinkOrdersToday.isEmpty()) {
            System.out.println("На сегодня нет заказов напитков.");
        } else {
            System.out.println("Информация о клиентах, заказавших напитки сегодня:");
            for (Order order : drinkOrdersToday) {
                System.out.println("Клиент ID: " + order.getClientId() + ", Бариста ID: " + order.getStaffId());
            }
        }
    }

    public void showAverageOrderAmountByDate(LocalDate date) {
        BigDecimal averageAmount = ordersDao.findAverageOrderAmountByDate(date);
        System.out.println("Средняя сумма заказа на " + date + ": " + averageAmount);
    }

    public void showMaxOrderAmountByDate(LocalDate date) {
        BigDecimal maxAmount = ordersDao.findMaxOrderAmountByDate(date);
        System.out.println("Максимальная сумма заказа на " + date + ": " + maxAmount);
    }

    public void showCustomerWithMaxOrderAmountByDate(LocalDate date) {
        Order customerOrder = ordersDao.findCustomerWithMaxOrderAmountByDate(date);
        if (customerOrder != null) {
            System.out.println("Клиент, совершивший максимальную сумму заказа на " + date + ":");
            System.out.println("Клиент ID: " + customerOrder.getClientId() + ", Сумма заказа: " + customerOrder.getTotalPrice());
        } else {
            System.out.println("На " + date + " не было заказов.");
        }
    }
}