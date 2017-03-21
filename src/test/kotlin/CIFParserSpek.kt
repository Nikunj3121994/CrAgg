
import com.github.decyg.CrAgg.cif.CIFSingleton
import com.github.decyg.CrAgg.cif.parser.CIF_ID
import org.amshove.kluent.`should not throw`
import org.amshove.kluent.`should throw`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


/**
 * Created by declan on 21/03/2017.
 */
class CIFParserSpek : Spek({
    describe("CIF Parser") {
        given("a cif file"){

            on("provided a valid cif file that it should be able to parse") {
                it("should pass"){
                    val parseFunc = {
                        CIFSingleton.parseCIF(javaClass.getResource("/cif/valid.cif").readText())
                    }

                    parseFunc `should not throw` CIFSingleton.ParseException::class
                }
            }

            on("provided an invalid cif file that it should not be able to parse") {
                it("should not pass"){
                    val parseFunc = {
                        CIFSingleton.parseCIF(javaClass.getResource("/cif/invalid.cif").readText())
                    }

                    parseFunc `should throw` CIFSingleton.ParseException::class
                }
            }
        }

        given("a valid cif file"){

            val cifFile = CIFSingleton.parseCIF(javaClass.getResource("/cif/valid.cif").readText())

            on("being parsed from a CIFNode into a CIFDetailedResult"){
                it("should pass"){
                    val parseFunc = {
                        CIFSingleton.getPopulatedCIF(CIF_ID("", ""), cifFile)
                    }

                    parseFunc `should not throw` Exception::class
                }
            }

        }

    }
})