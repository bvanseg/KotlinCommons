package bvanseg.kcommons.collections

import java.util.*


fun <T> cartesianProduct(lists: List<List<T>>): List<List<T>> {
    val resultLists = ArrayList<List<T>>()
    if (lists.isEmpty()) {
        resultLists.add(ArrayList())
        return resultLists
    } else {
        val firstList = lists[0]
        val remainingLists = cartesianProduct(lists.subList(1, lists.size))
        for (condition in firstList) {
            for (remainingList in remainingLists) {
                val resultList = ArrayList<T>()
                resultList.add(condition)
                resultList.addAll(remainingList)
                resultLists.add(resultList)
            }
        }
    }
    return resultLists
}