import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import java.io.File

/**
* add your integration spec here.
* An integration test will fire up a whole play application in a real (or headless) browser
*/
/*
@RunWith(classOf[JUnitRunner])
class IntegrationSpec extends Specification {

	val modulePath = new File("./modules/admin/")

	"Admin Module" should {

		"work from within a browser" in new WithBrowser(app = FakeApplication(path = modulePath)) {

			browser.goTo("http://admin.myweb.com:" + port)

			browser.pageSource must contain("ADMIN template")
		}
	}
}
*/