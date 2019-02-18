Enlightened
===========
[![Build Status](https://travis-ci.com/MasoodFallahpoor/Enlightened.svg?branch=master)](https://travis-ci.com/MasoodFallahpoor/Enlightened) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/b919ed970a2c47319821a81deeeae94c)](https://www.codacy.com/app/MasoodFallahpoor/Enlightened?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=MasoodFallahpoor/Enlightened&amp;utm_campaign=Badge_Grade)

Enlightened is a news reader app.

Goal
=====
Enlightened tries to follow the best practices of Android app development regarding app architecture. It's kept as minimal as possible (in terms of functionality) so don't expect a full-fledged news reader app.

Architecture
============
The architecture of Enlightened is based on [Clean Architecture](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html).
To implement Clean Architecture in Android you can read [Architecting Android...The clean way?](https://fernandocejas.com/2014/09/03/architecting-android-the-clean-way/) and [Android architecture](http://five.agency/android-architecture-part-1-every-new-beginning-is-hard/).
[Architecting Android...reloaded](https://fernandocejas.com/2018/05/07/architecting-android-reloaded/) is also a good read.

API
=====
Enlightened uses the API provided by [NewsApi.org](https://newsapi.org/) to list and search latest news headlines. Because of the 'developer plan' of NewsApi.org some
limitations are imposed on the app like news headlines are updated every 15 minutes.

Notable libraries
================
A bunch of libraries are used to implement Enlightened. Notable ones are:
- [Dagger 2](https://github.com/google/dagger/)
- [Retrofit](https://square.github.io/retrofit/)
- [RxJava](https://github.com/ReactiveX/RxJava/)
- [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle/)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel/)
- [Room](https://developer.android.com/topic/libraries/architecture/room/)
- [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/)