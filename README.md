# Fabric Plugin for Cordova > 3.0 (iOS and Android)

## Installation

1) Make sure that you have [Node](http://nodejs.org/) and [Cordova CLI](https://github.com/apache/cordova-cli) or [PhoneGap's CLI](https://github.com/mwbrooks/phonegap-cli) installed on your machine.

2) ***(Important)*** Add Fabric SDK to the Android / iOS project [Fabric.io](https://fabric.io)

3) Follow the steps to setup the SDK in your project.

4) Add a plugin to your project using Cordova CLI:

```bash
cordova plugin add https://github.com/jorisroling/cordova-fabric
```

Or using PhoneGap CLI:

```bash
phonegap local plugin add https://github.com/jorisroling/cordova-fabric
```

## Modules

- Core  
> This module is intended to contain the core and generic features

```
window.fabric.core
```

- Answers  
> This module contains the Crashlytics Answers API. This is not yet complete but has all the mayor features  

```
window.fabric.Answers
```

- Crashlytics
> This module has the Crashlytics features  

```
window.fabric.Crashlytics
```

## Usage

### Answers

#### Example

```
// Register a successful login
window.fabric.Answers.sendLogIn();
```

### Methods

#### sendLogIn()  
Sends a login event  

#### sendSignUp()  
Sends a SignUp event  

#### sendContentView(name, type, id, attributes)  
Sends a Content View event with the parameters:  
- Name: name of View  
- type: Type of View  
- id: the ID of the View  
- attributes: Object containing extra attributes to be set in the event ( Must be an Object )  

#### sendScreenView(name, id, attributes)  
Sends a Content View with Screen parameter in the type. Required parameters:  
- Name: name of View  
- id: the ID of the View  
- attributes: Object containing extra attributes to be set in the event ( Must be an Object )  

#### sendCustomEvent(name, attributes)  
Sends a Custom Event. Required parameters:  
- Name: name of View  
- attributes: Object containing custom information to be set in the event ( Must be an Object )  


### Crashlytics

```js
function sendCrashWithData() {
	window.fabric.Crashlytics.setUserIdentifier('TheIdentifier');
    window.fabric.Crashlytics.setUserName('My Name');
    window.fabric.Crashlytics.setUserEmail('some@example.com');

    window.fabric.Crashlytics.setStringValueForKey('MyString', 'stringkey');
    window.fabric.Crashlytics.setIntValueForKey(200, 'intkey');
    window.fabric.Crashlytics.setBoolValueForKey(true, 'boolkey');
    window.fabric.Crashlytics.setFloatValueForKey(1.5, 'floatkey');

    window.fabric.Crashlytics.addLog('This my a log message from JS!');
    window.fabric.Crashlytics.addLog('This is another log message from JS!');
    window.fabric.Crashlytics.sendCrash();
}
```

### Methods

#### setUserIdentifier(value)
Set the user identifier value

#### setUserName(value) - iOS, Android
Set the username

#### setUserEmail(value) - iOS, Android
Set the user email

#### setStringValueForKey(value, key) - iOS, Android
Set String value for key

#### setIntValueForKey(value, key) - iOS, Android
Set integer value for key

#### setBoolValueForKey(value, key) - iOS, Android
Set boolean for key

#### setFloatValueForKey(value, key) - iOS, Android
Set float for key

#### addLog(value) - iOS, Android
Add log for the crash.

#### sendCrash() - iOS, Android
Send a (fatal) crash to the backand of CrashLytics.

## Changes

- Javascript code moved to ES6  
- Separated in modules  
- Full support for both OSs with same API  

## ToDo  

- Implement missing events from Answers API
- Move to the entire Fabric API instead of Just Crashlytics

## AUTHORS

- Román A. Sarria  
- Based on Crashlytics plugin: https://github.com/francescobitmunks/cordova-plugin-crashlytics
