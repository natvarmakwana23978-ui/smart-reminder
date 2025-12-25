// આ કોડ તમારી CalendarWidget.kt માં 'onUpdate' ફંક્શનની અંદર અથવા અલગથી ઉમેરો

private fun fetchDataFromGithub(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val url = "તમારી_ગીટહબ_JSON_ફાઈલની_રો_લિંક_અહીં_મૂકો"

    // નોંધ: નેટવર્ક કોલ માટે Thread અથવા Coroutines વાપરવા જરૂરી છે
    Thread {
        try {
            val jsonText = java.net.URL(url).readText()
            val jsonArray = org.json.JSONArray(jsonText)
            
            // આપણે અત્યારે ટેસ્ટિંગ માટે જાન્યુઆરી ૨૦૨૬ ના લિસ્ટમાંથી પહેલો ડેટા લઈએ છીએ
            // વાસ્તવિક રીતે આપણે આજની તારીખ મુજબ ફિલ્ટર કરીશું
            val data = jsonArray.getJSONObject(0) 

            val views = RemoteViews(context.packageName, R.layout.calendar_widget_layout)

            // JSON માંથી વિગતો મેળવીને સેટ કરવી
            views.setTextViewText(R.id.txt_eng_date, data.getString("eng_date"))
            views.setTextViewText(R.id.txt_event_top, data.getString("event_title"))
            views.setTextViewText(R.id.txt_event_note, data.getString("event_note"))
            views.setTextViewText(R.id.txt_guj_tithi, data.getString("guj_tithi"))
            views.setTextViewText(R.id.txt_guj_event, data.getString("guj_event"))
            views.setTextViewText(R.id.txt_birthday_wish, data.getString("wish_note"))

            appWidgetManager.updateAppWidget(appWidgetId, views)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.start()
}
