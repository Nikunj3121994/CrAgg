package com.github.decyg.CrAgg.spring.models

import com.github.decyg.CrAgg.database.DBSingleton
import com.github.decyg.CrAgg.database.DBSource
import com.github.decyg.CrAgg.database.query.TermCategory

/**
 * This shouldn't be in its own file but due to this bug
 * https://youtrack.jetbrains.com/issue/IDEA-132738
 * It needs to be otherwise it makes it a pain to develop with it.
 *
 * Created by declan on 07/03/2017.
 */
data class SearchModel(
        val queryTerms : Array<TermCategory> = TermCategory.values(),
        val dbOptions : Array<DBSource> = DBSingleton.datasetMap.keys.toTypedArray()
)