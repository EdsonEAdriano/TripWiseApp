package com.example.tripwiseapp.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tripwiseapp.dao.TripDAO
import com.example.tripwiseapp.dao.UserDAO

class TripViewModelFactory(
    private val id: Int?,
    private val tripDao: TripDAO
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TripViewModel(id, tripDao) as T
    }
}