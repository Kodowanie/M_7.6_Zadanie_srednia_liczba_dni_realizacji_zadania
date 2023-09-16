package porfolio;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import stream.portfolio.Board;
import stream.portfolio.Task;
import stream.portfolio.TaskList;
import stream.portfolio.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BoarTestSuiteTask {

    private Board prepareTestData(){
        //users
        User user1 = new User("developer1", "John Smith");
        User user2 = new User("projectmanager1", "Nina White");
        User user3 = new User("developer2", "Emilia Stephanson");
        User user4 = new User("developer3", "Konrad Bridge");

        //tasks
        Task task1 = new Task("Microservice for taking temperature",
                "Write and test the microservice taking\n" +
                        "the temperaure from external service",
                user1,
                user2,
                LocalDate.now().minusDays(20),
                LocalDate.now().plusDays(30));
        Task task2 = new Task("HQLs for analysis",
                "Prepare some HQL queries for analysis",
                user1,
                user2,
                LocalDate.now().minusDays(20),
                LocalDate.now().minusDays(5));
        Task task3 = new Task("Temperatures entity",
                "Prepare entity for temperatures",
                user3,
                user2,
                LocalDate.now().minusDays(20),
                LocalDate.now().plusDays(15));
        Task task4 = new Task("Own logger",
                "Refactor company logger to meet our needs",
                user3,
                user2,
                LocalDate.now().minusDays(10),
                LocalDate.now().plusDays(25));
        Task task5 = new Task("Optimize searching",
                "Archive data searching has to be optimized",
                user4,
                user2,
                LocalDate.now(),
                LocalDate.now().plusDays(5));
        Task task6 = new Task("Use Streams",
                "use Streams rather than for-loops in predictions",
                user4,
                user2,
                LocalDate.now().minusDays(15),
                LocalDate.now().minusDays(2));

        //taskLists
        TaskList taskListToDo = new TaskList("To do");
        taskListToDo.addTask(task1);
        taskListToDo.addTask(task3);
        TaskList taskListInProgress = new TaskList("In progress");
        taskListInProgress.addTask(task5);
        taskListInProgress.addTask(task4);
        taskListInProgress.addTask(task2);
        TaskList taskListDone = new TaskList("Done");
        taskListDone.addTask(task6);

        //board
        Board project = new Board("Project Weather Prediction");
        project.addTaskList(taskListToDo);
        project.addTaskList(taskListInProgress);
        project.addTaskList(taskListDone);

        return project;
    }

    @Test
    void testAddTaskListAverageWorkingOnTask(){
        //Given
        Board project = prepareTestData();

        //When
        List<TaskList> taskInProgress = new ArrayList<>();
        taskInProgress.add(new TaskList("In progress"));
        //in progress tasks: task5, task4, task2
        //task5: -20 days
        //task4: -10 days
        //task2: 0 days
        //average: 10 dni
        List list = project.getTaskLists().stream()
                .filter(tl -> taskInProgress.contains(tl))
                .flatMap(tl -> tl.getTasks().stream())
                .map(t -> t.getCreated())
                .collect(Collectors.toList());

        System.out.println("Print list of task creation date: " + list);

        double averageNumberOfDayPerTasks = project.getTaskLists().stream()
                .filter(tl -> taskInProgress.contains(tl))
                .flatMap(tl -> tl.getTasks().stream())
                //LocalData: created - deadline
                .map(t -> Period.between(t.getCreated(),LocalDate.now()))
                .mapToLong(Period::getDays)
                .average().getAsDouble();

        System.out.println("Calculate sum of days from creation has passed: " +
                averageNumberOfDayPerTasks);


        long numbersOfTasks = project.getTaskLists().stream()
                .filter(tl -> taskInProgress.contains(tl))
                .flatMap(tl -> tl.getTasks().stream())
                .count();

        System.out.println("Calculate the numbers of Tasks: " + numbersOfTasks);


        //Then

        Assertions.assertEquals(10,averageNumberOfDayPerTasks);



    }


}
