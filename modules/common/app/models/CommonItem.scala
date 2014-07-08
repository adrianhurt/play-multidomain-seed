package models

import java.util.{Date}

case class Computer(id: Long, name: String, introduced: Option[Date], discontinued: Option[Date], company: String)

object Computer {

	private val format = new java.text.SimpleDateFormat("yyyy-MM-dd")
	private def optDate (str: String): Option[Date] = Some(format.parse(str))

	def list: List[Computer] = List(
		Computer(16, "Apple II", optDate("1977-04-01"), optDate("1993-10-01"), "Apple Inc."),
		Computer(51, "Commodore 64", optDate("1982-08-01"), optDate("1994-01-01"), "Commodore International"),
		Computer(57, "Altair 8800", optDate("1974-12-19"), None, "Micro Instrumentation and Telemetry Systems"),
		Computer(74, "Amiga 2000", optDate("1986-01-01"), optDate("1990-01-01"), "Commodore International"),
		Computer(95, "NeXTcube Turbo", None, None, "NeXT"),
		Computer(107,"IBM 650", optDate("1953-01-01"), optDate("1962-01-01"), "IBM"),
		Computer(122,"Atari ST", optDate("1985-01-01"), optDate("1993-01-01"), "Atari"),
		Computer(150,"Xerox Star", optDate("1981-01-01"), None, "Xerox"),
		Computer(154,"Super Nintendo Entertainment System", optDate("1991-08-01"), optDate("1999-01-01"), "Nintendo"),
		Computer(158,"PlayStation", optDate("1994-12-03"), None, "Sony"),
		Computer(323,"ZX Spectrum 48K", optDate("1982-01-01"), None, "Sinclair Research Ltd"),
		Computer(341,"NeXT Computer", None, None, "NeXT"),
		Computer(421,"Sony Vaio P VGN-P588E/Q", None, None, "Sony"),
		Computer(476,"lenovo thinkpad t60p", None, None, "Lenovo")
	)
  
}
