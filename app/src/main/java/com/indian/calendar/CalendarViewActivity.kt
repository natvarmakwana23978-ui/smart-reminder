package com.smart.reminder
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.smart.reminder.databinding.ActivityCalendarViewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val langIndex = intent.getIntExtra("LANG_INDEX", 0)
        supportActionBar?.title = intent.getStringExtra("LANG_NAME")
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        
        binding.progressBar.visibility = View.VISIBLE
        RetrofitClient.instance.getCalendarData().enqueue(object : Callback<List<CalendarDayData>> {
            override fun onResponse(call: Call<List<CalendarDayData>>, response: Response<List<CalendarDayData>>) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    binding.recyclerView.adapter = CalendarAdapter(response.body()!!, langIndex)
                }
            }
            override fun onFailure(call: Call<List<CalendarDayData>>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@CalendarViewActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
