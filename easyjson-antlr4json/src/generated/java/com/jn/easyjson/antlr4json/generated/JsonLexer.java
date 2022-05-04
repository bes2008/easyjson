// Generated from Json.g4 by ANTLR 4.5.3
package com.jn.easyjson.antlr4json.generated;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class JsonLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, NUMBER=2, BOOL=3, STRING=4, NULL=5, OBJ_START=6, OBJ_END=7, PAIR_SEPAR=8, 
		ARRAY_SEPAR=9, ARRAY_START=10, ARRAY_END=11;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"WS", "INT", "SIC", "NUMBER", "TRUE", "FALSE", "BOOL", "STRING_FLAG", 
		"HEX_CHAR", "UNICODE", "ESCAPE_CHAR", "SAFE_CODE_POINT", "STRING", "NULL", 
		"OBJ_START", "OBJ_END", "PAIR_SEPAR", "ARRAY_SEPAR", "ARRAY_START", "ARRAY_END"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, null, null, null, "'null'", "'{'", "'}'", "':'", "','", "'['", 
		"']'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "WS", "NUMBER", "BOOL", "STRING", "NULL", "OBJ_START", "OBJ_END", 
		"PAIR_SEPAR", "ARRAY_SEPAR", "ARRAY_START", "ARRAY_END"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public JsonLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Json.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\r\u008d\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\3\2\6\2-\n\2\r\2\16\2.\3\2\3\2\3\3\3\3"+
		"\3\3\7\3\66\n\3\f\3\16\39\13\3\5\3;\n\3\3\4\3\4\5\4?\n\4\3\4\3\4\3\5\5"+
		"\5D\n\5\3\5\3\5\3\5\6\5I\n\5\r\5\16\5J\5\5M\n\5\3\5\5\5P\n\5\3\6\3\6\3"+
		"\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\5\b_\n\b\3\t\3\t\3\n\3\n\3"+
		"\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\r\3\r\3\16\3\16\3"+
		"\16\3\16\7\16v\n\16\f\16\16\16y\13\16\3\16\3\16\3\17\3\17\3\17\3\17\3"+
		"\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\2\2\26"+
		"\3\3\5\2\7\2\t\4\13\2\r\2\17\5\21\2\23\2\25\2\27\2\31\2\33\6\35\7\37\b"+
		"!\t#\n%\13\'\f)\r\3\2\n\5\2\13\f\17\17\"\"\3\2\63;\3\2\62;\4\2GGgg\4\2"+
		"--//\5\2\62;CHch\n\2$$\61\61^^ddhhppttvv\5\2\2!$$^^\u008f\2\3\3\2\2\2"+
		"\2\t\3\2\2\2\2\17\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3"+
		"\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\3,\3\2\2\2\5:\3\2"+
		"\2\2\7<\3\2\2\2\tC\3\2\2\2\13Q\3\2\2\2\rV\3\2\2\2\17^\3\2\2\2\21`\3\2"+
		"\2\2\23b\3\2\2\2\25d\3\2\2\2\27l\3\2\2\2\31o\3\2\2\2\33q\3\2\2\2\35|\3"+
		"\2\2\2\37\u0081\3\2\2\2!\u0083\3\2\2\2#\u0085\3\2\2\2%\u0087\3\2\2\2\'"+
		"\u0089\3\2\2\2)\u008b\3\2\2\2+-\t\2\2\2,+\3\2\2\2-.\3\2\2\2.,\3\2\2\2"+
		"./\3\2\2\2/\60\3\2\2\2\60\61\b\2\2\2\61\4\3\2\2\2\62;\7\62\2\2\63\67\t"+
		"\3\2\2\64\66\t\4\2\2\65\64\3\2\2\2\669\3\2\2\2\67\65\3\2\2\2\678\3\2\2"+
		"\28;\3\2\2\29\67\3\2\2\2:\62\3\2\2\2:\63\3\2\2\2;\6\3\2\2\2<>\t\5\2\2"+
		"=?\t\6\2\2>=\3\2\2\2>?\3\2\2\2?@\3\2\2\2@A\5\5\3\2A\b\3\2\2\2BD\7/\2\2"+
		"CB\3\2\2\2CD\3\2\2\2DE\3\2\2\2EL\5\5\3\2FH\7\60\2\2GI\t\4\2\2HG\3\2\2"+
		"\2IJ\3\2\2\2JH\3\2\2\2JK\3\2\2\2KM\3\2\2\2LF\3\2\2\2LM\3\2\2\2MO\3\2\2"+
		"\2NP\5\7\4\2ON\3\2\2\2OP\3\2\2\2P\n\3\2\2\2QR\7v\2\2RS\7t\2\2ST\7w\2\2"+
		"TU\7g\2\2U\f\3\2\2\2VW\7h\2\2WX\7c\2\2XY\7n\2\2YZ\7u\2\2Z[\7g\2\2[\16"+
		"\3\2\2\2\\_\5\13\6\2]_\5\r\7\2^\\\3\2\2\2^]\3\2\2\2_\20\3\2\2\2`a\7$\2"+
		"\2a\22\3\2\2\2bc\t\7\2\2c\24\3\2\2\2de\7^\2\2ef\7w\2\2fg\3\2\2\2gh\5\23"+
		"\n\2hi\5\23\n\2ij\5\23\n\2jk\5\23\n\2k\26\3\2\2\2lm\7^\2\2mn\t\b\2\2n"+
		"\30\3\2\2\2op\n\t\2\2p\32\3\2\2\2qw\5\21\t\2rv\5\27\f\2sv\5\25\13\2tv"+
		"\5\31\r\2ur\3\2\2\2us\3\2\2\2ut\3\2\2\2vy\3\2\2\2wu\3\2\2\2wx\3\2\2\2"+
		"xz\3\2\2\2yw\3\2\2\2z{\5\21\t\2{\34\3\2\2\2|}\7p\2\2}~\7w\2\2~\177\7n"+
		"\2\2\177\u0080\7n\2\2\u0080\36\3\2\2\2\u0081\u0082\7}\2\2\u0082 \3\2\2"+
		"\2\u0083\u0084\7\177\2\2\u0084\"\3\2\2\2\u0085\u0086\7<\2\2\u0086$\3\2"+
		"\2\2\u0087\u0088\7.\2\2\u0088&\3\2\2\2\u0089\u008a\7]\2\2\u008a(\3\2\2"+
		"\2\u008b\u008c\7_\2\2\u008c*\3\2\2\2\16\2.\67:>CJLO^uw\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}