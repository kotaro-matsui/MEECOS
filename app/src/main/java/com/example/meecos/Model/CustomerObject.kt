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

    /**
     * 全てのcustomerObjectをフリガナの昇順にソートし、その結果を返す
     */
    fun fetchAllCustomerObject() : RealmResults<CustomerObject> {
        val realm: Realm = Realm.getDefaultInstance()
        return realm.where(CustomerObject::class.java).sort("howToRead").findAll()
    }

    /**
     * idと一致するcustomerObjectを返す
     */
    fun findCustomerById(id : Int) : CustomerObject? {
        val realm: Realm = Realm.getDefaultInstance()
        return realm.where(CustomerObject::class.java).equalTo("id", id).findFirst()
    }

    /**
     * idの最大値をインクリメントした値を取得する。
     * CustomerObjectが1度も作成されていなければ1を取得する。
     */
    fun getNextUserId(): Int {
        val realm: Realm = Realm.getDefaultInstance()
        // 初期化
        var nextUserId = 1
        // userIdの最大値を取得
        val maxUserId: Number? = realm.where(CustomerObject::class.java).max("id")
        // 1度もデータが作成されていない場合はNULLが返ってくるため、NULLチェックをする
        if (maxUserId != null) {
            nextUserId = maxUserId.toInt() + 1
        }
        return nextUserId
    }

}