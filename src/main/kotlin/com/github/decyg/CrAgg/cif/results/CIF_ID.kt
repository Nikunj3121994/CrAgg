package com.github.decyg.CrAgg.cif.results

import com.github.decyg.CrAgg.database.DBSingleton
import com.github.decyg.CrAgg.database.DBSource

/**
 * This is a small data class designed to represent a joined ID of any CIF object, it needs to be a mix between
 * a [db] and an [id] as multiple datasources may have conflicts
 *
 * @property db The database source as a [DBSource] object or a [String] which gets converted automatically
 * @property id The id of the entry
 */
data class CIF_ID(val db : DBSource, val id : String){
    /**
     * Identical to the primary constructor except it takes a [String] as the db object and uses the [DBSingleton]
     * to convert it into a [DBSource] object
     */
    constructor(db : String, id : String) : this(
            DBSingleton.getDBSourceByName(db)!!,
            id
    )

    override fun toString(): String {
        return "${db.simpleName} : $id"
    }
}