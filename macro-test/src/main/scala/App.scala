
import org.msgpack.io.{StreamInput, StreamOutput}
import org.msgpack.packer.Packer
import org.msgpack.scala.{SampleClass, MacroDef,TemplateMacroDef}
import java.io._
import org.msgpack.unpacker.Unpacker

/**
 * 
 * User: takeshita
 * DateTime: 13/02/01 20:39
 */
object App {

  def main(args : Array[String]){

    println("Hello macro")

    {
      val bao = new ByteArrayOutputStream()
      implicit val packer = new Packer(bao)

      //val c = SampleClass(30,"ffff",4)
      val c = SampleClass(5,"jfeiwakfklewkalfkjae",33)

      //シリアライズ
      MacroDef.serialize(c)

      val data = bao.toByteArray()
      printf("Data length = %d",data.length)

      implicit val unpacker = new Unpacker(new ByteArrayInputStream(data))

      //デシリアライズ
      val des = MacroDef.deserialize[SampleClass]()

      println("\n\nDeserialized object ----")
      println(des)
    }
//    {
//
//      val bao = new ByteArrayOutputStream()
//      implicit val packer = new Packer(bao)
//
//      val c = SampleClass(1,"hoge",23)
//
//      val f = TemplateMacroDef.serializeTemplate[SampleClass]
//
//      f(c,packer)
//
//      val data = bao.toByteArray()
//      printf("Data length = %d",data.length)
//
////      implicit val unpacker = new Unpacker(new ByteArrayInputStream(data))
////
////      val des = MacroDef.deserialize[SampleClass]()
////
////      println("----")
////      println(des)
//    }

  }




}
