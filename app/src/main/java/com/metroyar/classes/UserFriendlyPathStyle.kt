package com.metroyar.classes

import android.text.BidiFormatter
import java.util.Locale

class UserFriendlyPathStyle(private val pathResultList: List<String>) {
    val result = mutableListOf<String>()
    val expandableItems: MutableMap<String, MutableList<String>> =
        mutableMapOf()

    init {
        generateLastResult()
    }

    private fun processOnFirstElement(): String {
        val splitString = pathResultList.first().split("به سمت")
        val bidiFormatter = BidiFormatter.getInstance(Locale("fa"))

        val formattedString =
            " وارد ایستگاه ${bidiFormatter.unicodeWrap(splitString[0])} بشید و به سمت ${
                bidiFormatter.unicodeWrap(splitString[1])
            }  حرکت کنید "
        return formattedString
    }

    private fun processLastElement(): String {
        val bidiFormatter = BidiFormatter.getInstance(Locale("fa"))

        val formattedString =
            "به ایستگاه ${bidiFormatter.unicodeWrap(pathResultList.last())} که رسیدید از قطار پیاده شید ${
                bidiFormatter.unicodeWrap(
                    "\uD83D\uDE03"
                )
            } "
        return formattedString
    }


    private fun processOnInterchangeStation(input: String): String {
        val splitString = input.split("به سمت")

        val bidiFormatter = BidiFormatter.getInstance(Locale("fa"))

        val formattedString =
            " به ایستگاه ${bidiFormatter.unicodeWrap(splitString[0])}که رسیدید، به سمت ${
                bidiFormatter.unicodeWrap(splitString[1])
            } خط عوض کنید "

        return formattedString
    }

    fun generateLastResult(): List<String> {
        var i = -1
        while (i <= pathResultList.lastIndex) {
            try {
                if (i == pathResultList.lastIndex) {
                    for (expandableItem in expandableItems.keys) {
                        result.add(expandableItem)
                    }
                    result.add(processLastElement())
                    break
                }
                i++
                if (pathResultList[i].contains("به سمت")) {
                    val children =
                        mutableListOf<String>().apply { add(pathResultList[i].split("به سمت")[0]) }
                    var key = ""
                    if (i == 0) {
                        key = processOnFirstElement()
                        expandableItems[key] =
                            children
                    } else {
                        key = processOnInterchangeStation(pathResultList[i])
                        expandableItems[key] = children
                    }

                    while (i + 1 < pathResultList.size) {
                        if (pathResultList[i + 1].contains("به سمت")) {
                            expandableItems[key]?.add(pathResultList[i + 1].split("به سمت")[0])
                            break
                        }
                        i++
                        expandableItems[key]?.add(pathResultList[i])
                    }
                }
            } catch (e: Exception) {
            }
        }
        return result
    }
}
