---
layout: page
title: Development
permalink: /dev/
---

# License, Versioning, and Development Workflow

Capitalizing on existing processes from the DG Toolkit and Open Contracting Explorer, which are both released under the permissive open-source 
[MIT License](https://github.com/devgateway/forms-makueni/blob/develop/LICENSE), 
the OC Portal uses the same license model. Source code contributions can be provided [on the Github Platform](https://github.com/devgateway/ocportal) by the community, while the whole tool 
can be reused in different contexts – free of charge as is, or enhanced and modified as needed.

As for our development workflow, we used the popular Gitflow workflow during development, which helped isolate feature development on branches. 
Additionally, it enabled us to maintain a stable master version of the tool, while the development version was going through active changes.

For version releases, we are using the Semantic Versioning Specification (SemVer) model and strictly adhere to updating both frontend and backend components.

# Releasing Software Updates Through Artifact Publishing

Once a new version of the tool is ready to be released, software artifacts that can be readily deployed on servers are published on the [DG's releases portal](https://artifactory.dgdev.org/ui/repos/tree/General/ocportal-open-source) 
and can be downloaded by anyone. This publishing model makes setup and deployment easier as there is no need to download and compile the source code of the tool every time a server is updated.
