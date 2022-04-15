package com.trinidad.beansanconstruction.utils

import android.text.TextUtils

import java.math.BigDecimal
import java.text.DecimalFormat

/**
 * 字符串的工具类
 *
 * @author zhangrq
 */
object StringUtil {

    /**
     * 对里面的内容content进行判断，为空则返回defaultString
     */
    @JvmOverloads
    fun getContent(content: String, defaultString: String = "——"): String {
        return if (TextUtils.isEmpty(content)) defaultString else content
    }

    /**
     * 编辑为开头保留startKeepLength长度，结尾保留endKeepLength长度
     *
     * @param startKeepLength 开头保留的长度
     * @param endKeepLength   结尾保留的长度
     */
    fun editIdNumber(content: String?, startKeepLength: Int, endKeepLength: Int): String? {
        if (content != null && startKeepLength >= 0 && endKeepLength >= 0 && content.length > startKeepLength + endKeepLength) {
            val charArray = content.toCharArray()
            for (i in charArray.indices) {
                if (i >= startKeepLength && i < charArray.size - endKeepLength) {
                    charArray[i] = '*'
                }
            }
            return String(charArray)
        }
        return content
    }

    /**
     * 开头保留startKeepLength位数，之后全部为*
     *
     * @param content         内容
     * @param startKeepLength 开头保留的长度
     */
    fun editName(content: String?, startKeepLength: Int): String? {
        if (content != null && startKeepLength >= 0 && content.length > startKeepLength) {
            val charArray = content.toCharArray()
            for (i in charArray.indices) {
                if (i >= startKeepLength) {
                    charArray[i] = '*'
                }
            }
            return String(charArray)
        }
        return content
    }

    /**
     * 小数的 取小数点和零 如 12.30 -> 12.3 | 23.00 -> 23
     */
    fun subZeroAndDot(s: String): String {
        var s = s
        if (s.indexOf(".") > 0) {
            s = s.replace("0+?$".toRegex(), "")// 去掉多余的0
            s = s.replace("[.]$".toRegex(), "")// 如最后一位是.则去掉
        }
        return s
    }

    /**
     * 给金额显示添加千分位","
     *
     * @param val 金额
     */
    fun parseMoney(`val`: Any?): String {
        try {
            val pattern = "##,###,##0.00"
            if (`val` == null || `val` == "")
                return ""
            var valStr = `val`.toString() + ""
            val df = DecimalFormat(pattern)
            valStr = df.format(BigDecimal(valStr))
            return valStr
        } catch (e: Exception) {
            e.printStackTrace()
            return "——"
        }

    }

    /**
     * 格式化手机号
     *
     * @param phoneNumberStr 手机号
     * @return 返回格式为：151 6666 8888
     */
    fun formatPhoneNumber(phoneNumberStr: String?): String {
        if (phoneNumberStr == null || phoneNumberStr.isEmpty()) {
            return ""
        }
        val sb = StringBuilder()
        val chars = phoneNumberStr.toCharArray()
        for (i in chars.indices) {
            if (i == 3 || (i - 3) % 4 == 0)
                sb.append(" ")
            sb.append(chars[i])
        }
        return sb.toString()
    }

    /**
     * 保留两位小数
     *
     * @return 金额四舍五入，例如：0.22222 返回0.22；0.55555 返回0.56
     */
    fun saveTwoDecimal(price: Any): String {
        return try {
            val df = DecimalFormat("##0.00")
            if (price is String) {
                df.format(BigDecimal(price))
            } else
                df.format(price)

        } catch (e: Exception) {
            e.printStackTrace()
            "- -"
        }
    }

    /**
     * 保留一位小数
     *
     * @return 金额四舍五入，例如：0.22222 返回0.2；0.55555 返回0.6
     */
    fun saveOneDecimal(price: Any): String {
        return try {
            val df = DecimalFormat("##0.0")
            if (price is String) {
                df.format(BigDecimal(price))
            } else
                df.format(price)

        } catch (e: Exception) {
            e.printStackTrace()
            "- -"
        }

    }

    /**
     * 相加 結果保留两位小数
     *
     * @param doubleValA
     * @param doubleValB
     * @return
     */
    fun add(doubleValA: String, doubleValB: String): Double {
        val a2 = BigDecimal(doubleValA)
        val b2 = BigDecimal(doubleValB)
        val b = BigDecimal(a2.add(b2).toDouble())
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
    }


    /**
     * 相減 結果保留两位小数
     *
     * @param doubleValA
     * @param doubleValB
     * @return
     */
    fun sub(doubleValA: String, doubleValB: String): Double {
        val a2 = BigDecimal(doubleValA)
        val b2 = BigDecimal(doubleValB)
        val b = BigDecimal(a2.subtract(b2).toDouble())
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    /**
     * 相乘 結果保留两位小数
     *
     * @param doubleValA
     * @param doubleValB
     * @return
     */
    fun mul(doubleValA: String, doubleValB: String): Double {
        val a2 = BigDecimal(doubleValA)
        val b2 = BigDecimal(doubleValB)
        val b = BigDecimal(a2.multiply(b2).toDouble())
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    /**
     * 相除
     *
     * @param doubleValA
     * @param doubleValB
     * @param scale      除不尽时指定精度
     * @return
     */
    fun div(doubleValA: String, doubleValB: String, scale: Int): Double {
        val a2 = BigDecimal(doubleValA)
        val b2 = BigDecimal(doubleValB)
        return a2.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    /**
     * 把字符串在中间截取，分成两段
     *
     * @param content 要截取的内容
     * @return String的数组，长度为2，String[0]、String[1]不会为null
     */
    fun getSubstring(content: String?): Array<String> {
        val substringArray = arrayOf("", "")
        if (content != null && content.isNotEmpty()) {
            val subIndex = (content.length + 1) / 2
            substringArray[0] = content.substring(0, subIndex)
            substringArray[1] = content.substring(subIndex)
        }
        return substringArray
    }
}
/**
 * 对里面的内容content进行判断，为空则返回——
 */