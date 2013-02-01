
import org.msgpack.io.{StreamInput, StreamOutput}
import org.msgpack.packer.Packer
import org.msgpack.scala.{SampleClass, MacroDef}
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

    val bao = new ByteArrayOutputStream()
    implicit val packer = new Packer(bao)
    //MacroDef.serialize(User(2,"hoge"))

    //MacroDef.serializeSampleClass(SampleClass(20,"hoge"))

    val c = SampleClass(30,"ffff")
    MacroDef.serialize(c)

    val data = bao.toByteArray()
    printf("Data length = %d",data.length)

    implicit val unpacker = new Unpacker(new ByteArrayInputStream(data))

    val des = MacroDef.deserializeSampleClass()

    println(des)
  }

}
