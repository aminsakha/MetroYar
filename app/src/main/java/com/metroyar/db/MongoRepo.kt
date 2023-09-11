package com.metroyar.db

import io.realm.kotlin.Realm
import io.realm.kotlin.types.RealmList


interface MongoDBRepository {
    fun getListOfFavoriteStations(): RealmList<String>
    suspend fun insertStation(station: String)
    suspend fun deleteStation(stationName: String)
    fun initRealmObject(realm: Realm)
}