package org.example.menu;

import org.example.scheduleDAO.ScheduleDaoImpl;
import org.example.model.Schedule;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import java.util.Scanner;
public class ScheduleManager implements Manager {
    private ScheduleDaoImpl scheduleDao;
    private Scanner scanner;

    public ScheduleManager(ScheduleDaoImpl scheduleDao) {
        this.scheduleDao = scheduleDao;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void manage() {
        while (true) {
            System.out.println("\nУправление расписанием:");
            System.out.println("1. Показать всё расписание");
            System.out.println("2. Показать расписание работы конкретного бариста на неделю");
            System.out.println("3. Показать расписание работы всех баристов на неделю");
            System.out.println("4. Показать расписание работы для всех работников кафе на неделю");
            System.out.println("5. Добавить запись в расписание");
            System.out.println("6. Добавить запись в расписание на ближайший понедельник");
            System.out.println("7. Изменить запись в расписание");
            System.out.println("8. Изменить запись в расписание на ближаший вторник");
            System.out.println("9. Удалить запись из расписания");
            System.out.println("10. Удалить расписание работы на конкретный промежуток между указанными датами");
            System.out.println("11. Вернуться в предыдущее меню");
            System.out.print("Ваш выбор: ");

            int action = scanner.nextInt();
            scanner.nextLine();

            switch (action) {
                case 1:
                    showAll();
                    break;
                case 2:
                    System.out.print("Введите ID бариста: ");
                    int baristaId = scanner.nextInt();
                    showBaristaScheduleForWeek(baristaId);
                    break;
                case 3:
                    showAllBaristasScheduleForWeek();
                    break;
                case 4:
                    showAllStaffScheduleForWeek();
                    break;
                case 5:
                    addNew();
                    break;
                case 6:
                    addNextMonday();
                    break;
                case 7:
                    update();
                    break;
                case 8:
                    updateScheduleForNextTuesday();
                    break;
                case 9:
                    delete();
                    break;
                case 10:
                    deleteByDateRange();
                    break;
                case 11:
                    return;
                default:
                    System.out.println("Неверный ввод, попробуйте еще раз.");
                    break;
            }
        }
    }


    @Override
    public void showAll() {
        List<Schedule> scheduleList = scheduleDao.findAll();
        if (scheduleList.isEmpty()) {
            System.out.println("Расписание пусто.");
        } else {
            for (Schedule schedule : scheduleList) {
                System.out.println("ID: " + schedule.getScheduleId() + ", ID Сотрудника: " + schedule.getStaffId() + ", Дата: " + schedule.getWorkDate() + ", Начало: " + schedule.getStartTime() + ", Конец: " + schedule.getEndTime());
            }
        }
    }

    @Override
    public void addNew() {
        System.out.println("Добавление новой записи в расписание:");
        System.out.print("Введите ID сотрудника: ");
        int staffId = scanner.nextInt();
        System.out.print("Введите дату работы (ГГГГ-ММ-ДД): ");
        String workDate = scanner.next();
        System.out.print("Введите время начала (ЧЧ:ММ): ");
        String startTime = scanner.next();
        System.out.print("Введите время окончания (ЧЧ:ММ): ");
        String endTime = scanner.next();

        Schedule schedule = new Schedule(0, staffId, LocalDate.parse(workDate), LocalTime.parse(startTime), LocalTime.parse(endTime));
        scheduleDao.save(schedule);
        System.out.println("Запись в расписание успешно добавлена.");
    }

    @Override
    public void update() {
        System.out.print("Введите ID записи в расписании, которую хотите обновить: ");
        int scheduleId = scanner.nextInt();
        System.out.print("Введите новый ID сотрудника: ");
        int staffId = scanner.nextInt();
        System.out.print("Введите новую дату работы (ГГГГ-ММ-ДД): ");
        String workDate = scanner.next();
        System.out.print("Введите новое время начала (ЧЧ:ММ): ");
        String startTime = scanner.next();
        System.out.print("Введите новое время окончания (ЧЧ:ММ): ");
        String endTime = scanner.next();

        Schedule schedule = new Schedule(scheduleId, staffId, LocalDate.parse(workDate), LocalTime.parse(startTime), LocalTime.parse(endTime));
        scheduleDao.update(schedule);
        System.out.println("Запись в расписание успешно обновлена.");
    }

    @Override
    public void delete() {
        System.out.print("Введите ID записи в расписании, которую хотите удалить: ");
        int scheduleId = scanner.nextInt();

        scheduleDao.delete(scheduleId);
        System.out.println("Запись в расписание успешно удалена.");
    }

    public void addNextMonday() {
        LocalDate nextMonday = LocalDate.now().with(DayOfWeek.MONDAY);
        System.out.println("Добавление новой записи в расписание на ближайший понедельник (" + nextMonday + "):");
        System.out.print("Введите ID сотрудника: ");
        int staffId = scanner.nextInt();
        System.out.print("Введите время начала (ЧЧ:ММ): ");
        String startTime = scanner.next();
        System.out.print("Введите время окончания (ЧЧ:ММ): ");
        String endTime = scanner.next();

        Schedule schedule = new Schedule(0, staffId, nextMonday, LocalTime.parse(startTime), LocalTime.parse(endTime));
        scheduleDao.saveNextMondaySchedule(schedule);
        System.out.println("Запись в расписание на ближайший понедельник успешно добавлена.");
    }

    public void updateScheduleForNextTuesday() {
        System.out.print("Введите новое время начала работы на ближайший вторник (ЧЧ:ММ): ");
        String startTime = scanner.next();
        System.out.print("Введите новое время окончания работы на ближайший вторник (ЧЧ:ММ): ");
        String endTime = scanner.next();

        scheduleDao.updateScheduleForNextTuesday(startTime, endTime);
        System.out.println("Расписание на ближайший вторник успешно обновлено.");
    }

    public void deleteByDateRange() {
        System.out.print("Введите начальную дату в формате ГГГГ-ММ-ДД: ");
        String startDateStr = scanner.next();
        System.out.print("Введите конечную дату в формате ГГГГ-ММ-ДД: ");
        String endDateStr = scanner.next();

        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        scheduleDao.deleteByDateRange(startDate, endDate);
        System.out.println("Расписание работы на указанный промежуток дат успешно удалено.");
    }

    public void showBaristaScheduleForWeek(int baristaId) {
        LocalDate today = LocalDate.now();
        LocalDate mondayOfWeek = today.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate sundayOfWeek = today.with(java.time.temporal.TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));

        List<Schedule> baristaSchedule = scheduleDao.findScheduleForEmployee(baristaId, mondayOfWeek, sundayOfWeek);

        if (baristaSchedule.isEmpty()) {
            System.out.println("Расписание работы для указанного бариста на эту неделю отсутствует.");
        } else {
            System.out.println("Расписание работы для бариста " + baristaId + " на эту неделю:");
            for (Schedule schedule : baristaSchedule) {
                System.out.println("Дата: " + schedule.getWorkDate() + ", Начало: " + schedule.getStartTime() + ", Конец: " + schedule.getEndTime());
            }
        }
    }

