package com.example.icecream.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.icecream.data.mapper.ExtraMapper
import com.example.icecream.domain.repository.ExtraRepository
import com.example.icecream.presentation.model.ExtraCategoryWithExtrasUIModel
import com.example.icecream.presentation.model.ExtraUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class ExtraViewModel @Inject constructor(
    private val extraRepository: ExtraRepository,
    private val extraMapper: ExtraMapper
) : ViewModel() {

    val categoriesWithExtras: StateFlow<List<ExtraCategoryWithExtrasUIModel>> =
        extraRepository.getCategoriesWithExtrasFlow()
            .map { categories ->
                extraMapper.mapToExtraCategoryWithExtrasUIModelList(categories)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList()
            )

    private val _selectedExtras = MutableStateFlow<List<ExtraUIModel>>(emptyList())
    val selectedExtras: StateFlow<List<ExtraUIModel>> = _selectedExtras.asStateFlow()

    fun toggleExtraSelection(extra: ExtraUIModel) {
        val currentSelected = _selectedExtras.value.toMutableList()
        if (currentSelected.contains(extra)) {
            currentSelected.remove(extra)
        } else {
            currentSelected.add(extra)
        }
        _selectedExtras.value = currentSelected
    }

    fun setSelectedExtras(extras: List<ExtraUIModel>) {
        _selectedExtras.value = extras
    }

    fun clearSelectedExtras() {
        _selectedExtras.value = emptyList()
    }


}
