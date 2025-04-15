package com.example.icecream.data.mapper

import com.example.icecream.common.res.CategoryStringResourceMapper
import com.example.icecream.common.res.ExtraStringResourceMapper
import com.example.icecream.data.local.entity.ExtraCategoryEntity
import com.example.icecream.data.local.entity.ExtraCategoryWithExtras
import com.example.icecream.data.local.entity.ExtraEntity
import com.example.icecream.data.remote.model.ExtraCategoryDTO
import com.example.icecream.data.remote.model.ExtraDTO
import com.example.icecream.presentation.model.ExtraCategoryUIModel
import com.example.icecream.presentation.model.ExtraCategoryWithExtrasUIModel
import com.example.icecream.presentation.model.ExtraUIModel
import javax.inject.Inject

class ExtraMapper @Inject constructor() {

    fun mapToExtraCategoryUIModel(entity: ExtraCategoryEntity): ExtraCategoryUIModel {
        return ExtraCategoryUIModel(
            id = entity.id,
            type = entity.type,
            required = entity.required,
            nameResId = entity.nameResId
        )
    }

    fun mapToExtraUIModel(entity: ExtraEntity, category: ExtraCategoryEntity): ExtraUIModel {
        return ExtraUIModel(
            id = entity.id,
            name = entity.name,
            price = entity.price,
            category = mapToExtraCategoryUIModel(category),
            nameResId = entity.nameResId
        )
    }

    fun mapToExtraCategoryWithExtrasUIModel(entity: ExtraCategoryWithExtras): ExtraCategoryWithExtrasUIModel {
        return ExtraCategoryWithExtrasUIModel(
            category = mapToExtraCategoryUIModel(entity.category),
            extras = entity.extras.map { it -> mapToExtraUIModel(it, entity.category) }
        )
    }

    fun mapToExtraCategoryWithExtrasUIModelList(entities: List<ExtraCategoryWithExtras>): List<ExtraCategoryWithExtrasUIModel> {
        return entities.map { entity ->
            mapToExtraCategoryWithExtrasUIModel(entity)
        }
    }

    fun mapToExtraCategoryEntity(dto: ExtraCategoryDTO): ExtraCategoryEntity {
        return ExtraCategoryEntity(
            type = dto.type,
            required = dto.required,
            nameResId = CategoryStringResourceMapper.getResIdByName(dto.type)
        )
    }


    fun mapToExtraEntity(item: ExtraDTO, categoryId: Long): ExtraEntity {
        return ExtraEntity(
            id = item.id,
            name = item.name,
            price = item.price,
            categoryId = categoryId,
            nameResId = ExtraStringResourceMapper.getResIdByName(item.name)
        )
    }

}