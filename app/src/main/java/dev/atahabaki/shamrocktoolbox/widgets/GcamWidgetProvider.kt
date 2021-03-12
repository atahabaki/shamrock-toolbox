package dev.atahabaki.shamrocktoolbox.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import dev.atahabaki.shamrocktoolbox.R
import java.io.BufferedReader
import java.io.InputStreamReader

class GcamWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        appWidgetIds?.forEach {
            val pendingIntent: PendingIntent = Intent(context, GcamWidgetProvider::class.java).let {
                PendingIntent.getActivity(context, 0, it, 0)
            }
            val views: RemoteViews = RemoteViews(
                context?.packageName,
                R.layout.widget_gcam
            ).apply {
            }
        }
    }

    private fun getGcamStatus(context: Context?): Boolean {
        try {
            val p = java.lang.Runtime.getRuntime().exec("getprop $gcamProp")
            p.waitFor()
            val stdOut = BufferedReader(InputStreamReader(p.inputStream))
            val line = stdOut.readLine().trim()
            return line == "1"
        } catch (e: Exception) {
            Log.d("${context?.packageName}.toggleGcam", "${e.message}")
        }
        return false
    }
}