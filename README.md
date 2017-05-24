# CustomCalendarView
Android custom calendar view - not quite forked from https://github.com/npanigrahy/Custom-Calendar-View

The npanigrahy/Custom-Calendar-View library, although the best I could find, has several problems
* It is slow starting, probably due to finding views using keys rather than ids
* If used in a dialog it can be very wide, especially in landscape, probably due to
  using `android:layout_width="match_parent"` rather than `wrap_content`
* Using a colour rather than a drawable for the selected entry background, so
  you can't use a rounded rectangle, for example, without providing a DayDecorator
* Defining a theme, which probably stops it using the app's theme

This is an attempt to fix some of the above without breaking it
