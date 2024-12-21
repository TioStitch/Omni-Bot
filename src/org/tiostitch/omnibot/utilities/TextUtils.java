package org.tiostitch.omnibot.utilities;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class TextUtils {

    private final static TreeMap<Integer, String> romanMap = new TreeMap<Integer, String>() {{
        put(1000, "M");
        put(900, "CM");
        put(500, "D");
        put(400, "CD");
        put(100, "C");
        put(90, "XC");
        put(50, "L");
        put(40, "XL");
        put(10, "X");
        put(9, "IX");
        put(5, "V");
        put(4, "IV");
        put(1, "I");
    }};

    public static String toRoman(int number) {
        if (number <= 0) return "";

        int l = romanMap.floorKey(number);
        if (number == l) {
            return romanMap.get(number);
        }
        return romanMap.get(l) + toRoman(number - l);
    }

    public static int fromRoman(String roman) {
        int result = 0;
        for (int i = 0; i < roman.length(); i++) {
            int s1 = value(roman.charAt(i));
            if (i + 1 < roman.length()) {
                int s2 = value(roman.charAt(i + 1));
                if (s1 >= s2) {
                    result = result + s1;
                } else {
                    result = result + s2 - s1;
                    i++;
                }
            } else {
                result = result + s1;
                i++;
            }
        }
        return result;
    }

    private static int value(char r) {
        if (r == 'I')
            return 1;
        if (r == 'V')
            return 5;
        if (r == 'X')
            return 10;
        if (r == 'L')
            return 50;
        if (r == 'C')
            return 100;
        if (r == 'D')
            return 500;
        if (r == 'M')
            return 1000;
        return -1;
    }

    public static String formatTime(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        return (hours +
                "h " +
                minutes +
                "m " +
                seconds +
                "s");
    }

    public static String calculateTimeBetweenNow(long oldMillis) {
        long currentMillis = System.currentTimeMillis();
        long diffMillis = currentMillis - oldMillis;

        long years = TimeUnit.MILLISECONDS.toDays(diffMillis) / 365;
        if (years > 0) {
            return years + (years == 1 ? " ano atrás" : " anos atrás");
        }

        long months = (TimeUnit.MILLISECONDS.toDays(diffMillis) % 365) / 30;
        if (months > 0) {
            return months + (months == 1 ? " mês atrás" : " meses atrás");
        }

        long days = TimeUnit.MILLISECONDS.toDays(diffMillis) % 30;
        if (days > 0) {
            return days + (days == 1 ? " dia atrás" : " dias atrás");
        }

        long hours = TimeUnit.MILLISECONDS.toHours(diffMillis) % 24;
        if (hours > 0) {
            return hours + (hours == 1 ? " hora atrás" : " horas atrśa");
        }

        long minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis) % 60;
        if (minutes > 0) {
            return minutes + (minutes == 1 ? " minuto atrás" : " minutos atrás");
        }

        long seconds = TimeUnit.MILLISECONDS.toSeconds(diffMillis) % 60;
        return seconds + (seconds == 1 ? " segundo atrás" : " segundos atrás");
    }

    public static String asTime(int ticks) {
        String time = "";

        int hours = ticks / 1000;
        int minutes = (ticks - (hours * 1000)) / 50;
        int seconds = (ticks - (hours * 1000) - (minutes * 50)) / 5;

        if (hours > 0) time += hours + ":";
        if (minutes > 0) {
            if (minutes >= 10) time += minutes + ":";
            else if (hours > 0) time += "0" + minutes + ":";
            else time += minutes + ":";
        }
        if (seconds > 0) {
            if (seconds >= 10) time += seconds;
            else time += "0" + seconds;
        } else time += "00";

        return time;
    }

    public static String formatTimeLeft(long timeLeft) {

        long seconds = timeLeft / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        String time = "";

        if (days > 0) time += days + "d ";
        if (hours > 0) time += hours % 24 + "h ";
        if (minutes > 0) time += minutes % 60 + "m ";
        if (seconds > 0) time += seconds % 60 + "s";

        if (days > 5) return days + "d";
        if (hours > 5 && days == 0) return hours + "h";

        return time;
    }

    public static String calculateTimeAgoWithPeriodAndDuration(LocalDateTime pastTime, ZoneId zone) {
        Period period = Period.between(pastTime.toLocalDate(), new Date().toInstant().atZone(zone).toLocalDate());
        Duration duration = Duration.between(pastTime, new Date().toInstant().atZone(zone));
        if (period.getYears() != 0) {
            return "alguns anos atrás";
        } else if (period.getMonths() != 0) {
            return "alguns meses atrás";
        } else if (period.getDays() != 0) {
            return "alguns dias atrás";
        } else if (duration.toHours() != 0) {
            return "alguns horas atrás";
        } else if (duration.toMinutes() != 0) {
            return "alguns minutos atrás";
        } else if (duration.getSeconds() != 0) {
            return "alguns segundos atrás";
        } else {
            return "alguns momentos atrás";
        }
    }
}
