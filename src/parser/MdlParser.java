/* MdlParser.java */
/* Generated By:JavaCC: Do not edit this line. MdlParser.java */
package parser;

import java.util.*;
import parseTables.*;

public class MdlParser implements MdlParserConstants {
  private ArrayList<opCode> opcodeArrayList = new ArrayList<opCode>();
  private SymTab symtab = new SymTab();
  public ArrayList<opCode> getOps()
  {
        return opcodeArrayList;
  }
  public SymTab getSymTab()
  {
        return symtab;
  }

  public static void main(String args[]) throws ParseException {
  MdlParser parser = new MdlParser(System.in);
    parser.start();
  }

  final public void start() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case BOX:
    case SPHERE:
    case TORUS:
    case LINE:
    case MESH:
    case ROTATE:
    case MOVE:
    case SCALE:
    case PUSH:
    case POP:
    case BASENAME:
    case SET:
    case SETKNOBS:
    case GENRAYS:
    case SHADING:
    case FOCAL:
    case DISPLAY:
    case LIGHT:
    case AMBIENT:
    case CONSTANTS:
    case SAVECS:
    case CAMERA:
    case SAVEKNOBS:
    case TWEEN:
    case FRAMES:
    case VARY:
    case SAVE:{
      label_1:
      while (true) {
        statement();
        jj_consume_token(38);
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case BOX:
        case SPHERE:
        case TORUS:
        case LINE:
        case MESH:
        case ROTATE:
        case MOVE:
        case SCALE:
        case PUSH:
        case POP:
        case BASENAME:
        case SET:
        case SETKNOBS:
        case GENRAYS:
        case SHADING:
        case FOCAL:
        case DISPLAY:
        case LIGHT:
        case AMBIENT:
        case CONSTANTS:
        case SAVECS:
        case CAMERA:
        case SAVEKNOBS:
        case TWEEN:
        case FRAMES:
        case VARY:
        case SAVE:{
          ;
          break;
          }
        default:
          jj_la1[0] = jj_gen;
          break label_1;
        }
      }
      break;
      }
    case 0:{
      jj_consume_token(0);
      break;
      }
    default:
      jj_la1[1] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void statement() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case BOX:{
      box();
      break;
      }
    case SPHERE:{
      sphere();
      break;
      }
    case TORUS:{
      torus();
      break;
      }
    case LINE:{
      line();
      break;
      }
    case MESH:{
      mesh();
      break;
      }
    case MOVE:{
      move();
      break;
      }
    case SCALE:{
      scale();
      break;
      }
    case ROTATE:{
      rotate();
      break;
      }
    case PUSH:{
      push();
      break;
      }
    case POP:{
      pop();
      break;
      }
    case GENRAYS:{
      generate_rayfiles();
      break;
      }
    case SHADING:{
      shading();
      break;
      }
    case FOCAL:{
      focal();
      break;
      }
    case DISPLAY:{
      display();
      break;
      }
    case LIGHT:{
      light();
      break;
      }
    case AMBIENT:{
      ambient();
      break;
      }
    case CONSTANTS:{
      constants();
      break;
      }
    case BASENAME:{
      basename();
      break;
      }
    case SET:{
      set();
      break;
      }
    case SETKNOBS:{
      setknobs();
      break;
      }
    case SAVECS:{
      savecs();
      break;
      }
    case CAMERA:{
      camera();
      break;
      }
    case SAVEKNOBS:{
      saveknobs();
      break;
      }
    case TWEEN:{
      tween();
      break;
      }
    case FRAMES:{
      frames();
      break;
      }
    case VARY:{
      vary();
      break;
      }
    case SAVE:{
      save();
      break;
      }
    default:
      jj_la1[2] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void tween() throws ParseException {int start,stop;
        Token k1Tok,k2Tok;
        Token startTok,stopTok;
    jj_consume_token(TWEEN);
    startTok = jj_consume_token(DOUBLE);
    stopTok = jj_consume_token(DOUBLE);
    k1Tok = jj_consume_token(ID);
    k2Tok = jj_consume_token(ID);
start=Integer.parseInt(startTok.toString());
        stop=Integer.parseInt(stopTok.toString());
        opTween t = new opTween(start,stop,k1Tok.toString(),k2Tok.toString());
        opcodeArrayList.add(t);
  }

  final public void vary() throws ParseException {Token kTok,sfTok,efTok,svTok,evTok;
    jj_consume_token(VARY);
    kTok = jj_consume_token(ID);
    sfTok = jj_consume_token(DOUBLE);
    efTok = jj_consume_token(DOUBLE);
    svTok = jj_consume_token(DOUBLE);
    evTok = jj_consume_token(DOUBLE);
opVary v = new opVary(kTok.toString(),
                                Integer.parseInt(sfTok.toString()),
                                Integer.parseInt(efTok.toString()),
                                Double.parseDouble(svTok.toString()),
                                Double.parseDouble(evTok.toString()));
        opcodeArrayList.add(v);
  }

  final public void frames() throws ParseException {Token t;
    jj_consume_token(FRAMES);
    t = jj_consume_token(DOUBLE);
opFrames f = new opFrames(Integer.parseInt(t.toString()));
        opcodeArrayList.add(f);
  }

  final public void camera() throws ParseException {double[] eye, aim;
    jj_consume_token(CAMERA);
    eye = triple();
    aim = triple();
opCamera c = new opCamera(eye,aim);
        opcodeArrayList.add(c);
  }

  final public void light() throws ParseException {double[] rgb, location;
        Token tok;
    jj_consume_token(LIGHT);
    tok = jj_consume_token(ID);
    rgb = triple();
    location = triple();
symtab.add(tok.toString(),tok.toString());
        opLight l = new opLight(tok.toString(),rgb,location);
        opcodeArrayList.add(l);
  }

  final public void ambient() throws ParseException {double[] rgb;
    jj_consume_token(AMBIENT);
    rgb = triple();
opAmbient a = new opAmbient(rgb);
        opcodeArrayList.add(a);
  }

  final public void constants() throws ParseException {double[] ambient,diffuse,specular,rgb=null;
        Token sTok;
    jj_consume_token(CONSTANTS);
    sTok = jj_consume_token(ID);
    ambient = triple();
    diffuse = triple();
    specular = triple();
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case DOUBLE:{
      rgb = triple();
      break;
      }
    default:
      jj_la1[3] = jj_gen;
      ;
    }
