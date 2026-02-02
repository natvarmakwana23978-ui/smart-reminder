// ... (જૂનો કોડ) ...

fun bind(monthPos: Int, jsonData: String, lang: String) {
    // સેમ્પલ ડેટા (અહીં તમારે ગૂગલ શીટનો ડેટા મેપ કરવાનો રહેશે)
    val dayList = mutableListOf<DayModel>()
    
    // અત્યારે આપણે ટેસ્ટ કરવા માટે ૧ થી ૩૦ તારીખ ભરીએ
    for (i in 1..30) {
        dayList.add(DayModel(i.toString(), "તિથિ $i"))
    }

    daysRecyclerView.layoutManager = GridLayoutManager(itemView.context, 7)
    daysRecyclerView.adapter = DaysAdapter(dayList)
}

