package com.lodz.android.corekt.anko

import androidx.annotation.IntRange
import com.lodz.android.corekt.array.Groupable
import java.util.*
import kotlin.collections.ArrayList

/**
 * 数组列表帮助类
 * Created by zhouL on 2018/11/2.
 */

/** 数组数据去重 */
inline fun <reified T> Array<T>.deduplication(): Array<*> = LinkedHashSet<T>(toList()).toTypedArray()

/** 列表数据去重 */
fun <T> Collection<T>.deduplication(): Collection<T> = LinkedHashSet<T>(this).toList()

/** 列表转ArrayList */
fun <T> Collection<T>.toArrayList(): ArrayList<T> = ArrayList(this)

/** 数组转ArrayList */
fun <T> Array<T>.toArrayList(): ArrayList<T> = ArrayList(this.toList())

/** 获取数组长度 */
fun Array<*>?.getSize(): Int = if (this == null) 0 else this.size

/** 获取列表长度 */
fun Collection<*>?.getSize(): Int = if (this == null) 0 else this.size

/** 获取数组长度 */
fun Array<*>?.isNullOrEmpty(): Boolean = this == null || this.size == 0

/** 获取列表长度 */
fun Collection<*>?.isNullOrEmpty(): Boolean = this == null || this.size == 0

/** 将数据分组，泛型T可以为String或者实现了Groupable的任意类，[groups]为分组标题，[compareLength]为匹配长度 */
inline fun <reified T> Array<T>.group(groups: Array<String>, @IntRange(from = 1) compareLength: Int = 1): Array<T> = toList().group(groups.toList(), compareLength).toTypedArray()

/** 将数据分组，泛型T可以为String或者实现了Groupable的任意类，[groups]为分组标题，[compareLength]为匹配长度 */
fun <T> Collection<T>.group(groups: Collection<String>, @IntRange(from = 1) compareLength: Int = 1): Collection<T> {
    if (isEmpty() || groups.isEmpty() || compareLength <= 0) {
        return this
    }

    val KEY = System.currentTimeMillis().toString()// 其他分组的key

    val map = LinkedHashMap<String, LinkedList<T>>()
    for (group in groups) {
        val tag = if (group.length <= compareLength) group else group.substring(0, compareLength)
        map.put(tag, LinkedList())
    }
    map.put(KEY, LinkedList())

    for (t in this) {
        if (t is String || t is Groupable) {
            val item = if (t is Groupable) t.getSortStr() else t as String
            if (item.isEmpty()) {
                map.getValue(KEY).add(t)
                continue
            }
            val tag = if (item.length <= compareLength) item else item.substring(0, compareLength)// 获取分组标签
            if (map.get(tag) != null) {
                map.get(tag)!!.add(t)//存在该分组则加入
            } else {
                map.getValue(KEY).add(t)
            }
        } else {
            map.getValue(KEY).add(t)// 未实现Groupable接口直接加入到其他分组中
        }
    }

    val results = LinkedList<T>()
    for (entry in map) {
        val list = entry.value
        if (list.isNotEmpty()) {
            results.addAll(list)
        }
    }
    return results
}

/** 通过索引文字[indexText]来获取原数据列表里的首个item位置，[groups]为分组标题 */
fun <T> Collection<T>.getPositionByIndex(groups: List<String>, indexText: String): Int {
    if (isEmpty() || groups.isEmpty() || indexText.isEmpty()) {
        return 0
    }
    for ((i, t) in withIndex()) {
        if (t is Groupable || t is String) {
            val item = if (t is Groupable) t.getSortStr() else t as String
            if (item.isEmpty()) {
                continue
            }
            if (item.length <= indexText.length) {// 列表文字长度小于等于索引字符长度
                if (item.equals(indexText.substring(0, item.length))) {
                    return i
                }
            } else {
                if (indexText.equals(item.substring(0, indexText.length))) {
                    return i
                }
            }
        }
    }

    // 都没有匹配到说明数据里没有indexText的内容，则匹配他的前一位
    val position = groups.indexOf(indexText)
    if (position <= 0) {
        return 0
    }
    return getPositionByIndex(groups, groups.get(position - 1))
}







