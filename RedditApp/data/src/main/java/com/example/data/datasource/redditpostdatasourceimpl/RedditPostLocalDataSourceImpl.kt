package com.example.data.datasource.redditpostdatasourceimpl


import android.os.Handler
import android.os.HandlerThread
import com.example.data.datasource.models.AfterInfoRealm
import com.example.data.datasource.models.RedditPostRealm
import com.example.data.datasource.models.mapToDomain
import com.example.data.datasource.models.mapToRealm
import com.example.data.datasource.redditpostdatasourceinterface.RedditPostLocalDataSource
import com.example.domain.models.AfterInfo
import com.example.domain.models.RedditPost
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.toFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.android.asCoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext



class RedditPostLocalDataSourceImpl(): RedditPostLocalDataSource {

    companion object {
        var redditPostCounter = 0;
    }
    override suspend fun getRedditPostCount(): Int {
        return withContext(RealmThread.dispatcher) {
            Realm.getDefaultInstance().use { realm ->
                val realmResult = realm.where(RedditPostRealm::class.java).findAll()
                realmResult.count()
            }
        }
    }
    override suspend fun getRedditPostList(): List<RedditPost> {
        return withContext(RealmThread.dispatcher) {
            Realm.getDefaultInstance().use { realm ->
                val realmResult = realm.where(RedditPostRealm::class.java).findAll()
                realm.copyFromRealm(realmResult)?.map { redditPostRealmList ->
                    redditPostRealmList.mapToDomain()
                } ?: listOf()
            }
        }
    }
    override suspend fun refreshRedditPostList(redditPostDataList: List<RedditPost>) {
        return withContext(RealmThread.dispatcher) {
            val realmInst = Realm.getDefaultInstance()
            realmInst.executeTransaction { realm ->
                //clear RedditPost table
                realm.where(RedditPostRealm::class.java).findAll().deleteAllFromRealm()
                redditPostCounter = 0
                //save data to RedditPost table
                redditPostDataList.forEach { someData ->
                    val realmObj = someData.mapToRealm()
                    realmObj.id = ++redditPostCounter
                    realm.copyToRealmOrUpdate(realmObj)

                }
            }
            realmInst.close()
        }
    }
    override suspend fun updateRedditPostList(redditPostDataList: List<RedditPost>) {
        return withContext(RealmThread.dispatcher) {

            val realmInst = Realm.getDefaultInstance()
            realmInst.executeTransaction { realm ->
                redditPostCounter = realm.where(RedditPostRealm::class.java).findAll().sort("id",Sort.ASCENDING).last()?.id ?: 0
                //save data to RedditPost table
                redditPostDataList.forEach { someData ->
                    val realmObj = someData.mapToRealm()
                    realmObj.id = ++redditPostCounter
                    realm.copyToRealmOrUpdate(realmObj)

                }
            }
            realmInst.close()
        }
    }
    override suspend fun setRedditAfterInfo(afterInfo: AfterInfo) {
        withContext(RealmThread.dispatcher) {
            val realmInst = Realm.getDefaultInstance()
            realmInst.executeTransaction { realm ->
                realm.where(AfterInfoRealm::class.java).findAll().deleteAllFromRealm()
                realm.copyToRealmOrUpdate(afterInfo.mapToRealm())
            }
            realmInst.close()
        }
    }
    override suspend fun getRedditAfterInfo(): AfterInfo {
        return withContext(RealmThread.dispatcher) {
                Realm.getDefaultInstance().use { realm ->
                val realmResult = realm.where(AfterInfoRealm::class.java).findFirst()
                realm.copyFromRealm(realmResult)?.mapToDomain()
            } ?: AfterInfo("")
        }

    }
    override fun observeRedditPost(): Flow<List<RedditPost>> = flow {
        emitAll(
            Realm.getDefaultInstance()
                .where(RedditPostRealm::class.java)
                .findAll()
                .sort("id",Sort.ASCENDING)
                .toFlow()
                .map { someData -> someData.map { someDataRealm -> someDataRealm.mapToDomain() } }
        )
    }.flowOn(RealmThread.dispatcher)
    override fun observeAfterInfo(): Flow<AfterInfo> = flow {
        emitAll(
            Realm.getDefaultInstance()
                .where(AfterInfoRealm::class.java)
                .findAll()
                .toFlow()
                .map { someData ->
                    someData.firstOrNull()?.mapToDomain() ?: AfterInfo("")}
        )
    }.flowOn(RealmThread.dispatcher)
}

private object RealmThread {
    private val threadR = HandlerThread("RealmThread")
    private val handler = Handler(getThread().looper)

    private fun getThread(): HandlerThread {
        if (!threadR.isAlive) threadR.start()
        return threadR
    }

    val dispatcher = handler.asCoroutineDispatcher()
}

