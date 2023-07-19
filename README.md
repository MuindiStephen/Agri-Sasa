# Shamba App
## 
- Shamba App is an AgriTech Android app with the following solution features for the farmer:)
  - Hire Farm Equipments Feature
  - Apply and Pay for Crop Insurance
  - Access to Online farm content(Knowledge) Hosted on Cloud
  - Chat With Farm Experts
  - A GPS to monitor their farms (Smart Digital)
  - Track nearby AgriTech Companies
  - Predict Their farm production via an ```integrated Machine Learning Algorithm (Linear regression)```
  - Reach ready market for their produce to maximise their profits


## Screens
<p float="left">
   <img src="art/splash.png" width="30%" />
   <img src="art/mainauth.png" width="30%" />
   <img src="art/mainlogin.png" width="30%" />
   <img src="art/emailregister.png" width="30%" />
   <img src="art/phoneregister.png" width="30%" />
   <img src="art/emaillogin.png" width="30%" />
   <img src="art/homepage.png" width="30%"/>
   <img src="art/insurance.png" width="30%"/>
   <img src="art/paymentoptions.png" width="30%"/>
   <img src="art/transactionhistory.png" width="30%"/>
   <img src="art/mpesa.png" width="30%"/>
   <img src="art/successpayment.png" width="30%"/>
   <img src="art/successpayment.png" width="30%"/>
   <img src="art/newfarmingtechnology.png" width="30%"/>
   <img src="art/map.png" width="30%" />
   <img src="art/gps.png" width="30%" />
   <img src="art/agritechmap.png" width="30%" />
   <img src="art/trackagritech.png" width="30%" />
   <img src="art/alerts.png" width="30%" />
   <img src="art/predictfarm1.png" width="30%" />
   <img src="art/predictfarm2.png" width="30%" />
   <img src="art/marketproduce.png" width="30%" />
   <img src="art/hirefarmequipments.png" width="30%" />
   <img src="art/checkoutequipment.png" width="30%" />
   <img src="art/approvedequipment.png" width="30%" />
   <img src="art/alerts.png" width="30%" />

</p>



##
> NOTE: other screens coming soon.

## Tech Stack & Concepts
- 
    * [Kotlin](https://kotlinlang.org/) - a cross-platform, statically typed, general-purpose programming language with type inference.
    * [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - perform background operations.
    * [Flow](https://kotlinlang.org/docs/reference/coroutines/flow.html) - handle the stream of data asynchronously that executes sequentially.
    * [Dagger hilt](https://dagger.dev/hilt/) - a pragmatic lightweight dependency injection framework.
    - [Logging Interceptor](https://github.com/square/okhttp/blob/master/okhttp-logging-interceptor/README.md) -  logs HTTP requests and responses data messages and lines and their headers based on their level
    - [MVVM](https://developer.android.com/topic/architecture)- MVVM stands for Model, View, ViewModel. Model: This holds the data of the application. It cannot directly talk to the View. Generally, it's recommended to expose the data to the ViewModel through Observables.
        - [Room database](https://developer.android.com/training/data-storage/room) - The Room persistence library provides an abstraction layer over SQLite to allow fluent database access while harnessing the full power of SQLite.
   - [Kotlin Coroutines](https://developer.android.com/kotlin/coroutines) - A concurrency design pattern that you can use on Android to simplify code that executes asynchronously.
   - [Retrofit](https://square.github.io/retrofit) -  Retrofit is a REST client for Java/ Kotlin and Android by Square inc under Apache 2.0 license. Its a simple network library that is used for network transactions. By using this library we can seamlessly capture JSON response from web service/web API.
  - [GSON](https://github.com/square/gson) - JSON Parser,used to parse requests on the data layer for Entities and understands Kotlin non-nullable and default parameters.
  - [Network bound resource]() - An algorithm that provides an easy function to fetch resource from both database and network
    * [Jetpack](https://developer.android.com/jetpack)
        * [Room](https://developer.android.com/topic/libraries/architecture/room) - a persistence library provides an abstraction layer over SQLite.
        * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - is an observable data holder.
        * [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) - perform action when lifecycle state changes.
        * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - store and manage UI-related data in a lifecycle conscious way.

- Lottie Animations


## Payment Integration Gateways
 * [Mpesa-Daraja-API](http://developer.safaricom.co.ke/)

## DevOps Tool
- Circle CI 


```
MIT License

Copyright (c) 2023 Stephen Muindi

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```




