/* A Bison parser, made by GNU Bison 3.8.2.  */

/* Bison implementation for Yacc-like parsers in C

   Copyright (C) 1984, 1989-1990, 2000-2015, 2018-2021 Free Software Foundation,
   Inc.

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <https://www.gnu.org/licenses/>.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.

   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */

/* C LALR(1) parser skeleton written by Richard Stallman, by
   simplifying the original so-called "semantic" parser.  */

/* DO NOT RELY ON FEATURES THAT ARE NOT DOCUMENTED in the manual,
   especially those whose name start with YY_ or yy_.  They are
   private implementation details that can be changed or removed.  */

/* All symbols defined below should begin with yy or YY, to avoid
   infringing on user name space.  This should be done even for local
   variables, as they might otherwise be expanded by user macros.
   There are some unavoidable exceptions within include files to
   define necessary library symbols; they are noted "INFRINGES ON
   USER NAME SPACE" below.  */

/* Identify Bison output, and Bison version.  */
#define YYBISON 30802

/* Bison version string.  */
#define YYBISON_VERSION "3.8.2"

/* Skeleton name.  */
#define YYSKELETON_NAME "yacc.c"

/* Pure parsers.  */
#define YYPURE 0

/* Push parsers.  */
#define YYPUSH 0

/* Pull parsers.  */
#define YYPULL 1




/* First part of user prologue.  */
#line 1 "plp4.y"

#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <string>
#include <iostream>
using namespace std;
#include "comun.h"
#include "TablaSimbolos.h"

extern int ncol, nlin, findefichero;
string prefijo = "";       // prefijo del ámbito actual
bool provieneDV = false;   // true si DV viene de A, false si viene de I

/* las necesarias para flex */
extern int yylex();
extern char *yytext;
extern int yyleng;
extern FILE *yyin;

int yyerror(char *s);

TablaSimbolos *tsActual = new TablaSimbolos(NULL);

const int ERRYADECL=1, ERRNODECL=2, ERRTIPOS=3, ERRNOSIMPLE=4, ERRNOENTERO=5;
Atributos opera(string trad1, int tipo1, string trad2, int tipo2, string operador);
void errorSemantico(int nerror, char *lexema, int fila, int columna);

#line 100 "plp4.tab.c"

# ifndef YY_CAST
#  ifdef __cplusplus
#   define YY_CAST(Type, Val) static_cast<Type> (Val)
#   define YY_REINTERPRET_CAST(Type, Val) reinterpret_cast<Type> (Val)
#  else
#   define YY_CAST(Type, Val) ((Type) (Val))
#   define YY_REINTERPRET_CAST(Type, Val) ((Type) (Val))
#  endif
# endif
# ifndef YY_NULLPTR
#  if defined __cplusplus
#   if 201103L <= __cplusplus
#    define YY_NULLPTR nullptr
#   else
#    define YY_NULLPTR 0
#   endif
#  else
#   define YY_NULLPTR ((void*)0)
#  endif
# endif

#include "plp4.tab.h"
/* Symbol kind.  */
enum yysymbol_kind_t
{
  YYSYMBOL_YYEMPTY = -2,
  YYSYMBOL_YYEOF = 0,                      /* "end of file"  */
  YYSYMBOL_YYerror = 1,                    /* error  */
  YYSYMBOL_YYUNDEF = 2,                    /* "invalid token"  */
  YYSYMBOL_CLASS = 3,                      /* CLASS  */
  YYSYMBOL_FUN = 4,                        /* FUN  */
  YYSYMBOL_INT = 5,                        /* INT  */
  YYSYMBOL_FLOAT = 6,                      /* FLOAT  */
  YYSYMBOL_IF = 7,                         /* IF  */
  YYSYMBOL_ELSE = 8,                       /* ELSE  */
  YYSYMBOL_FI = 9,                         /* FI  */
  YYSYMBOL_PRINT = 10,                     /* PRINT  */
  YYSYMBOL_ID = 11,                        /* ID  */
  YYSYMBOL_NUMENTERO = 12,                 /* NUMENTERO  */
  YYSYMBOL_NUMREAL = 13,                   /* NUMREAL  */
  YYSYMBOL_OPAS = 14,                      /* OPAS  */
  YYSYMBOL_OPMUL = 15,                     /* OPMUL  */
  YYSYMBOL_OPREL = 16,                     /* OPREL  */
  YYSYMBOL_ASIG = 17,                      /* ASIG  */
  YYSYMBOL_LBRA = 18,                      /* LBRA  */
  YYSYMBOL_RBRA = 19,                      /* RBRA  */
  YYSYMBOL_PARI = 20,                      /* PARI  */
  YYSYMBOL_PARD = 21,                      /* PARD  */
  YYSYMBOL_PYC = 22,                       /* PYC  */
  YYSYMBOL_DOSP = 23,                      /* DOSP  */
  YYSYMBOL_YYACCEPT = 24,                  /* $accept  */
  YYSYMBOL_X = 25,                         /* X  */
  YYSYMBOL_S = 26,                         /* S  */
  YYSYMBOL_27_1 = 27,                      /* @1  */
  YYSYMBOL_M = 28,                         /* M  */
  YYSYMBOL_SF = 29,                        /* SF  */
  YYSYMBOL_Fun = 30,                       /* Fun  */
  YYSYMBOL_31_2 = 31,                      /* @2  */
  YYSYMBOL_A = 32,                         /* A  */
  YYSYMBOL_33_3 = 33,                      /* $@3  */
  YYSYMBOL_34_4 = 34,                      /* $@4  */
  YYSYMBOL_DV = 35,                        /* DV  */
  YYSYMBOL_Tipo = 36,                      /* Tipo  */
  YYSYMBOL_Cod = 37,                       /* Cod  */
  YYSYMBOL_I = 38,                         /* I  */
  YYSYMBOL_39_5 = 39,                      /* $@5  */
  YYSYMBOL_40_6 = 40,                      /* @6  */
  YYSYMBOL_Ip = 41,                        /* Ip  */
  YYSYMBOL_Expr = 42,                      /* Expr  */
  YYSYMBOL_E = 43,                         /* E  */
  YYSYMBOL_T = 44,                         /* T  */
  YYSYMBOL_F = 45                          /* F  */
};
typedef enum yysymbol_kind_t yysymbol_kind_t;




#ifdef short
# undef short
#endif

/* On compilers that do not define __PTRDIFF_MAX__ etc., make sure
   <limits.h> and (if available) <stdint.h> are included
   so that the code can choose integer types of a good width.  */

#ifndef __PTRDIFF_MAX__
# include <limits.h> /* INFRINGES ON USER NAME SPACE */
# if defined __STDC_VERSION__ && 199901 <= __STDC_VERSION__
#  include <stdint.h> /* INFRINGES ON USER NAME SPACE */
#  define YY_STDINT_H
# endif
#endif

/* Narrow types that promote to a signed type and that can represent a
   signed or unsigned integer of at least N bits.  In tables they can
   save space and decrease cache pressure.  Promoting to a signed type
   helps avoid bugs in integer arithmetic.  */

#ifdef __INT_LEAST8_MAX__
typedef __INT_LEAST8_TYPE__ yytype_int8;
#elif defined YY_STDINT_H
typedef int_least8_t yytype_int8;
#else
typedef signed char yytype_int8;
#endif

#ifdef __INT_LEAST16_MAX__
typedef __INT_LEAST16_TYPE__ yytype_int16;
#elif defined YY_STDINT_H
typedef int_least16_t yytype_int16;
#else
typedef short yytype_int16;
#endif

/* Work around bug in HP-UX 11.23, which defines these macros
   incorrectly for preprocessor constants.  This workaround can likely
   be removed in 2023, as HPE has promised support for HP-UX 11.23
   (aka HP-UX 11i v2) only through the end of 2022; see Table 2 of
   <https://h20195.www2.hpe.com/V2/getpdf.aspx/4AA4-7673ENW.pdf>.  */
#ifdef __hpux
# undef UINT_LEAST8_MAX
# undef UINT_LEAST16_MAX
# define UINT_LEAST8_MAX 255
# define UINT_LEAST16_MAX 65535
#endif

