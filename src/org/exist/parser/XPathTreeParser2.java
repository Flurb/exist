// $ANTLR 2.7.2: "XPathParser2.g" -> "XPathTreeParser2.java"$

	package org.exist.parser;

	import antlr.debug.misc.*;
	import java.io.StringReader;
	import java.io.BufferedReader;
	import java.io.InputStreamReader;
	import java.util.ArrayList;
	import java.util.List;
	import java.util.Iterator;
	import org.exist.storage.BrokerPool;
	import org.exist.storage.DBBroker;
	import org.exist.storage.analysis.Tokenizer;
	import org.exist.EXistException;
	import org.exist.dom.DocumentSet;
	import org.exist.dom.DocumentImpl;
	import org.exist.dom.QName;
	import org.exist.security.PermissionDeniedException;
	import org.exist.security.User;
	import org.exist.xpath.*;
	import org.exist.xpath.value.*;
	import org.exist.xpath.functions.*;

import antlr.TreeParser;
import antlr.Token;
import antlr.collections.AST;
import antlr.RecognitionException;
import antlr.ANTLRException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.collections.impl.BitSet;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;


public class XPathTreeParser2 extends antlr.TreeParser       implements XPathParser2TokenTypes
 {

	private StaticContext context;
	protected ArrayList exceptions= new ArrayList(2);
	protected boolean foundError= false;

	public XPathTreeParser2(StaticContext context) {
		this();
		this.context= context;
	}

	public boolean foundErrors() {
		return foundError;
	}

	public String getErrorMessage() {
		StringBuffer buf= new StringBuffer();
		for (Iterator i= exceptions.iterator(); i.hasNext();) {
			buf.append(((Exception) i.next()).toString());
			buf.append('\n');
		}
		return buf.toString();
	}

	protected void handleException(Exception e) {
		foundError= true;
		exceptions.add(e);
	}

	private static class ForLetClause {
		String varName;
		Expression inputSequence;
		Expression action;
		boolean isForClause= true;
	}
public XPathTreeParser2() {
	tokenNames = _tokenNames;
}

	public final void xpointer(AST _t,
		PathExpr path
	) throws RecognitionException {
		
		AST xpointer_AST_in = (AST)_t;
		AST nc = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case XPOINTER:
			{
				AST __t173 = _t;
				AST tmp1_AST_in = (AST)_t;
				match(_t,XPOINTER);
				_t = _t.getFirstChild();
				expr(_t,path);
				_t = _retTree;
				_t = __t173;
				_t = _t.getNextSibling();
				break;
			}
			case XPOINTER_ID:
			{
				AST __t174 = _t;
				AST tmp2_AST_in = (AST)_t;
				match(_t,XPOINTER_ID);
				_t = _t.getFirstChild();
				nc = (AST)_t;
				match(_t,NCNAME);
				_t = _t.getNextSibling();
				_t = __t174;
				_t = _t.getNextSibling();
				
						Function fun= new FunId(context);
						List params= new ArrayList(1);
						params.add(new LiteralValue(context, new StringValue(nc.getText())));
						fun.setArguments(params);
						path.addPath(fun);
					
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException e) {
			handleException(e);
		}
		catch (EXistException e) {
			handleException(e);
		}
		catch (PermissionDeniedException e) {
			handleException(e);
		}
		catch (XPathException e) {
			handleException(e);
		}
		_retTree = _t;
	}
	
	public final void expr(AST _t,
		PathExpr path
	) throws RecognitionException, PermissionDeniedException,EXistException,XPathException {
		
		AST expr_AST_in = (AST)_t;
		AST varName = null;
		AST letVarName = null;
		Expression step= null;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case COMMA:
		{
			AST __t195 = _t;
			AST tmp3_AST_in = (AST)_t;
			match(_t,COMMA);
			_t = _t.getFirstChild();
			
						PathExpr left= new PathExpr(context);
						PathExpr right= new PathExpr(context);
					
			expr(_t,left);
			_t = _retTree;
			expr(_t,right);
			_t = _retTree;
			
						SequenceConstructor sc = new SequenceConstructor(context);
						sc.addExpression(left);
						sc.addExpression(right);
						path.add(sc);
					
			_t = __t195;
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_if:
		{
			AST __t196 = _t;
			AST tmp4_AST_in = (AST)_t;
			match(_t,LITERAL_if);
			_t = _t.getFirstChild();
			
						PathExpr testExpr = new PathExpr(context);
						PathExpr thenExpr = new PathExpr(context);
						PathExpr elseExpr = new PathExpr(context);
					
			expr(_t,testExpr);
			_t = _retTree;
			expr(_t,thenExpr);
			_t = _retTree;
			expr(_t,elseExpr);
			_t = _retTree;
			
						ConditionalExpression cond = 
							new ConditionalExpression(context, testExpr, thenExpr, elseExpr);
						path.add(cond);
					
			_t = __t196;
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_return:
		{
			AST __t197 = _t;
			AST tmp5_AST_in = (AST)_t;
			match(_t,LITERAL_return);
			_t = _t.getFirstChild();
			
						List clauses= new ArrayList();
						Expression action= new PathExpr(context);
						PathExpr whereExpr= null;
					
			{
			int _cnt207=0;
			_loop207:
			do {
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case LITERAL_for:
				{
					AST __t199 = _t;
					AST tmp6_AST_in = (AST)_t;
					match(_t,LITERAL_for);
					_t = _t.getFirstChild();
					{
					int _cnt202=0;
					_loop202:
					do {
						if (_t==null) _t=ASTNULL;
						if ((_t.getType()==VARIABLE_BINDING)) {
							AST __t201 = _t;
							varName = _t==ASTNULL ? null :(AST)_t;
							match(_t,VARIABLE_BINDING);
							_t = _t.getFirstChild();
							
														ForLetClause clause= new ForLetClause();
														PathExpr inputSequence= new PathExpr(context);
													
							expr(_t,inputSequence);
							_t = _retTree;
							
														clause.varName= varName.getText();
														clause.inputSequence= inputSequence;
														clauses.add(clause);
													
							_t = __t201;
							_t = _t.getNextSibling();
						}
						else {
							if ( _cnt202>=1 ) { break _loop202; } else {throw new NoViableAltException(_t);}
						}
						
						_cnt202++;
					} while (true);
					}
					_t = __t199;
					_t = _t.getNextSibling();
					break;
				}
				case LITERAL_let:
				{
					AST __t203 = _t;
					AST tmp7_AST_in = (AST)_t;
					match(_t,LITERAL_let);
					_t = _t.getFirstChild();
					{
					int _cnt206=0;
					_loop206:
					do {
						if (_t==null) _t=ASTNULL;
						if ((_t.getType()==VARIABLE_BINDING)) {
							AST __t205 = _t;
							letVarName = _t==ASTNULL ? null :(AST)_t;
							match(_t,VARIABLE_BINDING);
							_t = _t.getFirstChild();
							
														ForLetClause clause= new ForLetClause();
														clause.isForClause= false;
														PathExpr inputSequence= new PathExpr(context);
													
							expr(_t,inputSequence);
							_t = _retTree;
							
														clause.varName= letVarName.getText();
														clause.inputSequence= inputSequence;
														clauses.add(clause);
													
							_t = __t205;
							_t = _t.getNextSibling();
						}
						else {
							if ( _cnt206>=1 ) { break _loop206; } else {throw new NoViableAltException(_t);}
						}
						
						_cnt206++;
					} while (true);
					}
					_t = __t203;
					_t = _t.getNextSibling();
					break;
				}
				default:
				{
					if ( _cnt207>=1 ) { break _loop207; } else {throw new NoViableAltException(_t);}
				}
				}
				_cnt207++;
			} while (true);
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LITERAL_where:
			{
				AST tmp8_AST_in = (AST)_t;
				match(_t,LITERAL_where);
				_t = _t.getNextSibling();
				whereExpr= new PathExpr(context);
				expr(_t,whereExpr);
				_t = _retTree;
				break;
			}
			case QNAME:
			case PARENTHESIZED:
			case ABSOLUTE_SLASH:
			case ABSOLUTE_DSLASH:
			case WILDCARD:
			case PREFIX_WILDCARD:
			case FUNCTION:
			case UNARY_MINUS:
			case UNARY_PLUS:
			case VARIABLE_REF:
			case ELEMENT:
			case TEXT:
			case NCNAME:
			case STRING_LITERAL:
			case EQ:
			case LCURLY:
			case COMMA:
			case STAR:
			case PLUS:
			case LITERAL_return:
			case LITERAL_if:
			case LITERAL_or:
			case LITERAL_and:
			case NEQ:
			case GT:
			case GTEQ:
			case LT:
			case LTEQ:
			case ANDEQ:
			case OREQ:
			case LITERAL_to:
			case MINUS:
			case LITERAL_div:
			case LITERAL_mod:
			case UNION:
			case SLASH:
			case DSLASH:
			case LITERAL_text:
			case LITERAL_node:
			case SELF:
			case XML_COMMENT:
			case AT:
			case PARENT:
			case LITERAL_child:
			case LITERAL_self:
			case LITERAL_attribute:
			case LITERAL_descendant:
			case 91:
			case 92:
			case LITERAL_parent:
			case LITERAL_ancestor:
			case 95:
			case 96:
			case DOUBLE_LITERAL:
			case DECIMAL_LITERAL:
			case INTEGER_LITERAL:
			case XML_PI:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			expr(_t,(PathExpr) action);
			_t = _retTree;
			
						for (int i= clauses.size() - 1; i >= 0; i--) {
							ForLetClause clause= (ForLetClause) clauses.get(i);
							BindingExpression expr;
							if (clause.isForClause)
								expr= new ForExpr(context);
							else
								expr= new LetExpr(context);
							expr.setVariable(clause.varName);
							expr.setInputSequence(clause.inputSequence);
							expr.setReturnExpression(action);
							if (whereExpr != null) {
								expr.setWhereExpression(whereExpr);
								whereExpr= null;
							}
							action= expr;
						}
						path.add(action);
					
			_t = __t197;
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_or:
		{
			AST __t209 = _t;
			AST tmp9_AST_in = (AST)_t;
			match(_t,LITERAL_or);
			_t = _t.getFirstChild();
			
						PathExpr left= new PathExpr(context);
						PathExpr right= new PathExpr(context);
					
			expr(_t,left);
			_t = _retTree;
			expr(_t,right);
			_t = _retTree;
			_t = __t209;
			_t = _t.getNextSibling();
			
					OpOr or= new OpOr(context);
					or.add(left);
					or.add(right);
					path.addPath(or);
				
			break;
		}
		case LITERAL_and:
		{
			AST __t210 = _t;
			AST tmp10_AST_in = (AST)_t;
			match(_t,LITERAL_and);
			_t = _t.getFirstChild();
			
						PathExpr left= new PathExpr(context);
						PathExpr right= new PathExpr(context);
					
			expr(_t,left);
			_t = _retTree;
			expr(_t,right);
			_t = _retTree;
			_t = __t210;
			_t = _t.getNextSibling();
			
					OpAnd and= new OpAnd(context);
					and.add(left);
					and.add(right);
					path.addPath(and);
				
			break;
		}
		case PARENTHESIZED:
		{
			AST __t211 = _t;
			AST tmp11_AST_in = (AST)_t;
			match(_t,PARENTHESIZED);
			_t = _t.getFirstChild();
			
						PathExpr pathExpr= new PathExpr(context);
						path.addPath(pathExpr);
					
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case QNAME:
			case PARENTHESIZED:
			case ABSOLUTE_SLASH:
			case ABSOLUTE_DSLASH:
			case WILDCARD:
			case PREFIX_WILDCARD:
			case FUNCTION:
			case UNARY_MINUS:
			case UNARY_PLUS:
			case VARIABLE_REF:
			case ELEMENT:
			case TEXT:
			case NCNAME:
			case STRING_LITERAL:
			case EQ:
			case LCURLY:
			case COMMA:
			case STAR:
			case PLUS:
			case LITERAL_return:
			case LITERAL_if:
			case LITERAL_or:
			case LITERAL_and:
			case NEQ:
			case GT:
			case GTEQ:
			case LT:
			case LTEQ:
			case ANDEQ:
			case OREQ:
			case LITERAL_to:
			case MINUS:
			case LITERAL_div:
			case LITERAL_mod:
			case UNION:
			case SLASH:
			case DSLASH:
			case LITERAL_text:
			case LITERAL_node:
			case SELF:
			case XML_COMMENT:
			case AT:
			case PARENT:
			case LITERAL_child:
			case LITERAL_self:
			case LITERAL_attribute:
			case LITERAL_descendant:
			case 91:
			case 92:
			case LITERAL_parent:
			case LITERAL_ancestor:
			case 95:
			case 96:
			case DOUBLE_LITERAL:
			case DECIMAL_LITERAL:
			case INTEGER_LITERAL:
			case XML_PI:
			{
				expr(_t,pathExpr);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t211;
			_t = _t.getNextSibling();
			break;
		}
		case UNION:
		{
			AST __t213 = _t;
			AST tmp12_AST_in = (AST)_t;
			match(_t,UNION);
			_t = _t.getFirstChild();
			
						PathExpr left= new PathExpr(context);
						PathExpr right= new PathExpr(context);
					
			expr(_t,left);
			_t = _retTree;
			expr(_t,right);
			_t = _retTree;
			_t = __t213;
			_t = _t.getNextSibling();
			
					Union union= new Union(context, left, right);
					path.addPath(union);
				
			break;
		}
		case ABSOLUTE_SLASH:
		{
			AST __t214 = _t;
			AST tmp13_AST_in = (AST)_t;
			match(_t,ABSOLUTE_SLASH);
			_t = _t.getFirstChild();
			
						RootNode root= new RootNode(context);
						path.add(root);
					
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case QNAME:
			case PARENTHESIZED:
			case ABSOLUTE_SLASH:
			case ABSOLUTE_DSLASH:
			case WILDCARD:
			case PREFIX_WILDCARD:
			case FUNCTION:
			case UNARY_MINUS:
			case UNARY_PLUS:
			case VARIABLE_REF:
			case ELEMENT:
			case TEXT:
			case NCNAME:
			case STRING_LITERAL:
			case EQ:
			case LCURLY:
			case COMMA:
			case STAR:
			case PLUS:
			case LITERAL_return:
			case LITERAL_if:
			case LITERAL_or:
			case LITERAL_and:
			case NEQ:
			case GT:
			case GTEQ:
			case LT:
			case LTEQ:
			case ANDEQ:
			case OREQ:
			case LITERAL_to:
			case MINUS:
			case LITERAL_div:
			case LITERAL_mod:
			case UNION:
			case SLASH:
			case DSLASH:
			case LITERAL_text:
			case LITERAL_node:
			case SELF:
			case XML_COMMENT:
			case AT:
			case PARENT:
			case LITERAL_child:
			case LITERAL_self:
			case LITERAL_attribute:
			case LITERAL_descendant:
			case 91:
			case 92:
			case LITERAL_parent:
			case LITERAL_ancestor:
			case 95:
			case 96:
			case DOUBLE_LITERAL:
			case DECIMAL_LITERAL:
			case INTEGER_LITERAL:
			case XML_PI:
			{
				expr(_t,path);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t214;
			_t = _t.getNextSibling();
			break;
		}
		case ABSOLUTE_DSLASH:
		{
			AST __t216 = _t;
			AST tmp14_AST_in = (AST)_t;
			match(_t,ABSOLUTE_DSLASH);
			_t = _t.getFirstChild();
			
						RootNode root= new RootNode(context);
						path.add(root);
					
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case QNAME:
			case WILDCARD:
			case PREFIX_WILDCARD:
			case FUNCTION:
			case VARIABLE_REF:
			case NCNAME:
			case STRING_LITERAL:
			case SLASH:
			case DSLASH:
			case LITERAL_text:
			case LITERAL_node:
			case SELF:
			case AT:
			case PARENT:
			case LITERAL_child:
			case LITERAL_self:
			case LITERAL_attribute:
			case LITERAL_descendant:
			case 91:
			case 92:
			case LITERAL_parent:
			case LITERAL_ancestor:
			case 95:
			case 96:
			case DOUBLE_LITERAL:
			case DECIMAL_LITERAL:
			case INTEGER_LITERAL:
			{
				step=pathExpr(_t,path);
				_t = _retTree;
				
								if (step instanceof LocationStep) {
									LocationStep s= (LocationStep) step;
									if (s.getAxis() == Constants.ATTRIBUTE_AXIS)
										// combines descendant-or-self::node()/attribute:*
										s.setAxis(Constants.DESCENDANT_ATTRIBUTE_AXIS);
									else
										s.setAxis(Constants.DESCENDANT_SELF_AXIS);
								}
							
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t216;
			_t = _t.getNextSibling();
			break;
		}
		case LITERAL_to:
		{
			AST __t218 = _t;
			AST tmp15_AST_in = (AST)_t;
			match(_t,LITERAL_to);
			_t = _t.getFirstChild();
			
						PathExpr start = new PathExpr(context);
						PathExpr end = new PathExpr(context);
						List args = new ArrayList(2);
						args.add(start);
						args.add(end);
					
			expr(_t,start);
			_t = _retTree;
			expr(_t,end);
			_t = _retTree;
			
						RangeExpression range = new RangeExpression(context);
						range.setArguments(args);
						path.addPath(range);
					
			_t = __t218;
			_t = _t.getNextSibling();
			break;
		}
		case EQ:
		case NEQ:
		case GT:
		case GTEQ:
		case LT:
		case LTEQ:
		{
			step=generalComp(_t,path);
			_t = _retTree;
			break;
		}
		case ANDEQ:
		case OREQ:
		{
			step=fulltextComp(_t,path);
			_t = _retTree;
			break;
		}
		case QNAME:
		case WILDCARD:
		case PREFIX_WILDCARD:
		case FUNCTION:
		case VARIABLE_REF:
		case NCNAME:
		case STRING_LITERAL:
		case SLASH:
		case DSLASH:
		case LITERAL_text:
		case LITERAL_node:
		case SELF:
		case AT:
		case PARENT:
		case LITERAL_child:
		case LITERAL_self:
		case LITERAL_attribute:
		case LITERAL_descendant:
		case 91:
		case 92:
		case LITERAL_parent:
		case LITERAL_ancestor:
		case 95:
		case 96:
		case DOUBLE_LITERAL:
		case DECIMAL_LITERAL:
		case INTEGER_LITERAL:
		{
			step=pathExpr(_t,path);
			_t = _retTree;
			break;
		}
		case UNARY_MINUS:
		case UNARY_PLUS:
		case STAR:
		case PLUS:
		case MINUS:
		case LITERAL_div:
		case LITERAL_mod:
		{
			step=numericExpr(_t,path);
			_t = _retTree;
			break;
		}
		case ELEMENT:
		case TEXT:
		case LCURLY:
		case XML_COMMENT:
		case XML_PI:
		{
			step=constructor(_t,path);
			_t = _retTree;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
	}
	
	public final void xpath(AST _t,
		PathExpr path
	) throws RecognitionException {
		
		AST xpath_AST_in = (AST)_t;
		
		try {      // for error handling
			module(_t,path);
			_t = _retTree;
		}
		catch (RecognitionException e) {
			handleException(e);
		}
		catch (EXistException e) {
			handleException(e);
		}
		catch (PermissionDeniedException e) {
			handleException(e);
		}
		catch (XPathException e) {
			handleException(e);
		}
		_retTree = _t;
	}
	
	public final void module(AST _t,
		PathExpr path
	) throws RecognitionException, PermissionDeniedException,EXistException,XPathException {
		
		AST module_AST_in = (AST)_t;
		
		prolog(_t,path);
		_t = _retTree;
		expr(_t,path);
		_t = _retTree;
		_retTree = _t;
	}
	
	public final void prolog(AST _t,
		PathExpr path
	) throws RecognitionException, PermissionDeniedException,EXistException,XPathException {
		
		AST prolog_AST_in = (AST)_t;
		AST v = null;
		AST prefix = null;
		AST uri = null;
		AST defu = null;
		AST deff = null;
		AST qname = null;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case QNAME:
		case PARENTHESIZED:
		case ABSOLUTE_SLASH:
		case ABSOLUTE_DSLASH:
		case WILDCARD:
		case PREFIX_WILDCARD:
		case FUNCTION:
		case UNARY_MINUS:
		case UNARY_PLUS:
		case VARIABLE_REF:
		case ELEMENT:
		case TEXT:
		case VERSION_DECL:
		case NAMESPACE_DECL:
		case DEF_NAMESPACE_DECL:
		case DEF_FUNCTION_NS_DECL:
		case GLOBAL_VAR:
		case NCNAME:
		case STRING_LITERAL:
		case EQ:
		case LCURLY:
		case COMMA:
		case STAR:
		case PLUS:
		case LITERAL_return:
		case LITERAL_if:
		case LITERAL_or:
		case LITERAL_and:
		case NEQ:
		case GT:
		case GTEQ:
		case LT:
		case LTEQ:
		case ANDEQ:
		case OREQ:
		case LITERAL_to:
		case MINUS:
		case LITERAL_div:
		case LITERAL_mod:
		case UNION:
		case SLASH:
		case DSLASH:
		case LITERAL_text:
		case LITERAL_node:
		case SELF:
		case XML_COMMENT:
		case AT:
		case PARENT:
		case LITERAL_child:
		case LITERAL_self:
		case LITERAL_attribute:
		case LITERAL_descendant:
		case 91:
		case 92:
		case LITERAL_parent:
		case LITERAL_ancestor:
		case 95:
		case 96:
		case DOUBLE_LITERAL:
		case DECIMAL_LITERAL:
		case INTEGER_LITERAL:
		case XML_PI:
		{
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case VERSION_DECL:
			{
				AST __t179 = _t;
				v = _t==ASTNULL ? null :(AST)_t;
				match(_t,VERSION_DECL);
				_t = _t.getFirstChild();
				
								if(!v.getText().equals("1.0"))
									throw new XPathException("Wrong XQuery version: require 1.0");
							
				_t = __t179;
				_t = _t.getNextSibling();
				break;
			}
			case QNAME:
			case PARENTHESIZED:
			case ABSOLUTE_SLASH:
			case ABSOLUTE_DSLASH:
			case WILDCARD:
			case PREFIX_WILDCARD:
			case FUNCTION:
			case UNARY_MINUS:
			case UNARY_PLUS:
			case VARIABLE_REF:
			case ELEMENT:
			case TEXT:
			case NAMESPACE_DECL:
			case DEF_NAMESPACE_DECL:
			case DEF_FUNCTION_NS_DECL:
			case GLOBAL_VAR:
			case NCNAME:
			case STRING_LITERAL:
			case EQ:
			case LCURLY:
			case COMMA:
			case STAR:
			case PLUS:
			case LITERAL_return:
			case LITERAL_if:
			case LITERAL_or:
			case LITERAL_and:
			case NEQ:
			case GT:
			case GTEQ:
			case LT:
			case LTEQ:
			case ANDEQ:
			case OREQ:
			case LITERAL_to:
			case MINUS:
			case LITERAL_div:
			case LITERAL_mod:
			case UNION:
			case SLASH:
			case DSLASH:
			case LITERAL_text:
			case LITERAL_node:
			case SELF:
			case XML_COMMENT:
			case AT:
			case PARENT:
			case LITERAL_child:
			case LITERAL_self:
			case LITERAL_attribute:
			case LITERAL_descendant:
			case 91:
			case 92:
			case LITERAL_parent:
			case LITERAL_ancestor:
			case 95:
			case 96:
			case DOUBLE_LITERAL:
			case DECIMAL_LITERAL:
			case INTEGER_LITERAL:
			case XML_PI:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			_loop185:
			do {
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case NAMESPACE_DECL:
				{
					AST __t181 = _t;
					prefix = _t==ASTNULL ? null :(AST)_t;
					match(_t,NAMESPACE_DECL);
					_t = _t.getFirstChild();
					uri = (AST)_t;
					match(_t,STRING_LITERAL);
					_t = _t.getNextSibling();
					
									context.declareNamespace(prefix.getText(), uri.getText());
								
					_t = __t181;
					_t = _t.getNextSibling();
					break;
				}
				case DEF_NAMESPACE_DECL:
				{
					AST __t182 = _t;
					AST tmp16_AST_in = (AST)_t;
					match(_t,DEF_NAMESPACE_DECL);
					_t = _t.getFirstChild();
					defu = (AST)_t;
					match(_t,STRING_LITERAL);
					_t = _t.getNextSibling();
					
									context.declareNamespace("", defu.getText());
								
					_t = __t182;
					_t = _t.getNextSibling();
					break;
				}
				case DEF_FUNCTION_NS_DECL:
				{
					AST __t183 = _t;
					AST tmp17_AST_in = (AST)_t;
					match(_t,DEF_FUNCTION_NS_DECL);
					_t = _t.getFirstChild();
					deff = (AST)_t;
					match(_t,STRING_LITERAL);
					_t = _t.getNextSibling();
					
									context.setDefaultFunctionNamespace(deff.getText());
								
					_t = __t183;
					_t = _t.getNextSibling();
					break;
				}
				case GLOBAL_VAR:
				{
					AST __t184 = _t;
					qname = _t==ASTNULL ? null :(AST)_t;
					match(_t,GLOBAL_VAR);
					_t = _t.getFirstChild();
					PathExpr enclosed = new PathExpr(context);
					expr(_t,enclosed);
					_t = _retTree;
					
									VariableDeclaration decl = new VariableDeclaration(context, qname.getText(), enclosed);
									path.add(decl);
								
					_t = __t184;
					_t = _t.getNextSibling();
					break;
				}
				default:
				{
					break _loop185;
				}
				}
			} while (true);
			}
			break;
		}
		case FUNCTION_DECL:
		{
			functionDecl(_t,path);
			_t = _retTree;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
	}
	
	public final void functionDecl(AST _t,
		PathExpr path
	) throws RecognitionException, PermissionDeniedException,EXistException,XPathException {
		
		AST functionDecl_AST_in = (AST)_t;
		AST name = null;
		
		AST __t187 = _t;
		name = _t==ASTNULL ? null :(AST)_t;
		match(_t,FUNCTION_DECL);
		_t = _t.getFirstChild();
		PathExpr body = new PathExpr(context);
		
					System.out.println("found function declaration " + name.getText()); 
				
		{
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case VARIABLE_BINDING:
		{
			paramList(_t);
			_t = _retTree;
			break;
		}
		case QNAME:
		case PARENTHESIZED:
		case ABSOLUTE_SLASH:
		case ABSOLUTE_DSLASH:
		case WILDCARD:
		case PREFIX_WILDCARD:
		case FUNCTION:
		case UNARY_MINUS:
		case UNARY_PLUS:
		case VARIABLE_REF:
		case ELEMENT:
		case TEXT:
		case NCNAME:
		case STRING_LITERAL:
		case EQ:
		case LCURLY:
		case COMMA:
		case STAR:
		case PLUS:
		case LITERAL_return:
		case LITERAL_if:
		case LITERAL_or:
		case LITERAL_and:
		case NEQ:
		case GT:
		case GTEQ:
		case LT:
		case LTEQ:
		case ANDEQ:
		case OREQ:
		case LITERAL_to:
		case MINUS:
		case LITERAL_div:
		case LITERAL_mod:
		case UNION:
		case SLASH:
		case DSLASH:
		case LITERAL_text:
		case LITERAL_node:
		case SELF:
		case XML_COMMENT:
		case AT:
		case PARENT:
		case LITERAL_child:
		case LITERAL_self:
		case LITERAL_attribute:
		case LITERAL_descendant:
		case 91:
		case 92:
		case LITERAL_parent:
		case LITERAL_ancestor:
		case 95:
		case 96:
		case DOUBLE_LITERAL:
		case DECIMAL_LITERAL:
		case INTEGER_LITERAL:
		case XML_PI:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		}
		expr(_t,body);
		_t = _retTree;
		_t = __t187;
		_t = _t.getNextSibling();
		_retTree = _t;
	}
	
	public final void paramList(AST _t) throws RecognitionException {
		
		AST paramList_AST_in = (AST)_t;
		
		param(_t);
		_t = _retTree;
		{
		_loop191:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_t.getType()==VARIABLE_BINDING)) {
				param(_t);
				_t = _retTree;
			}
			else {
				break _loop191;
			}
			
		} while (true);
		}
		_retTree = _t;
	}
	
	public final void param(AST _t) throws RecognitionException {
		
		AST param_AST_in = (AST)_t;
		AST var = null;
		
		AST __t193 = _t;
		var = _t==ASTNULL ? null :(AST)_t;
		match(_t,VARIABLE_BINDING);
		_t = _t.getFirstChild();
		
					System.out.println("found variable binding: " + var.getText());
				
		_t = __t193;
		_t = _t.getNextSibling();
		_retTree = _t;
	}
	
	public final Expression  pathExpr(AST _t,
		PathExpr path
	) throws RecognitionException, PermissionDeniedException,EXistException,XPathException {
		Expression step;
		
		AST pathExpr_AST_in = (AST)_t;
		AST c = null;
		AST i = null;
		AST dec = null;
		AST dbl = null;
		AST v = null;
		AST qn = null;
		AST nc1 = null;
		AST nc = null;
		AST attr = null;
		AST nc2 = null;
		AST nc3 = null;
		
			Expression rightStep= null;
			step= null;
			int axis= Constants.CHILD_AXIS;
		
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case STRING_LITERAL:
		{
			c = (AST)_t;
			match(_t,STRING_LITERAL);
			_t = _t.getNextSibling();
			
					step= new LiteralValue(context, new StringValue(c.getText()));
					path.add(step);
				
			break;
		}
		case INTEGER_LITERAL:
		{
			i = (AST)_t;
			match(_t,INTEGER_LITERAL);
			_t = _t.getNextSibling();
			
					step= new LiteralValue(context, new IntegerValue(Integer.parseInt(i.getText())));
					path.add(step);
				
			break;
		}
		case DOUBLE_LITERAL:
		case DECIMAL_LITERAL:
		{
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case DECIMAL_LITERAL:
			{
				dec = (AST)_t;
				match(_t,DECIMAL_LITERAL);
				_t = _t.getNextSibling();
				step= new LiteralValue(context, new DoubleValue(Double.parseDouble(dec.getText())));
				break;
			}
			case DOUBLE_LITERAL:
			{
				dbl = (AST)_t;
				match(_t,DOUBLE_LITERAL);
				_t = _t.getNextSibling();
				step= new LiteralValue(context, new DoubleValue(Double.parseDouble(dbl.getText())));
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			path.add(step);
			break;
		}
		case VARIABLE_REF:
		{
			v = (AST)_t;
			match(_t,VARIABLE_REF);
			_t = _t.getNextSibling();
			
					step= new VariableReference(context, v.getText());
					path.add(step);
				
			break;
		}
		case FUNCTION:
		{
			step=functionCall(_t,path);
			_t = _retTree;
			break;
		}
		case QNAME:
		case WILDCARD:
		case PREFIX_WILDCARD:
		case NCNAME:
		case LITERAL_text:
		case LITERAL_node:
		case LITERAL_child:
		case LITERAL_self:
		case LITERAL_attribute:
		case LITERAL_descendant:
		case 91:
		case 92:
		case LITERAL_parent:
		case LITERAL_ancestor:
		case 95:
		case 96:
		{
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LITERAL_child:
			case LITERAL_self:
			case LITERAL_attribute:
			case LITERAL_descendant:
			case 91:
			case 92:
			case LITERAL_parent:
			case LITERAL_ancestor:
			case 95:
			case 96:
			{
				axis=forwardAxis(_t);
				_t = _retTree;
				break;
			}
			case QNAME:
			case WILDCARD:
			case PREFIX_WILDCARD:
			case NCNAME:
			case LITERAL_text:
			case LITERAL_node:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			NodeTest test;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case QNAME:
			{
				qn = (AST)_t;
				match(_t,QNAME);
				_t = _t.getNextSibling();
				
							QName qname= QName.parse(context, qn.getText());
							test= new NameTest(Type.ELEMENT, qname);
						
				break;
			}
			case PREFIX_WILDCARD:
			{
				AST __t223 = _t;
				AST tmp18_AST_in = (AST)_t;
				match(_t,PREFIX_WILDCARD);
				_t = _t.getFirstChild();
				nc1 = (AST)_t;
				match(_t,NCNAME);
				_t = _t.getNextSibling();
				_t = __t223;
				_t = _t.getNextSibling();
				
							QName qname= new QName(nc1.getText(), null, null);
							test= new NameTest(Type.ELEMENT, qname);
						
				break;
			}
			case NCNAME:
			{
				AST __t224 = _t;
				nc = _t==ASTNULL ? null :(AST)_t;
				match(_t,NCNAME);
				_t = _t.getFirstChild();
				AST tmp19_AST_in = (AST)_t;
				match(_t,WILDCARD);
				_t = _t.getNextSibling();
				_t = __t224;
				_t = _t.getNextSibling();
				
							String namespaceURI= context.getURIForPrefix(nc.getText());
							QName qname= new QName(null, namespaceURI, null);
							test= new NameTest(Type.ELEMENT, qname);
						
				break;
			}
			case WILDCARD:
			{
				AST tmp20_AST_in = (AST)_t;
				match(_t,WILDCARD);
				_t = _t.getNextSibling();
				test= new TypeTest(Type.ELEMENT);
				break;
			}
			case LITERAL_node:
			{
				AST tmp21_AST_in = (AST)_t;
				match(_t,LITERAL_node);
				_t = _t.getNextSibling();
				test= new AnyNodeTest();
				break;
			}
			case LITERAL_text:
			{
				AST tmp22_AST_in = (AST)_t;
				match(_t,LITERAL_text);
				_t = _t.getNextSibling();
				test= new TypeTest(Type.TEXT);
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			
					step= new LocationStep(context, axis, test);
					path.add(step);
				
			{
			_loop226:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==PREDICATE)) {
					predicate(_t,(LocationStep) step);
					_t = _retTree;
				}
				else {
					break _loop226;
				}
				
			} while (true);
			}
			break;
		}
		case AT:
		{
			AST tmp23_AST_in = (AST)_t;
			match(_t,AT);
			_t = _t.getNextSibling();
			QName qname;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case QNAME:
			{
				attr = (AST)_t;
				match(_t,QNAME);
				_t = _t.getNextSibling();
				qname= QName.parseAttribute(context, attr.getText());
				break;
			}
			case PREFIX_WILDCARD:
			{
				AST __t228 = _t;
				AST tmp24_AST_in = (AST)_t;
				match(_t,PREFIX_WILDCARD);
				_t = _t.getFirstChild();
				nc2 = (AST)_t;
				match(_t,NCNAME);
				_t = _t.getNextSibling();
				_t = __t228;
				_t = _t.getNextSibling();
				qname= new QName(nc2.getText(), null, null);
				break;
			}
			case NCNAME:
			{
				AST __t229 = _t;
				nc3 = _t==ASTNULL ? null :(AST)_t;
				match(_t,NCNAME);
				_t = _t.getFirstChild();
				AST tmp25_AST_in = (AST)_t;
				match(_t,WILDCARD);
				_t = _t.getNextSibling();
				_t = __t229;
				_t = _t.getNextSibling();
				
							String namespaceURI= context.getURIForPrefix(nc3.getText());
							if (namespaceURI == null)
								throw new EXistException("No namespace defined for prefix " + nc.getText());
							qname= new QName(null, namespaceURI, null);
						
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			
					step= new LocationStep(context, Constants.ATTRIBUTE_AXIS, new NameTest(Type.ATTRIBUTE, qname));
					path.add(step);
				
			{
			_loop231:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==PREDICATE)) {
					predicate(_t,(LocationStep) step);
					_t = _retTree;
				}
				else {
					break _loop231;
				}
				
			} while (true);
			}
			break;
		}
		case SELF:
		{
			AST tmp26_AST_in = (AST)_t;
			match(_t,SELF);
			_t = _t.getNextSibling();
			
					step= new LocationStep(context, Constants.SELF_AXIS, new TypeTest(Type.NODE));
					path.add(step);
				
			{
			_loop233:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==PREDICATE)) {
					predicate(_t,(LocationStep) step);
					_t = _retTree;
				}
				else {
					break _loop233;
				}
				
			} while (true);
			}
			break;
		}
		case PARENT:
		{
			AST tmp27_AST_in = (AST)_t;
			match(_t,PARENT);
			_t = _t.getNextSibling();
			
					step= new LocationStep(context, Constants.PARENT_AXIS, new TypeTest(Type.NODE));
					path.add(step);
				
			{
			_loop235:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==PREDICATE)) {
					predicate(_t,(LocationStep) step);
					_t = _retTree;
				}
				else {
					break _loop235;
				}
				
			} while (true);
			}
			break;
		}
		case SLASH:
		{
			AST __t236 = _t;
			AST tmp28_AST_in = (AST)_t;
			match(_t,SLASH);
			_t = _t.getFirstChild();
			step=pathExpr(_t,path);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case QNAME:
			case WILDCARD:
			case PREFIX_WILDCARD:
			case FUNCTION:
			case VARIABLE_REF:
			case NCNAME:
			case STRING_LITERAL:
			case SLASH:
			case DSLASH:
			case LITERAL_text:
			case LITERAL_node:
			case SELF:
			case AT:
			case PARENT:
			case LITERAL_child:
			case LITERAL_self:
			case LITERAL_attribute:
			case LITERAL_descendant:
			case 91:
			case 92:
			case LITERAL_parent:
			case LITERAL_ancestor:
			case 95:
			case 96:
			case DOUBLE_LITERAL:
			case DECIMAL_LITERAL:
			case INTEGER_LITERAL:
			{
				rightStep=pathExpr(_t,path);
				_t = _retTree;
				
								if (rightStep instanceof LocationStep && ((LocationStep) rightStep).getAxis() == -1)
									 ((LocationStep) rightStep).setAxis(Constants.CHILD_AXIS);
							
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t236;
			_t = _t.getNextSibling();
			
					if (step instanceof LocationStep && ((LocationStep) step).getAxis() == -1)
						 ((LocationStep) step).setAxis(Constants.CHILD_AXIS);
				
			break;
		}
		case DSLASH:
		{
			AST __t238 = _t;
			AST tmp29_AST_in = (AST)_t;
			match(_t,DSLASH);
			_t = _t.getFirstChild();
			step=pathExpr(_t,path);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case QNAME:
			case WILDCARD:
			case PREFIX_WILDCARD:
			case FUNCTION:
			case VARIABLE_REF:
			case NCNAME:
			case STRING_LITERAL:
			case SLASH:
			case DSLASH:
			case LITERAL_text:
			case LITERAL_node:
			case SELF:
			case AT:
			case PARENT:
			case LITERAL_child:
			case LITERAL_self:
			case LITERAL_attribute:
			case LITERAL_descendant:
			case 91:
			case 92:
			case LITERAL_parent:
			case LITERAL_ancestor:
			case 95:
			case 96:
			case DOUBLE_LITERAL:
			case DECIMAL_LITERAL:
			case INTEGER_LITERAL:
			{
				rightStep=pathExpr(_t,path);
				_t = _retTree;
				
								if (rightStep instanceof LocationStep) {
									LocationStep rs= (LocationStep) rightStep;
									if (rs.getAxis() == Constants.ATTRIBUTE_AXIS)
										rs.setAxis(Constants.DESCENDANT_ATTRIBUTE_AXIS);
									else
										rs.setAxis(Constants.DESCENDANT_SELF_AXIS);
								}
							
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t238;
			_t = _t.getNextSibling();
			
					if (step instanceof LocationStep && ((LocationStep) rightStep).getAxis() == -1)
						 ((LocationStep) step).setAxis(Constants.DESCENDANT_SELF_AXIS);
				
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return step;
	}
	
	public final Expression  generalComp(AST _t,
		PathExpr path
	) throws RecognitionException, PermissionDeniedException,EXistException,XPathException {
		Expression step;
		
		AST generalComp_AST_in = (AST)_t;
		
			step= null;
			PathExpr left= new PathExpr(context);
			PathExpr right= new PathExpr(context);
		
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case EQ:
		{
			AST __t259 = _t;
			AST tmp30_AST_in = (AST)_t;
			match(_t,EQ);
			_t = _t.getFirstChild();
			expr(_t,left);
			_t = _retTree;
			expr(_t,right);
			_t = _retTree;
			
						step= new GeneralComparison(context, left, right, Constants.EQ);
						path.add(step);
					
			_t = __t259;
			_t = _t.getNextSibling();
			break;
		}
		case NEQ:
		{
			AST __t260 = _t;
			AST tmp31_AST_in = (AST)_t;
			match(_t,NEQ);
			_t = _t.getFirstChild();
			expr(_t,left);
			_t = _retTree;
			expr(_t,right);
			_t = _retTree;
			
						step= new GeneralComparison(context, left, right, Constants.NEQ);
						path.add(step);
					
			_t = __t260;
			_t = _t.getNextSibling();
			break;
		}
		case LT:
		{
			AST __t261 = _t;
			AST tmp32_AST_in = (AST)_t;
			match(_t,LT);
			_t = _t.getFirstChild();
			expr(_t,left);
			_t = _retTree;
			expr(_t,right);
			_t = _retTree;
			
						step= new GeneralComparison(context, left, right, Constants.LT);
						path.add(step);
					
			_t = __t261;
			_t = _t.getNextSibling();
			break;
		}
		case LTEQ:
		{
			AST __t262 = _t;
			AST tmp33_AST_in = (AST)_t;
			match(_t,LTEQ);
			_t = _t.getFirstChild();
			expr(_t,left);
			_t = _retTree;
			expr(_t,right);
			_t = _retTree;
			
						step= new GeneralComparison(context, left, right, Constants.LTEQ);
						path.add(step);
					
			_t = __t262;
			_t = _t.getNextSibling();
			break;
		}
		case GT:
		{
			AST __t263 = _t;
			AST tmp34_AST_in = (AST)_t;
			match(_t,GT);
			_t = _t.getFirstChild();
			expr(_t,left);
			_t = _retTree;
			expr(_t,right);
			_t = _retTree;
			
						step= new GeneralComparison(context, left, right, Constants.GT);
						path.add(step);
					
			_t = __t263;
			_t = _t.getNextSibling();
			break;
		}
		case GTEQ:
		{
			AST __t264 = _t;
			AST tmp35_AST_in = (AST)_t;
			match(_t,GTEQ);
			_t = _t.getFirstChild();
			expr(_t,left);
			_t = _retTree;
			expr(_t,right);
			_t = _retTree;
			
						step= new GeneralComparison(context, left, right, Constants.GTEQ);
						path.add(step);
					
			_t = __t264;
			_t = _t.getNextSibling();
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return step;
	}
	
	public final Expression  fulltextComp(AST _t,
		PathExpr path
	) throws RecognitionException, PermissionDeniedException,EXistException,XPathException {
		Expression step;
		
		AST fulltextComp_AST_in = (AST)_t;
		
			step= null;
			PathExpr nodes= new PathExpr(context);
			PathExpr query= new PathExpr(context);
		
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case ANDEQ:
		{
			AST __t256 = _t;
			AST tmp36_AST_in = (AST)_t;
			match(_t,ANDEQ);
			_t = _t.getFirstChild();
			expr(_t,nodes);
			_t = _retTree;
			expr(_t,query);
			_t = _retTree;
			_t = __t256;
			_t = _t.getNextSibling();
			
					ExtFulltext exprCont= new ExtFulltext(context, Constants.FULLTEXT_AND);
					exprCont.setPath(nodes);
					exprCont.addTerm(query);
					path.addPath(exprCont);
				
			break;
		}
		case OREQ:
		{
			AST __t257 = _t;
			AST tmp37_AST_in = (AST)_t;
			match(_t,OREQ);
			_t = _t.getFirstChild();
			expr(_t,nodes);
			_t = _retTree;
			expr(_t,query);
			_t = _retTree;
			_t = __t257;
			_t = _t.getNextSibling();
			
					ExtFulltext exprCont= new ExtFulltext(context, Constants.FULLTEXT_OR);
					exprCont.setPath(nodes);
					exprCont.addTerm(query);
					path.addPath(exprCont);
				
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return step;
	}
	
	public final Expression  numericExpr(AST _t,
		PathExpr path
	) throws RecognitionException, PermissionDeniedException,EXistException,XPathException {
		Expression step;
		
		AST numericExpr_AST_in = (AST)_t;
		
			step= null;
			PathExpr left= new PathExpr(context);
			PathExpr right= new PathExpr(context);
		
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case PLUS:
		{
			AST __t241 = _t;
			AST tmp38_AST_in = (AST)_t;
			match(_t,PLUS);
			_t = _t.getFirstChild();
			expr(_t,left);
			_t = _retTree;
			expr(_t,right);
			_t = _retTree;
			_t = __t241;
			_t = _t.getNextSibling();
			
					OpNumeric op= new OpNumeric(context, left, right, Constants.PLUS);
					path.addPath(op);
					step= op;
				
			break;
		}
		case MINUS:
		{
			AST __t242 = _t;
			AST tmp39_AST_in = (AST)_t;
			match(_t,MINUS);
			_t = _t.getFirstChild();
			expr(_t,left);
			_t = _retTree;
			expr(_t,right);
			_t = _retTree;
			_t = __t242;
			_t = _t.getNextSibling();
			
					OpNumeric op= new OpNumeric(context, left, right, Constants.MINUS);
					path.addPath(op);
					step= op;
				
			break;
		}
		case UNARY_MINUS:
		{
			AST __t243 = _t;
			AST tmp40_AST_in = (AST)_t;
			match(_t,UNARY_MINUS);
			_t = _t.getFirstChild();
			expr(_t,left);
			_t = _retTree;
			_t = __t243;
			_t = _t.getNextSibling();
			
					UnaryExpr unary= new UnaryExpr(context, Constants.MINUS);
					unary.add(left);
					path.addPath(unary);
					step= unary;
				
			break;
		}
		case UNARY_PLUS:
		{
			AST __t244 = _t;
			AST tmp41_AST_in = (AST)_t;
			match(_t,UNARY_PLUS);
			_t = _t.getFirstChild();
			expr(_t,left);
			_t = _retTree;
			_t = __t244;
			_t = _t.getNextSibling();
			
					UnaryExpr unary= new UnaryExpr(context, Constants.PLUS);
					unary.add(left);
					path.addPath(unary);
					step= unary;
				
			break;
		}
		case LITERAL_div:
		{
			AST __t245 = _t;
			AST tmp42_AST_in = (AST)_t;
			match(_t,LITERAL_div);
			_t = _t.getFirstChild();
			expr(_t,left);
			_t = _retTree;
			expr(_t,right);
			_t = _retTree;
			_t = __t245;
			_t = _t.getNextSibling();
			
					OpNumeric op= new OpNumeric(context, left, right, Constants.DIV);
					path.addPath(op);
					step= op;
				
			break;
		}
		case LITERAL_mod:
		{
			AST __t246 = _t;
			AST tmp43_AST_in = (AST)_t;
			match(_t,LITERAL_mod);
			_t = _t.getFirstChild();
			expr(_t,left);
			_t = _retTree;
			expr(_t,right);
			_t = _retTree;
			_t = __t246;
			_t = _t.getNextSibling();
			
					OpNumeric op= new OpNumeric(context, left, right, Constants.MOD);
					path.addPath(op);
					step= op;
				
			break;
		}
		case STAR:
		{
			AST __t247 = _t;
			AST tmp44_AST_in = (AST)_t;
			match(_t,STAR);
			_t = _t.getFirstChild();
			expr(_t,left);
			_t = _retTree;
			expr(_t,right);
			_t = _retTree;
			_t = __t247;
			_t = _t.getNextSibling();
			
					OpNumeric op= new OpNumeric(context, left, right, Constants.MULT);
					path.addPath(op);
					step= op;
				
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return step;
	}
	
	public final Expression  constructor(AST _t,
		PathExpr path
	) throws RecognitionException, PermissionDeniedException,EXistException,XPathException {
		Expression step;
		
		AST constructor_AST_in = (AST)_t;
		AST e = null;
		AST attrName = null;
		AST attrVal = null;
		AST pcdata = null;
		AST cdata = null;
		AST p = null;
		
			step= null;
			PathExpr elementContent= null;
		
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case ELEMENT:
		{
			AST __t266 = _t;
			e = _t==ASTNULL ? null :(AST)_t;
			match(_t,ELEMENT);
			_t = _t.getFirstChild();
			
						ElementConstructor c= new ElementConstructor(context, e.getText());
						path.add(c);
						step= c;
					
			{
			_loop272:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==ATTRIBUTE)) {
					AST __t268 = _t;
					attrName = _t==ASTNULL ? null :(AST)_t;
					match(_t,ATTRIBUTE);
					_t = _t.getFirstChild();
					
										AttributeConstructor attrib= new AttributeConstructor(context, attrName.getText());
										c.addAttribute(attrib);
									
					{
					int _cnt271=0;
					_loop271:
					do {
						if (_t==null) _t=ASTNULL;
						switch ( _t.getType()) {
						case ATTRIBUTE_CONTENT:
						{
							attrVal = (AST)_t;
							match(_t,ATTRIBUTE_CONTENT);
							_t = _t.getNextSibling();
							attrib.addValue(attrVal.getText());
							break;
						}
						case LCURLY:
						{
							AST __t270 = _t;
							AST tmp45_AST_in = (AST)_t;
							match(_t,LCURLY);
							_t = _t.getFirstChild();
							PathExpr enclosed= new PathExpr(context);
							expr(_t,enclosed);
							_t = _retTree;
							attrib.addEnclosedExpr(enclosed);
							_t = __t270;
							_t = _t.getNextSibling();
							break;
						}
						default:
						{
							if ( _cnt271>=1 ) { break _loop271; } else {throw new NoViableAltException(_t);}
						}
						}
						_cnt271++;
					} while (true);
					}
					_t = __t268;
					_t = _t.getNextSibling();
				}
				else {
					break _loop272;
				}
				
			} while (true);
			}
			{
			_loop274:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_tokenSet_0.member(_t.getType()))) {
					
									if (elementContent == null) {
										elementContent= new PathExpr(context);
										c.setContent(elementContent);
									}
								
					constructor(_t,elementContent);
					_t = _retTree;
				}
				else {
					break _loop274;
				}
				
			} while (true);
			}
			_t = __t266;
			_t = _t.getNextSibling();
			break;
		}
		case TEXT:
		{
			AST __t275 = _t;
			pcdata = _t==ASTNULL ? null :(AST)_t;
			match(_t,TEXT);
			_t = _t.getFirstChild();
			
						TextConstructor text= new TextConstructor(context, pcdata.getText());
						path.add(text);
						step= text;
					
			_t = __t275;
			_t = _t.getNextSibling();
			break;
		}
		case XML_COMMENT:
		{
			AST __t276 = _t;
			cdata = _t==ASTNULL ? null :(AST)_t;
			match(_t,XML_COMMENT);
			_t = _t.getFirstChild();
			
						CommentConstructor comment= new CommentConstructor(context, cdata.getText());
						path.add(comment);
						step= comment;
					
			_t = __t276;
			_t = _t.getNextSibling();
			break;
		}
		case XML_PI:
		{
			AST __t277 = _t;
			p = _t==ASTNULL ? null :(AST)_t;
			match(_t,XML_PI);
			_t = _t.getFirstChild();
			
						PIConstructor pi= new PIConstructor(context, p.getText());
						path.add(pi);
						step= pi;
					
			_t = __t277;
			_t = _t.getNextSibling();
			break;
		}
		case LCURLY:
		{
			AST __t278 = _t;
			AST tmp46_AST_in = (AST)_t;
			match(_t,LCURLY);
			_t = _t.getFirstChild();
			EnclosedExpr subexpr= new EnclosedExpr(context);
			expr(_t,subexpr);
			_t = _retTree;
			
						path.addPath(subexpr);
						step= subexpr;
					
			_t = __t278;
			_t = _t.getNextSibling();
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return step;
	}
	
	public final Expression  functionCall(AST _t,
		PathExpr path
	) throws RecognitionException, PermissionDeniedException,EXistException,XPathException {
		Expression step;
		
		AST functionCall_AST_in = (AST)_t;
		AST fn = null;
		
			PathExpr pathExpr;
			step= null;
		
		
		AST __t251 = _t;
		fn = _t==ASTNULL ? null :(AST)_t;
		match(_t,FUNCTION);
		_t = _t.getFirstChild();
		List params= new ArrayList(2);
		{
		_loop253:
		do {
			if (_t==null) _t=ASTNULL;
			if ((_tokenSet_1.member(_t.getType()))) {
				pathExpr= new PathExpr(context);
				expr(_t,pathExpr);
				_t = _retTree;
				params.add(pathExpr);
			}
			else {
				break _loop253;
			}
			
		} while (true);
		}
		_t = __t251;
		_t = _t.getNextSibling();
		step = Util.createFunction(context, path, fn.getText(), params);
		_retTree = _t;
		return step;
	}
	
	public final int  forwardAxis(AST _t) throws RecognitionException, PermissionDeniedException,EXistException {
		int axis;
		
		AST forwardAxis_AST_in = (AST)_t;
		axis= -1;
		
		if (_t==null) _t=ASTNULL;
		switch ( _t.getType()) {
		case LITERAL_child:
		{
			AST tmp47_AST_in = (AST)_t;
			match(_t,LITERAL_child);
			_t = _t.getNextSibling();
			axis= Constants.CHILD_AXIS;
			break;
		}
		case LITERAL_attribute:
		{
			AST tmp48_AST_in = (AST)_t;
			match(_t,LITERAL_attribute);
			_t = _t.getNextSibling();
			axis= Constants.ATTRIBUTE_AXIS;
			break;
		}
		case LITERAL_self:
		{
			AST tmp49_AST_in = (AST)_t;
			match(_t,LITERAL_self);
			_t = _t.getNextSibling();
			axis= Constants.SELF_AXIS;
			break;
		}
		case LITERAL_parent:
		{
			AST tmp50_AST_in = (AST)_t;
			match(_t,LITERAL_parent);
			_t = _t.getNextSibling();
			axis= Constants.PARENT_AXIS;
			break;
		}
		case LITERAL_descendant:
		{
			AST tmp51_AST_in = (AST)_t;
			match(_t,LITERAL_descendant);
			_t = _t.getNextSibling();
			axis= Constants.DESCENDANT_AXIS;
			break;
		}
		case 91:
		{
			AST tmp52_AST_in = (AST)_t;
			match(_t,91);
			_t = _t.getNextSibling();
			axis= Constants.DESCENDANT_SELF_AXIS;
			break;
		}
		case 92:
		{
			AST tmp53_AST_in = (AST)_t;
			match(_t,92);
			_t = _t.getNextSibling();
			axis= Constants.FOLLOWING_SIBLING_AXIS;
			break;
		}
		case 96:
		{
			AST tmp54_AST_in = (AST)_t;
			match(_t,96);
			_t = _t.getNextSibling();
			axis= Constants.PRECEDING_SIBLING_AXIS;
			break;
		}
		case LITERAL_ancestor:
		{
			AST tmp55_AST_in = (AST)_t;
			match(_t,LITERAL_ancestor);
			_t = _t.getNextSibling();
			axis= Constants.ANCESTOR_AXIS;
			break;
		}
		case 95:
		{
			AST tmp56_AST_in = (AST)_t;
			match(_t,95);
			_t = _t.getNextSibling();
			axis= Constants.ANCESTOR_SELF_AXIS;
			break;
		}
		default:
		{
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return axis;
	}
	
	public final void predicate(AST _t,
		LocationStep step
	) throws RecognitionException, PermissionDeniedException,EXistException,XPathException {
		
		AST predicate_AST_in = (AST)_t;
		
		AST __t249 = _t;
		AST tmp57_AST_in = (AST)_t;
		match(_t,PREDICATE);
		_t = _t.getFirstChild();
		Predicate predicateExpr= new Predicate(context);
		expr(_t,predicateExpr);
		_t = _retTree;
		step.addPredicate(predicateExpr);
		_t = __t249;
		_t = _t.getNextSibling();
		_retTree = _t;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"QNAME",
		"PREDICATE",
		"FLWOR",
		"PARENTHESIZED",
		"ABSOLUTE_SLASH",
		"ABSOLUTE_DSLASH",
		"WILDCARD",
		"PREFIX_WILDCARD",
		"FUNCTION",
		"UNARY_MINUS",
		"UNARY_PLUS",
		"XPOINTER",
		"XPOINTER_ID",
		"VARIABLE_REF",
		"VARIABLE_BINDING",
		"ELEMENT",
		"ATTRIBUTE",
		"TEXT",
		"VERSION_DECL",
		"NAMESPACE_DECL",
		"DEF_NAMESPACE_DECL",
		"DEF_FUNCTION_NS_DECL",
		"GLOBAL_VAR",
		"FUNCTION_DECL",
		"PROLOG",
		"\"xpointer\"",
		"LPAREN",
		"RPAREN",
		"NCNAME",
		"SEMICOLON",
		"\"declare\"",
		"\"namespace\"",
		"\"default\"",
		"\"function\"",
		"\"xquery\"",
		"\"version\"",
		"STRING_LITERAL",
		"EQ",
		"\"element\"",
		"\"variable\"",
		"DOLLAR",
		"LCURLY",
		"RCURLY",
		"COMMA",
		"\"as\"",
		"\"empty\"",
		"\"?\"",
		"STAR",
		"PLUS",
		"\"item\"",
		"\"where\"",
		"\"return\"",
		"\"for\"",
		"\"let\"",
		"\"in\"",
		"COLON",
		"\"if\"",
		"\"then\"",
		"\"else\"",
		"\"or\"",
		"\"and\"",
		"NEQ",
		"GT",
		"GTEQ",
		"LT",
		"LTEQ",
		"ANDEQ",
		"OREQ",
		"\"to\"",
		"MINUS",
		"\"div\"",
		"\"mod\"",
		"UNION",
		"SLASH",
		"DSLASH",
		"\"text\"",
		"\"node\"",
		"SELF",
		"XML_COMMENT",
		"LPPAREN",
		"RPPAREN",
		"AT",
		"PARENT",
		"\"child\"",
		"\"self\"",
		"\"attribute\"",
		"\"descendant\"",
		"\"descendant-or-self\"",
		"\"following-sibling\"",
		"\"parent\"",
		"\"ancestor\"",
		"\"ancestor-or-self\"",
		"\"preceding-sibling\"",
		"DOUBLE_LITERAL",
		"DECIMAL_LITERAL",
		"INTEGER_LITERAL",
		"WS",
		"END_TAG_START",
		"QUOT",
		"ATTRIBUTE_CONTENT",
		"ELEMENT_CONTENT",
		"XML_COMMENT_END",
		"XML_PI",
		"XML_PI_END",
		"XML_PI_START",
		"CHAR",
		"BASECHAR",
		"IDEOGRAPHIC",
		"COMBINING_CHAR",
		"DIGIT",
		"EXTENDER",
		"LETTER",
		"DIGITS",
		"HEX_DIGITS",
		"NMSTART",
		"NMCHAR",
		"EXPR_COMMENT",
		"PREDEFINED_ENTITY_REF",
		"CHAR_REF",
		"NEXT_TOKEN"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 35184374710272L, 4398046773248L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { -8027487111094829168L, 4466764414975L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	}
	
