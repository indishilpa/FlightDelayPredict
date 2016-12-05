package example

import scala.language.dynamics

import scala.scalajs.js
import js.Dynamic.{ global => g }
import org.scalajs.jquery.{jQuery => jQ, JQueryXHR, JQueryAjaxSettings}
//import scala.scalajs.js.{Dictionary, String, Any}


//code by Benjamin Jackman published to the Scala.js mailing list on Dec 16
class  JsObjectBuilder extends scala.Dynamic {
  def applyDynamicNamed[A](name: String)(args: (String, js.Any)*): A = {
    println(s"applyDynamicNamed($name)(args: $args")
    if (name != "apply") {
      sys.error("Call jsObj like this jsObj(x=1, y=2) which returns a javascript object that is {x:1,y:2}")
    }
    val obj = js.Object().asInstanceOf[js.Dictionary]
    args.foreach { case (name, value) =>
      obj(name) = value
    }
    obj.asInstanceOf[A]
  }
  //Allows for jsObj()
  def applyDynamic[A](name : String)(args: Nothing*) = {
    if (args.nonEmpty) {
      sys.error("Call jsObj only with named parameters.")
    }
    js.Object().asInstanceOf[A]
  }

  //Note that jsObj can also be given a type parameter
  //that type will be used as the return type,
  //However it's just a NOP and doesn't do real type
  //safety

}


object ScalaJSExample {
  val console = js.Dynamic.global.console
  def main(): Unit = {
    console.log("hello")
    val title = jQ("#h1")
    title.replaceWith("<h2>New Title<h2>")
    val paragraph = g.document.createElement("p")
    paragraph.innerHTML = "<strong>It works now!</strong>"
    g.document.getElementById("playground").appendChild(paragraph)
     val ttl=jQ("#turtle")
    val jsObj = new JsObjectBuilder
    val json = jsObj(url = "http://www.w3.org/People/Berners-Lee/card",
            success =   (data: js.Any, textStatus: js.String, jqXHR: JQueryXHR) =>{
              console.log(s"data=$data,text=$textStatus,jqXHR=$jqXHR");
              js.Dictionary()
            },
            error =  ( jqXHR: JQueryXHR, textStatus: js.String, errorThrow: js.String) => {
              console.log(s"jqXHR=$jqXHR,text=$textStatus,err=$errorThrow");
              js.Dictionary()
            },
          `type` = "GET"
    )
    jQ.ajax(js.Dynamic.literal(

    url = "http://www.w3.org/People/Berners-Lee/card",
    success = { (data: js.Any, textStatus: js.String, jqXHR: JQueryXHR) =>
      console.log(s"data=$data,text=$textStatus,jqXHR=$jqXHR")
    },
    error = { (jqXHR: JQueryXHR, textStatus: js.String, errorThrow: js.String) =>
      console.log(s"jqXHR=$jqXHR,text=$textStatus,err=$errorThrow")
    },
    `type` = "GET"
).asInstanceOf[org.scalajs.jquery.JQueryAjaxSettings])

  }

  private def test(data: js.Any, textStatus: js.String, jqXHR: JQueryXHR): js.Dynamic = {
    console.log(s"data=$data,text=$textStatus,jqXHR=$jqXHR");
    null
  }

  private def err(jqXHR: JQueryXHR,  textStatus: js.String, errorThrow: js.String) : js.Any = {
    console.log(s"jqXHR=$jqXHR,text=$textStatus,err=$errorThrow");
    js.Dictionary()
  }
}

  