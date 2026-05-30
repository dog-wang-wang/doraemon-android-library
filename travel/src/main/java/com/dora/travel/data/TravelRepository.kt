package com.dora.travel.data

import com.dora.travel.model.TravelCategory
import com.dora.travel.model.TravelItem

object TravelRepository {
    fun getFeaturedItems(): List<TravelItem> = listOf(
        TravelItem(
            id = 1,
            title = "马尔代夫水上别墅",
            description = "享受纯净的海水和私密的度假体验。这里有最美的落日和最清澈的浮潜环境。",
            price = "¥5999",
            imageUrl = "https://images.unsplash.com/photo-1514282401047-d79a71a590e8",
            rating = 4.9f,
            location = "马尔代夫"
        ),
        TravelItem(
            id = 2,
            title = "瑞士阿尔卑斯山小镇",
            description = "在雪山环绕中呼吸新鲜空气，体验地道的欧洲风情。适合远足和滑雪爱好者。",
            price = "¥8800",
            imageUrl = "https://images.unsplash.com/photo-1531210483974-4f8c1f33fd35",
            rating = 4.8f,
            location = "瑞士"
        ),
        TravelItem(
            id = 3,
            title = "京都清水寺之旅",
            description = "感受日本传统的禅意之美。樱花季或红叶季的清水寺景色如诗如画。",
            price = "¥3200",
            imageUrl = "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e",
            rating = 4.7f,
            location = "日本, 京都"
        ),
        TravelItem(
            id = 4,
            title = "冰岛极光探险",
            description = "追寻北极光的脚步，探索外星般的冰岛地貌。包括蓝湖温泉和黄金圈之旅。",
            price = "¥12000",
            imageUrl = "https://images.unsplash.com/photo-1476610182048-b716b8518aae",
            rating = 5.0f,
            location = "冰岛"
        )
    )

    fun getCategories(): List<TravelCategory> = listOf(
        TravelCategory(1, "海滩"),
        TravelCategory(2, "山脉"),
        TravelCategory(3, "城市"),
        TravelCategory(4, "冒险"),
        TravelCategory(5, "文化")
    )
}
