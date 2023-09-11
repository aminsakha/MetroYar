package com.metroyar.db

import com.metroyar.model.FavoriteStations
import com.metroyar.utils.log
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.RealmList

object RealmObject {
    private val config = RealmConfiguration.create(schema = setOf(FavoriteStations::class))
    val realm: Realm = Realm.open(config)
    val realmRepo=MongoRepositoryImpl(realm = realm)
}

