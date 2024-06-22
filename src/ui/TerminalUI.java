package ui;

import model.Day;
import model.Month;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class TerminalUI {
    private static final String JSON_STORE = "./data/saveFile.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private Month month;

    public TerminalUI() throws FileNotFoundException {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        Scanner myObj = new Scanner(System.in);
        System.out.println("Load previous save? (Y/N)");
        String inp = myObj.nextLine();
        if (Objects.equals(inp, "Y")) {
            loadMonth();
            printCalendar();
            enterEntry();
            printCalendar();
        } else {
            setUpCalendar();
            printCalendar();
            enterEntry();
            printCalendar();
            saveFile();
        }
    }

    private void loadMonth() {
        try {
            month = jsonReader.read();
            System.out.println("Loaded");
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    public void saveFile() {
        try {
            jsonWriter.open();
            jsonWriter.write(month);
            jsonWriter.close();
            System.out.println("Saved");
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    public void setUpCalendar() {
        month = new Month();
        Scanner myObj = new Scanner(System.in);
        ArrayList<String> g = new ArrayList<>();
        ArrayList<String> b = new ArrayList<>();
        ArrayList<String> c = new ArrayList<>();

        System.out.println("Welcome to your habit tracker. Let's start by " +
                "adding positive habits that you'd like to keep track of and affirm.");
        System.out.println("Enter a habit (Type X to finish): ");
        String habit = myObj.nextLine();

        while (!Objects.equals(habit, "X")) {
            g.add(habit);
            habit = myObj.nextLine();
        }

        System.out.println("Now, let's continue with your bad habits.");
        System.out.println("Enter a habit (Type X to finish): ");
        habit = myObj.nextLine();

        while (!Objects.equals(habit, "X")) {
            b.add(habit);
            habit = myObj.nextLine();
        }

        System.out.println("And finally, statistics of certain habits.");
        System.out.println("Enter a stat (Type X to finish): ");
        habit = myObj.nextLine();

        while (!Objects.equals(habit, "X")) {
            c.add(habit);
            habit = myObj.nextLine();
        }

        month.setAllBoxes(g, b, c);
    }

    public void printCalendar() {
        System.out.println(month.getMonth());

        for (int x = 0; x < month.getMonthSize(); x++) {
            Day day = month.getSpDay(x);
            System.out.println(" ");
            if (day.getDate() == month.getTodayDate()) {
                System.out.print("- - -");
                System.out.println(" ");
            }
            System.out.print(day.getDate() + " [" + day.getWeekDate() + "]: " + day.getEntry() + " ");
            for (int y = 0; y < month.getGoodBool().size(); y++) {
                String check = " ";
                if (day.getBox(true, y)) {
                    check = "X";
                }
                System.out.print("[" + check + "]");
            }
            System.out.print(" | ");

            for (int y = 0; y < month.getBadBool().size(); y++) {
                String check = " ";
                if (day.getBox(false, y)) {
                    check = "X";
                }
                System.out.print("[" + check + "]");
            }
            System.out.print(" | ");

            for (int y = 0; y < month.getCusData().size(); y++) {
                System.out.print("[" + day.getData(y)+ "]");
            }

            System.out.println(" ");
            if (day.getDate() == month.getTodayDate()) {
                System.out.print("- - -");
                System.out.println(" ");
            }

        }
    }

    public void enterEntry() {
        System.out.println("It is " + month.getCurrTime());
        Scanner myObj = new Scanner(System.in);
        System.out.println("How do you feel today? ");
        String note = myObj.nextLine();
        String entry;
        System.out.println("Now let's check your good habits. Type X to mark yes, and any other key for no.");
        ArrayList<Boolean> g = new ArrayList<>();
        ArrayList<Boolean> b = new ArrayList<>();
        ArrayList<String> c = new ArrayList<>();
        int score = 0;
        for (int x = 0; x < month.getGoodBool().size(); x++) {
            System.out.println("Box " + "[" + (x + 1) + "]: " + month.getGoodBool().get(x));
            entry = myObj.nextLine();
            if (Objects.equals(entry, "X")) {
                g.add(true);
                score++;
            } else {
                g.add(false);
            }
        }
        if (score == month.getGoodBool().size()) {
            System.out.println("Awesome job! Keep at it.");
            score = 0;
        }
        System.out.println("Now your bad habits. Type X to mark yes, and any other key for no.");
        for (int x = 0; x < month.getBadBool().size(); x++) {
            System.out.println("Box " + "[" + (x + 1) + "]: " + month.getBadBool().get(x));
            entry = myObj.nextLine();
            if (Objects.equals(entry, "X")) {
                b.add(true);
                score++;
            } else {
                b.add(false);
            }
        }
        if (score == 0) {
            System.out.println("Great work, keep it up.");
        }
        System.out.println("And finally any statistics.");
        for (int x = 0; x < month.getCusData().size(); x++) {
            System.out.println("Stat " + "[" + (x + 1) + "]: " + month.getCusData().get(x));
            entry = myObj.nextLine();
            c.add(entry);
        }
        month.enterDay(note, g, b, c, month.getTodayDate() - 1);
    }

}
