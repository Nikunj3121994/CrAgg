package com.github.decyg.CrAgg.cif

import org.parboiled.BaseParser
import org.parboiled.Rule
import org.parboiled.annotations.BuildParseTree
import org.parboiled.annotations.SuppressNode
import org.parboiled.annotations.SuppressSubnodes
import org.parboiled.support.Var
import org.parboiled.trees.MutableTreeNodeImpl

/**
 * This class represents an encoding of the formal grammar of CIF.
 *
 * Various liberties were taking during the development of this due to the inconsistencies found in the official
 * grammar. Such inconsistencies include mismatches brackets, non specific handling of whitespaces and EOL's.
 *
 * The version below can successfully parse a variety of
 *
 * It's encoded using the Parboiled library.
 */
@BuildParseTree
open class CIFParser : BaseParser<CIFParser.CIFNode>() {

    //data class test(val vol : String) : GraphNode<test>

    class CIFNode(val value : String) : MutableTreeNodeImpl<CIFNode>() {

        constructor(value : String, vararg children : CIFNode) : this(value) {
            for ((i, v) in children.withIndex()){
                super.addChild(i, v)
            }
        }

        fun addChildBlind(child : CIFNode) : CIFNode {

            super.addChild(super.getChildren().count(), child)

            return this
        }

        override fun toString(): String {
            return value
        }

    }

    // Top level CIF rules

    open fun CIF() : Rule {
        var tempNode : Var<CIFNode> = Var(CIFNode("CIF"))

        return Sequence(
                Optional(Comments()),
                //tempNode.set(tempNode.get().addChildBlind(pop())),
                Optional(WhiteSpace()),
                Optional(
                        DataBlock(),
                        tempNode.set(tempNode.get().addChildBlind(pop())),
                        ZeroOrMore(
                                WhiteSpace(),
                                DataBlock(),
                                tempNode.set(tempNode.get().addChildBlind(pop()))
                        ),
                        Optional(WhiteSpace())
                ),
                EOI,
                push(tempNode.get())
        )
    }

    open fun DataBlock() : Rule {
        var tempNode : Var<CIFNode> = Var(CIFNode("DataBlock"))

        return Sequence(
                DataBlockHeading(),
                tempNode.set(tempNode.get().addChildBlind(pop())),
                WhiteSpace(),
                ZeroOrMore(
                        FirstOf(
                                DataItems(),
                                SaveFrame() // Doesn't usually fire
                        ),
                        tempNode.set(tempNode.get().addChildBlind(pop())),
                        Optional(WhiteSpace())
                ),
                push(tempNode.get())
        )
    }

    open fun DataBlockHeading() : Rule {
        return Sequence(
                DATA_(),
                OneOrMore(NonBlankChar()),
                push(CIFNode("DataBlockHeading", CIFNode(match())))
        )
    }

    open fun SaveFrame() : Rule {
        var tempNode : Var<CIFNode> = Var(CIFNode("SaveFrame"))

        return Sequence(
                SaveFrameHeading(),
                tempNode.set(tempNode.get().addChildBlind(pop())),
                OneOrMore(
                        WhiteSpace(),
                        DataItems(),
                        tempNode.set(tempNode.get().addChildBlind(pop()))
                ),
                WhiteSpace(),
                SAVE_(),
                push(tempNode.get())
        )
    }

    open fun SaveFrameHeading() : Rule {
        return Sequence(
                SAVE_(),
                OneOrMore(NonBlankChar()),
                push(CIFNode("SaveFrameHeading", CIFNode(match())))
        )
    }

    open fun DataItems() : Rule {
        return FirstOf(
                Sequence(
                        LoopHeader(),
                        WhiteSpace(),
                        LoopBody(),
                        push(CIFNode("LoopedDataItem", pop(1), pop()))
                ),
                Sequence(
                        Tag(),
                        WhiteSpace(),
                        Value(),
                        push(CIFNode("DataItem", pop(1), pop()))
                )

        )
    }

    @SuppressSubnodes
    open fun LoopHeader() : Rule {
        var tempNode : Var<CIFNode> = Var(CIFNode("LoopHeader"))

        return Sequence(
                LOOP_(),
                OneOrMore(
                        WhiteSpace(),
                        Tag(),
                        tempNode.set(tempNode.get().addChildBlind(pop()))
                ),
                push(tempNode.get())
        )
    }

    @SuppressSubnodes
    open fun LoopBody() : Rule {
        var tempNode : Var<CIFNode> = Var(CIFNode("LoopBody"))

        return Sequence(
                ZeroOrMore(
                    Value(),
                    tempNode.set(tempNode.get().addChildBlind(pop())),
                    WhiteSpace()
                ),
                push(tempNode.get())
        )
    }

    // Reserved words

    @SuppressNode
    open fun DATA_() : Rule {
        return Sequence(AnyOf("dD"), AnyOf("aA"), AnyOf("tT"), AnyOf("aA"), '_')
    }

    @SuppressNode
    open fun LOOP_() : Rule {
        return Sequence(AnyOf("lL"), AnyOf("oO"), AnyOf("oO"), AnyOf("pP"), '_')
    }

    @SuppressNode
    open fun GLOBAL_() : Rule {
        return Sequence(AnyOf("gG"), AnyOf("lL"), AnyOf("oO"), AnyOf("bB"), AnyOf("aA"), AnyOf("lL"), '_')
    }

    @SuppressNode
    open fun SAVE_() : Rule {
        return Sequence(AnyOf("sS"), AnyOf("aA"), AnyOf("vV"), AnyOf("eE"), '_')
    }

    @SuppressNode
    open fun STOP_() : Rule {
        return Sequence(AnyOf("sS"), AnyOf("tT"), AnyOf("oO"), AnyOf("pP"), '_')
    }

