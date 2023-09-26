# Tap-Card-Android-SDK

We at [Tap Payments](https://www.tap.company/) strive to make your payments easier than ever. We as a PCI compliant company, provide you a from the self solution to process card payments in your Android app.

[![Platform](https://img.shields.io/badge/platform-Android-inactive.svg?style=flat)](https://tap-payments.github.io/goSellSDK-Android/)
[![Documentation](https://img.shields.io/badge/documentation-100%25-bright%20green.svg)](https://tap-payments.github.io/goSellSDK-Android/)
[![SDK Version](https://img.shields.io/badge/minSdkVersion-24-blue.svg)](https://stuff.mit.edu/afs/sipb/project/android/docs/reference/packages.html)
[![SDK Version](https://img.shields.io/badge/targetSdkVersion-33-informational.svg)](https://stuff.mit.edu/afs/sipb/project/android/docs/reference/packages.html)
[![SDK Version](https://img.shields.io/badge/latestVersion-0.0.20-informational.svg)](https://stuff.mit.edu/afs/sipb/project/android/docs/reference/packages.html)

# Demo
![Imgur](https://i.imgur.com/qLaQdN5.gif)

# Requirements

 1. We support from Android minSdk 24

# Get your Tap keys
You can always use the example keys within our example app, but we do recommend you to head to our [onboarding](https://register.tap.company/sell)  page. You will need to register your `package name` to get your `Tap Key` that you will need to activate our `Card SDK`.

# Installation

We got you covered, `TapCardSDK` can be installed with all possible technologies.

## Gradle

in project module gradle 

```kotlin
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```

Then get latest dependency  in your app module gradle
```kotlin
dependencies {
       implementation : 'com.github.Tap-Payments:Tap-Card-SDK-Android:{latest-tag-release}'
}
```



# Prepare input

## Documentation

To make our sdk as dynamic as possible, we do accept many configurations as input. Let us start by declaring them and explaining the structure and the usage of each.

```kotlin
/**

Creates a configuration model to be passed to the SDK

- Parameters:

	- publicKey: The Tap public key

	- scope: The scope of the card sdk. Default is generating a tap token

	- purpose: The intended purpose of using the generated token afterwards.
	- transaction: The transaction details

	- order: The tap order id

	- invoice: Link this token to an invoice

	- merchant: The Tap merchant details

	- customer: The Tap customer details

	- acceptance: The acceptance details for the transaction

	- fields: Defines the fields visibility

	- addons: Defines some UI/UX addons enablement

	- interface: Defines some UI related configurations

*/
```

|Configuration|Description | Required | Type| Sample
|--|--|--| --|--|
| publicKey| This is the `Tap Key` that you will get after registering you bundle id. | True  | String| `var publicKey:String = "pk_test_YhUjg9PNT8oDlKJ1aE2fMRz7"` |
| scope| Defines the intention of using the `TapCardSDK`. | True  | String| ` var scope:String = "Token" //This means you will get a Tap token to use afterwards` OR ` var scope:String = "Authenticate" //This means you will get an authenticated Tap token to use in our charge api right away`  |
| purpose| Defines the intention of using the `Token` after generation. | True  | String| ` var purpose:String = "PAYMENT_TRANSACTION" //Using the token for a single charge.` OR ` var purpose:String = "RECURRING_TRANSACTION" //Using the token for multiple recurring charges.` OR ` var purpose:String = "INSTALLMENT_TRANSACTION" //Using the token for a charge that is a part of an installement plan.` OR ` var purpose:String = "ADD_CARD" //Using the token for a save a card for a customer.` OR ` var purpose:String = "CARDHOLDER_VERIFICATION" //Using the token for to verify the ownership of the card.` |
| transaction| Needed to define the refrence id if you are generating an authenticated token. | False  | `Dictionry`| ` var transaction = HashMap(String,Any) transaction.put("reference","A reference to this transaciton in your system") ,transaction.put("metada",HashMapOf<String,Any>)` |
| order| This is the `Tap order id` that you created before and want to attach this token to it if any,also need to add amount , currency and description . | False  | `Dictionary`| `var order = HashMap<String, Any>() order.put("id","") order.put("amount",1),order.put("currency","SAR"),order.put("description","")` |
| invoice| This is the `invoice id` that you want to link this token to if any. | False  | `Dictionary`| ` var invoice = HashMap<String,Any>.put("id","")` |
| merchant| This is the `Merchant id` that you will get after registering you bundle id. | True  | `Dictionary`| ` var merchant = HashMap<String,Any>.put("id","")` |
| customer| The customer details you want to attach to this tokenization process. | True  | `Dictionary`| ` var customer =  HashMap<String,Any> ,customer.put("id,""), customer.put("nameOnCard","Tap Payments"),customer.put("editable",true),) var name :HashMap<String,Any> = [["lang":"en","first":"TAP","middle":"","last":"PAYMENTS"]] "contact":["email":"tap@tap.company", "phone":["countryCode":"+965","number":"88888888"]]] customer.put("name",name) , customer.put("contact",contact)` |
| acceptance| The acceptance details for the transaction. Including, which card brands and types you want to allow for the customer to tokenize. | False  | `Dictionary`| ` var acceptance = HashMap<String,Any> ,acceptance.put("supportedBrands",["AMERICAN_EXPRESS","VISA","MASTERCARD","OMANNET","MADA"]),acceptance.put("supportedCards",["CREDIT","DEBIT"])` |
| fields| Needed to define visibility of the optional fields in the card form. | False  | `Dictionary`| ` var fields = HashMap<String,Any> ,fields.put("cardHolder",true)` |
| addons| Needed to define the enabling of some extra features on top of the basic card form. | False  | `Dictionary`| ` var addons = HashMap<String,Any> , addons.put("displayPaymentBrands",true)  , addons.put("displayPaymentBrands",true) ,addons.put("loader",true) ,addons.put("scanner",true)` `/**- displayPaymentBrands: Defines to show the supported card brands logos - loader: Defines to show a loader on top of the card when it is in a processing state - scanner: Defines whether to enable card scanning functionality or not*/`|
| interface| Needed to defines look and feel related configurations. | False  | `Dictionary`| ` var interface = HashMap<String,Any> ,interface.put("locale","en"), interface.put("theme","light"), interface.put("edges","curved"), interface.put("direction","dynamic") // Allowed values for theme : light/dark. locale: en/ar, edges: curved/flat, direction:ltr/dynaimc` |
| post| This is the `webhook` for your server, if you want us to update you server to server. | False  | `Dictionary`| ` var post = HashMap<String,Any>.put("url","")` |

## Initialization of the input

### Initialize as a  Dictionary HashMap 
You can create a Dictionary HashMap to pass the data to our sdk. The good part about this, is that you can generate the data from one of your apis. Whenever we have an update to the configurations, you can update your api. This will make sure, that you will not have to update your app on the Google Play Store.

```kotlin

        /**
         * merchant
         */
        val merchant = HashMap<String,Any>()
        merchant.put("id","")

        /**
         * invoice
         */
        val invoice = java.util.HashMap<String,Any>()
        invoice.put("id","")
        /**
         * post
         */
        val post = java.util.HashMap<String,Any>()
        post.put("url","")

        /**
         * metadata
         */
        val metada = HashMap<String,Any>()
        metada.put("id","")


        /**
         * transaction
         */
        val transaction = HashMap<String,Any>()
        transaction.put("metadata",metadata)
        transaction.put("reference","refrence_id")
        /**
         * phone
         */
        val phone = HashMap<String,Any>()
        phone.put("countryCode","+20")
        phone.put("number","011")

        /**
         * contact
         */
        val contact = HashMap<String,Any>()
        contact.put("email","test@gmail.com")
        contact.put("phone",phone)
        /**
         * name
         */
        val name = HashMap<String,Any>()
        name.put("lang","en")
        name.put("first","Tap")
        name.put("middle","")
        name.put("last","Payment")

        /**
         * customer
         */
        val customer = HashMap<String,Any>()
        customer.put("nameOnCard","")
        customer.put("editable",true)
        customer.put("contact",contact)
        customer.put("name", listOf(name))




        /**
         * acceptance
         */
        val acceptance = HashMap<String,Any>()
        acceptance.put("supportedBrands", listOf("MADA","VISA","MASTERCARD","AMEX"))
        acceptance.put("supportedCards",listOf("CREDIT","DEBIT"))

        /**
         * fields
         */
        val fields = HashMap<String,Any>()
        fields.put("cardHolder",true)

        /**
         * addons
         */
        val addons = HashMap<String,Any>()
        addons.put("loader",true)
        addons.put("saveCard",true)
        addons.put("displayPaymentBrands",true)
        addons.put("scanner",true)
        addons.put("nfc",true)

        /**
         * order
         */
        val order = HashMap<String,Any>()
        order.put("id","order_id")
        order.put("amount","1")
        order.put("currency","SAR")
        order.put("description","description")

        /**
         * interface
         */
        val tapInterface = HashMap<String,Any>()
        tapInterface.put("locale","en")
        tapInterface.put("theme","light")
        tapInterface.put("edges","curved")
        tapInterface.put("direction","dynamic")

        /**
         * configuration request
         */

        val configuration = LinkedHashMap<String,Any>()
   
        configuration.put("merchant",merchant)
        configuration.put("transaction",transaction)
        configuration.put("order",order)
        configuration.put("invoice",invoice)
        configuration.put("post",post)
        configuration.put("purpose","PAYMENT_TRANSACTION")
        configuration.put("fields",fields)
        configuration.put("acceptance",acceptance)
        configuration.put("addons",addons)
        configuration.put("publicKey","pk_test_YhUjg9PNT8oDlKJ1aE2fMRz7")
        configuration.put("interface",interfacee) 
        configuration.put("scope","Authenticate") // or  configuration.put("scope","Token")
        configuration.put("customer",customer)

```

# Initializing the TapCardSDK form

##  First Step :  UI
-  add TapCardKit view to your xml  as follows :
```kotlin
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:orientation="vertical"
    android:layout_height="match_parent"
    tools:context=".main_activity.MainActivity">

 <company.tap.tapcardformkit.open.web_wrapper.TapCardKit
        android:id="@+id/tapCardForm"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        />

</LinearLayout>
```
- or programmatically through code  as follows :
 ```kotlin
       lateinit var tapCardKitView: TapCardKit

       override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) 


        val linearLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        /** create dynamic view of TapCardKit view **/ 
        tapCardKitView  = TapCardKit(this)
        tapCardKitView.layoutParams = linearLayoutParams
        /** refrence to parent layout view **/  
        this.findViewById<LinearLayout>(R.id.linear_layout).addView(tapCardKitView)


}
        
```


## Second Step : 2 - Code
```kotlin
     TapCardConfiguration.configureWithTapCardDictionaryConfiguration(
            this /** context **/,
            findViewById<TapCardKit>(R.id.tapCardForm) /** refrence to TapCardKit view **/,
            tapCardConfig /** pass already defiend HashMap Dictionary for TapConfiguration **/,
            this /** pass this of activity that will going to listen for callbacks  **/)

```


# TapCardStatusDelegate
A protocol that allows integrators to get notified from events fired from the `TapCardSDK`. 
```kotlin

interface TapCardStatusDelegate {


    fun onSuccess(data: String)
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

    fun onReady()
    /**
     *  Will be fired whenever the card is rendered and loaded
     */

    fun onFocus()
    /**
     *  Will be fired once the user focuses any of the card fields
     */

 
    fun onBindIdentification(data: String)
/** - Parameter data: will include the data in JSON format. example :
     *{
        "bin": "424242",
        "bank": "",
        "card_brand": "VISA",
        "card_type": "CREDIT",
        "card_category": "",
        "card_scheme": "VISA",
        "country": "GB",
        "address_required": false,
        "api_version": "V2",
        "issuer_id": "bnk_TS02A5720231337s3YN0809429",
        "brand": "VISA"
     }*     */

    fun onValidInput(isValid: String)
    /**
     *  Will be fired whenever the validity of the card data changes.
     *  Parameter isValid: Will be true if the card data is valid and false otherwise.
     */


    fun onError(error: String)=
   /**
     *  Will be fired whenever there is an error related to the card connectivity or apis
     *  Parameter data: includes a JSON format for the error description and error
     */

    fun onHeightChange(heightChange:String)
   /**
     *  Will be fired whenever the card element changes its height for your convience
     *   Parameter height: The new needed height
     */

}


```

# Tokenize the card

Once you get notified that the `TapCardView` now has a valid input from the delegate. You can start the tokenization process by calling the public interface:

```kotlin
///  Wil start the process of generating a `TapToken` with the current card data
            findViewById<TapCardKit>(R.id.tapCardForm).generateTapToken()
```
