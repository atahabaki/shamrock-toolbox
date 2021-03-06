package dev.atahabaki.phoenixtoolbox.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.appwidget.AppWidgetProviderInfo
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import dev.atahabaki.phoenixtoolbox.R
import dev.atahabaki.phoenixtoolbox.execRoot
import java.io.BufferedReader
import java.io.InputStreamReader

class GcamWidgetProvider : AppWidgetProvider() {

    companion object {
        const val gcamProp = "persist.camera.HAL3.enabled"
        const val REFRESH_ACTION = "android.appwidget.action.APPWIDGET_UPDATE"
        const val TOGGLE_ACTION= "dev.atahabaki.phoenixtoolbox.appwidget.TOGGLE_ACTION"
        const val REQUEST_CODE = 986342
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        updateWidgets(context, appWidgetManager, appWidgetIds)
    }

    private fun updateWidgets(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        appWidgetIds?.forEach {
            if (appWidgetManager?.getAppWidgetInfo(it)?.label == context?.getString(R.string.toggle_gcam)) {
                val views: RemoteViews = RemoteViews(
                        context?.packageName,
                        R.layout.widget_gcam
                ).apply {
                    setOnClickPendingIntent(R.id.gcam_widget_status_changer, selfPendingIntent(context, TOGGLE_ACTION))
                    setOnClickPendingIntent(R.id.gcam_widget_reloader, selfPendingIntent(context, REFRESH_ACTION))
                    updateWidgetContent(context, this)
                }
                appWidgetManager?.updateAppWidget(it, views)
            }
        }
    }

    private fun updateWidgetContent(context: Context?, remoteViews: RemoteViews) {
        remoteViews.apply {
            if (getGcamStatus(context)) {
                setTextViewText(R.id.gcam_widget_status_changer, context?.getString(R.string.disable_gcam))
            } else setTextViewText(R.id.gcam_widget_status_changer, context?.getString(R.string.enable_gcam))
        }
    }

    private fun selfPendingIntent(context: Context?, action: String) : PendingIntent {
        val intent: Intent = Intent(context, GcamWidgetProvider::class.java)
        intent.action = action
        return PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        fun update() {
            val appWidgetManager: AppWidgetManager = AppWidgetManager.getInstance(context!!)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, GcamWidgetProvider::class.java))
            updateWidgets(context, appWidgetManager, appWidgetIds)
        }
        super.onReceive(context, intent)
        if (intent?.action.equals(TOGGLE_ACTION)) {
            if (getGcamStatus(context)) {
                disableGcam(context)
            } else enableGcam(context)
            update()
        }
        else if (intent?.action.equals(REFRESH_ACTION)) {
            update()
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

    private fun disableGcam(context: Context?) {
        execRoot("setprop $gcamProp 0", "${context?.packageName}.setProp")
    }

    private fun enableGcam(context: Context?) {
        execRoot("setprop $gcamProp 1", "${context?.packageName}.setProp")
    }
}