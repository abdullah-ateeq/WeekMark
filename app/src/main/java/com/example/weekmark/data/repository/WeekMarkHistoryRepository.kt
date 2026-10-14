package com.example.weekmark.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.weekmark.data.model.WeekMarkHistory
import com.example.weekmark.data.model.WeekMarkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.util.UUID

class WeekMarkHistoryRepository(
    private val prefs: SharedPreferences
) {
    constructor(context: Context) : this(
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    )

    private val _history = MutableStateFlow<List<WeekMarkHistory>>(loadHistory())
    val history: StateFlow<List<WeekMarkHistory>> = _history.asStateFlow()

    private fun loadHistory(): List<WeekMarkHistory> {
        val jsonString = prefs.getString(KEY_HISTORY, null) ?: return emptyList()
        val list = mutableListOf<WeekMarkHistory>()
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val item = WeekMarkHistory(
                    id = obj.optString("id", UUID.randomUUID().toString()),
                    selectedDate = LocalDate.parse(obj.getString("selectedDate")),
                    weekNumber = obj.getInt("weekNumber"),
                    weekYear = obj.getInt("weekYear"),
                    weekStart = LocalDate.parse(obj.getString("weekStart")),
                    weekEnd = LocalDate.parse(obj.getString("weekEnd")),
                    calculatedAt = obj.getLong("calculatedAt")
                )
                list.add(item)
            }
        } catch (_: Exception) {
            return emptyList()
        }
        return list.take(MAX_HISTORY_ENTRIES)
    }

    private fun saveToDisk(list: List<WeekMarkHistory>) {
        val jsonArray = JSONArray()
        list.take(MAX_HISTORY_ENTRIES).forEach { item ->
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("selectedDate", item.selectedDate.toString())
            obj.put("weekNumber", item.weekNumber)
            obj.put("weekYear", item.weekYear)
            obj.put("weekStart", item.weekStart.toString())
            obj.put("weekEnd", item.weekEnd.toString())
            obj.put("calculatedAt", item.calculatedAt)
            jsonArray.put(obj)
        }
        prefs.edit().putString(KEY_HISTORY, jsonArray.toString()).apply()
    }

    fun saveCalculation(result: WeekMarkResult, currentTimeMillis: Long = System.currentTimeMillis()): WeekMarkHistory {
        val currentList = _history.value.toMutableList()

        // Deduplicate: If an entry for the same date already exists, remove it first
        currentList.removeAll { it.selectedDate == result.selectedDate }

        val newEntry = WeekMarkHistory(
            id = UUID.randomUUID().toString(),
            selectedDate = result.selectedDate,
            weekNumber = result.weekNumber,
            weekYear = result.weekYear,
            weekStart = result.weekStart,
            weekEnd = result.weekEnd,
            calculatedAt = currentTimeMillis
        )

        // Prepend new entry
        currentList.add(0, newEntry)

        // Enforce maximum of 10 entries (discard oldest beyond 10)
        val trimmedList = currentList.take(MAX_HISTORY_ENTRIES)
        saveToDisk(trimmedList)
        _history.value = trimmedList
        return newEntry
    }

    fun deleteCalculation(id: String) {
        val updated = _history.value.filterNot { it.id == id }
        saveToDisk(updated)
        _history.value = updated
    }

    fun clearHistory() {
        prefs.edit().remove(KEY_HISTORY).apply()
        _history.value = emptyList()
    }

    companion object {
        const val MAX_HISTORY_ENTRIES = 10
        private const val PREFS_NAME = "weekmark_history"
        private const val KEY_HISTORY = "key_history_items"
    }
}
