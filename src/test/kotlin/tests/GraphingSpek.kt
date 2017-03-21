package tests
import com.github.decyg.CrAgg.database.graphing.GraphSingleton
import com.github.decyg.CrAgg.database.graphing.enums.ChemField
import com.github.decyg.CrAgg.database.graphing.enums.ChemGraphs
import org.amshove.kluent.AnyException
import org.amshove.kluent.`should equal`
import org.amshove.kluent.`should not throw`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


/**
 * Created by declan on 21/03/2017.
 */
class GraphingSpek : Spek({
    describe("Graphing") {
        given("an input in the form of a list of ChemField objects") {

            on("conversion of a query into a pair of json objects for the client using the database") {
                it("should pass") {
                    val parseFunc = {
                        // This query should get all results for elements without failing
                        GraphSingleton.processGraphToClientJson(
                                ChemGraphs.BASIC_RATIO,
                                listOf(
                                        Pair(ChemField.CHEMICAL_FORMULA_SUM_HAS_ELEMENT, "C"),
                                        Pair(ChemField.CHEMICAL_FORMULA_SUM_HAS_ELEMENT, "H"),
                                        Pair(ChemField.CHEMICAL_FORMULA_SUM_HAS_ELEMENT, ""),
                                        Pair(ChemField.CHEMICAL_FORMULA_SUM_HAS_N_ELEMENTS, "3")
                                )
                        )
                    }

                    parseFunc `should not throw` AnyException
                }
            }

            on("conversion of a query that doesn't match the graphs required query size"){
                it("should return a pair of empty strings"){
                    val res = GraphSingleton.processGraphToClientJson(
                            ChemGraphs.BASIC_RATIO,
                            listOf(
                                    Pair(ChemField.CHEMICAL_FORMULA_SUM_HAS_ELEMENT, "C"),
                                    Pair(ChemField.CHEMICAL_FORMULA_SUM_HAS_N_ELEMENTS, "3")
                            )
                    )

                    res `should equal` Pair("", "")
                }
            }

        }
    }
})