opConstants c = new opConstants(sTok.toString(),ambient,diffuse,specular,rgb);
                opcodeArrayList.add(c);
  }

  final public void set() throws ParseException {Token kTok=null;
        Token vTok=null;
        String knob;
        double value;
    jj_consume_token(SET);
    kTok = jj_consume_token(ID);
    vTok = jj_consume_token(DOUBLE);
knob=kTok.toString();
        value=Double.parseDouble(vTok.toString());

        symtab.add(knob,new Double(value));
        opSet s = new opSet(knob,value);
        opcodeArrayList.add(s);
  }

  final public void setknobs() throws ParseException {Token vTok=null;
        double value;
    jj_consume_token(SETKNOBS);
    vTok = jj_consume_token(DOUBLE);
value=Double.parseDouble(vTok.toString());
        opSetknobs s = new opSetknobs(value);
        opcodeArrayList.add(s);
  }

  final public void savecs() throws ParseException {Token nTok = null;
    jj_consume_token(SAVECS);
    nTok = jj_consume_token(ID);
opSavecs b = new opSavecs(nTok.toString());
        opcodeArrayList.add(b);
  }

  final public void saveknobs() throws ParseException {Token nTok = null;
    jj_consume_token(SAVEKNOBS);
    nTok = jj_consume_token(ID);
opSaveknobs b = new opSaveknobs(nTok.toString());
        opcodeArrayList.add(b);
  }

  final public void save() throws ParseException {Token nTok = null;
    jj_consume_token(SAVE);
    nTok = jj_consume_token(ID);
opSave b = new opSave(nTok.toString());
        opcodeArrayList.add(b);
  }

  final public void basename() throws ParseException {Token nTok = null;
    jj_consume_token(BASENAME);
    nTok = jj_consume_token(ID);
opBasename b = new opBasename(nTok.toString());
        opcodeArrayList.add(b);
  }

  final public void shading() throws ParseException {Token nTok = null;
    jj_consume_token(SHADING);
    nTok = jj_consume_token(ID);
opShading b = new opShading(nTok.toString());
        opcodeArrayList.add(b);
  }

  final public void focal() throws ParseException {Token nTok = null;
    jj_consume_token(FOCAL);
    nTok = jj_consume_token(DOUBLE);
opFocal b = new opFocal(Double.parseDouble(nTok.toString()));
        opcodeArrayList.add(b);
  }

  final public void push() throws ParseException {
    jj_consume_token(PUSH);
opPush p = new opPush();
        opcodeArrayList.add(p);
  }

  final public void display() throws ParseException {
    jj_consume_token(DISPLAY);
opDisplay p = new opDisplay();
        opcodeArrayList.add(p);
  }

  final public void generate_rayfiles() throws ParseException {
    jj_consume_token(GENRAYS);
opGeneraterayfiles p = new opGeneraterayfiles();
        opcodeArrayList.add(p);
  }

  final public void pop() throws ParseException {
    jj_consume_token(POP);
opPop p = new opPop();
        opcodeArrayList.add(p);
  }

  final public void rotate() throws ParseException {char axis;
        double deg;
        Token dTok=null;
        String knob=null;
        Token kTok=null;
    jj_consume_token(ROTATE);
    axis = axis();
    dTok = jj_consume_token(DOUBLE);
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case ID:{
      kTok = jj_consume_token(ID);
      break;
      }
    default:
      jj_la1[4] = jj_gen;
      ;
    }