    public void showAllBaristasScheduleForWeek() {
        LocalDate today = LocalDate.now();
        LocalDate mondayOfWeek = today.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate sundayOfWeek = today.with(java.time.temporal.TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));

        List<Schedule> allBaristasSchedule = scheduleDao.findScheduleForAllBaristas(mondayOfWeek, sundayOfWeek);

        if (allBaristasSchedule.isEmpty()) {
            System.out.println("Расписание работы для всех баристов на эту неделю отсутствует.");
        } else {
            System.out.println("Расписание работы для всех баристов на эту неделю:");
            for (Schedule schedule : allBaristasSchedule) {
                System.out.println("Бариста: " + schedule.getStaffId() + ", Дата: " + schedule.getWorkDate() + ", Начало: " + schedule.getStartTime() + ", Конец: " + schedule.getEndTime());
            }
        }
    }

    public void showAllStaffScheduleForWeek() {
        LocalDate today = LocalDate.now();
        LocalDate mondayOfWeek = today.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate sundayOfWeek = today.with(java.time.temporal.TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));

        List<Schedule> allStaffSchedule = scheduleDao.findScheduleForAllStaff(mondayOfWeek, sundayOfWeek);

        if (allStaffSchedule.isEmpty()) {
            System.out.println("Расписание работы для всех работников кафе на эту неделю отсутствует.");
        } else {
            System.out.println("Расписание работы для всех работников кафе на эту неделю:");
            for (Schedule schedule : allStaffSchedule) {
                System.out.println("ID: " + schedule.getScheduleId() + ", Сотрудник: " + schedule.getStaffId() + ", Дата: " + schedule.getWorkDate() + ", Начало: " + schedule.getStartTime() + ", Конец: " + schedule.getEndTime());
            }
        }
    }
}