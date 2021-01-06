/*
 * MIT License
 *
 * Copyright (c) 2021 Boston Vanseghi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package bvanseg.kotlincommons.collection

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

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
fun <T> linkedListOf(): LinkedList<T> = LinkedList()

/**
 * @author Boston Vanseghi
 * @since 2.7.0
 */
inline fun <reified T> List<T>.toLinkedList(): LinkedList<T> {
    val llist = LinkedList<T>()
    for(e in this) {
        llist.add(e)
    }

    return llist
}