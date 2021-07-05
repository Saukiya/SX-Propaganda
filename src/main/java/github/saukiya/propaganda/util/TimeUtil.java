package github.saukiya.propaganda.util;

import org.bukkit.entity.Player;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TimeUtil {
    private static final SimpleDateFormatUtils sdf = new SimpleDateFormatUtils();
    private static final Map<String, Long> timeMap = new HashMap();

    public static void add(Player player, String name, int times) {
        timeMap.put(player.getName() + ":" + name, System.currentTimeMillis() + (long) times);
    }

    private static double get(Player player, String name) {
        Long time = timeMap.get(player.getName() + ":" + name);
        return time != null ? (double) (time - System.currentTimeMillis()) / 1000.0D : -1.0D;
    }

    public static boolean is(Player player, String name) {
        if (get(player, name) > 0.0D) {
            return true;
        } else {
            timeMap.remove(player.getName() + ":" + name);
            return false;
        }
    }

    private static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date == null ? new Date() : date);
        int dayWeek = cal.get(7);
        if (1 == dayWeek) {
            cal.add(5, -1);
        }

        cal.setFirstDayOfWeek(2);
        int day = cal.get(7);
        cal.add(5, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }

    public static Date getDayTime(Date date, Integer day, int hour, int minute) {
        date = date == null ? new Date() : date;
        Calendar cal = Calendar.getInstance();
        if (day != null) {
            cal.setTime(getThisWeekMonday(date));
            cal.add(5, day - 1);
        } else {
            cal.setTime(date);
        }

        if (hour > -1) {
            cal.set(11, hour);
        }

        if (minute > -1) {
            cal.set(12, minute);
        }

        cal.set(13, 0);
        return cal.getTime();
    }

    public static SimpleDateFormatUtils getSdf() {
        return sdf;
    }

    public static class SimpleDateFormatUtils {
        private final ThreadLocal<SimpleDateFormat> threadLocal = ThreadLocal.withInitial(() -> {
            return new SimpleDateFormat("MM/dd/yy HH:mm");
        });

        public Date parseFormDate(String dateStr) throws ParseException {
            return this.threadLocal.get().parse(dateStr);
        }

        public long parseFormLong(String dateStr) throws ParseException {
            return this.threadLocal.get().parse(dateStr).getTime();
        }

        public String format(Long date) {
            return this.threadLocal.get().format(date);
        }

        public String format(Date date) {
            return this.threadLocal.get().format(date);
        }

        public void reload() {
            this.threadLocal.remove();
        }
    }
}
