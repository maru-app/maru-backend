package me.daegyeo.maru.shared.util

import jakarta.servlet.http.HttpServletRequest

object IPAddress {
    fun getClientIp(request: HttpServletRequest): String {
        var ip = request.getHeader("X-Forwarded-For")

        if (!ip.isNullOrEmpty() && ip != "unknown") {
            val ips = ip.split(",")
            ip = ips[0].trim()
        }

        if (ip.isNullOrEmpty() || ip == "unknown") {
            ip = request.getHeader("X-Real-IP")
        }

        if (ip.isNullOrEmpty() || ip == "unknown") {
            ip = request.remoteAddr
        }

        return ip ?: "unknown"
    }
}
