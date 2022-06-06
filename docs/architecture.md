---
layout: page
title: Architecture
permalink: /architecture/
---
The platform was created using [Spring Boot](https://spring.io/projects/spring-boot) and uses [PostgreSQL](https://www.postgresql.org/) as the main open-source database 
to store procurement metadata and files.

The data entry module uses the DG Toolkit form builder components, based on [Apache Wicket 8](https://wicket.apache.org/) and [Hibernate](https://hibernate.org/) 
to provide rich data input forms for the different procurement phases, enabling complex [Spring Security](https://spring.io/projects/spring-security) 
user role hierarchies, validation and workflow management. With different levels of validation and edit access assigned to user, admin, 
and approver roles within the data entry module, the system offers individual customization for different types of users.

![Architecture Diagram](/assets/img/architecture-diagram.png)

The public-facing version of the tool – accessible to anyone looking to search for individual procurements or use the M&E dashboard – 
leverages the Open Contracting Explorer platform. It is powered by [MongoDB](https://www.mongodb.com/) as the JSON store and [ReactJS](https://reactjs.org/) 
as the Javascript library. With a public tool, government officials, as well as citizens, can access the database and explore Makueni County’s data and use it 
to make decisions on how to improve procurement efficiency, increase value for money, and reduce corruption risk.

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