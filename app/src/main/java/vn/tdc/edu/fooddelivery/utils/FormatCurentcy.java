package vn.tdc.edu.fooddelivery.utils;

import java.text.DecimalFormat;

public class FormatCurentcy {
    public static String formatVietnamCurrency(Long value) {
        return new DecimalFormat("#,###.##").format(value);
    }

    public static String format(String value) {
        int count = (value.length()) / 3;
        double flag = ((value.length()) / (3 * 1.0));
        String total = "";
        String temp = "";
        if (flag > 1) {
            for (int i = 1; i <= count; i++) {
                int node = (value.length()) - (i * 3);
                temp = value.substring(node, node + 3);
                if ((i == count) && (value.length() % 3 == 0)) {
                    total = temp + total;
                } else {
                    total = "," + temp + total;
                    if ((i == count) && (value.length() % 3 != 0)) {
                        temp = value.substring(0, value.length() - count * 3);
                        total = temp + total;
                    }
                }
            }
        } else {
            total = value;
        }
        return total;
    }
}
