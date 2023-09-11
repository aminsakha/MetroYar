package com.metroyar.db

import com.metroyar.model.FavoriteStations
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList


class MongoRepositoryImpl(private val realm: Realm) : MongoDBRepository {

    override fun getListOfFavoriteStations(): RealmList<String> {
//        val favoriteStations = realm.where(FavoriteStations::class.java).findFirst()
//        return favoriteStations?.items!!
        return realmListOf()
    }

    override suspend fun insertStation(station: String) {
//        realm.executeTransactionAsync { realm ->
//            val favoriteStations = realm.where(FavoriteStations::class.java).findFirst()
//            favoriteStations?.items?.add(station)
//        }
    }

    override suspend fun deleteStation(stationName: String) {
//        realm.executeTransactionAsync { realm ->
//            val favoriteStations = realm.where(FavoriteStations::class.java).findFirst()
//            favoriteStations?.items?.remove(stationName)
//        }
    }
}