package com.bsuuv.grocerymanager.util;

import android.content.Context;

import com.bsuuv.grocerymanager.R;

public class PluralsProvider {

    private Context mContext;

    public PluralsProvider(Context context) {
        this.mContext = context;
    }

    public String getScheduleString(int frequency, int timeFrame) {
        switch (timeFrame) {
            case 1:
                return mContext.getResources().getQuantityString(
                        R.plurals.times_a_week, frequency, frequency);
            case 2:
                return mContext.getResources().getQuantityString(
                        R.plurals.times_in_two_weeks, frequency, frequency);
            case 4:
                return mContext.getResources().getQuantityString(
                        R.plurals.times_in_a_month, frequency, frequency);
            default:
                return "";
        }
    }

    public String getAmountString(int amount, String unit) {
        switch (unit) {
            case "Pieces":
                return mContext.getResources().getQuantityString(R.plurals.Pieces, amount,
                        amount);
            case "Packets":
                return mContext.getResources().getQuantityString(R.plurals.Packets, amount,
                        amount);
            case "Cans":
                return mContext.getResources().getQuantityString(R.plurals.Cans, amount,
                        amount);
            case "Bags":
                return mContext.getResources().getQuantityString(R.plurals.Bags, amount,
                        amount);
            default:
                return "";
        }
    }
}
