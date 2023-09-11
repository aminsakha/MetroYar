package com.metroyar.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

open class FavoriteStations : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var items: RealmList<String> = realmListOf()
}
