---
layout: page
title: Architecture
permalink: /architecture/
---

## Technologies

The OCPortal architecture stack is composed of the following open source frameworks:

- Apache Wicket - web framework
- Spring Web - web framework
- PostgreSQL - database
- Hibernate - object relational mapping framework
- Hibernate Envers - audit/versioning
- Spring Boot - development productivity
- Spring Data - easier data access
- Spring Security - authentication and access control
- React + Redux - UI / Frontend dashboards
- MongoDB - JSON database
- PMC Reporter App - Android

When a user request is being processed, Spring security is the first layer to be invoked. 

It enforces security policies by blocking access to unauthenticated users and for authenticated users it controls access based on the associated roles.

After that, the UI layer is invoked. Depending on the URL one of the three UI frameworks are executed. Wicket processes the main site. 
Spring Web handles the public API. React handles the dashboards and charts available for public use.

[Spring Boot](https://spring.io/projects/spring-boot) makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run". 

Features:

- Create stand-alone Spring applications
- Embed Tomcat, Jetty or Undertow directly (no need to deploy WAR files)
- Provide opinionated 'starter' dependencies to simplify your build configuration
- Automatically configure Spring and 3rd party libraries whenever possible
- Provide production-ready features such as metrics, health checks, and externalized configuration
- Absolutely no code generation and no requirement for XML configuration

[Spring Data’s](https://spring.io/projects/spring-data) mission is to provide a familiar and consistent, 
Spring-based programming model for data access while still retaining the special traits of the underlying data store.

It makes it easy to use data access technologies, relational and non-relational databases, map-reduce frameworks, and cloud-based data services. 
This is an umbrella project which contains many sub projects that are specific to a given database. The projects are developed by working together with many of the companies and developers that are behind these exciting technologies.

[Hibernate ORM](https://hibernate.org/orm/) (or simply Hibernate) is an object–relational mapping tool for the Java programming language. 
It provides a framework for mapping an object-oriented domain model to a relational database. Hibernate handles object–relational impedance mismatch 
problems by replacing direct, persistent database accesses with high-level object handling functions.

Hibernate's primary feature is mapping from Java classes to database tables, and mapping from Java data types to SQL data types. 
Hibernate also provides data query and retrieval facilities. It generates SQL calls and relieves the developer 
from the manual handling and object conversion of the result set.

[The Envers module](https://hibernate.org/orm/envers/) is a core Hibernate model that works both with Hibernate and JPA. 
In fact, you can use Envers anywhere Hibernate works whether that is standalone, inside WildFly or JBoss AS, Spring, Grails, etc.

The Envers module aims to provide an easy auditing / versioning solution for entity classes.

Features:
- Auditing of all mappings defined by the JPA specification
- Auditing some Hibernate mappings that extend JPA, e.g. custom types and collections/maps of "simple" types like Strings, Integers.
- Logging data for each revision using a "revision entity"
- Querying historical snapshots of an entity and its associations

[Apache Wicket](https://wicket.apache.org/), commonly referred to as Wicket, is a component-based web application framework 
for the Java programming language conceptually similar to JavaServer Faces and Tapestry.

[React](https://reactjs.org/) (also known as React.js or ReactJS) is an open-source front-end JavaScript library for building user interfaces or UI components.
React can be used as a base in the development of single-page or mobile applications. 
However, React is only concerned with state management and rendering that state to the DOM, so creating React applications 
usually requires the use of additional libraries for routing, as well as certain client-side functionality.

[Redux](https://redux.js.org/) is an open-source JavaScript library for managing application state. 
It is most commonly used with libraries such as React or Angular for building user interfaces, Similar to (and inspired by) Facebook's Flux architecture.


**PMC Reporter App** is an Android application built to facilitate data entry in remote areas where the internet is not available. 
It is developed as a web app, based exclusively on HTML5 technologies, bundled as an Android application. 
Because it is basically a webapp, it can be easily repacked as an iPhone app if needed.

[MongoDB](http://mongodb.com/) is a source-available cross-platform document-oriented database program. 
Classified as a NoSQL database program, MongoDB uses JSON-like documents with optional schemas.


## DG-Toolkit
The DG Toolkit is an open source technology starter kit, developed and continually tested over dozens of DG’s projects across 6 years.
It ties together multiple ready-to use technologies in a streamlined format. 
DG-Toolkit is an open-source boilerplate/template project that was used as a starting point for the OCPortal.

It is created as a mavenized multi-module project. Each module can be started independently of the rest. All modules are based on Spring Boot templates.

Features

- Spring Boot as back-end
- Preconfigured JPA/SQL Database
- Preconfigured NoSQL Database
- Preconfigured Security/Roles/Users
- Dependencies : Maven, npm
- Preconfigured UI Boilerplate - React
- Wicket/Dozer - Tools for building complex forms (IMS)
- More: Preconfigured Checkstyle, Integration testing, Service status, etc...

![DGToolkit Diagram](/assets/img/dg-toolkit.png)

## OC-Portal

The public-facing version of the tool – accessible to anyone looking to search for individual procurements or use the M&E dashboard – 
leverages the Open Contracting Explorer platform. It is powered by [MongoDB](https://www.mongodb.com/) as the JSON store and [ReactJS](https://reactjs.org/) 
as the Javascript library. With a public tool, government officials, as well as citizens, can access the database and explore Makueni County’s data and use it 
to make decisions on how to improve procurement efficiency, increase value for money, and reduce corruption risk.

![Architecture Diagram](/assets/img/architecture-diagram.png)

The public tool’s configuration provides fast access to data that can be shared with users, as well as a version of the dataset that is OCDS compliant, 
and which can be directly downloaded and used by the public. Additionally, the public tool uses a decoupled read-only database, 
which enhances the security of the tool. A single source of information is kept in the PostgreSQL database (not part of the public portal,) 
which therefore is more secure. Another benefit of our configuration is that public traffic does not affect the performance of the data entry platform running PostgreSQL.

MongoDB’s Aggregation Framework fuels the visualizations in the dashboard interface. It provides an aggregated view of the data, which can be 
narrowed down and filtered by different criteria. Additionally, the data can either be browsed or exported.

Two different flavors of the API are available to the public – one that is compliant with 
[Open Contracting Data Standard (OCDS) 1.1](https://opencontracting.makueni.go.ke/swagger-ui/#/ocds-controller), 
and a second API, [that can be used to download the Makueni-specific data model](https://opencontracting.makueni.go.ke/swagger-ui/#/makueni-data-controller). 
OCDS output is validated during generation, using the [jOCDS Validator](https://devgateway.github.io/jocds/).

The public portal is also providing SMS based procurement-related feedback through the Infobip message portal.