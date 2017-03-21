package tests
import com.github.decyg.CrAgg.cif.parser.CIF_ID
import com.github.decyg.CrAgg.database.DBSingleton
import com.github.decyg.CrAgg.database.slowdb.CIFSessionHandler
import com.github.decyg.CrAgg.database.slowdb.implementation.COD
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should not be`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.io.File


/**
 * Created by declan on 21/03/2017.
 */
class DatabaseSpek : Spek({
    describe("Database utilities") {
        given("a string representing a database implementation") {

            on("getting the DBSource for COD") {
                it("should pass") {
                    val source = DBSingleton.getDBSourceByName("COD")

                    source `should not be` null
                }
            }

            on("getting the DBAbstraction for a DBSource for COD"){
                it("should return a DBAbstraction"){
                    val source = DBSingleton.getDBBySource(COD::class)

                    source `should not be` null
                }
            }

        }
    }

    describe("Database cache"){
        given("the local cache"){

            val cifIDdelete = CIF_ID("COD", "1000015")

            val cifIDadd = CIF_ID("COD", "1000091")

            on("adding a new CIF file to local cache"){

                it("should not exist initially, it it does, delete it"){

                    if(CIFSessionHandler.cacheHasCIF(cifIDdelete))
                        CIFSessionHandler.removeCIFFromCache(cifIDdelete)

                    File(CIFSessionHandler.cacheFolder, "${cifIDdelete.db}-${cifIDdelete.id}.cif").exists() `should be` false

                }

                CIFSessionHandler.addCIFToCache(cifIDadd)

                val file = CIFSessionHandler.getCIFFromCacheAsFile(cifIDadd)

                it("should exist"){
                    CIFSessionHandler.cacheHasCIF(cifIDadd) `should be` true
                }

                it("should be accessible as a file"){
                    file.exists() `should be` true
                }

            }
        }
    }
})