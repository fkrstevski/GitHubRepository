# GitHubRepository

LibGDX - set up
--------------------------

To create new project
- Run gdx-setup.jar


To open new project
- Import project (build.gradle) into Android Studio

To Add Desktop target
- Run -> Edit Configurations...
- click the plus (+) button and select Application
- Set the Name to Desktop
- Set the field "Use classpath of module" to desktop
- Click on the button of the Main class field and select the DesktopLauncher class. 
- Set the Working directory to your android/assets/ (or your_project_path/core/assets/) folder! 
- Click Apply and then OK.

To Add iOS target
- Run -> Edit Configurations...
- click the plus (+) button and select Gradle
- Set the Name to iOS
- Select ios for the Gradle Project
- Set the Tasks to launchIPhoneSimulator or launchIPadSimulator or launchIOSDevice
- Click Apply and then OK.

To Change iOS simulator
- Open terminal
- Navigate to where the IOSLauncher.app is
- xcrun instruments -s
- lists all device types
- xcrun instruments -w "iPhone 5 (8.0 Simulator)"
- launch simulator
- xcrun simctl install booted IOSLauncher.app 
- install the app on the simulator
- xcrun simctl launch booted <app identifier or bundle id>
- launch the app

Try this for iOS simulators
"./gradlew -Probovm.arch=x86_64 -Probovm.device.name=iPhone-5s launchIPhoneSimulator"


Testing for iOS
--------------------------

1. Need to create a distribution certificate
2. Need to create a distribution mobile provision
3. Need to create IPA
- Open RoboVM
- Update Info.plist.xml
  - add (for iPad multitasking)
        <key>UIRequiresFullScreen</key>
        <true/>
  - add (for icons)
        <string>Icon-76</string>
        <string>Icon-120</string>
        <string>Icon-152</string>
- from the main menu "Build" --> "Create IPA"
- make sure you use the distribution certificate and distribution provision (develop will not work)
4. Open "Application Loader"
- Double click on "Deliver Your App"
- Select the generated IPA, and follow the prompts
5. Add the app on iTunes Connect
- In the TestFlight tab, select the build to test and add testers

New version
- Update version number in iTunesConnect (from 1.0 to 1.1 for example)
- Update robovm.properties version number (from 1.0 to 1.1 for example) 