if (kTok != null)
        {
                knob=kTok.toString();
                symtab.add(knob,new Double(0));
        }
        deg = Double.parseDouble(dTok.toString());
        opRotate r = new opRotate(axis,deg,knob);
        opcodeArrayList.add(r);
  }

  final public char axis() throws ParseException {Token t;
    t = jj_consume_token(ID);
{if ("" != null) return t.toString().charAt(0);}
    throw new Error("Missing return statement in function");
  }

  final public void move() throws ParseException {Token kTok=null;
        String knob=null;
        double[] tr;
    jj_consume_token(MOVE);
    tr = triple();
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case ID:{
      kTok = jj_consume_token(ID);
      break;
      }
    default:
      jj_la1[5] = jj_gen;
      ;
    }
if (kTok != null)
        {
                knob=kTok.toString();
                symtab.add(knob,new Double(0));
        }
        opMove m = new opMove(tr,knob);
        opcodeArrayList.add(m);
  }

  final public void scale() throws ParseException {Token kTok=null;
        String knob=null;
        double[] sc;
    jj_consume_token(SCALE);
    sc = triple();
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case ID:{
      kTok = jj_consume_token(ID);
      break;
      }
    default:
      jj_la1[6] = jj_gen;
      ;
    }
if (kTok != null)
        {
                knob=kTok.toString();
                symtab.add(knob,new Double(0));
        }
        opScale m = new opScale(sc,knob);
        opcodeArrayList.add(m);
  }

  final public void sphere() throws ParseException {Token t1,t2,t3;
        double[] center;
        Token RTok;
        Token consTok=null;
        Token csTok=null;
        String cons=null, cs=null;
    jj_consume_token(SPHERE);
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case ID:{
      consTok = jj_consume_token(ID);
      break;
      }
    default:
      jj_la1[7] = jj_gen;
      ;
    }
    center = triple();
    RTok = jj_consume_token(DOUBLE);
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case ID:{
      csTok = jj_consume_token(ID);
      break;
      }
    default:
      jj_la1[8] = jj_gen;
      ;
    }
if (consTok != null)
          cons = consTok.toString();
        if (csTok != null)
          cs = csTok.toString();
        opSphere s = new opSphere(center,Double.parseDouble(RTok.toString()),cs,cons);
          opcodeArrayList.add(s);
  }

  final public void torus() throws ParseException {Token t1,t2,t3;
        double[] center;
        double r,R;
        Token RTok,rTok;
        Token consTok=null;
        Token csTok=null;
        String cs=null,cons=null;
    jj_consume_token(TORUS);
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case ID:{
      consTok = jj_consume_token(ID);
      break;
      }
    default:
      jj_la1[9] = jj_gen;
      ;
    }
    center = triple();
    RTok = jj_consume_token(DOUBLE);
    rTok = jj_consume_token(DOUBLE);
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case ID:{
      csTok = jj_consume_token(ID);
      break;
      }
    default:
      jj_la1[10] = jj_gen;
      ;
    }
