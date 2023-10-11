# Android

## Needed Setup
### Add repositories
please add the following repositories to your Gradle file.
```script
repositories {
    google()
    mavenCentral()
    jcenter()
    maven { url '[https://jitpack.io](https://jitpack.io/)' }
}
```

Note, that if you are using the latest Android studio you will have to add this in setting.gradle like shown here:

![enter image description here](https://i.ibb.co/ZY544BN/Screenshot-2023-10-11-at-6-14-58-AM.png)

Or, if you are on older Android studio version, they will be added in project's build.gradle like shown here:
![enter image description here](https://i.ibb.co/BP6kNsy/Screenshot-2023-10-11-at-6-15-11-AM.png)



### Adding Card-Android dependency 

You will need to add `Card-Android` as a dependency in your apps' build.gralde like shown here:
```script
dependencies {
    implementation 'com.github.Tap-Payments:Card-Android:0.0.33'
}
```

![enter image description here](https://i.ibb.co/n348vqq/Screenshot-2023-10-11-at-6-18-33-AM.png)




### Adding required permissions in app's manifest:

You will need to add these permissions in your `application` tag inside your manifest file.
1. You will minimum need to add INTERNET permission.
2. Optionally, you may need to add Camera access permission, if you are willing to use the card scanning feature.
```script
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="CAMERA" />    
```
![enter image description here](https://i.ibb.co/4drFcdZ/Screenshot-2023-10-11-at-1-39-30-PM.png)

### Supporting for non AndroidX
At some project's configurations if AndroidX is enabled, you will have to add this line in `gradle.properties` file as shown:
```script
android.enableJetifier=true
```
![enter image description here](https://i.ibb.co/Kxj0rdX/Screenshot-2023-10-11-at-6-34-51-AM.png)


## Utilizing the Card-Android SDK:
You have the ability to create the UI part of the Card-Android by adding it as normal widget in your layout.xml or fully create it by code. Below we will describe both flows:

1. We will have to reach a point of creating a variable of type TapCardKit. This can be created in one of two ways:
	2. Created by XML and then link it to a variable in code.
	3. Created totally within the code.
2. Once you create the variable in any way, you will have to follow these steps:
	3. Create the parameters.
	4. Pass the parameters to the variable.
	5. Implement TapCardStatusDelegate interface, which allows you to get notified by different events fired from within the Card SDK.
3. Start tokenizing a card on demand.

### Creating the TapCardKit using  xml
When you create a new project, Android studio creates for you `activity_main.xml`, where it shows a default blank screen.
Please select the file, as displayed:
![enter image description here](https://i.ibb.co/M9F9Hcj/Screenshot-2023-10-11-at-6-27-43-AM.png)

#### Add Card Form widget into your layout as follows:
```xml
<company.tap.tapcardformkit.open.web_wrapper.TapCardKit
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:id="@+id/tapCard"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
```

please note, that height is set to `wrap_content`, to allow our card to expand and shrink based on the scenario. Like, showing/hiding the card name, etc.
![enter image description here](https://i.ibb.co/R3vwsFM/Screenshot-2023-10-11-at-6-38-42-AM.png)


#### Accessing the Card From created in XML in your code
First, go ahead to your `MainActivity` class.
Second, you will have to create a class variable that will be linked afterwards to the widget we created in the xml above. 
```kotlin
/// A reference to link to the XML view created
lateinit var tapCardForm: TapCardKit
```

Then, in your onCreate method, let us link the created variable to the ui widget inside our XML
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
	super.onCreate(savedInstanceState)
	setContentView(R.layout.activity_main)
	/// Here we attach the variable to the created view in the xml by providing the same id
	/// we hade inside the xml view we created above
	tapCardForm =  findViewById<TapCardKit>(R.id.tapCard)
	/// Let us create the parameters to configure our card form
	configureCardForm()
}
```


### Creating the TapCardKit from code
#### Changes needed in XML default layout file
When you generate a new project, Android studio creates a default layout file `activity_main.xml`, we will have to open it up and add an `id`parameter for the parent layout so we can reference the layout and add to it, our created TapCardKit widget. We have used in this sample, `LineaerLayout` for simplicity.

```kotlin
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="[http://schemas.android.com/apk/res/android](http://schemas.android.com/apk/res/android)"
    android:layout_width="match_parent"
    android:orientation="vertical"
    android:id="@+id/linearparent"
    android:layout_height="match_parent">

</LinearLayout>
```

![enter image description here](https://i.ibb.co/GQb7GHx/Screenshot-2023-10-11-at-6-57-00-AM-1.png)

#### Create the TapCardKit from your code

Then head to the auto generated file `MainActivity.kt` and do the following:
Create a class variable as follows
```kotlin
/// A reference to link to the XML view created
lateinit var tapCardForm: TapCardKit
```

Then, you will create and inflate the variable in the auto created `onCreate` function:
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
        /// We will have to create the layout parameters (widht & height) by code now
            val linearLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )
// Apply the layout parameters we configured to our varibale. Preparing it to be correctly inflated
            tapCardForm  = TapCardKit(this)
            tapCardForm.layoutParams = linearLayoutParams
// Now you need to add the widget to the layout, using the id we created in the last step
            this.findViewById<LinearLayout>(R.id.linearparent).addView(tapCardForm)
            	/// Let us create the parameters to configure our card form
			configureCardForm()
}
```

![enter image description here](https://i.ibb.co/r0tHgzr/Screenshot-2023-10-11-at-7-06-37-AM.png)


### Configuring the Card-Android SDK
After creating the UI using any of the previously mentioned ways, it is time to pass the parameters needed for the sdk to work as expected and serve your need correctly.

#### Creating the parameters
To allow flexibility and to ease the integration, your application will only has to pass the parameters as a `HashMap<String,Any>()` .

First, let us create the required parameters
```kotlin
val configuration = LinkedHashMap<String,Any>()

        /**
         * operator
         */
        val operator = HashMap<String,Any>()
        operator.put("publicKey","")
        /**
         * order
         */
        val order = HashMap<String,Any>()
        order.put("id", "")
        order.put("amount", "1")
        order.put("currency","SAR")
        order.put("description","")
        order.put("reference","")
       /**
         * phone
         */
        val phone = java.util.HashMap<String,Any>()
        phone.put("countryCode","+20")
        phone.put("number","011")

        /**
         * contact
         */
        val contact = java.util.HashMap<String,Any>()
        contact.put("email","test@gmail.com")
        contact.put("phone",phone)
        /**
         * name
         */
        val name = java.util.HashMap<String,Any>()
        name.put("lang","en")
        name.put("first", "first")
        name.put("middle", "middle")
        name.put("last",  "last")
        /**
         * customer
         */
        val customer = java.util.HashMap<String,Any>()
        customer.put("nameOnCard", "test")
        customer.put("editable",true)
        customer.put("contact",contact)
        customer.put("name", listOf(name))
        
        configuration.put("operator",operator)
        configuration.put("scope","Authenticate")
        configuration.put("order",order)
        configuration.put("customer",customer)
```

Second, we will have to pass these parameters to the created Card Form variable before as follows
```kotlin
/// This static interface is provided publicly from the Card-Android SDK to pass the parameters. It takes 4 parameters
/// 1. Conext. Which is your building context of the current activity
/// 2. The view of type CardKit we created before.
/// 3. The parameters we created before.
/// 4. A class that implements TapCardStatusDelegate (more on this later)
TapCardConfiguration.configureWithTapCardDictionaryConfiguration(
            this,
           tapCardForm,
            configuration,this)
```

Full code snippet for creating the parameters + passing it TapCardKit variable
```kotlin
/// A sample function showing how to integrate and configure the TapCardKit with minimal data required
fun configureCardForm()  {
// optionally call this function if you want to use the scanner functionality before configuring the card form.
		checkAndRequestPermissions()
		val configuration = LinkedHashMap<String,Any>()

        /**
         * operator
         */
        val operator = HashMap<String,Any>()
        operator.put("publicKey","")
        /**
         * order
         */
        val order = HashMap<String,Any>()
        order.put("id", "")
        order.put("amount", "1")
        order.put("currency","SAR")
        order.put("description","")
        order.put("reference","")
       /**
         * phone
         */
        val phone = java.util.HashMap<String,Any>()
        phone.put("countryCode","+20")
        phone.put("number","011")

        /**
         * contact
         */
        val contact = java.util.HashMap<String,Any>()
        contact.put("email","test@gmail.com")
        contact.put("phone",phone)
        /**
         * name
         */
        val name = java.util.HashMap<String,Any>()
        name.put("lang","en")
        name.put("first", "first")
        name.put("middle", "middle")
        name.put("last",  "last")
        /**
         * customer
         */
        val customer = java.util.HashMap<String,Any>()
        customer.put("nameOnCard", "test")
        customer.put("editable",true)
        customer.put("contact",contact)
        customer.put("name", listOf(name))
        
        configuration.put("operator",operator)
        configuration.put("scope","Authenticate")
        configuration.put("order",order)
        configuration.put("customer",customer)

		TapCardConfiguration.configureWithTapCardDictionaryConfiguration(
            this,
           tapCardForm,
            configuration,this)
```


```kotlin
/// optionally call this function if you want to use the scanner functionality before configuring the card form.
fun checkAndRequestPermissions(): Boolean {
    val camera = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.CAMERA
    )
    val listPermissionsNeeded: MutableList<String> = ArrayList()
    if (camera != PackageManager.PERMISSION_GRANTED) {
        listPermissionsNeeded.add(Manifest.permission.CAMERA)
    }
    if (!listPermissionsNeeded.isEmpty()) {
        ActivityCompat.requestPermissions(
            this,
            listPermissionsNeeded.toTypedArray(),
            REQUEST_ID_MULTIPLE_PERMISSIONS
        )
        return false
    }
    return true
}
```

### Implementing TapCardStatusDelegate interface
Now we have created the UI + the parameters we need to to correctly display Tap card form. For the best experience, your class will have to implement `TapCardStatusDelegate` interface, which is a set of optional callbacks, that will be fired based on different events from within the card form. This will help you in deciding the logic you need to do upon receiving each event.

So, we get back to our `MainActivity.tk` and head to the class declaration line and we add `TapCardStatusDelegate` and then override the required callbacks as follows:

```kotlin

 /// Will be fired whenever there is an error related to the card connectivity or apis
    /// - Parameter data: includes a JSON format for the error description and error
override fun onError(error: String) {
        Log.i("log",error)
    }
/**
 Will be fired whenever the card sdk finishes successfully the task assigned to it. Whether `TapToken` or `AuthenticatedToken`
 - Parameter data: will include the data in JSON format. For `TapToken`:
 {
 "id": "tok_MrL97231045SOom8cF8G939",
 "created": 1694169907939,
 "object": "token",
 "live_mode": false,
 "type": "CARD",
 "source": "CARD-ENCRYPTED",
 "used": false,
 "card": {
 "id": "card_d9Vj7231045akVT80B8n944",
 "object": "card",
 "address": {},
 "funding": "CREDIT",
 "fingerprint": "gRkNTnMrJPtVYkFDVU485Gc%2FQtEo%2BsV44sfBLiSPM1w%3D",
 "brand": "VISA",
 "scheme": "VISA",
 "category": "",
 "exp_month": 4,
 "exp_year": 24,
 "last_four": "4242",
 "first_six": "424242",
 "name": "AHMED",
 "issuer": {
 "bank": "",
 "country": "GB",
 "id": "bnk_TS07A0720231345Qx1e0809820"
 }
 },
 "url": ""
 }
 */
    override fun onSuccess(data: String) {
        Log.i("log",data)

    }
/**
 *  Will be fired whenever the validity of the card data changes.
 *  Parameter isValid: Will be true if the card data is valid and false otherwise.
 */
    override fun onValidInput(isValid: String) {
        Log.i("log",isValid)
    }
```


So at the end, your `MainActivity.kt` should look like this:
![enter image description here](https://i.ibb.co/cJYq3rP/Screenshot-2023-10-11-at-7-26-17-AM.png)


### Tokenizing the card
The Card-Adroid sdk provides a public interface that allows you to instruct it to start the tokenization process on demand. Whenever you see convenient, in your logic flow. As a guidance we would only recommend calling this interface after getting `onValidInput` callback as described above. Also, once you correctly trigger the interface, you should expect to hear back from the SDK in one of two callbacks, `onSuccess` or `onError`.

```kotlin
/// In this example, we will assume that you want to generate the token, once the user fills in correct and valid card data
override fun onValidInput(isValid: String) {
        Log.i("log", isValid)
        when (isValid.toBoolean()) {
            true -> tapCardForm.generateTapToken()
            false -> {}
        }

    }
```

So ideally the flow should be:
1. Create the view.
2. Configure the view.
3. Implement the interface.
4. Get onValid callback with true.
5. Call the tokenize interface.
6. Listen to onSuccess & onError callbacks.


At the end, your `MainAcitivyt.kt` file should look like that
```kotlin
package com.example.firstproject

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import company.tap.tapcardformkit.open.DataConfiguration
import company.tap.tapcardformkit.open.TapCardStatusDelegate
import company.tap.tapcardformkit.open.web_wrapper.TapCardConfiguration
import company.tap.tapcardformkit.open.web_wrapper.TapCardKit
import java.util.ArrayList

class MainActivity : AppCompatActivity(), TapCardStatusDelegate {
    lateinit var tapCardForm: TapCardKit
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 7

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkAndRequestPermissions()

        val linearLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        /** create dynamic view of TapCardKit view **/
        tapCardForm = TapCardKit(this)
        tapCardForm.layoutParams = linearLayoutParams
        /** refrence to parent layout view **/
        this.findViewById<LinearLayout>(R.id.linearparent).addView(tapCardForm)

        configureCardForm()

    }

    fun configureCardForm() {





        /**
       

        /**
         * phone
         */
        val phone = java.util.HashMap<String, Any>()
        phone.put("countryCode", "+20")
        phone.put("number", "011")

        /**
         * contact
         */
        val contact = java.util.HashMap<String, Any>()
        contact.put("email", "test@gmail.com")
        contact.put("phone", phone)
        /**
         * name
         */
        val name = java.util.HashMap<String, Any>()
        name.put("lang", "en")
        name.put("first", "first")
        name.put("middle", "middle")
        name.put("last", "last")



    
      
        val configuration = LinkedHashMap<String, Any>()

        /**
         * operator
         */
        val operator = HashMap<String, Any>()
        operator.put("publicKey", "pk_test_YhUjg9PNT8oDlKJ1aE2fMRz7")
        /**
         * order
         */
        val order = HashMap<String, Any>()
        order.put("id", "")
        order.put("amount", "1")
        order.put("currency", "SAR")
        order.put("description", "")
        order.put("reference", "")

        /**
         * customer
         */
        val customer = java.util.HashMap<String, Any>()
        customer.put("nameOnCard", "test")
        customer.put("editable", true)
        customer.put("contact", contact)
        customer.put("name", listOf(name))

        configuration.put("operator", operator)
        configuration.put("scope", "Authenticate")
        configuration.put("order", order)
        configuration.put("customer", customer)

        TapCardConfiguration.configureWithTapCardDictionaryConfiguration(
            this,
            tapCardForm,
            configuration, this
        )
    }

    private fun checkAndRequestPermissions(): Boolean {
        val camera = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        )
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }


    override fun onError(error: String) {
        Log.i("log", error)
    }

    override fun onSuccess(data: String) {
        Log.i("log", data)

    }


    override fun onValidInput(isValid: String) {
        Log.i("log", isValid)
        when (isValid.toBoolean()) {
            true -> tapCardForm.generateTapToken()
            false -> {}
        }

    }

}
```



