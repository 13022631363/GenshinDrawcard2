package cn.gionrose.facered.genshinDrawcard2.util

import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.*


/**
 * 所谓的序列化
 * 是指。将对象形式 用 key 和 value 的形式存储起来
 * 再用 json 将 map 转成字符串格式存储
 *
 * 反序列化
 * 是指， 将 map 的 json 的字符串格式重新转为 map
 * 再用 key value 重新构建一个对象
 */

    val BYPASS_CLASS = arrayOf(
    //红石元件
    "CraftMetaBlockState",
    //普通物品
    "CraftMetaItem",
    "GlowMetaItem")

    /**
     * 首先尝试将 itemStack 进行序列化操作
     */
    fun ItemStack.serializeF (): Map<String, Any>
    {
        /* 创建一个可变 map */
        val  result = mapOf<String, Any>().toMutableMap()

         /* itemStack 的 type 参数     itemStack type 先放入 map
                                       如果  type == AIR 就直接返回 map
                                       如果不是就继续下去  */
        result["type"] = this.type.name.takeIf { it != "AIR" } ?: return result.apply {
            this["type"] = this@serializeF.type.name
        }

        this.amount.let {
            result["amount"] = it
        }
        /* itemStack 中 itemMeta 的 使用了多少耐久        如果 durability == 0 就不存入 map
           例如 90/100  durability = 10                如果 durability > 0 就存入 map */
        this.durability.takeIf { it != 0.toShort ()}?.let {
            result["durability"] = it
        }

        /* itemStack 中的 itemMeta 信息*/
        this.takeIf { it.hasItemMeta() }?.let {itemStack->

            /* 如果这个 item 存在 meta 信息
            *  则创建一个存 meta 信息的 map*/
            val  meta = mapOf<String, Any>().toMutableMap()
            val itemMeta: ItemMeta = itemStack.itemMeta!!

            /* 如果 meta 存在 displayName 就存入 map 中
            *  如果不存在就什么都不做*/
            itemMeta.takeIf { it.hasDisplayName() }?.let {
                meta["displayName"] = it.displayName
            }

            itemMeta.takeIf { it.hasCustomModelData() }?.let {
                meta["customModelData"] = it.customModelData
            }
//            if (GenshinDrawcard2.plugin.server.version)
//            itemMeta.takeIf { it.hasAttributeModifiers() }?.let {
//                val attributeModifiers = arrayListOf<String>()
//                it.attributeModifiers!!.forEach {attribute, modifiers ->
//                    attributeModifiers.add("${attribute.name}:${modifiers.name}")
//                }
//                meta["attributeModifiers"]= attributeModifiers
//            }

            itemMeta.takeIf { it.hasAttributeModifiers() }?.let {
                val attributeModifiers = arrayListOf<String>()
                it.attributeModifiers!!.asMap().forEach { (attribute, modifiers) ->
                    modifiers.forEach {modifier->
                        attributeModifiers.add("${attribute.name}:${modifier.name}")
                    }
                }
                meta["attributeModifiers"]= attributeModifiers
            }

            /* 如果 meta 存在 lore 转存入 String 泛型的 ArrayList 中
            * 并放入 map
            *  如果不存在就什么都不做*/
            itemMeta.takeIf { it.hasLore() }?.let {
                val lore = arrayListOf<String>()
                itemMeta.lore!!.forEach {
                    lore.add(it)
                }
                meta["lore"] = lore.toList()
            }

            /* 如果 meta 存在 enchants 转存入 String 泛型的 ArrayList 中
            * 并放入 map
            *  如果不存在就什么都不做*/
            itemMeta.takeIf { it.hasEnchants () }?.let {
                val enchants = arrayListOf<String>()
                it.enchants.forEach { (enchantment, level) ->
                    enchants.add ("${enchantment.key}:$level")
                }
                meta["enchants"] = enchants.toList()
            }

            /* 如果 meta 存在 itemFlags 转存入 String 泛型的 ArrayList 中
             * 并放入 map
            *  如果不存在就什么都不做*/
            itemMeta.takeIf {it.itemFlags.isNotEmpty()}?.let {
                val itemFlags = arrayListOf<String>()
                it.itemFlags.stream().map(ItemFlag::name).forEach {flagName ->
                    itemFlags.add(flagName)
                }
                meta["flag"] = itemFlags.toList()
            }

            /* 从此开始
            *  如果它的类名是 BYPASS_CLASS 其中之一
            *  就不需要在进行序列化了*/
            BYPASS_CLASS.forEach { passCassName->
                meta.takeIf { it::class.java.simpleName == passCassName }?.let {
                    meta.takeIf { it.isNotEmpty() }?.let {
                        result["item-meta"] = it.toMap()
                    }
                    return result.toMap()
                }
            }


            itemMeta.takeIf { meta is SkullMeta }?.let {

            }

             when (itemMeta) {
                //头颅
                is SkullMeta -> {
                    itemMeta.takeIf { skullMeta -> skullMeta.hasOwner() }?.let {
                        //头颅所有者名字
                        mutableMapOf<String, Any>(Pair("owner",it.owner!!)).toMap ()
                    }
                }
                //旗帜
                is BannerMeta -> {


                    val extraMeta = mutableMapOf<String, Any>()
                    //旗帜基础配色 默认 null 需要设置
                    itemMeta.baseColor?.let { extraMeta["base-color"] = it }

                    itemMeta.takeIf { it.numberOfPatterns() > 0 }?.let {
                        //图案和颜色
                        var patterns = arrayListOf<String>()
                        it.patterns.stream().map {pattern-> "${pattern.color.name}:${pattern.pattern.identifier}"}
                            .forEach { str-> patterns.add(str) }
                        extraMeta["patterns"] = patterns.toList ()
                        extraMeta.toMap()
                    }
                }
                 //皮革材质的四件套
                 //由于皮革材质不同于别的装备材质 (可染色)
                is LeatherArmorMeta -> {
                    mutableMapOf<String, Any>(Pair("color", itemMeta.color.asRGB())).toMap()
                }
                is BookMeta -> {
                    //首先判断参数是否有一项存在
                    itemMeta.takeIf { it.hasAuthor() || it.hasTitle() || it.hasPages() }?.let {
                        val extra = mutableMapOf<String, Any>()
                        it.takeIf { it.hasTitle() }?.let {bookMeta ->
                           extra["title"] = bookMeta.title!!
                        }
                        it.takeIf { it.hasPages() }?.let {bookMeta ->
                            val pages = arrayListOf<String>()
                            bookMeta.pages.forEach {page ->
                                pages.add(page)
                            }
                            extra["pages"] = pages.toList ()
                        }
                        it.takeIf { it.hasAuthor() }?.let {bookMeta ->
                            extra["author"] = bookMeta.author!!
                        }
                        extra.toMap()
                    }
                }
                is PotionMeta -> {
                    val extra = mutableMapOf<String, Any>()

                    //基础药水类型
                    extra["basePotionType"] = itemMeta.basePotionData.type
                    //是否是投掷药水
                    extra["isExtended"] = itemMeta.basePotionData.isExtended
                    //是否是滞留药水
                    extra["isUpgraded"] = itemMeta.basePotionData.isUpgraded

                    itemMeta.takeIf { it.hasCustomEffects() }?.let {
                        val customEffects = arrayListOf<Map<String, Any>>()


                        it.customEffects.forEach { effect ->
                            val customEffect = mutableMapOf<String, Any>()
                            //效果的倍率
                            customEffect["amplifier"] = effect.amplifier
                            //效果持续时间 (tick)
                            customEffect["duration"] = effect.duration
                            //此状态使药水效果产生更多的、半透明的粒子
                            customEffect["isAmbient"] = effect.isAmbient
                            customEffect["hasIcon"] = effect.hasIcon()
                            customEffect["hasParticles"] = effect.hasParticles()
                            customEffect["type"] = effect.type.name
                            customEffects.add(customEffect.toMap ())
                        }
                        extra["custom-effects"] = customEffects.toList ()
                        extra.toMap()
                    }
                }
                is FireworkEffectMeta -> {
                    itemMeta.takeIf { it.hasEffect() }?.let {fireworkEffectMeta ->
                        val extra = mutableMapOf<String, Any>()
                        val effect = fireworkEffectMeta.effect
                        extra["type"] = effect!!.type.name
                        //闪烁效果
                        effect.takeIf { it.hasFlicker() }?.let {
                            extra["flicker"] = true
                        }
                        //尾迹效果
                        effect.takeIf { it.hasTrail() }?.let {
                            extra["trail"] = true
                        }
                        //烟花效果的主颜色
                        effect.takeIf { it.colors.isNotEmpty() }?.let {
                            val colors = arrayListOf<Int>()
                            it.colors.forEach {color ->
                                colors.add(color.asRGB())
                            }
                            extra["colors"] = colors.toList ()
                        }
                        //烟花效果的淡出颜色
                        effect.takeIf { it.fadeColors.isNotEmpty() }?.let {
                            val fadeColors = arrayListOf<Int>()
                            it.fadeColors.forEach {color ->
                                fadeColors.add(color.asRGB())
                            }
                            extra["fadeColors"] = fadeColors.toList ()
                        }
                        extra.toMap()
                    }
                }
                is FireworkMeta -> {
                    val extra = mutableMapOf<String, Any>()
                    extra["power"] = itemMeta.power

                    itemMeta.takeIf { it.hasEffects() }?.let {fireworkMeta ->
                        val effects = arrayListOf<Any>()

                        fireworkMeta.effects.forEach {
                            val effect = mutableMapOf<String, Any>()
                            extra["type"] = it.type.name
                            //闪烁效果
                            it.takeIf { it.hasFlicker() }?.let {
                                effect["flicker"] = true
                            }
                            //尾迹效果
                            it.takeIf { it.hasTrail() }?.let {
                                effect["trail"] = true
                            }

                            it.takeIf { it.colors.isNotEmpty() }?.let {
                                val colors = arrayListOf<Int>()
                                it.colors.forEach {color ->
                                    colors.add(color.asRGB())
                                }
                                effect["colors"] = colors.toList ()
                            }
                            //烟花效果的淡出颜色
                            it.takeIf { it.fadeColors.isNotEmpty() }?.let {
                                val fadeColors = arrayListOf<Int>()
                                it.fadeColors.forEach {color ->
                                    fadeColors.add(color.asRGB())
                                }
                                effect["fadeColors"] = fadeColors.toList ()
                            }
                            effects.add(effect.toList())
                        }
                        extra["effects"] = effects
                        extra.toMap()
                    }
                }
                is MapMeta -> {
                    val extra = mutableMapOf<String, Any>()
                    itemMeta.takeIf { it.hasLocationName() }?.let {mapMeta ->
                        extra["location-name"] = mapMeta.locationName!!
                    }
                    itemMeta.takeIf { it.hasColor() }?.let {mapMeta ->
                        extra["color"] = mapMeta.color!!.asRGB()
                    }
                    extra["scaling"] = itemMeta.isScaling

                    itemMeta.takeIf { it.hasMapView() }?.let {mapData ->
                        val mapView = mapData.mapView!!
                        val view = mutableMapOf<String, Any>()
                        view["id"] = mapView.id
                        view["centerX"] = mapView.centerX
                        view["centerZ"] = mapView.centerZ
                        view["isLocked"] = mapView.isLocked
                        //是虚拟的
                        view["isVirtual"] = mapView.isVirtual
                        //跟踪位置
                        view["isTrackingPostion"] = mapView.isTrackingPosition
                        //无限跟踪
                        view["isUnlimitedTracking"] = mapView.isUnlimitedTracking
                        //规模
                        view["scale"] = mapView.scale.name
                        mapView.world?.let {
                            view["world"] = it.name
                        }

                        extra["view"] = view.toMap ()
                    }
                    extra.toMap()
                }
                else -> {
                    //如果不匹配就返回 empty map
                    emptyMap()
                }
            }?.takeIf { it.isNotEmpty() }?.let {
                //它不为 empty 时 存入 meta map 中
                meta["extra-meta"] = it
            }


            /* meta 不是空 map 的就 转为不可变 map 存入 result map 中*/
            meta.takeIf { it.isNotEmpty() }?.let {
                result["item-meta"] = it.toMap()
            }
        }


        return result.toMap()
    }

fun Map<String, Any>.deserializeF (): ItemStack
{
    var item: ItemStack? = null

    //这里json后会出现int 变成double 的情况。而不用json就int 还是int
    var amount= this["amount"] as Double




    item = if (this["type"] != "AIR")
        ItemStack (Material.valueOf(this["type"].toString()),amount.toInt())
    else
        ItemStack (Material.AIR, 1)

    return item
}


