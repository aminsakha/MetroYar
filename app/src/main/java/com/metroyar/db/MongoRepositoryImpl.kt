package com.metroyar.db

import com.metroyar.model.FavoriteStations
import com.metroyar.utils.log
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.RealmList


class MongoRepositoryImpl(private val realm: Realm) : MongoDBRepository {

    override  fun initRealmObject(realm: Realm) {
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

    override fun getListOfFavoriteStations(): RealmList<String> {
        return realm.query<FavoriteStations>().find().first().items
    }

    override suspend fun insertStation(stationName: String) {
        val oldItems: RealmResults<FavoriteStations> = realm.query<FavoriteStations>().find()
        realm.writeBlocking {
            findLatest(oldItems[0])?.items?.add(stationName)
        }
    }

    override suspend fun deleteStation(stationName: String) {
        val oldItems: RealmResults<FavoriteStations> = realm.query<FavoriteStations>().find()
        realm.writeBlocking {
            findLatest(oldItems[0])?.items?.remove(stationName)
        }
    }
}