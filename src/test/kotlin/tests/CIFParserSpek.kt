package tests
import com.github.decyg.CrAgg.cif.CIFSingleton
import com.github.decyg.CrAgg.cif.parser.CIF_ID
import org.amshove.kluent.AnyException
import org.amshove.kluent.`should equal`
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

                    parseFunc `should not throw` AnyException
                }
            }

        }

        given("a valid processed CIFDetailedResult object"){

            val cifFile = CIFSingleton.getPopulatedCIF(
                    CIF_ID("", ""),
                    CIFSingleton.parseCIF(javaClass.getResource("/cif/valid.cif").readText())
            )

            it("should contain one data block with id 1000091"){
                val id = cifFile.cifResult.dataBlocks[0].dataBlockHeader

                id `should equal` "1000091"
            }

            it("should contain a loop with the header _publ_author_name and first value 'Laval, J P'"){
                val loop = cifFile.cifResult.dataBlocks[0].loopedDataItems[0]["publ_author_name"]
                val firstValue = loop!![0]

                firstValue `should equal` "Laval, J P"
            }

            it("should contain a data item with key _journal_coden_ASTM with value JSSCBI"){
                val dataitemvalue = cifFile.cifResult.dataBlocks[0].dataItems["journal_coden_ASTM"]

                dataitemvalue `should equal` "JSSCBI"
            }

            it("should contain a data item with key _symmetry_space_group_name_Hall with single quoted value '-I 4'"){
                val dataitemvalue = cifFile.cifResult.dataBlocks[0].dataItems["symmetry_space_group_name_Hall"]

                dataitemvalue `should equal` "-I 4"
            }

        }

    }
})