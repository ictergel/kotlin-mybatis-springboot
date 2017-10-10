package com.demo.base.utils

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.*
import java.util.regex.Pattern

object DateTimeUtil{

    val FORMAT_ONE = "yyyyMMdd"
    val FORMAT_TWO = "yyyyMMddHH"
    val FORMAT_THREE = "yyyyMMddHHmmss"
    val FORMAT_FOUR = "yyyy-MM"
    val FORMAT_FIVE = "yyyy-MM-dd"
    val FORMAT_SIX = "yyyy-MM-dd HH"
    val FORMAT_SEVEN = "yyyy-MM-dd HH:mm"
    val FORMAT_EIGHT = "yyyy-MM-dd HH:mm:ss"
    val FORMAT_NINE = "yyyy/MM/dd"
    val FORMAT_TEN = "yyyy-MM-dd'T'HH:mm:ss"

    private val dataTimeFormatterEight = DateTimeFormat.forPattern(FORMAT_EIGHT)

    /**
     * 按指定格式将字符串转换为日期类型
     *
     * @param dateStr String
     * @param format  String
     * @return
     */
    fun stringToDate(dateStr: String, format: String): DateTime? {
        return if (dateStr.isNotEmpty()) {
            DateTime.parse(dateStr, DateTimeFormat.forPattern(format))
        } else {
            null
        }
    }

    /**
     * 两个日期相减
     *
     * @param firstTime DateTime
     * @param secTime   DateTime
     * @return 相减得到的秒数
     */
    fun timeSub(firstTime: DateTime, secTime: DateTime): Long = (secTime.millis - firstTime.millis) / 1000

    /**
     * 获得某年某月的天数
     *
     * @param year  String
     * @param month String 月份[1-12]
     * @return int
     */
    fun getDaysOfMonth(year: String, month: String): Int = DateTime.now().withYear(year.toInt()).withMonthOfYear(month.toInt()).dayOfMonth().maximumValue


    /**
     * 计算两个日期相差的天数，如果date2 > date1 返回正数，否则返回负数
     *
     * @param date1 DateTime
     * @param date2 DateTime
     * @return long
     */
    fun dayDiff(date1: DateTime, date2: DateTime): Long {
        return (date2.millis - date1.millis) / 86400000
    }


    /**
     * 根据生日获取星座
     *
     * @param birth String "YYYY-mm-dd"
     * @return
     */
    fun getConstellation(birth: String): String {
        var newBirth = birth
        if (!isDate(newBirth)) {
            newBirth = "2000" + newBirth
        }
        if (!isDate(newBirth)) {
            return ""
        }
        val month = Integer.parseInt(newBirth.substring(newBirth.indexOf("-") + 1, newBirth.lastIndexOf("-")))
        val day = Integer.parseInt(newBirth.substring(newBirth.lastIndexOf("-") + 1))
        val s = "魔羯水瓶双鱼牡羊金牛双子巨蟹狮子处女天秤天蝎射手魔羯"
        val arr = intArrayOf(20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22)
        val start = month * 2 - if (day < arr[month - 1]) 2 else 0
        return s.substring(start, start + 2) + "座"
    }

    /**
     * 判断日期是否有效,包括闰年的情况
     * @param date String  "YYYY-mm-dd"
     * @return
     */
    private fun isDate(date: String): Boolean {
        val reg = StringBuffer("^((\\d{2}(([02468][048])|([13579][26]))-?((((0?")
        reg.append("[13578])|(1[02]))-?((0?[1-9])|([1-2][0-9])|(3[01])))")
        reg.append("|(((0?[469])|(11))-?((0?[1-9])|([1-2][0-9])|(30)))|")
        reg.append("(0?2-?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][12")
        reg.append("35679])|([13579][01345789]))-?((((0?[13578])|(1[02]))")
        reg.append("-?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))")
        reg.append("-?((0?[1-9])|([1-2][0-9])|(30)))|(0?2-?((0?[")
        reg.append("1-9])|(1[0-9])|(2[0-8]))))))")
        val p = Pattern.compile(reg.toString())
        return p.matcher(date).matches()
    }


    /**
     * 判断当前文件的结束时间是否为超过15分钟的数据,如果是则返回true
     * @param fileName
     * @return
     */
    fun isOldFile(fileName: String): Boolean {
        var result = false
        val file = fileName.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val dataString = long2Date(file[5])
        val startTime = stringToDate(dataString, FORMAT_EIGHT)
        //计算当前时间的最近一刻钟时间
        val dt = DateTime.now()
        val min = dt.minuteOfHour / 15 * 15
        dt.withMinuteOfHour(min)
        val timeSub = timeSub(startTime ?: DateTime.now(), dt)
        if (timeSub > 900) {
            result = true
        }
        return result
    }

