package ed.service.messaging.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    public static Date date(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        return calendar.getTime();
    }

}
