package com.example.data.datasource.models

import com.example.domain.models.AfterInfo

data class AfterInfoData (val after:String)


fun AfterInfo.mapToData():AfterInfoData{
    return AfterInfoData(after)
}
fun AfterInfoData.mapToDomain(): AfterInfo {
    return AfterInfo(after)
}
