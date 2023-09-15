package com.metroyar.db

import com.metroyar.model.MetroYarDb
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

object RealmObject {
    private val config = RealmConfiguration.create(schema = setOf(MetroYarDb::class))
    val realm: Realm = Realm.open(config)
    val realmRepo=MongoRepositoryImpl(realm = realm)
}

