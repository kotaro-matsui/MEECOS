package com.example.meecos.Manager

import com.example.meecos.Model.CustomerObject

class DataManager {

    //　親項目に振り分けるための、子項目を行ごとに作成してリストに入れ、配列にして返す
    fun createChildList(): Array<MutableList<String>?> {

        val co = CustomerObject()

        val linesArray: Array<MutableList<String>?> = arrayOfNulls(11)

        for (i in linesArray.indices) {
            val lineList = mutableListOf<String>()
            linesArray[i] = lineList
        }

        for (customer in co.fetchAllCustomerObject()) {
            val customerName = customer.name
            val customerHowToRead = customer.howToRead
            // 読み仮名未設定は#に振り分け
            if (customerHowToRead == "") {
                linesArray[10]?.add(customerName)
            } else {
                when (customerHowToRead.substring(0, 1)) {
                    "ア", "イ", "ウ", "エ", "オ" -> linesArray[0]?.add(customerName)
                    "カ", "キ", "ク", "ケ", "コ", "ガ", "ギ", "グ", "ゲ", "ゴ" -> linesArray[1]?.add(
                        customerName
                    )
                    "サ", "シ", "ス", "セ", "ソ", "ザ", "ジ", "ズ", "ゼ", "ゾ" -> linesArray[2]?.add(
                        customerName
                    )
                    "タ", "チ", "ツ", "テ", "ト", "ダ", "ヂ", "ヅ", "デ", "ド" -> linesArray[3]?.add(
                        customerName
                    )
                    "ナ", "ニ", "ヌ", "ネ", "ノ" -> linesArray[4]?.add(customerName)
                    "ハ", "ヒ", "フ", "ヘ", "ホ", "バ", "ビ", "ブ", "ベ", "ボ", "パ", "ピ", "プ", "ペ", "ポ" -> linesArray[5]?.add(
                        customerName
                    )
                    "マ", "ミ", "ム", "メ", "モ" -> linesArray[6]?.add(customerName)
                    "ヤ", "ユ", "ヨ" -> linesArray[7]?.add(customerName)
                    "ラ", "リ", "ル", "レ", "ロ" -> linesArray[8]?.add(customerName)
                    "ワ", "ヲ", "ン" -> linesArray[9]?.add(customerName)
                    else -> linesArray[10]?.add(customerName)
                }
            }
        }

        return linesArray

    }

}


