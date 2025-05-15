package com.example.icecream.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.icecream.data.mapper.ExtraMapper
import com.example.icecream.domain.repository.ExtraRepository
import com.example.icecream.presentation.model.ExtraCategoryWithExtrasUIModel
import com.example.icecream.presentation.model.ExtraUIModel
import com.example.icecream.presentation.viewmodel.extension.launchIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class ExtraViewModel @Inject constructor(
    private val extraRepository: ExtraRepository,
    private val extraMapper: ExtraMapper
) : ViewModel() {

    private val _categoriesWithExtras =
        MutableStateFlow<List<ExtraCategoryWithExtrasUIModel>>(emptyList())
    val categoriesWithExtras: StateFlow<List<ExtraCategoryWithExtrasUIModel>> =
        _categoriesWithExtras.asStateFlow()

    private val _selectedExtras = MutableStateFlow<List<ExtraUIModel>>(emptyList())
    val selectedExtras: StateFlow<List<ExtraUIModel>> = _selectedExtras.asStateFlow()

    init {
        loadExtras()
    }

    private fun loadExtras() {
        launchIO {
            val categories = extraRepository.getCategoriesWithExtrasFromDb()
            _categoriesWithExtras.value =
                extraMapper.mapToExtraCategoryWithExtrasUIModelList(categories)
        }
    }

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
