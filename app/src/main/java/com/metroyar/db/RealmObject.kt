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
}

fun initRealmObject(realm: Realm) {
    val oldItems: RealmResults<FavoriteStations> = realm.query<FavoriteStations>().find()
    if (oldItems.isEmpty()) {
        log("empty", true)
        realm.writeBlocking {
            copyToRealm(FavoriteStations().apply {
                items = realmListOf(String())
            })
        }
    }

}

fun insertToDb(name: String, realm: Realm) {
    val oldItems: RealmResults<FavoriteStations> = realm.query<FavoriteStations>().find()
    realm.writeBlocking {
        findLatest(oldItems[0])?.items?.add(name)
    }
}

fun deleteItem(name: String, realm: Realm) {
    val oldItems: RealmResults<FavoriteStations> = realm.query<FavoriteStations>().find()
    realm.writeBlocking {
        findLatest(oldItems[0])?.items?.remove(name)
    }
}

fun getItems(realm: Realm): RealmList<String> {
    return realm.query<FavoriteStations>().find().first().items

}