    /**
     * 判断当前文件的结束时间是否为超过15分钟的数据,如果是则返回true
     * @param fileName String
     * @param dt       DateTime
     * @return
     */
    fun isOldFile(fileName: String, dt: DateTime): Boolean {
        var result = false
        val file = fileName.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val dataString = long2Date(file[5])
        val startTime = stringToDate(dataString, FORMAT_EIGHT)
        //计算当前时间的最近一刻钟时间
        val min = dt.minuteOfHour / 15 * 15
        dt.withMinuteOfHour(min)
        val timeSub = timeSub(startTime ?: DateTime.now(), dt)
        if (timeSub > 900) {
            result = true
        }
        return result
    }

    /**
     * 如果time为空则获取当前时间前一小时的开始和结束时间，如果不为空则获取time时间小时的开始和结束
     */
    fun getHourStartAndEnd(time: String?): Map<String, String> {
        var startTime = DateTime().withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0)
        val endTime: DateTime
        if (time == null) {
            startTime = startTime.plusHours(-1)
            endTime = startTime.plusHours(1)
        } else {
            startTime = DateTime.parse(time, dataTimeFormatterEight).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0)
            endTime = startTime.plusHours(1)
        }
        val params = HashMap<String, String>()
        params.put("startTime", startTime.toString(FORMAT_EIGHT))
        params.put("endTime", endTime.toString(FORMAT_EIGHT))
        params.put("gatherTime", startTime.toString(FORMAT_EIGHT))
        params.put("year", startTime.year.toString())
        params.put("month", startTime.monthOfYear.toString())
        params.put("day", startTime.dayOfMonth.toString())
        params.put("hour", startTime.hourOfDay.toString())
        return params
    }

    /**
     * 如果time为null，设置开始时间为当前时间的前十五分钟，如果不为null则设置开始时间为传递过来的时间
     * @param time String
     * @return
     */
    fun stepMinDate(time: String?): Map<String, String> {
        val params = HashMap<String, String>()
        var startTime = when (time) {
            null -> DateTime.now().plusMinutes(-15)
            else -> DateTime.parse(time, dataTimeFormatterEight)
        }

        val m = startTime!!.minuteOfHour
        val minute = when {
            m < 15 -> 0
            m in 15..29 -> 15
            m in 30..44 -> 30
            else -> 45
        }
        startTime = startTime.withMinuteOfHour(minute).withSecondOfMinute(0).withMillisOfSecond(0)
        params.put("statTime", startTime!!.toString(FORMAT_EIGHT))
        return params
    }

    /**
     * 如果time为空则获取当前时间前一天的开始和结束时间，如果不为空则获取time时间天的开始和结束
     */
    fun getDayStartAndEnd(time: String?): Map<String, String> {
        var startTime = DateTime().withMillisOfDay(0)
        val endTime: DateTime
        if (time == null) {
            startTime = startTime.plusDays(-1)
            endTime = startTime.plusDays(1)
        } else {
            startTime = DateTime.parse(time, dataTimeFormatterEight).withMillisOfDay(0)
            endTime = startTime.plusDays(1)
        }
        val params = HashMap<String, String>()
        params.put("startTime", startTime.toString(FORMAT_EIGHT))
        params.put("endTime", endTime.toString(FORMAT_EIGHT))
        params.put("gatherTime", startTime.toString(FORMAT_EIGHT))
        return params
    }

    /**
     * 如果time为空则获取当前时间前一月的月开始和结束时间，如果不为空则获取time时间月的开始和结束
     */
    fun getMonthStartAndEnd(time: String?): Map<String, String> {
        val startTime = when (time) {
            null -> DateTime().withMillisOfDay(0).plusMonths(-1)
            else -> DateTime.parse(time, dataTimeFormatterEight)
        }
        val params = HashMap<String, String>()
        params.put("startTime", startTime.withMillisOfDay(0).withDayOfMonth(1).toString(FORMAT_FIVE))
        params.put("endTime", startTime.withMillisOfDay(0).withDayOfMonth(1).plusMonths(1).toString(FORMAT_FIVE))
        params.put("gatherTime", startTime.toString(FORMAT_FIVE))
        return params
    }

    /**
     * 如果time为空就取离之前十五分钟最近的一个点，例如当前时刻是29分钟，前十五分钟是14分钟，最近的14-0 >15-14,所以取15分钟时间点
     * @return
     */
    fun getMinJobTime(): Map<String, String> {
        val params = HashMap<String, String>()
        var startTime = DateTime.now().plusMinutes(-15)
        val m = startTime!!.minuteOfHour
        val minute = when {
            m < 10 -> 0
            m in 10..24 -> 15
            m in 25..39 -> 30
            m in 40..54 -> 45
            else -> 60
        }
        startTime = startTime.withMinuteOfHour(0).plusMinutes(minute).withSecondOfMinute(0).withMillisOfSecond(0)
        params.put("statTime", startTime!!.toString(FORMAT_EIGHT))
        return params
    }

    private fun long2Date(longDate: String): String {
        val sd = java.lang.Long.parseLong(longDate + "000")
        val dat = Date(sd)
        val gc = GregorianCalendar()
        gc.time = dat
        val format = java.text.SimpleDateFormat(FORMAT_ONE)
        return format.format(gc.time)
    }
}