package com.example.meecos.Manager

import com.example.meecos.Config.selectLineNumber
import com.example.meecos.Model.CustomerObject
import java.util.*
import kotlin.collections.ArrayList

class CustomerInfo(var name: String, var id: String)

class DataManager {

    private val co = CustomerObject()

    /**
     * customerObjectのnameを振り分けるために、必要な親項目のリストを作成する
     */
    fun createGroupList(): List<String>{
        val list = mutableListOf<String>()
        for (customer in co.fetchAllCustomerObject()) {
            list.add(customer.section)
        }
        val groupList = ArrayList<String>(TreeSet(list))
        // TreeSetで並び変えると#が先頭となるので、一度削除してから追加することでListの後ろに回す
        if(groupList.contains("#")){
            groupList.remove("#")
            groupList.add("#")
        }
        return groupList
    }

    /**
     * 親項目に振り分けるための、子項目を行ごとに作成してリストに入れ、配列にして返す
     */
    fun createChildList(): Array<MutableList<CustomerInfo>?> {

        val linesArray: Array<MutableList<CustomerInfo>?> = arrayOfNulls(11)

        for (i in linesArray.indices) {
            val lineList = mutableListOf<CustomerInfo>()
            linesArray[i] = lineList
        }

        for (customer in co.fetchAllCustomerObject()) {
            val howToRead = customer.howToRead
            val name = customer.name
            val id = customer.id.toString()
            linesArray[howToRead.selectLineNumber()]?.add(CustomerInfo(name, id))
        }
        return linesArray
    }
}


