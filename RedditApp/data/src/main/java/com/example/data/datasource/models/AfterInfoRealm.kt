package com.example.data.datasource.models

import com.example.domain.models.AfterInfo
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class AfterInfoRealm (@PrimaryKey var after:String = ""): RealmObject()


fun AfterInfo.mapToRealm():AfterInfoRealm{
    return AfterInfoRealm(after)
}
fun AfterInfoRealm.mapToDomain(): AfterInfo {
    return AfterInfo(after)
}
