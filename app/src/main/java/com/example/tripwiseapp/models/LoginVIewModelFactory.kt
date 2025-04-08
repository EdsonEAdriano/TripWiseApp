package com.example.tripwiseapp.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tripwiseapp.dao.UserDAO

class LoginViewModelFactory(
    private val userDao: UserDAO
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(userDao) as T
    }
}