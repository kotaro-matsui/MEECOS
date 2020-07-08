package com.example.meecos.Model

import io.realm.Realm
import io.realm.RealmResults
import io.realm.RealmObject

open class BaseModel: RealmObject() {

    class BaseModel : Object() {

        fun <T> delete(target: RealmResults<T>): Boolean {
            val realm = Realm.getDefaultInstance()
            return try {
                realm.executeTransaction {
                    target.deleteFromRealm(0)
                }
                true
            } catch (e: Exception) {
                false
            }
        }
    }

}