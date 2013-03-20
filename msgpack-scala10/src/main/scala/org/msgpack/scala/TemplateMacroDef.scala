package org.msgpack.scala

import language.experimental.macros
import reflect.macros.Context
import org.msgpack.io.Output
import org.msgpack.packer.Packer
import org.msgpack.unpacker.Unpacker

/**
 * Created with IntelliJ IDEA.
 * User: takezoux3
 * Date: 13/03/20
 * Time: 16:08
 * To change this template use File | Settings | File Templates.
 */
object TemplateMacroDef {

  def serializeTemplate[T] : (T,Packer) => Unit = macro _serializeTemplate[T]

  def _serializeTemplate[T : c.WeakTypeTag](c : Context) : c.Expr[(T,Packer) => Unit] = {
    import c.universe._

    val t = weakTypeOf[T]

    //assert(t.typeSymbol.asClass.isCaseClass)


    // Primary constructorの取得
    val primaryConstructor = t.declarations.collectFirst({
      case m : MethodSymbol if m.isPrimaryConstructor => m
    })

    if(!primaryConstructor.isDefined){
      throw new Exception(s"Class ${t} doesn't have primary constructor.")
    }

    val packBlocks = primaryConstructor.get.paramss.map( _params => {
      _params.map( {
        case t : TermSymbol => {
          t.typeSignature match{
            case definitions.IntTpe => {
              Apply(Select(Ident("packer"),newTermName("writeInt")), List(Select(Ident("obj"),t.getter.name)))
            }
            case definitions.LongTpe => {
              Apply(Select(Ident("packer"),newTermName("writeLong")), List(Select(Ident("obj"),t.getter.name)))
            }
            case tpe if tpe =:= typeOf[String] => {
              Apply(Select(Ident("packer"),newTermName("writeString")), List(Select(Ident("obj"),t.getter.name)))
            }
            case _ => {
              Apply(Select(Ident("packer"),newTermName("writeString")), List(Select(Ident("obj"),t.getter.name)))
            }
          }
        }
      })
    }) flatten

    /*if(t.typeSymbol.asInstanceOf[ClassSymbol].isCaseClass){

    }else{

    }*/


    c.Expr[(T,Packer) => Unit](
    Function(
      List(
        ValDef(Modifiers(),newTermName("obj"),TypeTree(t),Block()),
        ValDef(Modifiers(),newTermName("packer"),TypeTree(typeOf[Packer]),Block())
      ),
      Block(packBlocks :_*)
    )
    )


    /*reify{
      () => println("OK")
    }*/
//    c.Expr[() => String](
//      Function(List(),
//      Block(
//        ValDef(Modifiers(),newTermName("s"),TypeTree(typeOf[String]),Literal(Constant("hoge"))),
//        Ident("s")
//      )
//    ))

  }


}
