package com.example.meecos.Model

import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class ScheduleObject: RealmObject() {
    @PrimaryKey
    var id : Int? = null
    @Required
    var startDate: String? = null
    @Required //RealmではTime型に対応していない為
    var startTime: String? = null
    @Required
    var endDate: String? = null
    @Required
    var endTime: String? = null
    @Required
    var contents: String? = null

    fun deleteByID (id: Int): Boolean {
        val realm = Realm.getDefaultInstance()
        val target = realm.where(ScheduleObject::class.java)
            .equalTo("id", id)
            .findAll()
        return BaseModel().delete(target)
    }
}