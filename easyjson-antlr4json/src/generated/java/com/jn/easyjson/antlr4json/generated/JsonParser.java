// Generated from Json.g4 by ANTLR 4.5.3
package com.jn.easyjson.antlr4json.generated;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class JsonParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, NUMBER=2, BOOL=3, STRING=4, NULL=5, OBJ_START=6, OBJ_END=7, PAIR_SEPAR=8, 
		ARRAY_SEPAR=9, ARRAY_START=10, ARRAY_END=11;
	public static final int
		RULE_value = 0, RULE_pair = 1, RULE_object = 2, RULE_array = 3, RULE_json = 4;
	public static final String[] ruleNames = {
		"value", "pair", "object", "array", "json"
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

	@Override
	public String getGrammarFileName() { return "Json.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public JsonParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ValueContext extends ParserRuleContext {
		public ObjectContext object() {
			return getRuleContext(ObjectContext.class,0);
		}
		public ArrayContext array() {
			return getRuleContext(ArrayContext.class,0);
		}
		public TerminalNode STRING() { return getToken(JsonParser.STRING, 0); }
		public TerminalNode NUMBER() { return getToken(JsonParser.NUMBER, 0); }
		public TerminalNode BOOL() { return getToken(JsonParser.BOOL, 0); }
		public TerminalNode NULL() { return getToken(JsonParser.NULL, 0); }
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsonListener ) ((JsonListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsonListener ) ((JsonListener)listener).exitValue(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_value);
		try {
			setState(16);
			switch (_input.LA(1)) {
			case OBJ_START:
				enterOuterAlt(_localctx, 1);
				{
				setState(10);
				object();
				}
				break;
			case ARRAY_START:
				enterOuterAlt(_localctx, 2);
				{
				setState(11);
				array();
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 3);
				{
				setState(12);
				match(STRING);
				}
				break;
			case NUMBER:
				enterOuterAlt(_localctx, 4);
				{
				setState(13);
				match(NUMBER);
				}
				break;
			case BOOL:
				enterOuterAlt(_localctx, 5);
				{
				setState(14);
				match(BOOL);
				}
				break;
			case NULL:
				enterOuterAlt(_localctx, 6);
				{
				setState(15);
				match(NULL);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PairContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(JsonParser.STRING, 0); }
		public TerminalNode PAIR_SEPAR() { return getToken(JsonParser.PAIR_SEPAR, 0); }
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public PairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pair; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsonListener ) ((JsonListener)listener).enterPair(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsonListener ) ((JsonListener)listener).exitPair(this);
		}
	}

	public final PairContext pair() throws RecognitionException {
		PairContext _localctx = new PairContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_pair);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(18);
			match(STRING);
			setState(19);
			match(PAIR_SEPAR);
			setState(20);
			value();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ObjectContext extends ParserRuleContext {
		public TerminalNode OBJ_START() { return getToken(JsonParser.OBJ_START, 0); }
		public TerminalNode OBJ_END() { return getToken(JsonParser.OBJ_END, 0); }
		public List<PairContext> pair() {
			return getRuleContexts(PairContext.class);
		}
		public PairContext pair(int i) {
			return getRuleContext(PairContext.class,i);
		}
		public List<TerminalNode> ARRAY_SEPAR() { return getTokens(JsonParser.ARRAY_SEPAR); }
		public TerminalNode ARRAY_SEPAR(int i) {
			return getToken(JsonParser.ARRAY_SEPAR, i);
		}
		public ObjectContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_object; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsonListener ) ((JsonListener)listener).enterObject(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsonListener ) ((JsonListener)listener).exitObject(this);
		}
	}

	public final ObjectContext object() throws RecognitionException {
		ObjectContext _localctx = new ObjectContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_object);
		int _la;
		try {
			setState(35);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(22);
				match(OBJ_START);
				setState(23);
				match(OBJ_END);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(24);
				match(OBJ_START);
				setState(25);
				pair();
				setState(30);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==ARRAY_SEPAR) {
					{
					{
					setState(26);
					match(ARRAY_SEPAR);
					setState(27);
					pair();
					}
					}
					setState(32);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(33);
				match(OBJ_END);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayContext extends ParserRuleContext {
		public TerminalNode ARRAY_START() { return getToken(JsonParser.ARRAY_START, 0); }
		public TerminalNode ARRAY_END() { return getToken(JsonParser.ARRAY_END, 0); }
		public List<ValueContext> value() {
			return getRuleContexts(ValueContext.class);
		}
		public ValueContext value(int i) {
			return getRuleContext(ValueContext.class,i);
		}
		public ArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsonListener ) ((JsonListener)listener).enterArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsonListener ) ((JsonListener)listener).exitArray(this);
		}
	}

	public final ArrayContext array() throws RecognitionException {
		ArrayContext _localctx = new ArrayContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_array);
		int _la;
		try {
			setState(50);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(37);
				match(ARRAY_START);
				setState(38);
				match(ARRAY_END);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(39);
				match(ARRAY_START);
				setState(40);
				value();
				setState(45);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==ARRAY_SEPAR) {
					{
					{
					setState(41);
					match(ARRAY_SEPAR);
					setState(42);
					value();
					}
					}
					setState(47);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(48);
				match(ARRAY_END);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class JsonContext extends ParserRuleContext {
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public JsonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_json; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JsonListener ) ((JsonListener)listener).enterJson(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JsonListener ) ((JsonListener)listener).exitJson(this);
		}
	}

	public final JsonContext json() throws RecognitionException {
		JsonContext _localctx = new JsonContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_json);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(52);
			value();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\r9\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\3\2\3\2\3\2\3\2\3\2\3\2\5\2\23\n\2\3\3\3\3"+
		"\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\7\4\37\n\4\f\4\16\4\"\13\4\3\4\3\4\5"+
		"\4&\n\4\3\5\3\5\3\5\3\5\3\5\3\5\7\5.\n\5\f\5\16\5\61\13\5\3\5\3\5\5\5"+
		"\65\n\5\3\6\3\6\3\6\2\2\7\2\4\6\b\n\2\2<\2\22\3\2\2\2\4\24\3\2\2\2\6%"+
		"\3\2\2\2\b\64\3\2\2\2\n\66\3\2\2\2\f\23\5\6\4\2\r\23\5\b\5\2\16\23\7\6"+
		"\2\2\17\23\7\4\2\2\20\23\7\5\2\2\21\23\7\7\2\2\22\f\3\2\2\2\22\r\3\2\2"+
		"\2\22\16\3\2\2\2\22\17\3\2\2\2\22\20\3\2\2\2\22\21\3\2\2\2\23\3\3\2\2"+
		"\2\24\25\7\6\2\2\25\26\7\n\2\2\26\27\5\2\2\2\27\5\3\2\2\2\30\31\7\b\2"+
		"\2\31&\7\t\2\2\32\33\7\b\2\2\33 \5\4\3\2\34\35\7\13\2\2\35\37\5\4\3\2"+
		"\36\34\3\2\2\2\37\"\3\2\2\2 \36\3\2\2\2 !\3\2\2\2!#\3\2\2\2\" \3\2\2\2"+
		"#$\7\t\2\2$&\3\2\2\2%\30\3\2\2\2%\32\3\2\2\2&\7\3\2\2\2\'(\7\f\2\2(\65"+
		"\7\r\2\2)*\7\f\2\2*/\5\2\2\2+,\7\13\2\2,.\5\2\2\2-+\3\2\2\2.\61\3\2\2"+
		"\2/-\3\2\2\2/\60\3\2\2\2\60\62\3\2\2\2\61/\3\2\2\2\62\63\7\r\2\2\63\65"+
		"\3\2\2\2\64\'\3\2\2\2\64)\3\2\2\2\65\t\3\2\2\2\66\67\5\2\2\2\67\13\3\2"+
		"\2\2\7\22 %/\64";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}