#if defined __UINT_LEAST8_MAX__ && __UINT_LEAST8_MAX__ <= __INT_MAX__
typedef __UINT_LEAST8_TYPE__ yytype_uint8;
#elif (!defined __UINT_LEAST8_MAX__ && defined YY_STDINT_H \
       && UINT_LEAST8_MAX <= INT_MAX)
typedef uint_least8_t yytype_uint8;
#elif !defined __UINT_LEAST8_MAX__ && UCHAR_MAX <= INT_MAX
typedef unsigned char yytype_uint8;
#else
typedef short yytype_uint8;
#endif

#if defined __UINT_LEAST16_MAX__ && __UINT_LEAST16_MAX__ <= __INT_MAX__
typedef __UINT_LEAST16_TYPE__ yytype_uint16;
#elif (!defined __UINT_LEAST16_MAX__ && defined YY_STDINT_H \
       && UINT_LEAST16_MAX <= INT_MAX)
typedef uint_least16_t yytype_uint16;
#elif !defined __UINT_LEAST16_MAX__ && USHRT_MAX <= INT_MAX
typedef unsigned short yytype_uint16;
#else
typedef int yytype_uint16;
#endif

#ifndef YYPTRDIFF_T
# if defined __PTRDIFF_TYPE__ && defined __PTRDIFF_MAX__
#  define YYPTRDIFF_T __PTRDIFF_TYPE__
#  define YYPTRDIFF_MAXIMUM __PTRDIFF_MAX__
# elif defined PTRDIFF_MAX
#  ifndef ptrdiff_t
#   include <stddef.h> /* INFRINGES ON USER NAME SPACE */
#  endif
#  define YYPTRDIFF_T ptrdiff_t
#  define YYPTRDIFF_MAXIMUM PTRDIFF_MAX
# else
#  define YYPTRDIFF_T long
#  define YYPTRDIFF_MAXIMUM LONG_MAX
# endif
#endif

#ifndef YYSIZE_T
# ifdef __SIZE_TYPE__
#  define YYSIZE_T __SIZE_TYPE__
# elif defined size_t
#  define YYSIZE_T size_t
# elif defined __STDC_VERSION__ && 199901 <= __STDC_VERSION__
#  include <stddef.h> /* INFRINGES ON USER NAME SPACE */
#  define YYSIZE_T size_t
# else
#  define YYSIZE_T unsigned
# endif
#endif

#define YYSIZE_MAXIMUM                                  \
  YY_CAST (YYPTRDIFF_T,                                 \
           (YYPTRDIFF_MAXIMUM < YY_CAST (YYSIZE_T, -1)  \
            ? YYPTRDIFF_MAXIMUM                         \
            : YY_CAST (YYSIZE_T, -1)))

#define YYSIZEOF(X) YY_CAST (YYPTRDIFF_T, sizeof (X))


/* Stored state numbers (used for stacks). */
typedef yytype_int8 yy_state_t;

/* State numbers in computations.  */
typedef int yy_state_fast_t;

#ifndef YY_
# if defined YYENABLE_NLS && YYENABLE_NLS
#  if ENABLE_NLS
#   include <libintl.h> /* INFRINGES ON USER NAME SPACE */
#   define YY_(Msgid) dgettext ("bison-runtime", Msgid)
#  endif
# endif
# ifndef YY_
#  define YY_(Msgid) Msgid
# endif
#endif


#ifndef YY_ATTRIBUTE_PURE
# if defined __GNUC__ && 2 < __GNUC__ + (96 <= __GNUC_MINOR__)
#  define YY_ATTRIBUTE_PURE __attribute__ ((__pure__))
# else
#  define YY_ATTRIBUTE_PURE
# endif
#endif

#ifndef YY_ATTRIBUTE_UNUSED
# if defined __GNUC__ && 2 < __GNUC__ + (7 <= __GNUC_MINOR__)
#  define YY_ATTRIBUTE_UNUSED __attribute__ ((__unused__))
# else
#  define YY_ATTRIBUTE_UNUSED
# endif
#endif

/* Suppress unused-variable warnings by "using" E.  */
#if ! defined lint || defined __GNUC__
# define YY_USE(E) ((void) (E))
#else
# define YY_USE(E) /* empty */
#endif

/* Suppress an incorrect diagnostic about yylval being uninitialized.  */
#if defined __GNUC__ && ! defined __ICC && 406 <= __GNUC__ * 100 + __GNUC_MINOR__
# if __GNUC__ * 100 + __GNUC_MINOR__ < 407
#  define YY_IGNORE_MAYBE_UNINITIALIZED_BEGIN                           \
    _Pragma ("GCC diagnostic push")                                     \
    _Pragma ("GCC diagnostic ignored \"-Wuninitialized\"")
# else
#  define YY_IGNORE_MAYBE_UNINITIALIZED_BEGIN                           \
    _Pragma ("GCC diagnostic push")                                     \
    _Pragma ("GCC diagnostic ignored \"-Wuninitialized\"")              \
    _Pragma ("GCC diagnostic ignored \"-Wmaybe-uninitialized\"")
# endif
# define YY_IGNORE_MAYBE_UNINITIALIZED_END      \
    _Pragma ("GCC diagnostic pop")
#else
# define YY_INITIAL_VALUE(Value) Value
#endif
#ifndef YY_IGNORE_MAYBE_UNINITIALIZED_BEGIN
# define YY_IGNORE_MAYBE_UNINITIALIZED_BEGIN
# define YY_IGNORE_MAYBE_UNINITIALIZED_END
#endif
#ifndef YY_INITIAL_VALUE
# define YY_INITIAL_VALUE(Value) /* Nothing. */
#endif

#if defined __cplusplus && defined __GNUC__ && ! defined __ICC && 6 <= __GNUC__
# define YY_IGNORE_USELESS_CAST_BEGIN                          \
    _Pragma ("GCC diagnostic push")                            \
    _Pragma ("GCC diagnostic ignored \"-Wuseless-cast\"")
# define YY_IGNORE_USELESS_CAST_END            \
    _Pragma ("GCC diagnostic pop")
#endif
#ifndef YY_IGNORE_USELESS_CAST_BEGIN
# define YY_IGNORE_USELESS_CAST_BEGIN
# define YY_IGNORE_USELESS_CAST_END
#endif


#define YY_ASSERT(E) ((void) (0 && (E)))

#if !defined yyoverflow

/* The parser invokes alloca or malloc; define the necessary symbols.  */

# ifdef YYSTACK_USE_ALLOCA
#  if YYSTACK_USE_ALLOCA
#   ifdef __GNUC__
#    define YYSTACK_ALLOC __builtin_alloca
#   elif defined __BUILTIN_VA_ARG_INCR
#    include <alloca.h> /* INFRINGES ON USER NAME SPACE */
#   elif defined _AIX
#    define YYSTACK_ALLOC __alloca
#   elif defined _MSC_VER
#    include <malloc.h> /* INFRINGES ON USER NAME SPACE */
#    define alloca _alloca
#   else
#    define YYSTACK_ALLOC alloca
#    if ! defined _ALLOCA_H && ! defined EXIT_SUCCESS
#     include <stdlib.h> /* INFRINGES ON USER NAME SPACE */
      /* Use EXIT_SUCCESS as a witness for stdlib.h.  */
#     ifndef EXIT_SUCCESS
#      define EXIT_SUCCESS 0
#     endif
#    endif
#   endif
#  endif
# endif

# ifdef YYSTACK_ALLOC
   /* Pacify GCC's 'empty if-body' warning.  */
#  define YYSTACK_FREE(Ptr) do { /* empty */; } while (0)
#  ifndef YYSTACK_ALLOC_MAXIMUM
    /* The OS might guarantee only one guard page at the bottom of the stack,
       and a page size can be as small as 4096 bytes.  So we cannot safely
       invoke alloca (N) if N exceeds 4096.  Use a slightly smaller number
       to allow for a few compiler-allocated temporary stack slots.  */
