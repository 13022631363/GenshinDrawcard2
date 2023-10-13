package cn.gionrose.facered.genshinDrawcard2.api.card

/**
 * @description 卡片星级
 * @author facered
 * @date 2023/10/6 22:19
 */
enum class CardStarGrade (val level: Int, val content : String)
{
    THREE_STAR (3, "三星"),
    FOUR_STAR (4, "四星"),
    FIVE_STAR (5, "五星");

    companion object
    {
        /**
         * 通过等级获取星级
         * 默认返回 3 星
         */
        @JvmStatic
        fun getStarGrade (level: Int): CardStarGrade
        {
            CardStarGrade.values().forEach {
                if (it.level == level)
                {
                    return it
                }
            }
            return THREE_STAR

        }
    }

}