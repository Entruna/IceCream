package com.example.icecream.mapper

import com.example.icecream.common.model.Status
import com.example.icecream.common.res.IceCreamStringResourceMapper
import com.example.icecream.data.local.entity.IceCreamEntity
import com.example.icecream.data.mapper.IceCreamMapper
import com.example.icecream.data.remote.model.IceCreamDTO
import junit.framework.TestCase
import org.junit.Test

class IceCreamMapperTest {

    private val iceCreamMapper = IceCreamMapper()

    @Test
    fun testMapToUIModel() {
        val iceCreamEntity = IceCreamEntity(
            id = 1L,
            name = "Vanilla",
            imageUrl = "image_url",
            status = Status.AVAILABLE,
            nameResId = 123
        )
        val price = 5.0

        val result = iceCreamMapper.mapToUIModel(iceCreamEntity, price)

        TestCase.assertEquals(iceCreamEntity.id, result.id)
        TestCase.assertEquals(iceCreamEntity.name, result.name)
        TestCase.assertEquals(iceCreamEntity.imageUrl, result.imageUrl)
        TestCase.assertEquals(iceCreamEntity.status, result.status)
        TestCase.assertEquals(price, result.price)
        TestCase.assertEquals(iceCreamEntity.nameResId, result.nameResId)
    }

    @Test
    fun testMapToUIModelList() {
        val iceCreamEntity1 = IceCreamEntity(
            id = 1L,
            name = "Vanilla",
            imageUrl = "image_url1",
            status = Status.AVAILABLE,
            nameResId = 123
        )
        val iceCreamEntity2 = IceCreamEntity(
            id = 2L,
            name = "Chocolate",
            imageUrl = "image_url2",
            status = Status.UNAVAILABLE,
            nameResId = 456
        )
        val iceCreamEntities = listOf(iceCreamEntity1, iceCreamEntity2)
        val price = 5.0

        val result = iceCreamMapper.mapToUIModelList(iceCreamEntities, price)

        TestCase.assertEquals(iceCreamEntities.size, result.size)
        TestCase.assertEquals(iceCreamEntities[0].id, result[0].id)
        TestCase.assertEquals(iceCreamEntities[1].id, result[1].id)
    }

    @Test
    fun testMapToEntity() {
        val dto = IceCreamDTO(
            id = 1L,
            name = "Vanilla",
            status = "available",
            imageUrl = "image_url"
        )

        val result = iceCreamMapper.mapToEntity(dto)

        TestCase.assertEquals(dto.id, result.id)
        TestCase.assertEquals(dto.name, result.name)
        TestCase.assertEquals(Status.AVAILABLE, result.status)
        TestCase.assertEquals(dto.imageUrl, result.imageUrl)
        TestCase.assertEquals(
            IceCreamStringResourceMapper.getResIdByName(dto.name),
            result.nameResId
        )
    }

    @Test
    fun testMapToEntityList() {
        val dtoList = listOf(
            IceCreamDTO(1L, "Vanilla", "available", "url1"),
            IceCreamDTO(2L, "Chocolate", "unavailable", "url2")
        )

        val result = iceCreamMapper.mapToEntityList(dtoList)

        TestCase.assertEquals(2, result.size)
        TestCase.assertEquals(dtoList[0].id, result[0].id)
        TestCase.assertEquals(Status.UNAVAILABLE, result[1].status)
    }

    @Test
    fun testMapToBasePriceEntity() {
        val price = 4.5
        val result = iceCreamMapper.mapToBasePriceEntity(price)

        TestCase.assertEquals(price, result.price)
    }
}