#   define YYSTACK_ALLOC_MAXIMUM 4032 /* reasonable circa 2006 */
#  endif
# else
#  define YYSTACK_ALLOC YYMALLOC
#  define YYSTACK_FREE YYFREE
#  ifndef YYSTACK_ALLOC_MAXIMUM
#   define YYSTACK_ALLOC_MAXIMUM YYSIZE_MAXIMUM
#  endif
#  if (defined __cplusplus && ! defined EXIT_SUCCESS \
       && ! ((defined YYMALLOC || defined malloc) \
             && (defined YYFREE || defined free)))
#   include <stdlib.h> /* INFRINGES ON USER NAME SPACE */
#   ifndef EXIT_SUCCESS
#    define EXIT_SUCCESS 0
#   endif
#  endif
#  ifndef YYMALLOC
#   define YYMALLOC malloc
#   if ! defined malloc && ! defined EXIT_SUCCESS
void *malloc (YYSIZE_T); /* INFRINGES ON USER NAME SPACE */
#   endif
#  endif
#  ifndef YYFREE
#   define YYFREE free
#   if ! defined free && ! defined EXIT_SUCCESS
void free (void *); /* INFRINGES ON USER NAME SPACE */
#   endif
#  endif
# endif
#endif /* !defined yyoverflow */

#if (! defined yyoverflow \
     && (! defined __cplusplus \
         || (defined YYSTYPE_IS_TRIVIAL && YYSTYPE_IS_TRIVIAL)))

/* A type that is properly aligned for any stack member.  */
union yyalloc
{
  yy_state_t yyss_alloc;
  YYSTYPE yyvs_alloc;
};

/* The size of the maximum gap between one aligned stack and the next.  */
# define YYSTACK_GAP_MAXIMUM (YYSIZEOF (union yyalloc) - 1)

/* The size of an array large to enough to hold all stacks, each with
   N elements.  */
# define YYSTACK_BYTES(N) \
     ((N) * (YYSIZEOF (yy_state_t) + YYSIZEOF (YYSTYPE)) \
      + YYSTACK_GAP_MAXIMUM)

# define YYCOPY_NEEDED 1

/* Relocate STACK from its old location to the new one.  The
   local variables YYSIZE and YYSTACKSIZE give the old and new number of
   elements in the stack, and YYPTR gives the new location of the
   stack.  Advance YYPTR to a properly aligned location for the next
   stack.  */
# define YYSTACK_RELOCATE(Stack_alloc, Stack)                           \
    do                                                                  \
      {                                                                 \
        YYPTRDIFF_T yynewbytes;                                         \
        YYCOPY (&yyptr->Stack_alloc, Stack, yysize);                    \
        Stack = &yyptr->Stack_alloc;                                    \
        yynewbytes = yystacksize * YYSIZEOF (*Stack) + YYSTACK_GAP_MAXIMUM; \
        yyptr += yynewbytes / YYSIZEOF (*yyptr);                        \
      }                                                                 \
    while (0)

#endif

#if defined YYCOPY_NEEDED && YYCOPY_NEEDED
/* Copy COUNT objects from SRC to DST.  The source and destination do
   not overlap.  */
# ifndef YYCOPY
#  if defined __GNUC__ && 1 < __GNUC__
#   define YYCOPY(Dst, Src, Count) \
      __builtin_memcpy (Dst, Src, YY_CAST (YYSIZE_T, (Count)) * sizeof (*(Src)))
#  else
#   define YYCOPY(Dst, Src, Count)              \
      do                                        \
        {                                       \
          YYPTRDIFF_T yyi;                      \
          for (yyi = 0; yyi < (Count); yyi++)   \
            (Dst)[yyi] = (Src)[yyi];            \
        }                                       \
      while (0)
#  endif
# endif
#endif /* !YYCOPY_NEEDED */

/* YYFINAL -- State number of the termination state.  */
#define YYFINAL  5
/* YYLAST -- Last index in YYTABLE.  */
#define YYLAST   54

/* YYNTOKENS -- Number of terminals.  */
#define YYNTOKENS  24
/* YYNNTS -- Number of nonterminals.  */
#define YYNNTS  22
/* YYNRULES -- Number of rules.  */
#define YYNRULES  38
/* YYNSTATES -- Number of states.  */
#define YYNSTATES  68

/* YYMAXUTOK -- Last valid token kind.  */
#define YYMAXUTOK   278


/* YYTRANSLATE(TOKEN-NUM) -- Symbol number corresponding to TOKEN-NUM
   as returned by yylex, with out-of-bounds checking.  */
#define YYTRANSLATE(YYX)                                \
  (0 <= (YYX) && (YYX) <= YYMAXUTOK                     \
   ? YY_CAST (yysymbol_kind_t, yytranslate[YYX])        \
   : YYSYMBOL_YYUNDEF)

/* YYTRANSLATE[TOKEN-NUM] -- Symbol number corresponding to TOKEN-NUM
   as returned by yylex.  */
static const yytype_int8 yytranslate[] =
{
       0,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     1,     2,     3,     4,
       5,     6,     7,     8,     9,    10,    11,    12,    13,    14,
      15,    16,    17,    18,    19,    20,    21,    22,    23
};

#if YYDEBUG
/* YYRLINE[YYN] -- Source line where rule number YYN was defined.  */
static const yytype_int16 yyrline[] =
{
       0,    40,    40,    46,    46,    64,    68,    73,    74,    77,
      77,    90,    90,    93,    93,    99,   119,   123,   130,   133,
     139,   139,   143,   142,   153,   175,   183,   190,   193,   199,
     204,   211,   216,   223,   228,   235,   239,   244,   261
};
#endif

/** Accessing symbol of state STATE.  */
#define YY_ACCESSING_SYMBOL(State) YY_CAST (yysymbol_kind_t, yystos[State])

#if YYDEBUG || 0
/* The user-facing name of the symbol whose (internal) number is
   YYSYMBOL.  No bounds checking.  */
static const char *yysymbol_name (yysymbol_kind_t yysymbol) YY_ATTRIBUTE_UNUSED;

/* YYTNAME[SYMBOL-NUM] -- String name of the symbol SYMBOL-NUM.
   First, the terminals, then, starting at YYNTOKENS, nonterminals.  */
static const char *const yytname[] =
{
  "\"end of file\"", "error", "\"invalid token\"", "CLASS", "FUN", "INT",
  "FLOAT", "IF", "ELSE", "FI", "PRINT", "ID", "NUMENTERO", "NUMREAL",
  "OPAS", "OPMUL", "OPREL", "ASIG", "LBRA", "RBRA", "PARI", "PARD", "PYC",
  "DOSP", "$accept", "X", "S", "@1", "M", "SF", "Fun", "@2", "A", "$@3",
  "$@4", "DV", "Tipo", "Cod", "I", "$@5", "@6", "Ip", "Expr", "E", "T",
  "F", YY_NULLPTR
};

static const char *
yysymbol_name (yysymbol_kind_t yysymbol)
{
  return yytname[yysymbol];
}
#endif

#define YYPACT_NINF (-46)

#define yypact_value_is_default(Yyn) \
  ((Yyn) == YYPACT_NINF)

#define YYTABLE_NINF (-1)

#define yytable_value_is_error(Yyn) \
  0

/* YYPACT[STATE-NUM] -- Index in YYTABLE of the portion describing
   STATE-NUM.  */
static const yytype_int8 yypact[] =
{
      34,    27,    39,   -46,   -46,   -46,    22,   -46,    -3,    30,
     -46,   -46,   -46,   -46,   -46,   -46,     8,    15,   -46,   -46,
     -46,   -46,   -46,    31,     4,    15,   -46,    12,    12,    28,
     -46,     9,   -46,    15,   -46,   -46,   -46,   -46,    12,    20,
      13,    29,   -46,   -46,    12,    -1,   -46,    -1,   -46,    25,
      -1,    12,    12,    12,   -46,    14,   -46,   -46,    26,    29,
      33,   -46,   -46,    -1,   -46,   -46,    40,   -46
};

/* YYDEFACT[STATE-NUM] -- Default reduction number in state STATE-NUM.
   Performed when YYTABLE does not specify something else to do.  Zero
   means the default is an error.  */
