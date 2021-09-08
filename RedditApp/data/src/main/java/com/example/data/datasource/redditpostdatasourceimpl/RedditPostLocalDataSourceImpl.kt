package com.example.data.datasource.redditpostdatasourceimpl

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import com.example.data.datasource.models.AfterInfoRealm
import com.example.data.datasource.models.RedditPostRealm
import com.example.data.datasource.models.mapToDomain
import com.example.data.datasource.models.mapToRealm
import com.example.data.datasource.redditpostdatainterface.RedditPostLocalDataSource
import com.example.domain.models.AfterInfo
import com.example.domain.models.RedditPost
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.realm.Realm
import io.realm.kotlin.toFlow
import kotlinx.coroutines.android.asCoroutineDispatcher
import kotlinx.coroutines.flow.*

//To storage reddit 'after' data
private const val sharedPreferences = "REDDIT_SHARED_PREFERENCES"
private const val afterSharedPrefs = "REDDIT_AFTER"


class RedditPostLocalDataSourceImpl(val context: Context): RedditPostLocalDataSource {

    override suspend fun getRedditPostCount(): Int {
        return Realm.getDefaultInstance().use { realm ->
            val realmResult = realm.where(RedditPostRealm::class.java).findAll()
            realmResult.count()
        }
    }

    override suspend fun getRedditPostList(): List<RedditPost> {
        return Realm.getDefaultInstance().use { realm ->
            val realmResult = realm.where(RedditPostRealm::class.java).findAll()
            realm.copyFromRealm(realmResult)?.map { redditPostRealmList ->
                redditPostRealmList.mapToDomain()
            } ?: listOf()
        }
    }

    override suspend fun refreshRedditPostList(redditPostDataList: List<RedditPost>) {
        val realmInst = Realm.getDefaultInstance()
        realmInst.executeTransaction{ realm ->
            //clear RedditPost table
            realm.where(RedditPostRealm::class.java).findAll().deleteAllFromRealm()

            //save data to RedditPost table
            redditPostDataList.forEach { someData ->
                realm.copyToRealmOrUpdate(someData.mapToRealm())
            }
        }
        realmInst.close()
    }
    override suspend fun updateRedditPostList(redditPostDataList: List<RedditPost>) {
        val realmInst = Realm.getDefaultInstance()
        realmInst.executeTransaction{ realm ->
            //save data to RedditPost table
            redditPostDataList.forEach { someData ->
                realm.copyToRealmOrUpdate(someData.mapToRealm())
            }
        }
        realmInst.close()
    }
    override suspend fun setRedditAfterInfo(afterInfo: AfterInfo) {
        val realmInst = Realm.getDefaultInstance()
        realmInst.executeTransaction{ realm ->
            realm.where(RedditPostRealm::class.java).findFirst()?.deleteFromRealm()
            realm.copyToRealmOrUpdate(afterInfo.mapToRealm())
        }
        realmInst.close()
    }
    override suspend fun getRedditAfterInfo(): AfterInfo {
        return Realm.getDefaultInstance().use { realm ->
            val realmResult = realm.where(AfterInfoRealm::class.java).findFirst()
            realm.copyFromRealm(realmResult)?.mapToDomain()
        } ?: AfterInfo("")
    }

    override suspend fun clearRedditAfterInfo() {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { realm ->
            realm.where(RedditPostRealm::class.java).findAll().deleteAllFromRealm()
        }
        realm.close()
    }

    override suspend fun observeRedditPost(): Flow<List<RedditPost>> = flow {
        emitAll(
            Realm.getDefaultInstance()
                .where(RedditPostRealm::class.java)
                .findAll()
                .toFlow()
                .map { someData -> someData.map { someDataRealm -> someDataRealm.mapToDomain() } }
        )
    }.flowOn(RealmThread.dispatcher)

    override suspend fun observeAfterInfo(): Flow<AfterInfo> = flow {
        emitAll(
            Realm.getDefaultInstance()
                .where(AfterInfoRealm::class.java)
                .findFirst()
                .toFlow()
                .map { someData -> someData?.mapToDomain() ?: AfterInfo("") }
        )
    }.flowOn(RealmThread.dispatcher)


}

private object RealmThread {
    private val threadR = HandlerThread("RealmThread")
    private val handler = Handler(getThread().looper)

    val scheduler: Scheduler
        get() = AndroidSchedulers.from(getThread().looper)

    private fun getThread(): HandlerThread {
        if (!threadR.isAlive) threadR.start()
        return threadR
    }

    val dispatcher = handler.asCoroutineDispatcher()
}


