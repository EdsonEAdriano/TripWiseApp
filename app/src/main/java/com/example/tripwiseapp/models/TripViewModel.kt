package com.example.tripwiseapp.models

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripwiseapp.dao.TripDAO
import com.example.tripwiseapp.entity.Trip
import com.example.tripwiseapp.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
data class RegisterTrip(
    val id: Int = 0,
    val userId: Long = 0,
    val destiny: String = "",
    val type: String = "",
    val start: LocalDate = LocalDate.MIN,
    val end: LocalDate = LocalDate.MIN,
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun validateStart(): String {
        if (start == LocalDate.MIN)
            return "Start Date is required"

        return ""
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun validateEnd(): String {
        if (end == LocalDate.MIN)
            return "End Date is required"

        return ""
    }

    fun validateBudget(): String {
        if (budget <= 0)
            return "Invalid Budget"

        return ""
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun validateAllField() {
        if (destiny.isBlank()) {
            throw Exception("Destiny is required")
        }
        if (type.isBlank()) {
            throw Exception("Type is required")
        }
        if (start == LocalDate.MIN) {
            throw Exception("Start Date is required")
        }
        if (end == LocalDate.MIN) {
            throw Exception("End Date is required")
        }
        if (budget <= 0)
            throw Exception("Invalid Budget")
    }

    fun toTrip(): Trip {
        return Trip(
            id         =  id,
            userId     =  userId,
            destiny    =  destiny,
            type       =  type,
            startDate  =  start,
            endDate    =  end,
            budget     =  budget
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
class TripViewModel(
    private val id: Int?,
    private val _tripDAO: TripDAO
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterTrip())
    val uiState : StateFlow<RegisterTrip> = _uiState.asStateFlow()


    init {
        id?.let { id ->
            viewModelScope.launch {
                _tripDAO.findById(id)?.let { trip ->
                    _uiState.value = _uiState.value.copy(
                        id = trip.id,
                        userId = trip.userId,
                        destiny = trip.destiny,
                        type = trip.type,
                        start = trip.startDate,
                        end = trip.endDate,
                        budget = trip.budget
                    )
                }
            }
        }
    }


    fun onUserId(userId: Long) {
        _uiState.value = _uiState.value.copy(userId = userId)
    }

    fun onDestinyChange(destiny: String) {
        _uiState.value = _uiState.value.copy(destiny = destiny)
    }

    fun onTypeChange(type : String) {
        _uiState.value = _uiState.value.copy(type = type)
    }

    fun onStartChange(start: LocalDate) {
        _uiState.value = _uiState.value.copy(start = start)
    }

    fun onEndChange(end: LocalDate) {
        _uiState.value = _uiState.value.copy(end = end)
    }

    fun onBudgetChange(budget: Double) {
        _uiState.value = _uiState.value.copy(budget = budget)
    }


    fun register() {
        try {
            _uiState.value.validateAllField()

            viewModelScope.launch {
                _tripDAO.upsert(
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