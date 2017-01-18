# Crossover - Bike Rental - Android Architecture Position

This project is made as a take-home coding exercise for Crossover Android Mobile Architecture Position.

## Exercise Description

The Saitama prefecture wants to encourage the local people as well as tourists to use bicycles for transportation. Cycling has many benefits, e.g decreasing stress, improve mobility, reduce anxiety, increase cardiovascular fitness and so on. You are responsible for creating a mobile application to connect to Jitensha Saitama REST service, providing an amazing experience to your users.

# Functional Specifications

You must to create an application following ALL the requirements below:

1. The app allows the registration of new users:
  1. Email
  2. Password
2. The app allows login of existing users:
  1. Email
  2. Password
3. The app must get all available bicycle rental places near Saitama prefecture.
4. The app must present these places within a mobile map provider, showing only their names and marks:
  1. Name.
5. The app is integrated with a map provider, possible solutions:
  1. Google Maps (preferable)
  2. ArcGIS
6. The user can rent a bike just by touching one of the available places and select Rent
7. The user must provide his/her credit card information and send it to Saitama service to complete the operation.

# Non Functional Specifications:

1. The access token never expires, but it must be stored locally for next access.
2. The application must handle possible internet connectivity issues.
3. The sensitive data must be stored safely, not being allowed add it as plain text.
4. The returned JSON contains some japanese words, the application must be prepared to show them properly.
5. The application must be ready to accept new i18n packages.
6. The application must be as flexible as possible for possible future changes and improvements.

## Building System

The project building system is currently [Gradle Android][1] and can be easily imported to [Android Studio][2] by importing the project as a Gradle project, and below some more details about the project building process.

### Version

- Release: NaN
- Development: 0.0.1_SNAPSHOT (1)

### Dependencies

- Android SDK 4.2+ (API 17+), with Android Build-tools Rev.24+
- Groovy 2.3.6
- Gradle 2.8
- Android Plugin 2.2.2

Or you can use the `./gradlew` to let the Gradle Wrapper download the dependencies and do all the magic for you.

### Supported API Level

- The minimum SDK Version: 17
- The target SDK Version: 25

### Building Instructions

Below the description of some main tasks could be executed via Gradle:

```sh
~$ ./gradlew assemble # Assembles all variants (buildTypes and productFlavors) of the application
~$ ./gradlew assembleDebug # Assembles all Debug builds of all flavors
~$ ./gradlew assembleRelease # Assembles all Release builds of all flavors
```

## License    
    
    Copyright (c) Mahmoud Abdurrahman 2017. All Rights Reserved.
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
      http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

[1]: http://tools.android.com/tech-docs/new-build-system/user-guide
[2]: http://developer.android.com/sdk/installing/studio.html