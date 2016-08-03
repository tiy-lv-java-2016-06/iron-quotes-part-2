# Iron Quotes Part 2

![get to work](gettowork.jpg)

## Description
Let's take the Iron Quotes project up a level and make it much more like a REST api you might find in the real world.  There are two main parts to this assignement.  First, is to convert it to use token authentication.  Second, is to deploy it on heroku.

## Requirements

### Token Auth
* Add a token field to the user which is populated in the constructor.
* Add an expiration date that is set 30 days in the future whenever the token is created.
* Add a method to the repository to make it possible to find first record by token
* Make an endpoint that will take name and password and return a JSON payload of the token.
* Make the end points check for a token header and pull the user based on the token to check for authorization

### Heroku
* Create your heroku app
* Add a database at the free level
* Create the `Procfile`
* Make sure to set the port and JDBC_DATABASE_URL so that heroku can change it
* Deploy to heroku
* Test the app

## Resources
* [Github Repo](https://github.com/tiy-lv-java-2016-06/iron-quotes-part-2)
* [Deploying with Heroku](https://devcenter.heroku.com/articles/deploying-spring-boot-apps-to-heroku)
* [Heroku Toolbelt](https://toolbelt.heroku.com/)
