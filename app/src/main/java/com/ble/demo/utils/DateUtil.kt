package com.ble.demo.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by admin on 2017/8/31.
 */
object DateUtil {

    fun getDate_hm():String{
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
        return format.format(Date())
    }
    fun getDate_hms():String{
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        return format.format(Date())
    }
    fun getDate_ms():String{
        val format = SimpleDateFormat("mm:ss", Locale.CHINA)
        return format.format(Date())
    }
    fun getToday():String{
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        return format.format(Date())
    }
    fun getTaskDate():String{
        val format = SimpleDateFormat("yyyyMMdd", Locale.CHINA)
        return format.format(Date())
    }



   @JvmStatic fun long2String(millstime: Long): String {
        val date = Date(millstime)

        val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
        return format.format(date)
    }

    fun date2String(date: Date?): String {
        val format = SimpleDateFormat("yy-MM-dd HH:mm", Locale.CHINA)
        return format.format(date)
    }
    fun date2String2(date: Date?): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        return format.format(date)
    }

    fun string2Long(time:String):Long{
        val df = SimpleDateFormat("yyyy-MM-dd")
       val date =   df.parse(time)
        return  date.time
    }

    fun string2Long2(time:String):Long{
        val df = SimpleDateFormat("yy-MM-dd HH:mm")
        val date =   df.parse(time)
        return  date.time
    }
}
























