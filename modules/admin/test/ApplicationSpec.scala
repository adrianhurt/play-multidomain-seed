import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import java.io.File

/**
* Add your spec here.
* You can mock out a whole application including requests, plugins etc.
* For more information, consult the wiki.
*/
class ApplicationSpec extends Specification {

	val modulePath = new File("./modules/admin/")
	
	"Admin Module" should {

		"send 404 on a bad request" in {
			running(FakeApplication(path = modulePath)) {
				route(FakeRequest(GET, "/boum")) must beNone        
			}
		}
    
		"render the index page" in {
			running(FakeApplication(path = modulePath)) {
				val index = route(FakeRequest(GET, "/")).get
        
				status(index) must equalTo(OK)
				contentType(index) must beSome.which(_ == "text/html")
				contentAsString(index) must contain ("ADMIN template")
			}
		}
	}
}