# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring-based corporate LDAP directory web application that provides a public directory search interface. It uses Thymeleaf for templating and ldaptive for LDAP operations.

## Build & Development Commands

### Building the Project
```bash
./gradlew build
```

The project uses Gradle 8.12.1 with Java 17. A `gradle.properties` file is configured to use Java 17, avoiding compatibility issues with the Gretty plugin when running on systems with newer JDK versions.

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

### Running Tests
```bash
./gradlew test
```

Test reports are generated at `build/reports/tests/test/index.html`.

### Checking for Dependency Updates
```bash
./gradlew dependencyUpdates
```

## Architecture

### Package Structure

- **net.nicoleroy.directory** - Core LDAP functionality (directory abstraction layer)
  - `LDAPFactory` - Main LDAP connection factory using ldaptive 2.x ConnectionFactory pattern
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

The application uses Jakarta EE 10 (web-app 6.0) namespace in web.xml, and Spring WebMVC 6.0.23. Configuration classes implement `WebMvcConfigurer` interface (not the deprecated `WebMvcConfigurerAdapter`).

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

### LDAP Connection Management (Ldaptive 2.x)
- Uses `DefaultConnectionFactory` with builder pattern for connection management
- `SearchOperation` handles connections internally (no manual open/close required)
- Configuration loaded from `application.conf` using Typesafe Config library
- Connection factory built with `ConnectionConfig` for flexible configuration

### Dynamic Search Filter Generation
The `LDAPFactory.findEntriesByStringSearch()` method implements sophisticated search logic:
- Sanitizes input by removing `(`, `)`, and `\` characters
- Parses name variations (handles "first last", "last, first", single tokens)
- Builds multiple OR'd filter combinations for comprehensive matching
- Supports wildcard searches when `*` is included in input

### Reflection-Based Object Mapping
`LDAPUtils.LDAPInfoListToDirectoryPersonList()` uses reflection to map LDAP attributes to DirectoryPerson setters dynamically, allowing flexible attribute mapping without hardcoded field mappings.

## Testing

The project includes comprehensive test fixtures and unit tests:

- **TestFixtures.java** - Factory methods for creating test data (DirectoryPerson, LDAPInfo objects)
- **LDAPInfoTest.java** - 11 unit tests for LDAPInfo operations
- **LDAPUtilsTest.java** - 7 unit tests for utility methods
- **test-data.ldif** - Sample LDAP entries for integration testing

All tests use JUnit 4.13.2. Run tests with `./gradlew test`.

## Dependencies

Key dependencies and their versions:
- **Spring WebMVC**: 6.0.23
- **Jackson Databind**: 2.17.2 (fixes resource exhaustion vulnerabilities)
- **Thymeleaf**: 3.1.3.RELEASE with spring6 integration (fixes CVE-2023-38286 - sandbox bypass)
- **Ldaptive**: 2.3.2 (fixes CVE-2014-3607 - SSL certificate validation vulnerability)
- **SLF4J**: 2.0.9 with Logback Classic 1.4.14 for logging
- **JUnit**: 4.13.2

All known security vulnerabilities have been resolved.

## Logging

The application uses SLF4J with Logback for logging:
- **LdapSearchController** - Logs LDAP errors and unexpected exceptions with context (userid/search string)
- **LDAPUtils** - Logs reflection errors during attribute mapping
  - NoSuchMethodException at DEBUG level (expected for unmapped LDAP attributes)
  - Other reflection exceptions at ERROR level with attribute context

Configure logging levels via `logback.xml` in the classpath.

## Known Issues

None - all technical debt and security vulnerabilities have been resolved.
