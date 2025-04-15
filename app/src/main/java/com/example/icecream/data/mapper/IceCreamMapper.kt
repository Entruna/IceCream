package com.example.icecream.data.mapper

import com.example.icecream.common.model.Status
import com.example.icecream.common.res.IceCreamStringResourceMapper
import com.example.icecream.data.local.entity.BasePriceEntity
import com.example.icecream.data.local.entity.IceCreamEntity
import com.example.icecream.data.remote.model.IceCreamDTO
import com.example.icecream.data.remote.model.IceCreamResponse
import com.example.icecream.presentation.model.IceCreamUIModel
import javax.inject.Inject

class IceCreamMapper @Inject constructor() {

    fun mapToUIModel(entity: IceCreamEntity, price: Double): IceCreamUIModel {
        return IceCreamUIModel(
            id = entity.id,
            name = entity.name,
            imageUrl = entity.imageUrl,
            status = entity.status,
            price = price,
            nameResId = entity.nameResId
        )
    }

    fun mapToUIModelList(entities: List<IceCreamEntity>, price: Double): List<IceCreamUIModel> {
        return entities.map { entity ->
            mapToUIModel(entity, price)
        }
    }


    fun mapToEntity(iceCreamApiModel: IceCreamDTO): IceCreamEntity {
        return IceCreamEntity(
            id = iceCreamApiModel.id,
            name = iceCreamApiModel.name,
            status = Status.valueOf(iceCreamApiModel.status.uppercase()),
            imageUrl = iceCreamApiModel.imageUrl,
            nameResId = IceCreamStringResourceMapper.getResIdByName(iceCreamApiModel.name)
        )
    }

    fun mapToBasePriceEntity(basePrice: Double) : BasePriceEntity{
        return BasePriceEntity(price = basePrice)
    }

    fun mapToEntityList(iceCreamApiModels: List<IceCreamDTO>): List<IceCreamEntity> {
        return iceCreamApiModels.map { mapToEntity(it) }
    }
}