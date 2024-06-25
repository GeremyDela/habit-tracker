package ui;

import model.Month;
import org.json.JSONException;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class GUI {
    private static final String JSON_STORE = "./data/saveFile.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private final JPanel mainPanel = new JPanel();
    private final CardLayout panelCards = new CardLayout();
    private List<JPanel> panels = new ArrayList<>();
    private final JFrame frame = new JFrame("Habit Tracker");

    Month month;

    public GUI() throws FileNotFoundException {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        setUpCards();
    }

    public void setUpCards() {
        panels = new ArrayList<>();

        mainPanel.setLayout(panelCards);

        for (int x = 0; x < 3; x++) {
            panels.add(new JPanel());
        }

        mainMenu(panels.get(0));
        setUpCalendar(panels.get(1));
        calendar(panels.get(2));

        JScrollPane scrollPane = new JScrollPane(panels.get(2));

        mainPanel.add(panels.get(0), "main");
        mainPanel.add(panels.get(1), "setup");
        mainPanel.add(scrollPane, "calendar");

        panelCards.show(mainPanel, "main");

        frame.add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void mainMenu(JPanel panel) {
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(1200, 1200));

        JButton overwrite = new JButton("New");
        JButton load = new JButton("Load");

        overwrite.setBounds(500, 640, 200, 20);
        load.setBounds(500, 670, 200, 20);

        panel.add(overwrite);
        panel.add(load);

        overwrite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                panelCards.show(mainPanel, "setup");
            }
        });

        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    loadMonth();
                    printCalendar(panels.get(2));
                    panelCards.show(mainPanel, "calendar");
                } catch (JSONException e) {
                }
            }
        });
    }

    private Stack<Integer> addStack = new Stack<>();

    private void setUpCalendar(JPanel panel) {
        month = new Month();

        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(1200, 1200));

        JTextField input = new JTextField("Enter label");

        JButton addGood = new JButton("Add a good habit");
        JButton addBad = new JButton("Add a bad habit");
        JButton addCus = new JButton("Add a custom statistic");
        JButton undo = new JButton("Undo");
        JButton complete = new JButton("Complete");

        input.setBounds(500, 550, 200, 20);
        addGood.setBounds(500, 600, 200, 20);
        addBad.setBounds(500, 630, 200, 20);
        addCus.setBounds(500, 660, 200, 20);
        undo.setBounds(500, 690, 200, 20);
        complete.setBounds(500, 750, 200, 20);

        panel.add(input);
        panel.add(addGood);
        panel.add(addBad);
        panel.add(addCus);
        panel.add(undo);
        panel.add(complete);

        ArrayList<String> g = new ArrayList<>();
        ArrayList<String> b = new ArrayList<>();
        ArrayList<String> c = new ArrayList<>();

        addGood.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                g.add(input.getText());
                input.setText("");
                addStack.push(0);
            }
        });

        addBad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                b.add(input.getText());
                input.setText("");
                addStack.push(1);
            }
        });

        addCus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                c.add(input.getText());
                input.setText("");
                addStack.push(2);
            }
        });

        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!addStack.isEmpty()) {
                    int temp = addStack.pop();
                    switch (temp) {
                        case 0:
                            g.remove(g.size() - 1);
                            break;
                        case 1:
                            b.remove(b.size() - 1);
                            break;
                        case 2:
                            c.remove(c.size() - 1);
                            break;
                        default:
                            break;
                    }
                }
            }
        });

        complete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                month.setAllBoxes(g, b, c);
                printCalendar(panels.get(2));
                panelCards.show(mainPanel, "calendar");
            }
        });
    }

    private void calendar(JPanel panel) {
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(1200, 1200));
    }

    private void printCalendar(JPanel panel) {
        JLabel temp = new JLabel(month.getMonth());
        temp.setBounds(300, 10, 100, 50);
        panel.add(temp);

        temp = new JLabel(month.getCurrTime());
        temp.setBounds(400, 10, 500, 50);
        panel.add(temp);

        int ded = 850;
        for (int z = 0; z < month.getGoodBool().size(); z++) {
            JLabel label = new JLabel(month.getGoodBool().get(z));
            label.setBounds(ded, 20, 25, 30);
            panel.add(label);
            ded += 30;
        }

        int ded2 = ded + 5;
        ded2 += 10;

        for (int z = 0; z < month.getCusData().size(); z++) {
            JLabel label = new JLabel(month.getCusData().get(z));
            label.setBounds(ded2, 20, 25, 30);
            panel.add(label);
            ded2 += 60;
        }

        ded = 850;
        for (int z = 0; z < month.getBadBool().size(); z++) {
            ded -= 30;
            JLabel label = new JLabel(month.getBadBool().get(z));
            label.setBounds(ded, 20, 25, 30);
            panel.add(label);
        }

        int y = 50;

        for (int x = 0; x < month.getMonthSize(); x++) {
            if (x + 1 == month.getTodayDate()) {
                JLabel point = new JLabel("-> ");
                point.setBounds(175, y, 100, 30);
                panel.add(point);
            }
            JLabel day = new JLabel( month.getSpDay(x).getDate() + " [" + month.getSpDay(x).getWeekDate() + "] ");
            day.setBounds(200, y, 100, 30);
            panel.add(day);
            panel.add(month.getSpDay(x).getJField(300, y, 400, 30));

            if (x < month.getTodayDate()) {
                int h = 850;
                day = new JLabel("|");
                day.setBounds(845, y, 10, 30);
                panel.add(day);

                for (int z = 0; z < month.getGoodBool().size(); z++) {
                    panel.add(month.getSpDay(x).getJBox(h, y, 25, 30, true, z));
                    h += 30;
                }

                int h2 = h + 5;
                day = new JLabel("|");
                day.setBounds(h2, y, 10, 30);
                panel.add(day);
                h2 += 10;

                for (int z = 0; z < month.getCusData().size(); z++) {
                    panel.add(month.getSpDay(x).getDataJField(h2, y, 50, 30, z));
                    h2 += 60;
                }

                panel.add(month.getSpDay(x).getEmotion(h2, y, 25, 30));

                h = 850;
                for (int z = 0; z < month.getBadBool().size(); z++) {
                    h -= 30;
                    panel.add(month.getSpDay(x).getJBox(h, y, 25, 30, false, z));
                }
            }
            y += 25;
        }

        JButton save = new JButton("Save");
        save.setBounds(250, y + 30, 100, 50);
        panel.add(save);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                saveFile();
            }
        });
    }

    private void loadMonth() {
        try {
            Month tempMonth = jsonReader.read();
            if (!Objects.equals(tempMonth.getMonth(), tempMonth.getCurrMonth())) {
                month = new Month();
                month.setAllBoxes(tempMonth.getGoodBool(), tempMonth.getBadBool(), tempMonth.getCusData());
            }
            month = tempMonth;
        } catch (IOException e) {
        }
    }

    public void saveFile() {
        try {
            month.fieldSaveAll();
            jsonWriter.open();
            jsonWriter.write(month);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
        }
    }
}
