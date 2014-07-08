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
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

	val modulePath = new File("./modules/common/")
	
	"Common Module" should {

		"send 404 on a bad request" in {
			running(FakeApplication(path = modulePath)) {
				route(FakeRequest(GET, "/boum")) must beNone        
			}
		}
    
		"render the status page" in {
			running(FakeApplication(path = modulePath)) {
				val home = route(FakeRequest(GET, "/status")).get
        
				status(home) must equalTo(OK)
				contentAsString(home) must contain ("Everything is great")
			}
		}
	}
}