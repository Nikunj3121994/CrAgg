package com.github.decyg.CrAgg.database

import com.github.decyg.CrAgg.cif.CIFBriefResult
import com.github.decyg.CrAgg.cif.CIFDetailedResult
import com.github.decyg.CrAgg.database.query.QueryWrapper
import java.io.File

/**
 * Created by declan on 27/02/2017.
 */
object DBSingleton {

    val datasetMap = mapOf<DBSource, DBAbstraction>(

            DBSource.COD to object : DBAbstraction {

                override fun queryDatabaseSpecific(specificResult: CIFBriefResult): CIFDetailedResult {
                    // this is nice
                    return CIFDetailedResult(File(""))
                }

                override fun queryDatabase(queries: List<QueryWrapper>): List<CIFBriefResult> {
                    // real nice
                    return emptyList()
                }

            }

    )
}