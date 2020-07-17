package com.example.meecos.Model

import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class CustomerObject: RealmObject() {

    @PrimaryKey
    var id : Int? = null
    @Required
    var name : String = ""
    var howToRead : String = ""
    var addressNumber : String = ""
    var topAddress : String = ""
    var bottomAddress : String = ""
    var phoneNumber : String = ""
    var section : String = ""

    // 全てのcustomerObjectをフリガナの昇順にソートし、その結果を返す
    fun fetchAllCustomerObject() : RealmResults<CustomerObject> {
        val realm: Realm = Realm.getDefaultInstance()
        return realm.where(CustomerObject::class.java).sort("howToRead").findAll()
    }

    // idと一致するcustomerObjectを一件返す
    fun findCustomerById(id : Int) : CustomerObject? {
        val realm: Realm = Realm.getDefaultInstance()
        return realm.where(CustomerObject::class.java).equalTo("id", id).findFirst()
    }

}