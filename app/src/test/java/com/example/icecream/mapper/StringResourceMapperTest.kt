package com.example.icecream.mapper

import com.example.icecream.R
import com.example.icecream.common.res.CategoryStringResourceMapper
import com.example.icecream.common.res.ExtraStringResourceMapper
import com.example.icecream.common.res.IceCreamStringResourceMapper
import org.junit.Assert
import org.junit.Test

class StringResourceMapperTest {

    @Test
    fun testCategoryStringResourceMapper() {
        Assert.assertEquals(
            R.string.cone_category,
            CategoryStringResourceMapper.getResIdByName("Tölcsérek")
        )
        Assert.assertEquals(
            R.string.other_category,
            CategoryStringResourceMapper.getResIdByName("Egyéb")
        )
        Assert.assertEquals(
            R.string.topping_category,
            CategoryStringResourceMapper.getResIdByName("Öntetek")
        )
    }

    @Test
    fun testCategoryStringResourceMapperUnknownKey() {
        Assert.assertNull(CategoryStringResourceMapper.getResIdByName("Nemlétező"))
    }

    @Test
    fun testExtraStringResourceMapper() {
        Assert.assertEquals(
            R.string.normal_cone,
            ExtraStringResourceMapper.getResIdByName("Normál tölcsér")
        )
        Assert.assertEquals(
            R.string.strawberry_syrup,
            ExtraStringResourceMapper.getResIdByName("Eper öntet")
        )
        Assert.assertEquals(R.string.roletti, ExtraStringResourceMapper.getResIdByName("Roletti"))
    }

    @Test
    fun testExtraStringResourceMapperUnknownKey() {
        Assert.assertNull(ExtraStringResourceMapper.getResIdByName("Unicorn tölcsér"))
    }

    @Test
    fun testIceCreamStringResourceMapper() {
        Assert.assertEquals(
            R.string.vanilla,
            IceCreamStringResourceMapper.getResIdByName("Vanília")
        )
        Assert.assertEquals(
            R.string.rum_nut,
            IceCreamStringResourceMapper.getResIdByName("Rumosdió")
        )
        Assert.assertEquals(
            R.string.pistachio,
            IceCreamStringResourceMapper.getResIdByName("Pisztácia")
        )
    }

    @Test
    fun testIceCreamStringResourceMapperUnknownKey() {
        Assert.assertNull(IceCreamStringResourceMapper.getResIdByName("Banán"))
    }
}