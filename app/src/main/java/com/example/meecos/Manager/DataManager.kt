package com.example.meecos.Manager

import com.example.meecos.Model.CustomerObject

class CustomerInfo(var name: String, var id: String)

class DataManager {

    //　親項目に振り分けるための、子項目を行ごとに作成してリストに入れ、配列にして返す
    fun createChildList(): Array<MutableList<CustomerInfo>?> {

        val co = CustomerObject()

        val linesArray: Array<MutableList<CustomerInfo>?> = arrayOfNulls(11)

        for (i in linesArray.indices) {
            val lineList = mutableListOf<CustomerInfo>()
            linesArray[i] = lineList
        }

        for (customer in co.fetchAllCustomerObject()) {
            val customerHowToRead = customer.howToRead
            val customerName = customer.name
            val customerId = customer.id.toString()

            // 読み仮名未設定は#に振り分け
            if (customerHowToRead == "") {
                linesArray[10]?.add(CustomerInfo(customerName, customerId))
            } else {
                when (customerHowToRead.substring(0, 1)) {
                    "ア", "イ", "ウ", "エ", "オ" -> {
                        linesArray[0]?.add(CustomerInfo(customerName, customerId))
                    }
                    "カ", "キ", "ク", "ケ", "コ", "ガ", "ギ", "グ", "ゲ", "ゴ" -> {
                        linesArray[1]?.add(CustomerInfo(customerName, customerId))
                    }
                    "サ", "シ", "ス", "セ", "ソ", "ザ", "ジ", "ズ", "ゼ", "ゾ" -> {
                        linesArray[2]?.add(CustomerInfo(customerName, customerId))
                    }
                    "タ", "チ", "ツ", "テ", "ト", "ダ", "ヂ", "ヅ", "デ", "ド" -> {
                        linesArray[3]?.add(CustomerInfo(customerName, customerId))
                    }
                    "ナ", "ニ", "ヌ", "ネ", "ノ" -> {
                        linesArray[4]?.add(CustomerInfo(customerName, customerId))
                    }
                    "ハ", "ヒ", "フ", "ヘ", "ホ", "バ", "ビ", "ブ", "ベ", "ボ", "パ", "ピ", "プ", "ペ", "ポ" -> {
                        linesArray[5]?.add(CustomerInfo(customerName, customerId))
                    }
                    "マ", "ミ", "ム", "メ", "モ" -> {
                        linesArray[6]?.add(CustomerInfo(customerName, customerId))
                    }
                    "ヤ", "ユ", "ヨ" -> {
                        linesArray[7]?.add(CustomerInfo(customerName, customerId))
                    }
                    "ラ", "リ", "ル", "レ", "ロ" -> {
                        linesArray[8]?.add(CustomerInfo(customerName, customerId))
                    }
                    "ワ", "ヲ", "ン" -> {
                        linesArray[9]?.add(CustomerInfo(customerName, customerId))
                    }
                    else -> {
                        linesArray[10]?.add(CustomerInfo(customerName, customerId))
                    }
                }
            }
        }

        return linesArray

    }

}


