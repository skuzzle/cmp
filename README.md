# countmy.pizza
This is the source code of https://countmy.pizza

_Count my pizza_ serves two main purposes for me. Obviously, I use it to track my pizza consume and share the results
with everybody (who probably couldn't care less). More importantly it developed to be a serious learning project for
various technologies and architectural ideas.

## Design principles

### No bloated JS framework
The whole frontend is rendered on server side using Thymeleaf. The page is only gradually enhanced using JavaScript, 
following the ideas of _progressive enhancement_ and the [ROCA principles](https://roca-style.org/).

### Mobile first
The application is intended to be usable on mobile with the same comfort as on desktop. We're using 
[bulma.io](https://bulma.io) as the main responsive CSS framework.

Mobile first also means to take limited bandwidth and computing power of clients into account. We thrive to send as 
little data and JavaScript as possible to save on both. Additionally we use aggressive caching of all static or rarely
changing resources. 

### No external resources
All resources that are required by the frontend for being rendered shall be delivered by our own server. We do not want 
to expose users to third party trackers without them noticing.
There are some features that _obviously_ require third party resource access such as _Login with Google_. That's OK as
long as the user is explicitly made aware of this fact.

### No login or account required
Every core feature shall be usable without having to register or login through some third party provider. Logging in 
only provides some convenience features.

### Domain Driven Design
There is no one way of doing DDD, but we do lend many ideas from the concepts of DDD:
* The main business logic is implemented in a clean object oriented way
* Heavy validation for preventing illegal states of domain objects
* Loosely coupled feature implementations

### Stand alone API
Though the REST API of the backend is currently not exposed to the public and only used internally by the client, its 
whole design is centered around the idea of also being used stand alone.

### Continuous delivery
_If it builds, it works_

Every push to the _master_ branch shall build the project and, on success, deploy the the result to production. The test 
suite must thus be comprehensive to provide maximum confidence that the whole application works as intended after it has 
been automatically deployed.

As we currently run on docker-compose its not easy to achieve actualy _zero-downtime deployments_. We do our best though 
to fake it for the user:
* The proxy serves an update notification if it encounters a gateway timeout from the application
* The frontent is built resilient enough to not crash with an error in case the backend application has not fully booted

