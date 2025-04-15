package com.example.icecream.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.icecream.common.model.Status
import com.example.icecream.data.mapper.IceCreamMapper
import com.example.icecream.domain.repository.IceCreamRepository
import com.example.icecream.presentation.model.IceCreamUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IceCreamViewModel @Inject constructor(
    private val iceCreamRepository: IceCreamRepository,
    private val iceCreamMapper: IceCreamMapper,
) : ViewModel() {


    private val _sortedIceCreams = MutableStateFlow<List<IceCreamUIModel>>(emptyList())
    val sortedIceCreams: StateFlow<List<IceCreamUIModel>> = _sortedIceCreams.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadIceCreams()

    }

    private fun loadIceCreams() {
        viewModelScope.launch {
            val iceCreamEntities = iceCreamRepository.getIceCreamsFromDb()

            val iceCreamPrice = iceCreamRepository.getBasePrice()

            val iceCreamUIModels = iceCreamMapper.mapToUIModelList(iceCreamEntities, iceCreamPrice)

            _sortedIceCreams.value = iceCreamUIModels
            _isLoading.value = false
        }
    }

    fun sortIceCreamsByStatus(status: Status) {
        val sortedList = _sortedIceCreams.value.sortedBy { iceCream ->
            when (status) {
                Status.AVAILABLE -> if (iceCream.status == Status.AVAILABLE) 0 else 1
                Status.UNAVAILABLE -> if (iceCream.status == Status.UNAVAILABLE) 0 else 1
                Status.MELTED -> if (iceCream.status == Status.MELTED) 0 else 1
            }
        }
        _sortedIceCreams.value = sortedList
    }

}
