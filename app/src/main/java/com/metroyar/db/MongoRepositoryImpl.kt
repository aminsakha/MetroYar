package com.metroyar.db

import com.metroyar.model.MetroYarDbModel
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.RealmList


class MongoRepositoryImpl(private val realm: Realm) : MongoDBRepository {
    override fun initRealmObject(realm: Realm) {
        val oldItems: RealmResults<MetroYarDbModel> = realm.query<MetroYarDbModel>().find()
        if (oldItems.isEmpty()) {
            realm.writeBlocking {
                copyToRealm(MetroYarDbModel().apply {
                    items = realmListOf(String())
                    shouldPlaySound = true
                    shouldVibratePhone = true
                })
            }
        }
    }

    override fun getListOfFavoriteStations(): RealmList<String> {
        return realm.query<MetroYarDbModel>().find().first().items
    }

    override fun getShouldVibrate(): Boolean {
        return realm.query<MetroYarDbModel>().find().first().shouldVibratePhone
    }

    override fun getShouldPlaySound(): Boolean {
        return realm.query<MetroYarDbModel>().find().first().shouldPlaySound
    }

    override suspend fun insertStation(station: String) {
        val oldItems: RealmResults<MetroYarDbModel> = realm.query<MetroYarDbModel>().find()
        realm.writeBlocking {
            findLatest(oldItems[0])?.items?.add(station)
        }
    }

    override suspend fun changeShouldPlaySound(boolean: Boolean) {
        val realm: RealmResults<MetroYarDbModel> = realm.query<MetroYarDbModel>().find()
        this.realm.writeBlocking {
            findLatest(realm[0])?.shouldPlaySound = boolean
        }
    }

    override suspend fun changeShouldVibrate(boolean: Boolean) {
        val realm: RealmResults<MetroYarDbModel> = realm.query<MetroYarDbModel>().find()
        this.realm.writeBlocking {
            findLatest(realm[0])?.shouldVibratePhone = boolean
        }
    }

    override suspend fun deleteStation(stationName: String) {
        val oldItems: RealmResults<MetroYarDbModel> = realm.query<MetroYarDbModel>().find()
        realm.writeBlocking {
            findLatest(oldItems[0])?.items?.remove(stationName)
        }
    }
}