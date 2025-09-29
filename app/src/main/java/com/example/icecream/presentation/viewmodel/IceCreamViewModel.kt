package com.example.icecream.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.icecream.common.model.Status
import com.example.icecream.data.mapper.IceCreamMapper
import com.example.icecream.domain.repository.IceCreamRepository
import com.example.icecream.domain.usecase.InitAppDataUseCase
import com.example.icecream.presentation.model.IceCreamUIModel
import com.example.icecream.presentation.viewmodel.extension.launchIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject


@HiltViewModel
class IceCreamViewModel @Inject constructor(
    private val iceCreamRepository: IceCreamRepository,
    private val iceCreamMapper: IceCreamMapper,
    private val initAppDataUseCase: InitAppDataUseCase
) : ViewModel() {

    private val _sortedIceCreams: MutableStateFlow<List<IceCreamUIModel>> =
        MutableStateFlow(emptyList())
    val sortedIceCreams: StateFlow<List<IceCreamUIModel>> = _sortedIceCreams.asStateFlow()


    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError.asStateFlow()

    init {
        initIceCreams()

        launchIO {
            iceCreamRepository.getIceCreamsFromDbFlow()
                .combine(iceCreamRepository.getBasePriceFlow()) { iceCreamEntities, basePrice ->
                    iceCreamMapper.mapToUIModelList(iceCreamEntities, basePrice)
                }
                .collect { list ->
                    _sortedIceCreams.value = list
                }
        }
    }

    fun initIceCreams() {
        launchIO {
            _isLoading.value = true
            _isError.value = false
            try {
                val result = initAppDataUseCase()
                if (result.isFailure) {
                    _isError.value = true
                }
            } catch (e: Exception) {
                _isError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun sortIceCreamsByStatus(status: Status) {
        _sortedIceCreams.value = sortedIceCreams.value.sortedBy { iceCream ->
            when (status) {
                Status.AVAILABLE -> if (iceCream.status == Status.AVAILABLE) 0 else 1
                Status.UNAVAILABLE -> if (iceCream.status == Status.UNAVAILABLE) 0 else 1
                Status.MELTED -> if (iceCream.status == Status.MELTED) 0 else 1
            }
        }
    }

}
