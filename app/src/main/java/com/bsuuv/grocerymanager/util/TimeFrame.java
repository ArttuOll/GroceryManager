package com.bsuuv.grocerymanager.util;

public enum TimeFrame {
    WEEK(1), TWO_WEEKS(2), MONTH(4), NULL(-2);

    private final int mValue;

    TimeFrame(int value) {
        this.mValue = value;
    }

    public int value() {
        return mValue;
    }

    public TimeFrame parse(int value) {
        for (TimeFrame tf : TimeFrame.values()) {
            if (tf.value() == value) return tf;
        }
        throw new IllegalArgumentException("Given value does not match any time frame options!");
    }
}
