package com.example.meecos.Model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class CustomerObject: RealmObject() {

    @PrimaryKey
    var id : Int? = null

    @Required
    var name : String = ""

    var howToRead : String = ""

}