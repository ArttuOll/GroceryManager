package com.bsuuv.grocerymanager.ui.util;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Comparator for sorting weekdays. This class uses Monday as the first day of the week.
 */
public class WeekdaySorter {

  private static Comparator<String> weekdayComparator = (weekday1, weekday2) -> {
    int day1 = convertWeekDayToInt(weekday1);
    int day2 = convertWeekDayToInt(weekday2);
    return day1 - day2;
  };

  private static int convertWeekDayToInt(String weekday) {
    Date date = parseWeekdayStringToDate(weekday);
    return convertDateToInt(date);
  }

  private static Date parseWeekdayStringToDate(String weekday) {
    @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("E");
    try {
      return format.parse(weekday);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static int convertDateToInt(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setFirstDayOfWeek(Calendar.MONDAY);
    cal.setTime(Objects.requireNonNull(date));
    int weekday = cal.get(Calendar.DAY_OF_WEEK);
    // Make Sunday the last day of the week by returning 8 instead of 1
    return weekday == Calendar.SUNDAY ? 8 : weekday;
  }

  /**
   * Sorts a set of weekdays, using Monday as the beginning of the week. Since <code>Set</code>
   * doesn't hold any order of its items, this method turns the set into a <code>List</code>.
   *
   * @param unsortedWeekdays Set of weekdays to sort
   * @return List of sorted weekdays
   */
  public static List<String> getSorted(Set<String> unsortedWeekdays) {
    List<String> sortedWeekdays = new ArrayList<>(unsortedWeekdays);
    Collections.sort(sortedWeekdays, weekdayComparator);
    return sortedWeekdays;
  }
}
