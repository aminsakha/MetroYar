package com.metroyar.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId


open class MetroYarDb : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var shouldPlaySound: Boolean = true
    var shouldVibratePhone: Boolean = true
    var items: RealmList<String> = realmListOf()
}