static const yytype_int8 yydefact[] =
{
       0,     0,     0,     2,     3,     1,     0,     6,     0,     0,
       4,     7,     5,     8,     9,    13,     0,     0,     6,    11,
      16,    17,    14,     0,    20,     0,    15,     0,     0,     0,
      22,     0,    19,     0,    12,    37,    35,    36,     0,     0,
      30,    32,    34,    26,     0,    20,    10,    20,    21,     0,
      20,     0,     0,     0,    24,     0,    18,    38,     0,    31,
      29,    33,    23,    20,    28,    25,     0,    27
};

/* YYPGOTO[NTERM-NUM].  */
static const yytype_int8 yypgoto[] =
{
     -46,   -46,    48,   -46,    32,   -46,   -46,   -46,   -46,   -46,
     -46,   -21,   -46,     6,   -45,   -46,   -46,   -46,   -25,     0,
       2,     1
};

/* YYDEFGOTO[NTERM-NUM].  */
static const yytype_int8 yydefgoto[] =
{
       0,     2,    11,     6,     8,    12,    13,    15,    16,    25,
      17,    22,    23,    31,    32,    33,    45,    65,    39,    40,
      41,    42
};

/* YYTABLE[YYPACT[STATE-NUM]] -- What to do in state STATE-NUM.  If
   positive, shift that token.  If negative, reduce the rule whose
   number is the opposite.  If YYTABLE_NINF, syntax error.  */
static const yytype_int8 yytable[] =
{
       1,     9,    56,    43,    34,    58,    27,     1,     9,    28,
      29,    27,    48,    49,    28,    29,    10,    30,    66,    54,
      20,    21,    30,    35,    36,    37,    18,    51,    46,    52,
      19,    47,    38,    62,    63,    64,    47,     1,     4,     5,
       7,    14,    26,    50,    53,    44,    57,    51,     3,    67,
      24,    55,    60,    59,    61
};

static const yytype_int8 yycheck[] =
{
       3,     4,    47,    28,    25,    50,     7,     3,     4,    10,
      11,     7,    33,    38,    10,    11,    19,    18,    63,    44,
       5,     6,    18,    11,    12,    13,    18,    14,    19,    16,
      22,    22,    20,    19,     8,     9,    22,     3,    11,     0,
      18,    11,    11,    23,    15,    17,    21,    14,     0,     9,
      18,    45,    52,    51,    53
};

/* YYSTOS[STATE-NUM] -- The symbol kind of the accessing symbol of
   state STATE-NUM.  */
static const yytype_int8 yystos[] =
{
       0,     3,    25,    26,    11,     0,    27,    18,    28,     4,
      19,    26,    29,    30,    11,    31,    32,    34,    18,    22,
       5,     6,    35,    36,    28,    33,    11,     7,    10,    11,
      18,    37,    38,    39,    35,    11,    12,    13,    20,    42,
      43,    44,    45,    42,    17,    40,    19,    22,    35,    42,
      23,    14,    16,    15,    42,    37,    38,    21,    38,    44,
      43,    45,    19,     8,     9,    41,    38,     9
};

/* YYR1[RULE-NUM] -- Symbol kind of the left-hand side of rule RULE-NUM.  */
static const yytype_int8 yyr1[] =
{
       0,    24,    25,    27,    26,    28,    28,    29,    29,    31,
      30,    33,    32,    34,    32,    35,    36,    36,    37,    37,
      39,    38,    40,    38,    38,    38,    38,    41,    41,    42,
      42,    43,    43,    44,    44,    45,    45,    45,    45
};

/* YYR2[RULE-NUM] -- Number of symbols on the right-hand side of rule RULE-NUM.  */
static const yytype_int8 yyr2[] =
{
       0,     2,     1,     0,     6,     2,     0,     1,     1,     0,
       8,     0,     4,     0,     2,     2,     1,     1,     3,     1,
       0,     2,     0,     4,     3,     5,     2,     3,     1,     3,
       1,     3,     1,     3,     1,     1,     1,     1,     3
};


enum { YYENOMEM = -2 };

#define yyerrok         (yyerrstatus = 0)
#define yyclearin       (yychar = YYEMPTY)

#define YYACCEPT        goto yyacceptlab
#define YYABORT         goto yyabortlab
#define YYERROR         goto yyerrorlab
#define YYNOMEM         goto yyexhaustedlab


#define YYRECOVERING()  (!!yyerrstatus)

#define YYBACKUP(Token, Value)                                    \
  do                                                              \
    if (yychar == YYEMPTY)                                        \
      {                                                           \
        yychar = (Token);                                         \
        yylval = (Value);                                         \
        YYPOPSTACK (yylen);                                       \
        yystate = *yyssp;                                         \
        goto yybackup;                                            \
      }                                                           \
    else                                                          \
      {                                                           \
        yyerror (YY_("syntax error: cannot back up")); \
        YYERROR;                                                  \
      }                                                           \
  while (0)

/* Backward compatibility with an undocumented macro.
   Use YYerror or YYUNDEF. */
#define YYERRCODE YYUNDEF


/* Enable debugging if requested.  */
#if YYDEBUG

# ifndef YYFPRINTF
#  include <stdio.h> /* INFRINGES ON USER NAME SPACE */
#  define YYFPRINTF fprintf
# endif

# define YYDPRINTF(Args)                        \
do {                                            \
  if (yydebug)                                  \
    YYFPRINTF Args;                             \
} while (0)




# define YY_SYMBOL_PRINT(Title, Kind, Value, Location)                    \
do {                                                                      \
  if (yydebug)                                                            \
    {                                                                     \
      YYFPRINTF (stderr, "%s ", Title);                                   \
      yy_symbol_print (stderr,                                            \
                  Kind, Value); \
      YYFPRINTF (stderr, "\n");                                           \
    }                                                                     \
} while (0)


/*-----------------------------------.
| Print this symbol's value on YYO.  |
`-----------------------------------*/

static void
yy_symbol_value_print (FILE *yyo,
                       yysymbol_kind_t yykind, YYSTYPE const * const yyvaluep)
{
  FILE *yyoutput = yyo;
  YY_USE (yyoutput);
  if (!yyvaluep)
    return;
  YY_IGNORE_MAYBE_UNINITIALIZED_BEGIN
  YY_USE (yykind);
  YY_IGNORE_MAYBE_UNINITIALIZED_END
}


/*---------------------------.
| Print this symbol on YYO.  |
`---------------------------*/

static void
yy_symbol_print (FILE *yyo,
                 yysymbol_kind_t yykind, YYSTYPE const * const yyvaluep)
{
  YYFPRINTF (yyo, "%s %s (",
             yykind < YYNTOKENS ? "token" : "nterm", yysymbol_name (yykind));

  yy_symbol_value_print (yyo, yykind, yyvaluep);
  YYFPRINTF (yyo, ")");
}

/*------------------------------------------------------------------.
| yy_stack_print -- Print the state stack from its BOTTOM up to its |
| TOP (included).                                                   |
`------------------------------------------------------------------*/

static void
yy_stack_print (yy_state_t *yybottom, yy_state_t *yytop)
{
  YYFPRINTF (stderr, "Stack now");
  for (; yybottom <= yytop; yybottom++)
    {
      int yybot = *yybottom;
      YYFPRINTF (stderr, " %d", yybot);
    }
  YYFPRINTF (stderr, "\n");
}

# define YY_STACK_PRINT(Bottom, Top)                            \
do {                                                            \
  if (yydebug)                                                  \
    yy_stack_print ((Bottom), (Top));                           \
} while (0)


/*------------------------------------------------.
| Report that the YYRULE is going to be reduced.  |
`------------------------------------------------*/

static void
yy_reduce_print (yy_state_t *yyssp, YYSTYPE *yyvsp,
                 int yyrule)
{
  int yylno = yyrline[yyrule];
  int yynrhs = yyr2[yyrule];
  int yyi;
  YYFPRINTF (stderr, "Reducing stack by rule %d (line %d):\n",
             yyrule - 1, yylno);
  /* The symbols being reduced.  */
  for (yyi = 0; yyi < yynrhs; yyi++)
    {
      YYFPRINTF (stderr, "   $%d = ", yyi + 1);
      yy_symbol_print (stderr,
                       YY_ACCESSING_SYMBOL (+yyssp[yyi + 1 - yynrhs]),
                       &yyvsp[(yyi + 1) - (yynrhs)]);
      YYFPRINTF (stderr, "\n");
    }
}

