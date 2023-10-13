package cn.gionrose.facered.genshinDrawcard2.api.card

import cn.gionrose.facered.genshinDrawcard2.GenshinDrawcard2
import com.skillw.pouvoir.api.plugin.map.component.Registrable



/**
 * @description 奖池
 * @author facered
 * @date 2023/10/7 12:17
 */
class CardPool private constructor(override val key: String,
               val threeStarWeight: Int,
               val fourStarWeight: Int,
               val fiveStarWeight: Int,
               val threeSmallGuaranteeCount: Int,
               val fourSmallGuaranteeCount: Int,
               val fiveSmallGuaranteeCount: Int,
               val isEnabledThreeStarBigGuarantee: Boolean,
               val isEnabledFourStarBigGuarantee: Boolean,
               val isEnabledFiveStarBigGuarantee: Boolean,
               val period: Long
): Registrable<String>{



    /**
     * 结算动画
     */
    val displayAnimationLayout: List<List<String>> = listOf()

    /**
     * 结算界面
     */
    val billingScreenLayout: List<List<String>> = listOf()

    /**
     * 预览界面
     */
    val previewScreenLayout: List<List<String>> = listOf()



    override fun register() {
        GenshinDrawcard2.cardPoolManager.registerPool(this)
    }

    class Builder (private val key: String, init: Builder.() -> Unit)
    {
        var threeStarWeight: Int = 1
        var fourStarWeight: Int = 1
        var fiveStarWeight: Int = 1
        var threeSmallGuaranteeCount: Int = 10
        var fourSmallGuaranteeCount: Int = 10
        var fiveSmallGuaranteeCount: Int = 10
        var isEnabledThreeStarBigGuarantee: Boolean = true
        var isEnabledFourStarBigGuarantee: Boolean = true
        var isEnabledFiveStarBigGuarantee: Boolean = true
        var period: Long = 20

        init {
            init (this)
        }

        fun builder (): CardPool
        {
            return CardPool (key, threeStarWeight, fourStarWeight, fiveStarWeight, threeSmallGuaranteeCount, fourSmallGuaranteeCount, fiveSmallGuaranteeCount, isEnabledThreeStarBigGuarantee, isEnabledFourStarBigGuarantee, isEnabledFiveStarBigGuarantee, period)
        }
    }

    companion object
    {
        fun create ( key: String, init: Builder.() -> Unit): CardPool
        {
            return Builder (key, init).builder ()
        }
    }




}