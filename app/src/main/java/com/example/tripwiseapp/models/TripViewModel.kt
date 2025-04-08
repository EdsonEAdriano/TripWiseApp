package com.example.tripwiseapp.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripwiseapp.dao.TripDAO
import com.example.tripwiseapp.entity.Trip
import com.example.tripwiseapp.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RegisterTrip(
    val destiny: String = "",
    val type: String = "",
    val start: String = "",
    val end: String = "",
    val budget: Double = 0.0,
    val errorMessage: String = "",
    val isSaved: Boolean = false
){
    fun validateDestiny(): String {
        if (destiny.isBlank())
            return "Destiny is required"

        return ""
    }

    fun validateType(): String {
        if (type.isBlank())
            return "Type is required"

        return ""
    }

    fun validateStart(): String {
        if (start.isBlank())
            return "Start Date is required"

        return ""
    }

    fun validateEnd(): String {
        if (end.isBlank())
            return "End Date is required"

        return ""
    }

    fun validateBudget(): String {
        if (budget <= 0)
            return "Invalid Budget"

        return ""
    }

    fun validateAllField() {
        if (destiny.isBlank()) {
            throw Exception("Destiny is required")
        }
        if (type.isBlank()) {
            throw Exception("Type is required")
        }
        if (start.isBlank()) {
            throw Exception("Start Date is required")
        }
        if (end.isBlank()) {
            throw Exception("End Date is required")
        }
        if (budget <= 0)
            throw Exception("Invalid Budget")
    }

    fun toTrip(): Trip {
        return Trip(
            destiny    =  destiny,
            type       =  type,
            startDate  =  start,
            endDate    =  end,
            budget     =  budget
        )
    }
}


class TripViewModel(
    private val _tripDAO: TripDAO
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterTrip())
    val uiState : StateFlow<RegisterTrip> = _uiState.asStateFlow()

    fun onDestinyChange(destiny: String) {
        _uiState.value = _uiState.value.copy(destiny = destiny)
    }

    fun onTypeChange(type : String) {
        _uiState.value = _uiState.value.copy(type = type)
    }

    fun onStartChange(start: String) {
        _uiState.value = _uiState.value.copy(start = start)
    }

    fun onEndChange(end: String) {
        _uiState.value = _uiState.value.copy(end = end)
    }

    fun onBudgetChange(budget: Double) {
        _uiState.value = _uiState.value.copy(budget = budget)
    }


    fun register() {
        try {
            _uiState.value.validateAllField()

            viewModelScope.launch {
                _tripDAO.insertTrip(
                    _uiState.value.toTrip()
                )
                _uiState.value = _uiState.value.copy(isSaved = true)
            }
        }
        catch (e: Exception) {
            _uiState.value = _uiState.value.copy(errorMessage = e.message ?: "Unknow error")
        }
    }

    fun cleanErrorMessage() {
        _uiState.value = _uiState.value.copy(
            errorMessage = "",
            isSaved = false
        )
    }


}