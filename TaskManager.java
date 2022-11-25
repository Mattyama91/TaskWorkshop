package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class TaskManager {

    static final String FILE_NAME = "tasks.csv";
    static final String[] OPTIONS = {"add", "remove", "list", "exit"};
    static String[][] tasks;

    public static void main(String[] args) {
        tasks = downloadData(FILE_NAME);

//        System.out.println(tasks.length);
        showOption(OPTIONS);
        Scanner scanner = new Scanner(System.in);
        while(true) {
            String input = scanner.nextLine();
            switch (input) {
                case "exit":
                    uploadData(FILE_NAME, tasks);
                    System.out.println(ConsoleColors.RED + "Bye, bye.");
                    System.exit(0);
                    break;
                case "add":
                    addTask();
                    break;
                case "remove":
                    try {
                        removeTask(tasks, getTheNumber());
                        System.out.println("Value was successfully deleted.");
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Element not exist in tab");
                    }
                    break;
                case "list":
                    printTab(tasks);
                    break;
                default:
                    System.out.println("Please select a correct option.");
            }
            showOption(OPTIONS);
        }
    }

    // Metoda wyświetlająca opcje dostępne w programie
    public static void showOption(String[] tab) {
        System.out.println(ConsoleColors.BLUE);
        System.out.println("Please select an option:" + ConsoleColors.RESET);
        for (int i = 0; i < tab.length; i++) {
            System.out.println(tab[i]);
        }
    }

//    Metoda pobierająca dane z pliku
    public static String[][] downloadData(String fileName) {
        Path path = Paths.get(fileName);

        if (!Files.exists(path)) {
            System.out.println("File not exist.");
            System.exit(0);
        }

        String[][] tab = {};

        try (Scanner scanner = new Scanner(path)) {
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                tab = Arrays.copyOf(tab, tab.length + 1);
                String[] splitArr = line.split(",");
                tab[tab.length-1] = new String[splitArr.length];
                for (int i = 0; i < splitArr.length; i++) {
                    tab[tab.length-1][i] = splitArr[i];
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tab;
    }

//    Listowanie zadań
    public static void printTab(String[][] tab) {
        for (int i = 0; i < tab.length; i++) {
            System.out.print(i + " : ");
            for (int j = 0; j < tab[i].length; j++) {
                System.out.print(tab[i][j] + "  ");
            }
            System.out.println();
        }
    }

//    Dodawanie zadania
    public static void addTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please add task description");
        String description = scanner.nextLine();
        System.out.println("Please add task due date");
        String dueDate = scanner.nextLine();
        System.out.println("Is your task important: true/false?");
        String isImportant = scanner.nextLine();
        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[3];
        tasks[tasks.length - 1][0] = description;
        tasks[tasks.length - 1][1] = dueDate;
        tasks[tasks.length - 1][2] = isImportant;
    }
//    Walidacja wprowadzonego numeru
    public static boolean isNumberGreaterEqualZero(String input) {
        if (NumberUtils.isParsable(input)) {
            return Integer.parseInt(input) >= 0;
        }
        return false;
    }

    public static int getTheNumber() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select number to remove");

        String number = scanner.nextLine();
        while(!isNumberGreaterEqualZero(number)) {
            System.out.println("Incorrect argument passed. Please give number greater or equal 0");
            number = scanner.nextLine();
        }
        return Integer.parseInt(number);
    }

    public static void removeTask(String[][] arr, int index ) {
        if (index > arr.length) {
            throw new ArrayIndexOutOfBoundsException();
        } else {
            tasks = ArrayUtils.remove(arr, index);
        }
    }

    public static void uploadData(String fileName, String[][] arr) {
        File file = new File(fileName);

        try (PrintWriter printWriter = new PrintWriter(file)) {
            for (int i = 0; i < arr.length; i++) {
                printWriter.println(String.join(",", arr[i]));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
