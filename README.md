# Secure chat application

Simple chat app with frontend developed using Angular framework. Backend application is developed using Java Spring and has few steps:

Message is encrypted and splited into n random parts.
Encrypted parts are sent to three RabbitMQ servers.
Background service (message-listener) is collecting message parts from queue, composing message and saving it to database.

Message listener, every backend service, MySQL database and RabbitMQ server are deployed on Docker using a docker-compose file. 


