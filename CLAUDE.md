# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring-based corporate LDAP directory web application that provides a public directory search interface. It uses Thymeleaf for templating and ldaptive for LDAP operations.

## Build & Development Commands

### Building the Project
```bash
./gradlew build
```

Note: There's currently a Gradle/JDK compatibility issue. The project uses Gradle 8.10 with JDK 25, but the Gretty plugin (loaded from GitHub) may be incompatible with this JDK version. The build.gradle specifies Java 17 as sourceCompatibility.

### Running the Application
```bash
./gradlew appRun
```

This starts a Tomcat 10 server on port 8080 at context path `/`. The Gretty plugin is configured with managedClassReload for hot-reloading during development.

### Cleaning Build Artifacts
```bash
./gradlew clean
```

### Building WAR File
```bash
./gradlew war
```

This produces `dirf-0.3.1.war` in the `build/libs/` directory.

### Checking for Dependency Updates
```bash
./gradlew dependencyUpdates
```

## Architecture

### Package Structure

- **net.nicoleroy.directory** - Core LDAP functionality (directory abstraction layer)
  - `LDAPFactory` - Main LDAP connection factory using connection pooling (SoftLimitConnectionPool)
  - `LDAPInfo` / `LDAPInfoElement` - Generic LDAP attribute data structures
  - `DirectoryPerson` - Domain model for person objects
  - `LDAPUtils` - Conversion utilities (LDAP to domain objects)

- **net.nicoleroy.dirf.controller** - Spring MVC controllers
  - `LdapSearchController` - Main search interface controller
  - `LoginController` - Login handling
  - `SearchResult` - Session-scoped search result holder

- **net.nicoleroy.dirf.springconfig** - Spring configuration classes
  - `WebConfig` - Main Spring MVC configuration with component scanning
  - `ThymeleafConfig` - Thymeleaf template engine setup

### Configuration

Configuration is split across multiple files in `WEB-INF/classes/`:

- **application.conf** (Typesafe Config format) - LDAP connection settings
  - `directoryURL` - LDAP server URL (default: ldap://localhost:10389)
  - `searchBase` - LDAP search base DN (default: dc=example,dc=com)

- **application.properties** - Spring application properties (referenced in WebConfig)
- **controller.properties** - Controller-specific properties (referenced in LdapSearchController)
- **thymeleaf.properties** - Thymeleaf template properties (referenced in ThymeleafConfig)

### Spring Configuration

The application uses annotation-based Spring configuration with two contexts:

1. **Root Application Context** - Loaded from `/WEB-INF/applicationContext.xml` by ContextLoaderListener
2. **Web Application Context** - Loaded from `net.nicoleroy.dirf.springconfig` package by DispatcherServlet

Note: There's a package name inconsistency - WebConfig scans `net.nicholasroy.whitepages.controller` but controllers are in `net.nicoleroy.dirf.controller`.

### LDAP Search Flow

1. User submits search via POST to `/ldst`
2. `LdapSearchController.ldstPost()` calls `LDAPFactory.findEntriesByStringSearch()`
3. `LDAPFactory` parses the search string and builds complex LDAP filters:
   - Handles various name formats (first last, last first, single token)
   - Searches across multiple attributes (displayName, sn, givenName, userid, mail)
   - Automatically adds wildcards for broader matching
4. Results stored in session-scoped `SearchResult` bean
5. Redirect behavior based on result count:
   - 0 results: redirect to `/ldst` (empty form)
   - 1 result: redirect to `/ldst?userid={id}` (single person view)
   - Multiple results: redirect to `/ldsr` (search results list)

### Thymeleaf Templates

Templates located in `/WEB-INF/templates/`:
- `ldst.html` - Single person view / search form
- `ldsr.html` - Multiple search results list
- `login.html` - Login page (controller exists but may not be wired up)
- `thymeTest.html` - Test template

Templates use `.html` suffix and HTML template mode.

## Key Implementation Details

### LDAP Connection Management
- Uses connection pooling via `SoftLimitConnectionPool` for performance
- Configuration loaded from `application.conf` using Typesafe Config library
- Connections must be explicitly closed to return to pool

### Dynamic Search Filter Generation
The `LDAPFactory.findEntriesByStringSearch()` method implements sophisticated search logic:
- Sanitizes input by removing `(`, `)`, and `\` characters
- Parses name variations (handles "first last", "last, first", single tokens)
- Builds multiple OR'd filter combinations for comprehensive matching
- Supports wildcard searches when `*` is included in input

### Reflection-Based Object Mapping
`LDAPUtils.LDAPInfoListToDirectoryPersonList()` uses reflection to map LDAP attributes to DirectoryPerson setters dynamically, allowing flexible attribute mapping without hardcoded field mappings.

## Known Issues

1. **Package Name Mismatch** - WebConfig component scan points to wrong package (net.nicholasroy vs net.nicoleroy, whitepages vs dirf)
2. **Gradle/JDK Compatibility** - Gretty plugin loaded from GitHub may not support JDK 25
3. **No Test Suite** - src/test/ directory doesn't exist
4. **Hardcoded TODO Comments** - Exception handling has placeholder printStackTrace() calls
5. **Deprecated Thymeleaf Version** - Uses thymeleaf-spring4 (for Spring 4) but project uses Spring 6
