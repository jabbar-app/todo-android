package com.example.superbtodo.fragments.calendar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.superbtodo.R
import com.example.superbtodo.adapters.CalendarPickerAdapter
import com.example.superbtodo.databinding.FragmentCalendarBinding
import com.example.superbtodo.utils.DateFormatUtil
import com.example.superbtodo.viewmodel.TaskViewModel
import com.github.sundeepk.compactcalendarview.CompactCalendarView.CompactCalendarViewListener
import com.github.sundeepk.compactcalendarview.domain.Event
import java.util.*


class CalendarFragment : Fragment(R.layout.fragment_calendar) {
    private lateinit var binding: FragmentCalendarBinding
    private lateinit var mTaskViewModel: TaskViewModel
    private lateinit var adapter: CalendarPickerAdapter
    private object DateFormatter : DateFormatUtil()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCalendarBinding.bind(view)
        initAdapter()
        setupEvents()
        this.pickDate()
    }

    private fun initAdapter() {
        adapter = CalendarPickerAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun pickDate() {
        val firstDay = System.currentTimeMillis()
        binding.tvSelectMonth.text = DateFormatter.monthFormat().format(firstDay)
        showRecyclerView(DateFormatter.dateFormat().format(firstDay).toString())
        binding.calendarView.setListener(object : CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date) {
                val searchQuery = DateFormatter.dateFormat().format(dateClicked).toString()
                showRecyclerView(searchQuery)
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date) {
                binding.tvSelectMonth.text = DateFormatter.monthFormat().format(firstDayOfNewMonth)
            }
        })
    }

    private fun setupEvents() {
        mTaskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        mTaskViewModel.readNotDoneData().observe(viewLifecycleOwner) { tasks ->
            for (task in tasks) {
                val calendar = Calendar.getInstance()
                val date: ArrayList<String> =
                    DateFormatter.dateFormat().format(DateFormatter.hourly().parse(task.date) as Date).toString()
                        .split(".") as ArrayList<String>
                val dd: String = date[0]
                val month: String = date[1]
                val year: String = date[2]
                calendar[Calendar.DAY_OF_MONTH] = dd.toInt()
                calendar[Calendar.MONTH] = month.toInt() - 1
                calendar[Calendar.YEAR] = year.toInt()
                binding.calendarView.addEvent(
                    Event(
                        R.drawable.ic_baseline_access_alarm_24,
                        calendar.timeInMillis
                    )
                )
            }
        }
    }

    private fun showRecyclerView(searchQuery: String) {
        mTaskViewModel.calendarSearch("%$searchQuery%").observe(viewLifecycleOwner)
        { tasks ->
            tasks.let {
                adapter.setData(it)
            }
            binding.dayTxt.text = searchQuery
            if (tasks.size == 0) {
                binding.emptyLogo.visibility = View.VISIBLE
                binding.cheerTxt.visibility = View.VISIBLE
            } else {
                binding.emptyLogo.visibility = View.GONE
                binding.cheerTxt.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        }
    }

}
