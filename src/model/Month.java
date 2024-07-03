package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class Month implements Writable {
    private ArrayList<Day> days;
    private String month;
    private ArrayList<String> goodBool;
    private ArrayList<String> badBool;
    private ArrayList<String> cusData;
    private int td;

    public Month() {
        instantiateMonth();
        goodBool = new ArrayList<>();
        badBool = new ArrayList<>();
        cusData = new ArrayList<>();
        ArrayList<String> today = parseTime();
        td = Integer.parseInt(today.get(2));
    }

    public Month(String m, ArrayList<Day> d, ArrayList<String> g, ArrayList<String> b, ArrayList<String> c) {
        if (!Objects.equals(m, getCurrMonth())) {
            instantiateMonth();
            setAllBoxes(g, b, c);
            ArrayList<String> today = parseTime();
            td = Integer.parseInt(today.get(2));
        }  else {
            month = m;
            days = d;
            goodBool = g;
            badBool = b;
            cusData = c;
            ArrayList<String> today = parseTime();
            td = Integer.parseInt(today.get(2));
        }
    }

    public void enterDay(String entry, ArrayList<Boolean> gB, ArrayList<Boolean> bB, ArrayList<String> cD, int day) {
        days.get(day).enterData(0, true, entry);
        int x = 0;
        for (Boolean b : gB) {
            days.get(day).check(x, true, b);
            x++;
        }
        x = 0;
        for (Boolean b : bB) {
            days.get(day).check(x, false, b);
            x++;
        }
        x = 0;
        for (String d : cD) {
            days.get(day).enterData(x, false, d);
            x++;
        }
    }

    public ArrayList<String> getGoodBool() {
        return goodBool;
    }

    public ArrayList<String> getBadBool() {
        return badBool;
    }

    public ArrayList<String> getCusData() {
        return cusData;
    }

    public String getMonth() {
        return month;
    }

    public String getCurrTime() {
        Calendar calendar = Calendar.getInstance();
        String get = String.valueOf(calendar.getTime());
        return get;
    }

    public String getCurrMonth() {
        ArrayList<String> today = parseTime();
        return today.get(1);
    }

    public Integer getTodayDate() {
        return td;
    }

    public Day getSpDay(int day) {
        return days.get(day);
    }

    public void setAllBoxes(ArrayList<String> g, ArrayList<String> b, ArrayList<String> c) {
        goodBool = g;
        badBool = b;
        cusData = c;
        for (Day day : days) {
            day.setBoxes(g.size(), b.size(), c.size());
        }
    }

    public Integer getMonthSize() {
        return days.size();
    }

    //gui tool
    public void fieldSaveAll() {
        for (Day day : days) {
            day.fieldSave();
        }
    }
    //

    private void instantiateMonth() {
        ArrayList<String> dataStrings = parseTime();
        int max = amt(dataStrings.get(1), Integer.parseInt(dataStrings.get(5)));
        days = new ArrayList<>(max);
        int weekDate = startWeekday(Integer.parseInt(dataStrings.get(2)), dataStrings.get(0));
        month = dataStrings.get(1);

        for (int x = 1; x <= max; x++) {
            days.add(x - 1, new Day(x, weekDate));
            weekDate++;
            if (weekDate >= 7) {
                weekDate = 0;
            }
        }
    }

    private int startWeekday(int currD, String weekday) {
        int wd;
        switch(weekday) {
            case "Sun":
                wd = 0;
                break;
            case "Mon":
                wd = 1;
                break;
            case "Tue":
                wd = 2;
                break;
            case "Wed":
                wd = 3;
                break;
            case "Thu":
                wd = 4;
                break;
            case "Fri":
                wd = 5;
                break;
            case "Sat":
                wd = 6;
                break;
            default:
                wd = -1;
        }

        for (int x = currD; x != 1; x--) {
            wd--;
            if (wd == -1) {
                wd = 6;
            }
        }

        return wd;
    }

    private ArrayList<String> parseTime() {
        Calendar calendar = Calendar.getInstance();
        String get = String.valueOf(calendar.getTime());
        ArrayList<String> strings = new ArrayList<>(6);
        int idx = 0;
        StringBuilder d = new StringBuilder();
        for (int x = 0; x < get.length(); x++) {
            if (get.charAt(x) == ' ') {
                strings.add(idx, d.toString());
                idx++;
                d = new StringBuilder();
            } else {
                d.append(get.charAt(x));
            }
        }
        strings.add(5, d.toString());
        return strings;
    }

    private int amt(String month, int year) {
        switch(month) {
            case "Jan":
            case "May":
            case "Mar":
            case "Jul":
            case "Sep":
            case "Nov":
                return 31;
            case "Feb":
                if (year % 4 == 0) {
                    return 29;
                } else {
                    return 28;
                }
            case "Apr":
            case "Dec":
            case "Jun":
            case "Aug":
            case "Oct":
                return 30;
            default:
                return -1;
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("currMonth", month);
        json.put("days", daysToJson());
        for (int x = 0; x < goodBool.size(); x++) {
            json.put("gB " + x, goodBool.get(x));
        }
        for (int x = 0; x < badBool.size(); x++) {
            json.put("bB " + x, badBool.get(x));
        }
        for (int x = 0; x < cusData.size(); x++) {
            json.put("cD " + x, cusData.get(x));
        }

        return json;
    }

    private JSONArray daysToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Day day : days) {
            jsonArray.put(day.toJson());
        }
        return jsonArray;
    }
}
