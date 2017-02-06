package com.github.decyg.CrAgg.CIFParser

import org.parboiled.BaseParser
import org.parboiled.Rule
import org.parboiled.annotations.BuildParseTree
import org.parboiled.annotations.SuppressNode
import java.util.*

/**
 * This class represents a direct encoding of the formal grammar of CIF.
 *
 * It's encoded using the Parboiled library.
 */
@BuildParseTree
open class CIFParser : BaseParser<Objects>() {

    open fun TestFirstOf() : Rule {
        return FirstOf(
                        Sequence(Numeric(), ZeroOrMore(ANY), EOI),
                        Sequence(OneOrMore(ANY), EOI)
                )

    }

    // Top level CIF rules

    open fun CIF() : Rule {
        return Sequence(
                Optional(Comments()),
                Optional(WhiteSpace()),
                Optional(
                        DataBlock(),
                        ZeroOrMore(
                                WhiteSpace(),
                                DataBlock()
                        ),
                        Optional(WhiteSpace())
                ),
                EOI
        )
    }

    open fun DataBlock() : Rule {
        return Sequence(
                DataBlockHeading(),
                WhiteSpace(),
                ZeroOrMore(
                        FirstOf(
                                DataItems(),
                                SaveFrame() // Doesn't usually fire
                        ),
                        Optional(WhiteSpace())
                )
        )
    }

    open fun DataBlockHeading() : Rule {
        return Sequence(
                DATA_(),
                OneOrMore(NonBlankChar())
        )
    }

    open fun SaveFrame() : Rule {
        return Sequence(
                SaveFrameHeading(),
                OneOrMore(
                        WhiteSpace(),
                        DataItems()
                ),
                WhiteSpace(),
                SAVE_()
        )
    }

    open fun SaveFrameHeading() : Rule {
        return Sequence(
                SAVE_(),
                OneOrMore(NonBlankChar())
        )
    }

    open fun DataItems() : Rule {
        return FirstOf(
                Sequence(
                        Tag(),
                        WhiteSpace(),
                        Value()

                ),
                Sequence(
                        LoopHeader(),
                        WhiteSpace(),
                        LoopBody()
                )
        )
    }

    open fun LoopHeader() : Rule {
        return Sequence(
                LOOP_(),
                OneOrMore(
                        WhiteSpace(),
                        Tag()
                )
        )
    }

    open fun LoopBody() : Rule {
        return ZeroOrMore(
                Value(),
                WhiteSpace()
        )
    }

    // Reserved words

    open fun DATA_() : Rule {
        return Sequence(
                AnyOf("dD"),
                AnyOf("aA"),
                AnyOf("tT"),
                AnyOf("aA"),
                '_'
        )
    }

    open fun LOOP_() : Rule {
        return Sequence(
                AnyOf("lL"),
                AnyOf("oO"),
                AnyOf("oO"),
                AnyOf("pP"),
                '_'
        )
    }

    open fun GLOBAL_() : Rule {
        return Sequence(
                AnyOf("gG"),
                AnyOf("lL"),
                AnyOf("oO"),
                AnyOf("bB"),
                AnyOf("aA"),
                AnyOf("lL"),
                '_'
        )
    }

    open fun SAVE_() : Rule {
        return Sequence(
                AnyOf("sS"),
                AnyOf("aA"),
                AnyOf("vV"),
                AnyOf("eE"),
                '_'
        )
    }

    open fun STOP_() : Rule {
        return Sequence(
                AnyOf("sS"),
                AnyOf("tT"),
                AnyOf("oO"),
                AnyOf("pP"),
                '_'
        )
    }

    // Tags and values

    open fun Tag() : Rule {
        return Sequence(
                '_',
                OneOrMore(NonBlankChar())
        )
    }

    open fun Value() : Rule {
        return FirstOf(
                '.',
                '?',
                Numeric(),
                Sequence(TextField(), Test(WhiteSpace())),
                Sequence(CharString(), Test(WhiteSpace()))
            )

    }

    // Numeric values

    // Rewrite of original, should match
    // 11, 11.11, 11.11e11, 11., +11.11e+11()
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

    open fun CharString() : Rule {
        return FirstOf(
                SingleQuotedString(),
                DoubleQuotedString(),
                UnquotedString()
        )
    }

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

    open fun SingleQuotedString() : Rule {
        return Sequence(
                '\'',
                ZeroOrMore(
                    FirstOf(
                            Sequence(
                                    '\'',
                                    NonBlankChar()
                            ),
                            NotAnSQuote(),
                            WhiteSpace()
                    )
                ),
                '\''
        )
    }

    @SuppressNode
    open fun NotAnSQuote() : Rule {
        return FirstOf(
                CharRange('!', '&'),
                CharRange('(', '~')
        )
    }

    open fun DoubleQuotedString() : Rule {
        return Sequence(
                '\"',
                ZeroOrMore(AnyPrintChar()),
                '\"',
                WhiteSpace()
        )
    }

    open fun TextField() : Rule {
        return SemiColonTextField()
    }

    open fun SemiColonTextField() : Rule {
        return Sequence(
                ';',
                ZeroOrMore(AnyPrintChar()),
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

    open fun Comments() : Rule {
        return OneOrMore(
                Sequence(
                    '#',
                    ZeroOrMore(AnyPrintChar()),
                    EOL()
                )
        )
    }

    // Character sets

    @SuppressNode
    open fun AnyPrintChar() : Rule {
        return FirstOf(
                OrdinaryChar(),
                AnyOf("\"#$'_ \t[]")
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
                AnyOf("\"#$'_[]")
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