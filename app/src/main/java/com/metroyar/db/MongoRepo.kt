package com.metroyar.db

import io.realm.kotlin.Realm
import io.realm.kotlin.types.RealmList

interface MongoDBRepository {
    fun getListOfFavoriteStations(): RealmList<String>
    fun getShouldVibrate(): Boolean
    fun getShouldPlaySound(): Boolean
    suspend fun insertStation(station: String)
    suspend fun changeShouldPlaySound(boolean: Boolean)
    suspend fun changeShouldVibrate(boolean: Boolean)
    suspend fun deleteStation(stationName: String)
    fun initRealmObject(realm: Realm)
}