    @SuppressNode
    open fun ReservedString() : Rule {
        return FirstOf(
                DATA_(),
                LOOP_(),
                GLOBAL_(),
                SAVE_(),
                STOP_()
        )
    }

    // Tags and values

    @SuppressNode
    open fun Tag() : Rule {
        return Sequence(
                '_',
                OneOrMore(NonBlankChar()),
                push(CIFNode(match()))
        )
    }

    @SuppressNode
    open fun Value() : Rule {
        return Sequence(
                FirstOf(
                        Sequence(Numeric(), Test(WhiteSpace())),
                        '.',
                        '?',
                        Sequence(TextField(), Test(WhiteSpace())),
                        Sequence(TestNot(ReservedString()), CharString(), Test(WhiteSpace()))
                ),
                push(CIFNode(match())) // This is potentially going to need changing due to the fact that it pushed the delimiters for textfields
        )

    }

    // Numeric values

    // Rewrite of original, should match
    // 11, 11.11, 11.11e11, 11., +11.11e+11()
    @SuppressNode
    open fun Numeric() : Rule {
        return Sequence(
                Optional(AnyOf("+-")),
                FirstOf(
                        Sequence(
                                ZeroOrMore(Digit()),
                                '.',
                                OneOrMore(Digit())
                        ),
                        Sequence(
                                OneOrMore(Digit()),
                                '.'
                        ),
                        OneOrMore(Digit())
                ),
                Optional(
                        AnyOf("eE"),
                        Optional(AnyOf("+-")),
                        OneOrMore(Digit())
                ),
                Optional(
                        '(',
                        OneOrMore(Digit()),
                        ')'
                ),
                Test(WhiteSpace())

        )
    }

    @SuppressNode
    open fun Digit() : Rule {
        return CharRange('0', '9')
    }

    // Character strings and text fields

    @SuppressNode
    open fun CharString() : Rule {
        return FirstOf(
                SingleQuotedString(),
                DoubleQuotedString(),
                UnquotedString()
        )
    }

    @SuppressNode
    open fun UnquotedString() : Rule {
        return FirstOf(
                Sequence(
                        Test(EOL()),
                        OrdinaryChar(),
                        ZeroOrMore(NonBlankChar())
                ),
                Sequence(
                        Test(NotEOL()),
                        FirstOf(
                                OrdinaryChar(),
                                ';'
                        ),
                        ZeroOrMore(NonBlankChar())
                )
        )
    }

    @SuppressNode
    open fun SingleQuotedString() : Rule {
        return Sequence(
                '\'',
                ZeroOrMore(
                        AnyPrintChar(),
                        TestNot(EOL())
                ),
                '\''
        )
    }

    open fun DoubleQuotedString() : Rule {
        return Sequence(
                '\"',
                ZeroOrMore(
                        AnyPrintChar(),
                        TestNot(EOL())
                ),
                '\"'
        )
    }

    open fun TextField() : Rule {
        return SemiColonTextField()
    }

    open fun SemiColonTextField() : Rule {
        return Sequence(
                ';',
                ZeroOrMore(
                        AnyPrintChar()
                ),
                EOL(),
                ZeroOrMore(
                        Optional(
                                TextLeadChar(),
                                ZeroOrMore(AnyPrintChar())
                        ),
                        EOL()
                ),
                ';'
        )
    }

    // Whitespace and comments

    @SuppressNode
    open fun WhiteSpace() : Rule {
        return OneOrMore(
                FirstOf(
                        TokenizedComments(),
                        ' ',
                        '\t',
                        EOL()
                )
        )
    }

    @SuppressNode
    open fun TokenizedComments() : Rule {
        return Sequence(
                OneOrMore(
                        FirstOf(
                                ' ',
                                '\t',
                                EOL()
                        )
                ),
                Comments()
        )
    }

    // Decided not to match comments as a node as it's technically not relevant to the picked up data
    // Also because WhiteSpace are also technically interchangable with Comments which is a bit of a headache
    // A messy potential solution could be matching regularly here but just having a method up above called
    // appendComment or something that adds it to a top level Comment node
    open fun Comments() : Rule {
        return OneOrMore(
                '#',
                ZeroOrMore(AnyPrintChar()),
                EOL()
        )
    }

    /*open fun Comments() : Rule {
        var tempNode : Var<CIFNode> = Var(CIFNode("Comments"))

        return Sequence(
                OneOrMore(
                    '#',
                    ZeroOrMore(AnyPrintChar()),
                    push(CIFNode(match())),
                    tempNode.set(tempNode.get().addChildBlind(pop())),
                    EOL()
                ),
                push(tempNode.get())
        )
    }*/

    // Character sets

    @SuppressNode
    open fun AnyPrintChar() : Rule {
        return FirstOf(
                OrdinaryChar(),
                AnyOf("\"#$'_ \t;[]")
        )
    }

    @SuppressNode
    open fun TextLeadChar() : Rule {
        return FirstOf(
                OrdinaryChar(),
                AnyOf("\"#$'_ \t[]")
        )
    }

    @SuppressNode
    open fun NonBlankChar() : Rule {
        return FirstOf(
                OrdinaryChar(),
                AnyOf("\"#$'_;[]")
        )
    }

    @SuppressNode
    open fun OrdinaryChar() : Rule {
        return FirstOf(
                CharRange('a', 'z'),
                CharRange('A', 'Z'),
                CharRange('0', '9'),
                AnyOf("!%&()*+,-./:<=>?@\\^`{|}~")
        )
    }

    // Misc

    @SuppressNode
    open fun EOL() : Rule {
        return FirstOf("\n", "\r\n", "\r")
    }

    @SuppressNode
    open fun NotEOL() : Rule {
        return TestNot(EOL())
    }

}