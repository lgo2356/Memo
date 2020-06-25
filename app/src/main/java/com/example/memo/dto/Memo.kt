package com.example.memo.dto

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class Memo(
        @PrimaryKey
        var id: Long = -1,

        @Required
        var title: String = "",

        @Required
        var content: String = "",

        @Required
        var imageAddress: String = "",

        var position: Int = -1
) : RealmObject()
