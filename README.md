# Tap-Card-Android-SDK

We at [Tap Payments](https://www.tap.company/) strive to make your payments easier than ever. We as a PCI compliant company, provide you a from the self solution to process card payments in your Android app.

[![Platform](https://img.shields.io/badge/platform-Android-inactive.svg?style=flat)](https://tap-payments.github.io/goSellSDK-Android/)
[![Documentation](https://img.shields.io/badge/documentation-100%25-bright%20green.svg)](https://tap-payments.github.io/goSellSDK-Android/)
[![SDK Version](https://img.shields.io/badge/minSdkVersion-24-blue.svg)](https://stuff.mit.edu/afs/sipb/project/android/docs/reference/packages.html)
[![SDK Version](https://img.shields.io/badge/targetSdkVersion-33-informational.svg)](https://stuff.mit.edu/afs/sipb/project/android/docs/reference/packages.html)
[![SDK Version](https://img.shields.io/badge/latestVersion-0.0.19-informational.svg)](https://stuff.mit.edu/afs/sipb/project/android/docs/reference/packages.html)

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

	- merchant: The Tap merchant details

	- transaction: The transaction details

	- authenticate: The authentication data, needed only if scope is set to Authenticate

	- customer: The Tap customer details

	- acceptance: The acceptance details for the transaction

	- fields: Defines the fields visibility

	- addons: Defines some UI/UX addons enablement

	- interface: Defines some UI related configurations

*/
```

|Configuration|Description | Required | Type| Sample
|--|--|--| --|--|
| publicKey| This is the `Tap Key` that you will get after registering you package name. | True  | String| `var publicKey:String = "pk_test_YhUjg9PNT8oDlKJ1aE2fMRz7"` |
| scope| Defines the intention of using the `TapCardSDK`. | True  | `Scope` enum| ` var scope:Scope = Scope.Token //This means you will get a Tap token to use afterwards` OR ` var scope:Scope = Scope.Authenticate //This means you will get an authenticated Tap token to use in our charge api right away`  |
| merchant| This is the `Merchant id` that you will get after registering you package name. | True  | `Merchant`| ` var merchant:Merchant = Merchant(id= "")` |
| transaction| Needed to define the amount and the currency, if you are generating an authenticated token. | False  | `Transaction`| ` var transaction:Transaction = Transaction(amount= 1, currency= "SAR")` |
| authentication| If you want to generate an authenticated token, which allows you to perform charges without any further 3DS in our [Charge api](https://developers.tap.company/reference/create-a-charge). Moreover, please create an order first, with our Order api to get `Order id` & `Transaction id` | False  | `Authentication`| ` var authentication:Authentication = Authentication(description= "Authentication description", metadata:HashMap = metadata.put("utf1","data"), reference: Reference(transaction=  "Your transaction id", order= "Your order id"), invoice: Invoice(id= "If have an invoice id to attach"), authentication: AuthenticationClass(), post: Post(url="Your server webhook if needed"))` |
| customer| The customer details you want to attach to this tokenization process. | True  | `Customer`| ` var customer:Customer = Customer(id= "If you have a tap customer id", name= [Name(lang: "en", first: "Tap", last: "Payments", middle: "")], nameOnCard= "Tap Payments", editable= **true**, contact: Contact(email= "tappayments@tap.company", phone= Phone(countryCode= "+965", number= "88888888")))` |
| acceptance| The acceptance details for the transaction. Including, which card brands and types you want to allow for the customer to tokenize. | False  | `Acceptance`| ` var  acceptance:Acceptance = Acceptance(supportedBrands=["AMERICAN_EXPRESS","VISA","MASTERCARD","OMANNET","MADA", "MEEZA"], supportedCards= ["CREDIT","DEBIT"])` |
| fields| Needed to define visibility of the optional fields in the card form. | False  | `Fields`| ` var fields:Fields = Fields(cardHolder= true)` |
| addons| Needed to define the enabling of some extra features on top of the basic card form. | False  | `Addons`| ` var addons:Addons = Addons(displayPaymentBrands= true, loader= true,scanner= true)` `/**- displayPaymentBrands: Defines to show the supported card brands logos - loader: Defines to show a loader on top of the card when it is in a processing state - scanner: Defines whether to enable card scanning functionality or not*/`|
| interface| Needed to defines look and feel related configurations. | False  | `Interface`| ` var  interface:Interface = Interface(locale= "en", theme= "light", edges= "curved", direction= "dynamic")` |


## Initialization of the input

### Initialize as a model
You can create a model from our defined structure to pass it afterwards to our `TapCardSDK` as a configuration.
```kotlin
 val tapCardConfig = TapCardConfigurations(
            scope = Scope.Token /** or Scope.Authenticate **/,
            publicKey = "pk_test_YhUjg9PNT8oDlKJ1aE2fMRz7",
            merchant = Merchant(id = ""),
            transaction = Transaction(
                amount = 1,
                currency = "SAR"
            ),
            customer = Customer(
                nameOnCard =  "test",
                editable = true,
                contact = Contact(
                    email =  "test@gmail.com",
                    phone = Phone( "+965",
                        number =  "88888888"
                    )
                ),
                name = mutableListOf<Name>(
                    Name(
                        lang = "en",
                        first =  "Tap",
                        last = "Payments",
                        middle = ""
                    )
                )
            ),
            acceptance = Acceptance(
                supportedBrands = mutableListOf(CardBrand.americanExpress,CardBrand.visa,CardBrand.masterCard),
                supportedCards =  ["CREDIT","DEBIT"]
            ),
            addons = Addons(
                loader = true,
                saveCard = true,
                displayPaymentBrands = true,
                scanner = true,
                nfc = true
            ),
            tapCardConfigurationInterface = `interface`(
                locale = "en",
                theme = "light",
                edges = "curved",
                direction = "dynamic",
            ),
            fields = Fields(cardHolder = true),
            authentication = TapAuthentication(
                description =  "Authentication description",
                reference =  Refrence(
                    transaction = "transaction id",
                    order = "order id"
                ),
                invoice = Invoice(id = "if have an invoice id to attach"),
                authentication = Authentication(
                        channel = "PAYER_BROWSER",
                        purpose = "PAYMENT_TRANSACTION"
                    ),
                post =  Post(url = "Your server webhook if needed")
            )

        )
```

### Initialize as a  Dictionary HashMap 
You can create a Dictionary HashMap to pass the data to our sdk. The good part about this, is that you can generate the data from one of your apis. Whenever we have an update to the configurations, you can update your api. This will make sure, that you will not have to update your app on the Google Play Store.

```kotlin

        /**
         * merchant
         */
        val merchant = HashMap<String,Any>()
        merchant.put("id","")
        /**
         * transaction
         */
        val transaction = HashMap<String,Any>()
        transaction.put("amount","1")
        transaction.put("currency","SAR")
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
         * refrence
         */
        val refrence = HashMap<String,Any>()
        refrence.put("transaction","tck_LVL8sXysVSXfSgG0SFkPhQO1Gi")
        refrence.put("order","695646918101292112")

        /**
         * auth chanel
         */
        val auth = HashMap<String,Any>()
        auth.put("channel","PAYER_BROWSER")
        auth.put("purpose","PAYMENT_TRANSACTION")

        /**
         * invoice
         */
        val invoice = HashMap<String,Any>()
        invoice.put("id","")
        /**
         * post
         */
        val post = HashMap<String,Any>()
        post.put("id","")

        /**
        * authentication 
        */
        val authentication = HashMap<String,Any>()
        authentication.put("description","description")
        authentication.put("reference",refrence)
        authentication.put("invoice",invoice)
        authentication.put("authentication",auth)
        authentication.put("post",post)

        /**
         * interface
         */
        val tapInterface = HashMap<String,Any>()
        tapInterface.put("locale","en")
        tapInterface.put("theme","light")
        tapInterface.put("edges","curved")
        tapInterface.put("direction","ltr")

        /**
         * configuration request
         */

        val configuration = HashMap<String,Any>()
        configuration.put("acceptance",acceptance)
        configuration.put("publicKey","pk_test_Vlk842B1EA7tDN5QbrfGjYzh")
        configuration.put("merchant",merchant)
        configuration.put("transaction",transaction)
        configuration.put("customer",customer)
        configuration.put("interface",tapInterface)
        configuration.put("addons",addons)
        configuration.put("fields",fields)
        configuration.put("scope","Authenticate")  // or  configuration.put("scope","Token") 
        configuration.put("authentication",authentication)

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

        val linearLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        /** create dynamic view of TapCardKit view **/ 
        val tapCardKitView  = TapCardKit(this)
        tapCardKitView.layoutParams = linearLayoutParams
        /** refrence to parent layout view **/  
      this.findViewById<LinearLayout>(R.id.linear_layout).addView(tapCardKitView)
```


## Second Step : 2 - Code
```kotlin
 if Model configuration : 
     TapCardConfiguration.configureWithTapCardModelConfiguration(
            this /** context **/,
            findViewById<TapCardKit>(R.id.tapCardForm) /** refrence to TapCardKit view **/,
            tapCardConfig /** pass already defiend model for TapConfiguration **/,
            this /** pass this of activity that will going to listen for callbacks  **/)

 if HashMapConfiguation configuration : 
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
