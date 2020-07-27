package com.example.meecos.Model

import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class ProfileObject: RealmObject() {

    @PrimaryKey
    var id : Int? = null
    @Required
    var name : String = ""
    var addressNumber : String = ""
    var topAddress : String = ""
    var bottomAddress : String = ""
    var phoneNumber : String = ""

    /**
     * idと一致するprofileObjectを返す
     */
    fun findProfileById(id : Int?) : ProfileObject? {
        val realm: Realm = Realm.getDefaultInstance()
        return realm.where(ProfileObject::class.java).equalTo("id", id).findFirst()
    }
}