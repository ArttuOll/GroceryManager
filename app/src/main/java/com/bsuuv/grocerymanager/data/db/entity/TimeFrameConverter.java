package com.bsuuv.grocerymanager.data.db.entity;

import androidx.room.TypeConverter;
import com.bsuuv.grocerymanager.data.db.FoodItemRoomDatabase;
import com.bsuuv.grocerymanager.util.TimeFrame;

/**
 * Helps {@link FoodItemRoomDatabase} to convert the enum {@link TimeFrame} to integers that can be
 * persisted in the database.
 */
public class TimeFrameConverter {

  @TypeConverter
  public static TimeFrame toTimeFrame(int timeFrame) {
    if (timeFrame == TimeFrame.WEEK.value()) {
      return TimeFrame.WEEK;
    } else if (timeFrame == TimeFrame.TWO_WEEKS.value()) {
      return TimeFrame.TWO_WEEKS;
    } else if (timeFrame == TimeFrame.MONTH.value()) {
      return TimeFrame.MONTH;
    } else {
      throw new IllegalArgumentException(String.format("Integer %s couldn't be converted " +
          "to TimeFrame", timeFrame));
    }
  }

  @TypeConverter
  public static int toInteger(TimeFrame timeFrame) {
    return timeFrame.value();
  }
}
