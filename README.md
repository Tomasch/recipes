To run the project for development purposes, use Spring profile `dev`. It disables security for the application and will also allow you to use swagger for the requests. Documentation is generated semi-automatically, based on the integration tests, using Spring Rest Docs.

The user is directed to API documentation right after successful login with credentials `admin/admin`. In real life production environment, it would be integrated with AD/OAuth, so for this project just simple in-memory user storage was used.

API requests are using DTOs to separate requests from entity representation, but since we don't want to hide any information from Recipe entity, whole created/updated object is being presented to the user upon successful operation. Database operations are hidden behind custom repository class and `RecipeService` acts as intermediary for recipe operations between the controller and database layer.

Integration tests cover most of the integration points for every controller method. UT should be the biggest part of testing as those are giving the quickest feedback to the developer regarding features verification.