# define YY_REDUCE_PRINT(Rule)          \
do {                                    \
  if (yydebug)                          \
    yy_reduce_print (yyssp, yyvsp, Rule); \
} while (0)

/* Nonzero means print parse trace.  It is left uninitialized so that
   multiple parsers can coexist.  */
int yydebug;
#else /* !YYDEBUG */
# define YYDPRINTF(Args) ((void) 0)
# define YY_SYMBOL_PRINT(Title, Kind, Value, Location)
# define YY_STACK_PRINT(Bottom, Top)
# define YY_REDUCE_PRINT(Rule)
#endif /* !YYDEBUG */


/* YYINITDEPTH -- initial size of the parser's stacks.  */
#ifndef YYINITDEPTH
# define YYINITDEPTH 200
#endif

/* YYMAXDEPTH -- maximum size the stacks can grow to (effective only
   if the built-in stack extension method is used).

   Do not make this value too large; the results are undefined if
   YYSTACK_ALLOC_MAXIMUM < YYSTACK_BYTES (YYMAXDEPTH)
   evaluated with infinite-precision integer arithmetic.  */

#ifndef YYMAXDEPTH
# define YYMAXDEPTH 10000
#endif






/*-----------------------------------------------.
| Release the memory associated to this symbol.  |
`-----------------------------------------------*/

static void
yydestruct (const char *yymsg,
            yysymbol_kind_t yykind, YYSTYPE *yyvaluep)
{
  YY_USE (yyvaluep);
  if (!yymsg)
    yymsg = "Deleting";
  YY_SYMBOL_PRINT (yymsg, yykind, yyvaluep, yylocationp);

  YY_IGNORE_MAYBE_UNINITIALIZED_BEGIN
  YY_USE (yykind);
  YY_IGNORE_MAYBE_UNINITIALIZED_END
}


/* Lookahead token kind.  */
int yychar;

/* The semantic value of the lookahead symbol.  */
YYSTYPE yylval;
/* Number of syntax errors so far.  */
int yynerrs;




/*----------.
| yyparse.  |
`----------*/

