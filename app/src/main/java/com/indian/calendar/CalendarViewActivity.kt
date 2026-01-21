// ... બાકીનો કોડ ઉપર મુજબ ...
            override fun onResponse(call: Call<List<CalendarDayData>>, response: Response<List<CalendarDayData>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val daysList = response.body() ?: emptyList()
                    // અહીં નામ 'CalendarAdapter' હોવું જોઈએ [cite: 2026-01-21]
                    recyclerView.adapter = CalendarAdapter(daysList) 
                }
// ... બાકીનો કોડ ઉપર મુજબ ...
