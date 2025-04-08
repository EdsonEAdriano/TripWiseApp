package com.example.tripwiseapp.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tripwiseapp.dao.UserDAO

class UserViewModelFactory(
    private val userDao: UserDAO
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(userDao) as T
    }
}