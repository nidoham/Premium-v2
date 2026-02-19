package com.nidoham.premium.util

import java.util.Locale

class TimeUtil {
    companion object {
        fun Long.formatDuration(): String {
            if (this <= 0) return ""
            val h = this / 3600
            val m = (this % 3600) / 60
            val s = this % 60
            return if (h > 0) String.format(Locale.US, "%d:%02d:%02d", h, m, s)
            else String.format(Locale.US, "%d:%02d", m, s)
        }

        fun Long.formatCount(): String = when {
            this >= 1_000_000_000 -> String.format(Locale.US, "%.1fB", this / 1_000_000_000.0)
            this >= 1_000_000     -> String.format(Locale.US, "%.1fM", this / 1_000_000.0)
            this >= 1_000         -> String.format(Locale.US, "%.1fK", this / 1_000.0)
            else                  -> this.toString()
        }
    }
}