package com.example.icecream.mapper

import com.example.icecream.common.res.CategoryStringResourceMapper
import com.example.icecream.common.res.ExtraStringResourceMapper
import com.example.icecream.data.local.entity.ExtraCategoryEntity
import com.example.icecream.data.local.entity.ExtraCategoryWithExtras
import com.example.icecream.data.local.entity.ExtraEntity
import com.example.icecream.data.mapper.ExtraMapper
import com.example.icecream.data.remote.model.ExtraCategoryDTO
import com.example.icecream.data.remote.model.ExtraDTO
import junit.framework.TestCase
import org.junit.Test

class ExtraMapperTest {

    private val extraMapper = ExtraMapper()

    @Test
    fun testMapToExtraCategoryUIModel() {
        val extraCategoryEntity = ExtraCategoryEntity(
            id = 1L,
            type = "Toppings",
            required = true,
            nameResId = 123
        )

        val result = extraMapper.mapToExtraCategoryUIModel(extraCategoryEntity)

        TestCase.assertEquals(extraCategoryEntity.id, result.id)
        TestCase.assertEquals(extraCategoryEntity.type, result.type)
        TestCase.assertEquals(extraCategoryEntity.required, result.required)
        TestCase.assertEquals(extraCategoryEntity.nameResId, result.nameResId)
    }

    @Test
    fun testMapToExtraUIModel() {
        val extraCategoryEntity = ExtraCategoryEntity(
            id = 1L,
            type = "Toppings",
            required = true,
            nameResId = 123
        )
        val extraEntity = ExtraEntity(
            id = 1L,
            name = "Choco Chips",
            price = 1.0,
            categoryId = 1L,
            nameResId = 456
        )

        val result = extraMapper.mapToExtraUIModel(extraEntity, extraCategoryEntity)

        TestCase.assertEquals(extraEntity.id, result.id)
        TestCase.assertEquals(extraEntity.name, result.name)
        TestCase.assertEquals(extraEntity.price, result.price)
        TestCase.assertEquals(extraCategoryEntity.id, result.category.id)
        TestCase.assertEquals(extraEntity.nameResId, result.nameResId)
    }

    @Test
    fun testMapToExtraCategoryWithExtrasUIModel() {
        val category = ExtraCategoryEntity(1L, "Toppings", true, 101)
        val extras = listOf(
            ExtraEntity(1L, "Choco Chips", 1.0, 1L, 201),
            ExtraEntity(2L, "Sprinkles", 0.5, 1L, 202)
        )

        val input = ExtraCategoryWithExtras(category, extras)
        val result = extraMapper.mapToExtraCategoryWithExtrasUIModel(input)

        TestCase.assertEquals(category.id, result.category.id)
        TestCase.assertEquals(2, result.extras.size)
        TestCase.assertEquals("Choco Chips", result.extras[0].name)
    }

    @Test
    fun testMapToExtraCategoryWithExtrasUIModelList() {
        val list = listOf(
            ExtraCategoryWithExtras(
                ExtraCategoryEntity(1L, "Toppings", true, 101),
                listOf(ExtraEntity(1L, "Choco Chips", 1.0, 1L, 201))
            )
        )

        val result = extraMapper.mapToExtraCategoryWithExtrasUIModelList(list)

        TestCase.assertEquals(1, result.size)
        TestCase.assertEquals("Choco Chips", result[0].extras[0].name)
    }

    @Test
    fun testMapToExtraCategoryEntity() {
        val extraDTO = ExtraDTO(
            id = 1L,
            name = "Choco Chips",
            price = 2.0
        )
        val extraDTO2 = ExtraDTO(
            id = 2L,
            name = "Sprinkles",
            price = 1.0
        )
        val dto = ExtraCategoryDTO("Toppings", true, listOf(extraDTO, extraDTO2))
        val result = extraMapper.mapToExtraCategoryEntity(dto)

        TestCase.assertEquals(dto.type, result.type)
        TestCase.assertEquals(dto.required, result.required)
        TestCase.assertEquals(
            CategoryStringResourceMapper.getResIdByName(dto.type),
            result.nameResId
        )
    }

    @Test
    fun testMapToExtraEntity() {
        val dto = ExtraDTO(1L, "Choco Chips", 1.2)
        val categoryId = 10L

        val result = extraMapper.mapToExtraEntity(dto, categoryId)

        TestCase.assertEquals(dto.id, result.id)
        TestCase.assertEquals(dto.name, result.name)
        TestCase.assertEquals(dto.price, result.price)
        TestCase.assertEquals(categoryId, result.categoryId)
        TestCase.assertEquals(ExtraStringResourceMapper.getResIdByName(dto.name), result.nameResId)
    }

}