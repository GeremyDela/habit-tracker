package model;

import org.json.JSONObject;
import persistence.Writable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class Day implements Writable {
    private int day;
    private String weekDate;
    private String entry;
    private ArrayList<Boolean> goodBool;
    private ArrayList<Boolean> badBool;
    private ArrayList<String> cusData;
    private int gMax;
    private int bMax;
    private int cMax;
    private int emotion;

    public Day(int d, int wd) {
        day = d;
        entry = "";
        emotion = 1;
        setWeekDate(wd);
    }

    public Day(int d, String wd, String en, int g, int b, int c, int e) {
        day = d;
        weekDate = wd;
        entry = en;
        emotion = e;
        setBoxes(g, b, c);
    }

    public int getDate() {
        return day;
    }

    public String getWeekDate() {
        return weekDate;
    }

    public String getEntry() {
        return entry;
    }

    public void setBoxes(int g, int b, int c) {
        goodBool = new ArrayList<>(g);
        badBool = new ArrayList<>(b);
        cusData = new ArrayList<>(c);
        for (int x = 0; x < g; x++) {
            goodBool.add(false);
        }
        for (int x = 0; x < b; x++) {
            badBool.add(false);
        }
        for (int x = 0; x < c; x++) {
            cusData.add("  ");
        }
        gMax = g;
        bMax = b;
        cMax = c;
    }

    public Boolean getBox(Boolean good, int idx) {
        if (good) {
            return goodBool.get(idx);
        } else {
            return badBool.get(idx);
        }
    }

    public String getData(int idx) {
        return cusData.get(idx);
    }

    public void enterData(int box, Boolean ent, String d) {
        if (ent) {
            entry = d;
        } else {
            cusData.set(box, d);
        }
    }

    public void check(int box, Boolean whichList, Boolean check) {
        if (whichList) {
            goodBool.set(box, check);
        } else {
            badBool.set(box, check);
        }
    }


    //for gui
    JTextField ent;
    ArrayList<JTextField> dat = new ArrayList<>();
    public JCheckBox getJBox(int x, int y, int w, int h, Boolean good, int idx) {
        JCheckBox g = new JCheckBox();
        g.setBounds(x, y, w, h);
        if (getBox(good, idx)) {
            g.setSelected(true);
        }

        g.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    check(idx, good, true);
                }
            }
        });
        return g;
    }

    public JTextField getJField(int x, int y, int w, int h) {
        ent = new JTextField(getEntry());
        ent.setBounds(x, y, w, h);
        return ent;
    }

    public JTextField getDataJField(int x, int y, int w, int h, int idx) {
        JTextField temp = new JTextField(cusData.get(idx));
        temp.setBounds(x, y, w, h);
        try {
            dat.set(idx, temp);
        } catch (IndexOutOfBoundsException e) {
            dat.add(idx, temp);
        }
        return temp;
    }

    public void fieldSave() {
        try {
            entry = ent.getText();
        } catch (NullPointerException e) {
        }

        for (int x = 0; x < dat.size(); x++) {
            cusData.set(x, dat.get(x).getText());
        }
    }

    public JButton getEmotion(int x, int y, int w, int h) {
        JButton emote = new JButton();
        emote.setBackground(getColors().get(emotion));
        emote.setOpaque(true);
        emote.setBorderPainted(false);
        emote.setBounds(x, y, w, h);
        emote.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                emotion++;
                if (emotion == 3) {
                    emotion = 0;
                }
                emote.setBackground(getColors().get(emotion));
                emote.setOpaque(true);
                emote.setBorderPainted(false);
            }
        });
        return emote;
    }
    //end gui tools

    private void setWeekDate(int wd) {
        switch(wd) {
            case 0:
            case 6:
                weekDate = "S";
                break;
            case 1:
                weekDate = "M";
                break;
            case 2:
            case 4:
                weekDate = "T";
                break;
            case 3:
                weekDate = "W";
                break;
            case 5:
                weekDate = "F";
                break;
            default:
                weekDate = "null";
        }
    }

    private ArrayList<Color> getColors() {
        ArrayList<Color> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.YELLOW);
        colors.add(Color.GREEN);
        return colors;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("weekDate", weekDate);
        json.put("day", day);
        json.put("entry", entry);
        json.put("gBSize", gMax);
        json.put("bBSize", bMax);
        json.put("cDSize", cMax);
        json.put("emotion", emotion);

        for (int x = 0; x < goodBool.size(); x++) {
            if (goodBool.get(x)) {
                json.put("goodBox " + x, "true");
            } else {
                json.put("goodBox " + x, "false");
            }
        }
        for (int x = 0; x < badBool.size(); x++) {
            if (badBool.get(x)) {
                json.put("badBox " + x, "true");
            } else {
                json.put("badBox " + x, "false");
            }
        }
        for (int x = 0; x < cusData.size(); x++) {
            json.put("stat " + x, cusData.get(x));
        }

        return json;
    }
}