int
yyparse (void)
{
    yy_state_fast_t yystate = 0;
    /* Number of tokens to shift before error messages enabled.  */
    int yyerrstatus = 0;

    /* Refer to the stacks through separate pointers, to allow yyoverflow
       to reallocate them elsewhere.  */

    /* Their size.  */
    YYPTRDIFF_T yystacksize = YYINITDEPTH;

    /* The state stack: array, bottom, top.  */
    yy_state_t yyssa[YYINITDEPTH];
    yy_state_t *yyss = yyssa;
    yy_state_t *yyssp = yyss;

    /* The semantic value stack: array, bottom, top.  */
    YYSTYPE yyvsa[YYINITDEPTH];
    YYSTYPE *yyvs = yyvsa;
    YYSTYPE *yyvsp = yyvs;

  int yyn;
  /* The return value of yyparse.  */
  int yyresult;
  /* Lookahead symbol kind.  */
  yysymbol_kind_t yytoken = YYSYMBOL_YYEMPTY;
  /* The variables used to return semantic value and location from the
     action routines.  */
  YYSTYPE yyval;



#define YYPOPSTACK(N)   (yyvsp -= (N), yyssp -= (N))

  /* The number of symbols on the RHS of the reduced rule.
     Keep to zero when no symbol should be popped.  */
  int yylen = 0;

  YYDPRINTF ((stderr, "Starting parse\n"));

  yychar = YYEMPTY; /* Cause a token to be read.  */

  goto yysetstate;


/*------------------------------------------------------------.
| yynewstate -- push a new state, which is found in yystate.  |
`------------------------------------------------------------*/
yynewstate:
  /* In all cases, when you get here, the value and location stacks
     have just been pushed.  So pushing a state here evens the stacks.  */
  yyssp++;


/*--------------------------------------------------------------------.
| yysetstate -- set current state (the top of the stack) to yystate.  |
`--------------------------------------------------------------------*/
yysetstate:
  YYDPRINTF ((stderr, "Entering state %d\n", yystate));
  YY_ASSERT (0 <= yystate && yystate < YYNSTATES);
  YY_IGNORE_USELESS_CAST_BEGIN
  *yyssp = YY_CAST (yy_state_t, yystate);
  YY_IGNORE_USELESS_CAST_END
  YY_STACK_PRINT (yyss, yyssp);

  if (yyss + yystacksize - 1 <= yyssp)
#if !defined yyoverflow && !defined YYSTACK_RELOCATE
    YYNOMEM;
#else
    {
      /* Get the current used size of the three stacks, in elements.  */
      YYPTRDIFF_T yysize = yyssp - yyss + 1;

# if defined yyoverflow
      {
        /* Give user a chance to reallocate the stack.  Use copies of
           these so that the &'s don't force the real ones into
           memory.  */
        yy_state_t *yyss1 = yyss;
        YYSTYPE *yyvs1 = yyvs;

        /* Each stack pointer address is followed by the size of the
           data in use in that stack, in bytes.  This used to be a
           conditional around just the two extra args, but that might
           be undefined if yyoverflow is a macro.  */
        yyoverflow (YY_("memory exhausted"),
                    &yyss1, yysize * YYSIZEOF (*yyssp),
                    &yyvs1, yysize * YYSIZEOF (*yyvsp),
                    &yystacksize);
        yyss = yyss1;
        yyvs = yyvs1;
      }
# else /* defined YYSTACK_RELOCATE */
      /* Extend the stack our own way.  */
      if (YYMAXDEPTH <= yystacksize)
        YYNOMEM;
      yystacksize *= 2;
      if (YYMAXDEPTH < yystacksize)
        yystacksize = YYMAXDEPTH;

      {
        yy_state_t *yyss1 = yyss;
        union yyalloc *yyptr =
          YY_CAST (union yyalloc *,
                   YYSTACK_ALLOC (YY_CAST (YYSIZE_T, YYSTACK_BYTES (yystacksize))));
        if (! yyptr)
          YYNOMEM;
        YYSTACK_RELOCATE (yyss_alloc, yyss);
        YYSTACK_RELOCATE (yyvs_alloc, yyvs);
#  undef YYSTACK_RELOCATE
        if (yyss1 != yyssa)
          YYSTACK_FREE (yyss1);
      }
# endif

      yyssp = yyss + yysize - 1;
      yyvsp = yyvs + yysize - 1;

      YY_IGNORE_USELESS_CAST_BEGIN
      YYDPRINTF ((stderr, "Stack size increased to %ld\n",
                  YY_CAST (long, yystacksize)));
      YY_IGNORE_USELESS_CAST_END

      if (yyss + yystacksize - 1 <= yyssp)
        YYABORT;
    }
#endif /* !defined yyoverflow && !defined YYSTACK_RELOCATE */


  if (yystate == YYFINAL)
    YYACCEPT;

  goto yybackup;


/*-----------.
| yybackup.  |
`-----------*/
yybackup:
  /* Do appropriate processing given the current state.  Read a
     lookahead token if we need one and don't already have one.  */

  /* First try to decide what to do without reference to lookahead token.  */
  yyn = yypact[yystate];
  if (yypact_value_is_default (yyn))
    goto yydefault;

  /* Not known => get a lookahead token if don't already have one.  */

  /* YYCHAR is either empty, or end-of-input, or a valid lookahead.  */
  if (yychar == YYEMPTY)
    {
      YYDPRINTF ((stderr, "Reading a token\n"));
      yychar = yylex ();
    }

  if (yychar <= YYEOF)
    {
      yychar = YYEOF;
      yytoken = YYSYMBOL_YYEOF;
      YYDPRINTF ((stderr, "Now at end of input.\n"));
    }
  else if (yychar == YYerror)
    {
      /* The scanner already issued an error message, process directly
         to error recovery.  But do not keep the error token as
         lookahead, it is too special and may lead us to an endless
         loop in error recovery. */
      yychar = YYUNDEF;
      yytoken = YYSYMBOL_YYerror;
      goto yyerrlab1;
    }
  else
    {
      yytoken = YYTRANSLATE (yychar);
      YY_SYMBOL_PRINT ("Next token is", yytoken, &yylval, &yylloc);
    }

  /* If the proper action on seeing token YYTOKEN is to reduce or to
     detect an error, take that action.  */
  yyn += yytoken;
  if (yyn < 0 || YYLAST < yyn || yycheck[yyn] != yytoken)
    goto yydefault;
  yyn = yytable[yyn];
  if (yyn <= 0)
    {
      if (yytable_value_is_error (yyn))
        goto yyerrlab;
      yyn = -yyn;
      goto yyreduce;
    }

  /* Count tokens shifted since error; after three, turn off error
     status.  */
  if (yyerrstatus)
    yyerrstatus--;

  /* Shift the lookahead token.  */
  YY_SYMBOL_PRINT ("Shifting", yytoken, &yylval, &yylloc);
  yystate = yyn;
  YY_IGNORE_MAYBE_UNINITIALIZED_BEGIN
  *++yyvsp = yylval;
  YY_IGNORE_MAYBE_UNINITIALIZED_END

  /* Discard the shifted token.  */
  yychar = YYEMPTY;
  goto yynewstate;


/*-----------------------------------------------------------.
| yydefault -- do the default action for the current state.  |
`-----------------------------------------------------------*/
yydefault:
  yyn = yydefact[yystate];
  if (yyn == 0)
    goto yyerrlab;
  goto yyreduce;


/*-----------------------------.
| yyreduce -- do a reduction.  |
`-----------------------------*/
yyreduce:
  /* yyn is the number of a rule to reduce with.  */
  yylen = yyr2[yyn];

  /* If YYLEN is nonzero, implement the default value of the action:
     '$$ = $1'.

     Otherwise, the following line sets YYVAL to garbage.
     This behavior is undocumented and Bison
     users should not rely upon it.  Assigning to YYVAL
     unconditionally makes the parser a bit smaller, and it avoids a
     GCC warning that YYVAL may be used uninitialized.  */
  yyval = yyvsp[1-yylen];


  YY_REDUCE_PRINT (yyn);
  switch (yyn)
    {
  case 2: /* X: S  */
#line 40 "plp4.y"
     {
   int tk = yylex();
   if (tk != 0) yyerror("");
}
#line 1168 "plp4.tab.c"
    break;

  case 3: /* @1: %empty  */
#line 46 "plp4.y"
             { 
      yyval.cod = string(yyvsp[0].lexema);      // guardamos el lexema en el marcador

      if(prefijo.empty() == true){
         prefijo = string(yyvsp[0].lexema);                    // si no hay prefijo previo
      }else{
         prefijo = prefijo + "_" + string(yyvsp[0].lexema);    // si venimos de clase anidada
      }

      tsActual = new TablaSimbolos(tsActual);    // abrimos nuevo ámbito
   }
#line 1184 "plp4.tab.c"
    break;

  case 4: /* S: CLASS ID @1 LBRA M RBRA  */
#line 57 "plp4.y"
   {
      yyval.cod = "// class " + prefijo + "\n" + yyvsp[-1].cod;
      prefijo = yyvsp[-3].cod;                      // restauramos el prefijo anterior, es como usar el atributo heredado
      tsActual = tsActual->getParent();
   }
#line 1194 "plp4.tab.c"
    break;

  case 5: /* M: M SF  */
#line 64 "plp4.y"
        {
   yyval.cod = yyvsp[-1].cod + yyvsp[0].cod;
}
#line 1202 "plp4.tab.c"
    break;

  case 6: /* M: %empty  */
#line 68 "plp4.y"
{
   yyval.cod = "";
}
#line 1210 "plp4.tab.c"
    break;

  case 7: /* SF: S  */
#line 73 "plp4.y"
       {yyval.cod = yyvsp[0].cod;}
#line 1216 "plp4.tab.c"
    break;

  case 8: /* SF: Fun  */
#line 74 "plp4.y"
         {yyval.cod = yyvsp[0].cod;}
#line 1222 "plp4.tab.c"
    break;

  case 9: /* @2: %empty  */
#line 77 "plp4.y"
             {
        yyval.cod = prefijo;              // guardamos el prefijo anterior en el marcador
        prefijo = prefijo + "_" + string(yyvsp[0].lexema);
        tsActual = new TablaSimbolos(tsActual);    // abrimos ámbito ya que parámetros se declaran dentro
    }
#line 1232 "plp4.tab.c"
    break;

  case 10: /* Fun: FUN ID @2 A LBRA M Cod RBRA  */
#line 82 "plp4.y"
    {
        yyval.cod = "void " + prefijo + "(" + yyvsp[-4].cod + ") {\n" + yyvsp[-2].cod + yyvsp[-1].cod + "} // " + prefijo + "\n\n";
        prefijo = yyvsp[-5].cod;                          // restauramos el prefijo anterior
        tsActual = tsActual->getParent();
    }
#line 1242 "plp4.tab.c"
    break;

  case 11: /* $@3: %empty  */
#line 90 "plp4.y"
          {provieneDV = true;}
#line 1248 "plp4.tab.c"
    break;

  case 12: /* A: A PYC $@3 DV  */
#line 90 "plp4.y"
                                 {  // necesario saber que va a llamar de DV desde aquí para la traduccion
   yyval.cod = yyvsp[-3].cod + "," + yyvsp[0].cod;
}
#line 1256 "plp4.tab.c"
    break;

  case 13: /* $@4: %empty  */
#line 93 "plp4.y"
    {provieneDV = true;}
#line 1262 "plp4.tab.c"
    break;

  case 14: /* A: $@4 DV  */
#line 94 "plp4.y"
{
   yyval.cod = yyvsp[0].cod;
}
#line 1270 "plp4.tab.c"
    break;

  case 15: /* DV: Tipo ID  */
#line 99 "plp4.y"
            {

   Simbolo s;
   s.nombre = yyvsp[0].lexema;      // asignamos lexema y tipo
   s.tipo = yyvsp[-1].tipo;

   if(provieneDV == true){    // si viene de DV se forma como argumento
      s.nomtrad = "arg_" + prefijo + "_" + string(yyvsp[0].lexema);
   }else{
      s.nomtrad = prefijo + "_" + string(yyvsp[0].lexema);  // si viene de I se forma como variable normal
   }

   if(tsActual->set(s) == false){      // si ya está declarado en la tabla de símbolos
      errorSemantico(ERRYADECL, yyvsp[0].lexema, yyvsp[0].nlin, yyvsp[0].ncol);
   }
   yyval.cod = (s.tipo == ENTERO ? "int" : "float") + string(" ") + s.nomtrad;
   yyval.tipo = yyvsp[-1].tipo;
}
#line 1293 "plp4.tab.c"
    break;

  case 16: /* Tipo: INT  */
#line 119 "plp4.y"
          {
   yyval.tipo = ENTERO;
   yyval.cod = "int";
}
#line 1302 "plp4.tab.c"
    break;

  case 17: /* Tipo: FLOAT  */
#line 124 "plp4.y"
{
   yyval.tipo = REAL;
   yyval.cod = "float";
}
#line 1311 "plp4.tab.c"
    break;

  case 18: /* Cod: Cod PYC I  */
#line 130 "plp4.y"
               {
   yyval.cod = yyvsp[-2].cod + yyvsp[0].cod;
}
#line 1319 "plp4.tab.c"
    break;

  case 19: /* Cod: I  */
#line 134 "plp4.y"
{
   yyval.cod = yyvsp[0].cod;
}
#line 1327 "plp4.tab.c"
    break;

  case 20: /* $@5: %empty  */
#line 139 "plp4.y"
    {provieneDV = false;}
#line 1333 "plp4.tab.c"
    break;

  case 21: /* I: $@5 DV  */
#line 139 "plp4.y"
                            {     // necesario sabes que proviene de aqui
   yyval.cod = yyvsp[0].cod + ";\n";
}
#line 1341 "plp4.tab.c"
    break;

  case 22: /* @6: %empty  */
#line 143 "plp4.y"
{
   yyval.cod = prefijo;
   prefijo = prefijo + "_";
   tsActual = new TablaSimbolos(tsActual);   // abrimos ámbito nuevo
}
#line 1351 "plp4.tab.c"
    break;

  case 23: /* I: LBRA @6 Cod RBRA  */
#line 148 "plp4.y"
{
   yyval.cod = "{\n" + yyvsp[-1].cod + "}\n";    // construimos la traducción
   prefijo = yyvsp[-2].cod;
   tsActual = tsActual->getParent();   // y cerramos ámbito
}
#line 1361 "plp4.tab.c"
    break;

  case 24: /* I: ID ASIG Expr  */
#line 154 "plp4.y"
{
   Simbolo *s = tsActual->get(yyvsp[-2].lexema);

   // vamos a comprobar que no es nulo, es decir, existe el simbolo
   if(s == NULL){
      errorSemantico(ERRNODECL, yyvsp[-2].lexema, yyvsp[-2].nlin, yyvsp[-2].ncol);
   }

   // comprobamos que es de tipo entero o real (ha juntado class y fun en tablasimbolos.h)
   if(s->tipo == CLASSFUN){
      errorSemantico(ERRNOSIMPLE, yyvsp[-2].lexema, yyvsp[-2].nlin, yyvsp[-2].ncol);
   }

   // y comprobamos que ambos sean de tipos compatibles, es lo mismo que en la p3
   if(s->tipo == ENTERO && yyvsp[0].tipo == REAL){
      errorSemantico(ERRTIPOS, yyvsp[-1].lexema, yyvsp[-1].nlin, yyvsp[-1].ncol);
   }

   string tradExpr = (s->tipo == REAL && yyvsp[0].tipo == ENTERO) ? "itor(" + yyvsp[0].cod + ")" : yyvsp[0].cod;
   yyval.cod = "  " + s->nomtrad + " = " + tradExpr + ";\n";
}
#line 1387 "plp4.tab.c"
    break;

  case 25: /* I: IF Expr DOSP I Ip  */
#line 176 "plp4.y"
{
   // comprobamos que la expresion del if sea entera, igual que en la p3
   if(yyvsp[-3].tipo != ENTERO){
      errorSemantico(ERRNOENTERO, yyvsp[-4].lexema, yyvsp[-4].nlin, yyvsp[-4].ncol);
   }
   yyval.cod = "if (" + yyvsp[-3].cod + ")\n" + yyvsp[-1].cod + yyvsp[0].cod;
}
#line 1399 "plp4.tab.c"
    break;

  case 26: /* I: PRINT Expr  */
#line 184 "plp4.y"
{
   string formato = (yyvsp[0].tipo == ENTERO) ? "%d" : "%f";      // obtenemos el formato de impresion de la traduccion
   yyval.cod = "  printf(\"" + formato + "\"," + yyvsp[0].cod + ");\n";
}
#line 1408 "plp4.tab.c"
    break;

  case 27: /* Ip: ELSE I FI  */
#line 190 "plp4.y"
              {
   yyval.cod = "else\n" + yyvsp[-1].cod;
}
#line 1416 "plp4.tab.c"
    break;

  case 28: /* Ip: FI  */
#line 194 "plp4.y"
{
   yyval.cod = "";
}
#line 1424 "plp4.tab.c"
    break;

  case 29: /* Expr: E OPREL E  */
#line 199 "plp4.y"
                {
   Atributos res = opera(yyvsp[-2].cod, yyvsp[-2].tipo, yyvsp[0].cod, yyvsp[0].tipo, string(yyvsp[-1].lexema));   // obtenemos trad y tipo de la expresion oprel
   yyval.cod = res.cod;
   yyval.tipo = ENTERO;  // el resultado de un relacional siempre es entero
}
#line 1434 "plp4.tab.c"
    break;

  case 30: /* Expr: E  */
#line 205 "plp4.y"
{
   yyval.cod = yyvsp[0].cod;
   yyval.tipo = yyvsp[0].tipo;
}
#line 1443 "plp4.tab.c"
    break;

  case 31: /* E: E OPAS T  */
#line 211 "plp4.y"
            {
   Atributos res = opera(yyvsp[-2].cod, yyvsp[-2].tipo, yyvsp[0].cod, yyvsp[0].tipo, string(yyvsp[-1].lexema));      // obtenemos trad y tipo de la expresion opas
   yyval.cod = res.cod;
   yyval.tipo = res.tipo;
}
#line 1453 "plp4.tab.c"
    break;

  case 32: /* E: T  */
#line 217 "plp4.y"
{
   yyval.cod = yyvsp[0].cod;
   yyval.tipo = yyvsp[0].tipo;
}
#line 1462 "plp4.tab.c"
    break;

  case 33: /* T: T OPMUL F  */
#line 223 "plp4.y"
             {
   Atributos res = opera(yyvsp[-2].cod, yyvsp[-2].tipo, yyvsp[0].cod, yyvsp[0].tipo, string(yyvsp[-1].lexema));      // obtenemos trad y tipo de la expresion opmul
   yyval.cod = res.cod;
   yyval.tipo = res.tipo;
}
#line 1472 "plp4.tab.c"
    break;

  case 34: /* T: F  */
#line 229 "plp4.y"
{
   yyval.cod = yyvsp[0].cod;
   yyval.tipo = yyvsp[0].tipo;
}
#line 1481 "plp4.tab.c"
    break;

  case 35: /* F: NUMENTERO  */
#line 235 "plp4.y"
             {
   yyval.tipo = ENTERO;
   yyval.cod = string(yyvsp[0].lexema);
}
#line 1490 "plp4.tab.c"
    break;

  case 36: /* F: NUMREAL  */
#line 240 "plp4.y"
{
   yyval.tipo = REAL;
   yyval.cod = string(yyvsp[0].lexema);
}
#line 1499 "plp4.tab.c"
    break;

  case 37: /* F: ID  */
#line 245 "plp4.y"
{
   Simbolo *s = tsActual->get(yyvsp[0].lexema);

   // comprobamos que el id exista, es decir, esté declarado, igual que p3
   if(s == NULL){
      errorSemantico(ERRNODECL, yyvsp[0].lexema, yyvsp[0].nlin, yyvsp[0].ncol);
   }

   // comprobamos que no sea classfun, es decir, que sea entero o real
   if(s->tipo == CLASSFUN){
      errorSemantico(ERRNOSIMPLE, yyvsp[0].lexema, yyvsp[0].nlin, yyvsp[0].ncol);
   }

   yyval.tipo = s->tipo;
   yyval.cod = s->nomtrad;
}
#line 1520 "plp4.tab.c"
    break;

  case 38: /* F: PARI Expr PARD  */
#line 262 "plp4.y"
{
   yyval.tipo = yyvsp[-1].tipo;
   yyval.cod = "(" + yyvsp[-1].cod + ")";
}
#line 1529 "plp4.tab.c"
    break;


#line 1533 "plp4.tab.c"

      default: break;
    }
  /* User semantic actions sometimes alter yychar, and that requires
     that yytoken be updated with the new translation.  We take the
     approach of translating immediately before every use of yytoken.
     One alternative is translating here after every semantic action,
     but that translation would be missed if the semantic action invokes
     YYABORT, YYACCEPT, or YYERROR immediately after altering yychar or
     if it invokes YYBACKUP.  In the case of YYABORT or YYACCEPT, an
     incorrect destructor might then be invoked immediately.  In the
     case of YYERROR or YYBACKUP, subsequent parser actions might lead
     to an incorrect destructor call or verbose syntax error message
     before the lookahead is translated.  */
  YY_SYMBOL_PRINT ("-> $$ =", YY_CAST (yysymbol_kind_t, yyr1[yyn]), &yyval, &yyloc);

  YYPOPSTACK (yylen);
  yylen = 0;

  *++yyvsp = yyval;

  /* Now 'shift' the result of the reduction.  Determine what state
     that goes to, based on the state we popped back to and the rule
     number reduced by.  */
  {
    const int yylhs = yyr1[yyn] - YYNTOKENS;
    const int yyi = yypgoto[yylhs] + *yyssp;
    yystate = (0 <= yyi && yyi <= YYLAST && yycheck[yyi] == *yyssp
               ? yytable[yyi]
               : yydefgoto[yylhs]);
  }

  goto yynewstate;


/*--------------------------------------.
| yyerrlab -- here on detecting error.  |
`--------------------------------------*/
yyerrlab:
  /* Make sure we have latest lookahead translation.  See comments at
     user semantic actions for why this is necessary.  */
  yytoken = yychar == YYEMPTY ? YYSYMBOL_YYEMPTY : YYTRANSLATE (yychar);
  /* If not already recovering from an error, report this error.  */
  if (!yyerrstatus)
    {
      ++yynerrs;
      yyerror (YY_("syntax error"));
    }

  if (yyerrstatus == 3)
    {
      /* If just tried and failed to reuse lookahead token after an
         error, discard it.  */

      if (yychar <= YYEOF)
        {
          /* Return failure if at end of input.  */
          if (yychar == YYEOF)
            YYABORT;
        }
      else
        {
          yydestruct ("Error: discarding",
                      yytoken, &yylval);
          yychar = YYEMPTY;
        }
    }

  /* Else will try to reuse lookahead token after shifting the error
     token.  */
  goto yyerrlab1;


/*---------------------------------------------------.
| yyerrorlab -- error raised explicitly by YYERROR.  |
`---------------------------------------------------*/
yyerrorlab:
  /* Pacify compilers when the user code never invokes YYERROR and the
     label yyerrorlab therefore never appears in user code.  */
  if (0)
    YYERROR;
  ++yynerrs;

  /* Do not reclaim the symbols of the rule whose action triggered
     this YYERROR.  */
  YYPOPSTACK (yylen);
  yylen = 0;
  YY_STACK_PRINT (yyss, yyssp);
  yystate = *yyssp;
  goto yyerrlab1;


/*-------------------------------------------------------------.
| yyerrlab1 -- common code for both syntax error and YYERROR.  |
`-------------------------------------------------------------*/
yyerrlab1:
  yyerrstatus = 3;      /* Each real token shifted decrements this.  */

  /* Pop stack until we find a state that shifts the error token.  */
  for (;;)
    {
      yyn = yypact[yystate];
      if (!yypact_value_is_default (yyn))
        {
          yyn += YYSYMBOL_YYerror;
          if (0 <= yyn && yyn <= YYLAST && yycheck[yyn] == YYSYMBOL_YYerror)
            {
              yyn = yytable[yyn];
              if (0 < yyn)
                break;
            }
        }

      /* Pop the current state because it cannot handle the error token.  */
      if (yyssp == yyss)
        YYABORT;


      yydestruct ("Error: popping",
                  YY_ACCESSING_SYMBOL (yystate), yyvsp);
      YYPOPSTACK (1);
      yystate = *yyssp;
      YY_STACK_PRINT (yyss, yyssp);
    }

  YY_IGNORE_MAYBE_UNINITIALIZED_BEGIN
  *++yyvsp = yylval;
  YY_IGNORE_MAYBE_UNINITIALIZED_END


  /* Shift the error token.  */
  YY_SYMBOL_PRINT ("Shifting", YY_ACCESSING_SYMBOL (yyn), yyvsp, yylsp);

  yystate = yyn;
  goto yynewstate;


/*-------------------------------------.
| yyacceptlab -- YYACCEPT comes here.  |
`-------------------------------------*/
yyacceptlab:
  yyresult = 0;
  goto yyreturnlab;


/*-----------------------------------.
| yyabortlab -- YYABORT comes here.  |
`-----------------------------------*/
yyabortlab:
  yyresult = 1;
  goto yyreturnlab;


/*-----------------------------------------------------------.
| yyexhaustedlab -- YYNOMEM (memory exhaustion) comes here.  |
`-----------------------------------------------------------*/
yyexhaustedlab:
  yyerror (YY_("memory exhausted"));
  yyresult = 2;
  goto yyreturnlab;


/*----------------------------------------------------------.
| yyreturnlab -- parsing is finished, clean up and return.  |
`----------------------------------------------------------*/
yyreturnlab:
  if (yychar != YYEMPTY)
    {
      /* Make sure we have latest lookahead translation.  See comments at
         user semantic actions for why this is necessary.  */
      yytoken = YYTRANSLATE (yychar);
      yydestruct ("Cleanup: discarding lookahead",
                  yytoken, &yylval);
    }
  /* Do not reclaim the symbols of the rule whose action triggered
     this YYABORT or YYACCEPT.  */
  YYPOPSTACK (yylen);
  YY_STACK_PRINT (yyss, yyssp);
  while (yyssp != yyss)
    {
      yydestruct ("Cleanup: popping",
                  YY_ACCESSING_SYMBOL (+*yyssp), yyvsp);
      YYPOPSTACK (1);
    }
#ifndef yyoverflow
  if (yyss != yyssa)
    YYSTACK_FREE (yyss);
#endif

  return yyresult;
}

