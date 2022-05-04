grammar Json;

// 公共部分
WS: [ \t\r\n]+ -> skip;

// 数字
fragment INT: '0' | [1-9][0-9]*; // no leading zeros
fragment SIC: [Ee][+\-]?INT;
NUMBER: '-'? INT ('.'[0-9]+) ? SIC? ;

// 布尔值
fragment TRUE: 'true';
fragment FALSE: 'false';
BOOL: TRUE | FALSE;

// 字符串
fragment STRING_FLAG: '"';
fragment HEX_CHAR: [0-9A-Fa-f];
fragment UNICODE: '\\u' HEX_CHAR HEX_CHAR HEX_CHAR HEX_CHAR;
fragment ESCAPE_CHAR: '\\' ["\\/bfnrt];
fragment SAFE_CODE_POINT: ~["\\\u0000-\u001F];
STRING: STRING_FLAG (ESCAPE_CHAR | UNICODE | SAFE_CODE_POINT)* STRING_FLAG;

// 注释 要跳过
fragment SINGLE_COMMENT: '//' (~('\r'|'\n'))* ;
fragment MULTIPLINE_COMMENT: '/*' .*? '*/';
COMMENT: (SINGLE_COMMENT | MULTIPLINE_COMMENT) -> skip;

// 空
NULL: 'null';

value
    : object
    | array
    | STRING
    | NUMBER
    | BOOL
    | NULL
    ;

// 对象
OBJ_START: '{';
OBJ_END: '}';
PAIR_SEPAR: ':' ;
pair: STRING PAIR_SEPAR value;
object
    : OBJ_START OBJ_END // empty object
    | OBJ_START pair (ARRAY_SEPAR pair)* OBJ_END
    ;

// 数组
ARRAY_SEPAR: ',' ;
ARRAY_START: '[';
ARRAY_END: ']';
array
    : ARRAY_START ARRAY_END // []
    | ARRAY_START value (',' value )* ARRAY_END
    ;

json : value;