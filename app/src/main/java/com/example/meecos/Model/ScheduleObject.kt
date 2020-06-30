package com.example.meecos.Model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class ScheduleObject :RealmObject(){
    @PrimaryKey
    var id : String? = null
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
}