#line 267 "plp4.y"
 

/* literalmente la misma funcion que en la p3 */
Atributos opera(string trad1, int tipo1, string trad2, int tipo2, string operador)
{
   string traduccion1, traduccion2, sufijoOperador, traduccionResultado;
   Atributos resultado;

   if(tipo1 == REAL && tipo2 == REAL){         // ambos reales
      sufijoOperador = "r";
      traduccion1 = trad1;
      traduccion2 = trad2;
   }else if(tipo1 == REAL && tipo2 == ENTERO){ // uno real, uno entero
      sufijoOperador = "r";
      traduccion1 = trad1;
      traduccion2 = "itor(" + trad2 + ")";
   }else if(tipo1 == ENTERO && tipo2 == REAL){ // uno entero, uno real
      sufijoOperador = "r";
      traduccion1 = "itor(" + trad1 + ")";
      traduccion2 = trad2;
   }else{                                      // ambos enteros
      sufijoOperador = "i";
      traduccion1 = trad1;
      traduccion2 = trad2;
   }

   // en el caso de un entero y un real sería
   // itor(arg_Main_main_a) +r arg_Main_main_b
   // y el tipo de Atributos sería REAL
   traduccionResultado = traduccion1 + " " + operador + sufijoOperador + " " + traduccion2;
   int tipo = (tipo1 == ENTERO && tipo2 == ENTERO) ? ENTERO : REAL;

   resultado.cod = traduccionResultado;
   resultado.tipo = tipo;
   return resultado;
}

