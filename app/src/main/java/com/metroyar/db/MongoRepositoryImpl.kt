package com.metroyar.db

import com.metroyar.model.MetroYarDb
import com.metroyar.utils.log
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.RealmList


class MongoRepositoryImpl(private val realm: Realm) : MongoDBRepository {

    override fun initRealmObject(realm: Realm) {
        val oldItems: RealmResults<MetroYarDb> = realm.query<MetroYarDb>().find()
        log("empty", false)
        if (oldItems.isEmpty()) {
            log("empty", true)
            realm.writeBlocking {
                copyToRealm(MetroYarDb().apply {
                    items = realmListOf(String())
                    shouldPlaySound = true
                    shouldVibratePhone = true
                })
            }
        }
    }

    override fun getListOfFavoriteStations(): RealmList<String> {
        return realm.query<MetroYarDb>().find().first().items
    }

    override fun getShouldVibrate(): Boolean {
        return realm.query<MetroYarDb>().find().first().shouldVibratePhone
    }

    override fun getShouldPlaySound(): Boolean {
        return realm.query<MetroYarDb>().find().first().shouldPlaySound
    }

    override suspend fun insertStation(stationName: String) {
        val oldItems: RealmResults<MetroYarDb> = realm.query<MetroYarDb>().find()
        realm.writeBlocking {
            findLatest(oldItems[0])?.items?.add(stationName)
        }
    }

    override suspend fun changeShouldPlaySound(boolean: Boolean) {
        val realm: RealmResults<MetroYarDb> = realm.query<MetroYarDb>().find()
        this.realm.writeBlocking {
            findLatest(realm[0])?.shouldPlaySound = boolean
        }
    }

    override suspend fun changeShouldVibrate(boolean: Boolean) {
        val realm: RealmResults<MetroYarDb> = realm.query<MetroYarDb>().find()
        this.realm.writeBlocking {
            findLatest(realm[0])?.shouldVibratePhone = boolean
        }
    }

    override suspend fun deleteStation(stationName: String) {
        val oldItems: RealmResults<MetroYarDb> = realm.query<MetroYarDb>().find()
        realm.writeBlocking {
            findLatest(oldItems[0])?.items?.remove(stationName)
        }
    }
}