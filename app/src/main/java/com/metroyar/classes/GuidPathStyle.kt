package com.metroyar.classes

import android.text.BidiFormatter
import java.util.Locale

class GuidPathStyle(private val readablePathFormStringList: List<String>) {
    val guidPathStyleStringList = mutableListOf<String>()
    val mapOfGuidPathToItsChildren: MutableMap<String, MutableList<String>> =
        mutableMapOf()

    init {
        fillGuidStyleStringAndItsChildren()
    }

    private fun processOnFirstElement(): String {
        val splitString = readablePathFormStringList.first().split("به سمت")
        val persianFormatter = BidiFormatter.getInstance(Locale("fa"))
        return " وارد ایستگاه ${persianFormatter.unicodeWrap(splitString[0])}بشید و به سمت${
            persianFormatter.unicodeWrap(splitString[1])
        } حرکت کنید "
    }

    private fun processLastElement(): String {
        val persianFormatter = BidiFormatter.getInstance(Locale("fa"))
        return "به ایستگاه ${persianFormatter.unicodeWrap(readablePathFormStringList.last())} که رسیدید از قطار پیاده شید  "
    }

    private fun processOnInterchangeStation(input: String): String {
        val splitString = input.split("به سمت")
        val persianFormatter = BidiFormatter.getInstance(Locale("fa"))
        return " به ایستگاه ${persianFormatter.unicodeWrap(splitString[0])}که رسیدید، به سمت${
            persianFormatter.unicodeWrap(splitString[1])
        } خط عوض کنید "
    }

    private fun fillGuidStyleStringAndItsChildren(): List<String> {
        var i = -1
        while (i <= readablePathFormStringList.lastIndex) {
            try {
                if (i == readablePathFormStringList.lastIndex) {
                    for (expandableItem in mapOfGuidPathToItsChildren.keys) {
                        guidPathStyleStringList.add(expandableItem)
                    }
                    guidPathStyleStringList.add(processLastElement())
                    break
                }
                i++
                if (readablePathFormStringList[i].contains("به سمت")) {
                    val children = mutableListOf<String>()
                    var key: String
                    if (i == 0) {
                        key = processOnFirstElement()
                        mapOfGuidPathToItsChildren[key] =
                            children
                    } else {
                        key = processOnInterchangeStation(readablePathFormStringList[i])
                        mapOfGuidPathToItsChildren[key] = children
                    }

                    while (i + 1 < readablePathFormStringList.size) {
                        if (readablePathFormStringList[i + 1].contains("به سمت")) {
                            mapOfGuidPathToItsChildren[key]?.add(
                                readablePathFormStringList[i + 1].split(
                                    "به سمت"
                                )[0]
                            )
                            break
                        }
                        i++
                        mapOfGuidPathToItsChildren[key]?.add(readablePathFormStringList[i])
                    }
                }
            } catch (_: Exception) {
            }
        }
        return guidPathStyleStringList
    }
}