void errorSemantico(int nerror, char *lexema, int fila, int columna)
{
    fprintf(stderr,"Error semantico (%d,%d): en '%s', ",fila,columna,lexema);
    switch (nerror) {
      case ERRYADECL: fprintf(stderr,"ya existe en este ambito\n"); break;
      case ERRNODECL: fprintf(stderr,"no ha sido declarado\n"); break;
      case ERRTIPOS: fprintf(stderr,"tipos incompatibles entero/real\n"); break;
      case ERRNOSIMPLE: fprintf(stderr,"debe ser de tipo entero o real\n"); break;
      case ERRNOENTERO: fprintf(stderr,"la expresion debe ser de tipo entero\n"); break;
    }
    exit(-1);
}

void msgError(int nerror, int nlin, int ncol, const char *s)
{
    switch (nerror) {
        case ERRLEXICO: fprintf(stderr,"Error lexico (%d,%d): caracter '%s' incorrecto\n",nlin,ncol,s); break;
        case ERRSINT: fprintf(stderr,"Error sintactico (%d,%d): en '%s'\n",nlin,ncol,s); break;
        case ERREOF: fprintf(stderr,"Error sintactico: fin de fichero inesperado\n"); break;
        case ERRLEXEOF: fprintf(stderr,"Error lexico: fin de fichero inesperado\n"); break;
    }
    exit(1);
}

int yyerror(char *s)
{
    if (findefichero)
        msgError(ERREOF,-1,-1,"");
    else
        msgError(ERRSINT,nlin,ncol-strlen(yytext),yytext);
    return 0;
}

int main(int argc, char *argv[])
{
    FILE *fent;
    if (argc == 2)
    {
        fent = fopen(argv[1],"rt");
        if (fent) { yyin = fent; yyparse(); fclose(fent); }
        else fprintf(stderr,"No puedo abrir el fichero\n");
    }
    else fprintf(stderr,"Uso: plp4 <nombre de fichero>\n");
}
