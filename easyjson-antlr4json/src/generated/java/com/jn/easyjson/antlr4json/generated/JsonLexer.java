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
		WS=1, NUMBER=2, BOOL=3, STRING=4, COMMENT=5, NULL=6, OBJ_START=7, OBJ_END=8, 
		PAIR_SEPAR=9, ARRAY_SEPAR=10, ARRAY_START=11, ARRAY_END=12;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"WS", "INT", "SIC", "NUMBER", "TRUE", "FALSE", "BOOL", "STRING_FLAG", 
		"HEX_CHAR", "UNICODE", "ESCAPE_CHAR", "SAFE_CODE_POINT", "STRING", "SINGLE_COMMENT", 
		"MULTIPLINE_COMMENT", "COMMENT", "NULL", "OBJ_START", "OBJ_END", "PAIR_SEPAR", 
		"ARRAY_SEPAR", "ARRAY_START", "ARRAY_END"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, null, null, null, null, "'null'", "'{'", "'}'", "':'", "','", 
		"'['", "']'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "WS", "NUMBER", "BOOL", "STRING", "COMMENT", "NULL", "OBJ_START", 
		"OBJ_END", "PAIR_SEPAR", "ARRAY_SEPAR", "ARRAY_START", "ARRAY_END"
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\16\u00ae\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\3\2"+
		"\6\2\63\n\2\r\2\16\2\64\3\2\3\2\3\3\3\3\3\3\7\3<\n\3\f\3\16\3?\13\3\5"+
		"\3A\n\3\3\4\3\4\5\4E\n\4\3\4\3\4\3\5\5\5J\n\5\3\5\3\5\3\5\6\5O\n\5\r\5"+
		"\16\5P\5\5S\n\5\3\5\5\5V\n\5\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3"+
		"\7\3\b\3\b\5\be\n\b\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\f\3\f\3\f\3\r\3\r\3\16\3\16\3\16\3\16\7\16|\n\16\f\16\16\16\177"+
		"\13\16\3\16\3\16\3\17\3\17\3\17\3\17\7\17\u0087\n\17\f\17\16\17\u008a"+
		"\13\17\3\20\3\20\3\20\3\20\7\20\u0090\n\20\f\20\16\20\u0093\13\20\3\20"+
		"\3\20\3\20\3\21\3\21\5\21\u009a\n\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22"+
		"\3\23\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\u0091\2"+
		"\31\3\3\5\2\7\2\t\4\13\2\r\2\17\5\21\2\23\2\25\2\27\2\31\2\33\6\35\2\37"+
		"\2!\7#\b%\t\'\n)\13+\f-\r/\16\3\2\13\5\2\13\f\17\17\"\"\3\2\63;\3\2\62"+
		";\4\2GGgg\4\2--//\5\2\62;CHch\n\2$$\61\61^^ddhhppttvv\5\2\2!$$^^\4\2\f"+
		"\f\17\17\u00b1\2\3\3\2\2\2\2\t\3\2\2\2\2\17\3\2\2\2\2\33\3\2\2\2\2!\3"+
		"\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2"+
		"\2\2\2/\3\2\2\2\3\62\3\2\2\2\5@\3\2\2\2\7B\3\2\2\2\tI\3\2\2\2\13W\3\2"+
		"\2\2\r\\\3\2\2\2\17d\3\2\2\2\21f\3\2\2\2\23h\3\2\2\2\25j\3\2\2\2\27r\3"+
		"\2\2\2\31u\3\2\2\2\33w\3\2\2\2\35\u0082\3\2\2\2\37\u008b\3\2\2\2!\u0099"+
		"\3\2\2\2#\u009d\3\2\2\2%\u00a2\3\2\2\2\'\u00a4\3\2\2\2)\u00a6\3\2\2\2"+
		"+\u00a8\3\2\2\2-\u00aa\3\2\2\2/\u00ac\3\2\2\2\61\63\t\2\2\2\62\61\3\2"+
		"\2\2\63\64\3\2\2\2\64\62\3\2\2\2\64\65\3\2\2\2\65\66\3\2\2\2\66\67\b\2"+
		"\2\2\67\4\3\2\2\28A\7\62\2\29=\t\3\2\2:<\t\4\2\2;:\3\2\2\2<?\3\2\2\2="+
		";\3\2\2\2=>\3\2\2\2>A\3\2\2\2?=\3\2\2\2@8\3\2\2\2@9\3\2\2\2A\6\3\2\2\2"+
		"BD\t\5\2\2CE\t\6\2\2DC\3\2\2\2DE\3\2\2\2EF\3\2\2\2FG\5\5\3\2G\b\3\2\2"+
		"\2HJ\7/\2\2IH\3\2\2\2IJ\3\2\2\2JK\3\2\2\2KR\5\5\3\2LN\7\60\2\2MO\t\4\2"+
		"\2NM\3\2\2\2OP\3\2\2\2PN\3\2\2\2PQ\3\2\2\2QS\3\2\2\2RL\3\2\2\2RS\3\2\2"+
		"\2SU\3\2\2\2TV\5\7\4\2UT\3\2\2\2UV\3\2\2\2V\n\3\2\2\2WX\7v\2\2XY\7t\2"+
		"\2YZ\7w\2\2Z[\7g\2\2[\f\3\2\2\2\\]\7h\2\2]^\7c\2\2^_\7n\2\2_`\7u\2\2`"+
		"a\7g\2\2a\16\3\2\2\2be\5\13\6\2ce\5\r\7\2db\3\2\2\2dc\3\2\2\2e\20\3\2"+
		"\2\2fg\7$\2\2g\22\3\2\2\2hi\t\7\2\2i\24\3\2\2\2jk\7^\2\2kl\7w\2\2lm\3"+
		"\2\2\2mn\5\23\n\2no\5\23\n\2op\5\23\n\2pq\5\23\n\2q\26\3\2\2\2rs\7^\2"+
		"\2st\t\b\2\2t\30\3\2\2\2uv\n\t\2\2v\32\3\2\2\2w}\5\21\t\2x|\5\27\f\2y"+
		"|\5\25\13\2z|\5\31\r\2{x\3\2\2\2{y\3\2\2\2{z\3\2\2\2|\177\3\2\2\2}{\3"+
		"\2\2\2}~\3\2\2\2~\u0080\3\2\2\2\177}\3\2\2\2\u0080\u0081\5\21\t\2\u0081"+
		"\34\3\2\2\2\u0082\u0083\7\61\2\2\u0083\u0084\7\61\2\2\u0084\u0088\3\2"+
		"\2\2\u0085\u0087\n\n\2\2\u0086\u0085\3\2\2\2\u0087\u008a\3\2\2\2\u0088"+
		"\u0086\3\2\2\2\u0088\u0089\3\2\2\2\u0089\36\3\2\2\2\u008a\u0088\3\2\2"+
		"\2\u008b\u008c\7\61\2\2\u008c\u008d\7,\2\2\u008d\u0091\3\2\2\2\u008e\u0090"+
		"\13\2\2\2\u008f\u008e\3\2\2\2\u0090\u0093\3\2\2\2\u0091\u0092\3\2\2\2"+
		"\u0091\u008f\3\2\2\2\u0092\u0094\3\2\2\2\u0093\u0091\3\2\2\2\u0094\u0095"+
		"\7,\2\2\u0095\u0096\7\61\2\2\u0096 \3\2\2\2\u0097\u009a\5\35\17\2\u0098"+
		"\u009a\5\37\20\2\u0099\u0097\3\2\2\2\u0099\u0098\3\2\2\2\u009a\u009b\3"+
		"\2\2\2\u009b\u009c\b\21\2\2\u009c\"\3\2\2\2\u009d\u009e\7p\2\2\u009e\u009f"+
		"\7w\2\2\u009f\u00a0\7n\2\2\u00a0\u00a1\7n\2\2\u00a1$\3\2\2\2\u00a2\u00a3"+
		"\7}\2\2\u00a3&\3\2\2\2\u00a4\u00a5\7\177\2\2\u00a5(\3\2\2\2\u00a6\u00a7"+
		"\7<\2\2\u00a7*\3\2\2\2\u00a8\u00a9\7.\2\2\u00a9,\3\2\2\2\u00aa\u00ab\7"+
		"]\2\2\u00ab.\3\2\2\2\u00ac\u00ad\7_\2\2\u00ad\60\3\2\2\2\21\2\64=@DIP"+
		"RUd{}\u0088\u0091\u0099\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}