if (consTok != null) cons = consTok.toString();
        if (csTok != null) cs=csTok.toString();
        r=Double.parseDouble(rTok.toString());
        R=Double.parseDouble(RTok.toString());
        opTorus t = new opTorus(center,R,r,cs,cons);
        opcodeArrayList.add(t);
  }

  final public void box() throws ParseException {Token t1,t2,t3;
        double[] p1,p2;
        Token consTok=null;
        Token csTok=null;
        Token cs2Tok=null;
        String cs=null,cs2=null,cons=null;
    jj_consume_token(BOX);
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case ID:{
      consTok = jj_consume_token(ID);
      break;
      }
    default:
      jj_la1[11] = jj_gen;
      ;
    }
    p1 = triple();
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case ID:{
      csTok = jj_consume_token(ID);
      break;
      }
    default:
      jj_la1[12] = jj_gen;
      ;
    }
    p2 = triple();
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case ID:{
      cs2Tok = jj_consume_token(ID);
      break;
      }
    default:
      jj_la1[13] = jj_gen;
      ;
    }
if (consTok != null) cons = consTok.toString();
        if (csTok != null) cs=csTok.toString();
        if (cs2Tok != null) cs2=cs2Tok.toString();
        opBox b = new opBox(p1,cs,p2,cs2,cons);
        opcodeArrayList.add(b);
  }

  final public void line() throws ParseException {Token t1,t2,t3;
        double[] p1,p2;
        Token consTok=null;
        Token csTok=null;
        Token cs2Tok=null;
        String cs=null,cs2=null,cons=null;
    jj_consume_token(LINE);
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case ID:{
      consTok = jj_consume_token(ID);
      break;
      }
    default:
      jj_la1[14] = jj_gen;
      ;
    }
    p1 = triple();
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case ID:{
      csTok = jj_consume_token(ID);
      break;
      }
    default:
      jj_la1[15] = jj_gen;
      ;
    }
    p2 = triple();
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case ID:{
      cs2Tok = jj_consume_token(ID);
      break;
      }
    default:
      jj_la1[16] = jj_gen;
      ;
    }
if (consTok != null) cons = consTok.toString();
        if (csTok != null) cs=csTok.toString();
        if (cs2Tok != null) cs2=cs2Tok.toString();
        opLine l = new opLine(p1,cs,p2,cs2,cons);
        opcodeArrayList.add(l);
  }

  final public void mesh() throws ParseException {Token consTok=null;
        Token csTok=null;
        Token fnTok=null;
        String cs=null,fn=null,cons=null;
    jj_consume_token(MESH);
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case ID:{
      consTok = jj_consume_token(ID);
      break;
      }
    default:
      jj_la1[17] = jj_gen;
      ;
    }
    jj_consume_token(FN);
    fnTok = jj_consume_token(ID);
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case ID:{
      csTok = jj_consume_token(ID);
      break;
      }
    default:
      jj_la1[18] = jj_gen;
      ;
    }
if (consTok != null) cons = consTok.toString();
        if (csTok != null) cs=csTok.toString();
        fn=fnTok.toString();
        opMesh m = new opMesh(fn,cs,cons);
        opcodeArrayList.add(m);
  }

  final public double[] triple() throws ParseException {Token t1,t2,t3;
        double[] d;
    t1 = jj_consume_token(DOUBLE);
    t2 = jj_consume_token(DOUBLE);
    t3 = jj_consume_token(DOUBLE);
d = new double[3];
                d[0]=Double.parseDouble(t1.image);
                d[1]=Double.parseDouble(t2.image);
                d[2]=Double.parseDouble(t3.image);
                {if ("" != null) return d;}
    throw new Error("Missing return statement in function");
  }

  /** Generated Token Manager. */
  public MdlParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[19];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0xffffffc0,0xffffffc1,0xffffffc0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x1,0x1,0x1,0x2,0x10,0x10,0x10,0x10,0x10,0x10,0x10,0x10,0x10,0x10,0x10,0x10,0x10,0x10,0x10,};
   }

  /** Constructor with InputStream. */
  public MdlParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public MdlParser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new MdlParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public MdlParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new MdlParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public MdlParser(MdlParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(MdlParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk_f() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[39];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 19; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 39; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

}
