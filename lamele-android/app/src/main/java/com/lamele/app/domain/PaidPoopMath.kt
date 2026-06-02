package com.lamele.app.domain

import java.util.Calendar

object PaidPoopMath {
    /** 估算时薪（元） */
    fun hourlyRate(monthlySalary: Float, workDays: Int, hoursPerDay: Float): Float {
        val d = workDays.coerceAtLeast(1)
        val h = hoursPerDay.coerceAtLeast(0.5f)
        return monthlySalary / d / h
    }

    /** 本次带薪释放「赚回」多少钱 */
    fun sessionEarnings(durationMinutes: Int, monthlySalary: Float, workDays: Int, hoursPerDay: Float): Float {
        if (durationMinutes <= 0) return 0f
        val hr = hourlyRate(monthlySalary, workDays, hoursPerDay)
        return hr * (durationMinutes / 60f)
    }

    fun monthPaidTotalMillis(
        records: List<com.lamele.app.data.local.PoopRecordEntity>,
        monthlySalary: Float,
        workDays: Int,
        hoursPerDay: Float,
    ): Float {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        cal.add(Calendar.MONTH, 1)
        val end = cal.timeInMillis
        var total = 0f
        for (r in records) {
            if (!r.isPaidPoop) continue
            if (r.timeMillis in start until end) {
                total += sessionEarnings(r.durationMinutes, monthlySalary, workDays, hoursPerDay)
            }
        }
        return total
    }
}
