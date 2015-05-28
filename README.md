#Judgels Sealtiel

[![Build Status](https://travis-ci.org/ia-toki/judgels-sealtiel.svg?branch=master)](https://travis-ci.org/ia-toki/judgels-sealtiel)

##Description
Sealtiel is a [message oriented middleware](http://en.wikipedia.org/wiki/Message-oriented_middleware) application built using [Play Framework](https://www.playframework.com/) to provide messaging functions and services between its clients.

Sealtiel can act as intermediary for sending any messages between its clients. Sealtiel's clients have to fetch the message periodically to get new messages.

##Set Up And Run
To set up Sealtiel, you need to:

1. Install [Rabbitmq](http://www.rabbitmq.com/) on your Operating System.

2. Clone [Judgels Play Commons](https://github.com/ia-toki/judgels-play-commons) into the same level of Sealtiel directory, so that the directory looks like:
    - Parent Directory
        - judgels-play-commons
        - judgels-sealtiel

3. Copy conf/application_default.conf into conf/application.conf and change the configuration accordingly. **Refer to the default configuration file for explanation of the configuration keys.** Sealtiel needs Rabbitmq to store incoming messages, you need to setup the access in the configuration.

4. Copy conf/db_default.conf into conf/db.conf and change the configuration accordingly. **Refer to the default configuration file for explanation of the configuration keys.**

To run Sealtiel, just run "activator" then it will check and download all dependencies and enter Play Console.
In Play Console use "run" command to run Sandalphon. By default it will listen on port 9000. For more information of Play Console, please read the [documentation](https://www.playframework.com/documentation/2.3.x/PlayConsole).

The version that is recommended for public use is [v0.1.0](https://github.com/ia-toki/judgels-sealtiel/tree/v0.1.0).
