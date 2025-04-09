package com.example.oamkhub.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oamkhub.data.model.event.EventItem
import com.example.oamkhub.data.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Month

class EventViewModel : ViewModel() {
    private val repository = EventRepository()

    private val _events = MutableStateFlow<List<EventItem>>(emptyList())
    val events: StateFlow<List<EventItem>> = _events

    private var originalEvents: List<EventItem> = emptyList()

    private val _selectedMonth = MutableStateFlow<Month?>(null)
    val selectedMonth: StateFlow<Month?> = _selectedMonth

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            val events = repository.fetchEvents()
            originalEvents = events
            _events.value = events
            _isLoading.value = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setMonthFilter(month: Month?) {
        _selectedMonth.value = month
        filterEvents()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun filterEvents() {
        val month = _selectedMonth.value
        _isLoading.value = true

        _events.value = if (month == null) {
            originalEvents
        } else {
            originalEvents.filter {
                it.parsedDate?.month == month
            }
        }

        _isLoading.value = false
    }
}
