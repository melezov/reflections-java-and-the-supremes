import scala.reflect.runtime.universe._
import scala.reflect.ClassTag
import scala.util.Try

object Harness extends App {
  def javaReflectField(value: AnyRef, field: String) =
    value.getClass.getDeclaredField(field).get(value)

  def scalaReflectMethod[T <: AnyRef : ClassTag](value: T, method: String)(implicit tt: TypeTag[T]) =
    runtimeMirror(getClass.getClassLoader)
      .reflect(value)
      .reflectMethod(tt.tpe.decl(TermName(method)).asMethod)()

  // patch via java.lang.reflect.Array.getLength required
  println(Try { javaReflectField(Array(1, 2, 3), "length") })
  println(Try { scalaReflectMethod(Array(1, 2, 3), "length")})
}
