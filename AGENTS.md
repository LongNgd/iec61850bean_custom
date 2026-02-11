# Agent Guidelines for IEC61850bean

This is a Java library implementing the IEC 61850 MMS communication standard for clients and servers.

## Build & Test Commands

```bash
# Build the project
./gradlew build

# Run all tests
./gradlew test

# Run a single test class
./gradlew test --tests "com.beanit.iec61850bean.integrationtests.ClientServerITest"

# Run a single test method
./gradlew test --tests "com.beanit.iec61850bean.integrationtests.SclTests.testClientServerCom"

# Check code formatting (Spotless with Google Java Format)
./gradlew spotlessCheck

# Apply code formatting fixes
./gradlew spotlessApply

# Generate Javadoc
./gradlew javadoc

# Generate Javadoc for all modules
./gradlew javadocAll

# Create distribution archive
./gradlew tar

# Clean build artifacts
./gradlew clean
```

## Project Structure

- **Build System**: Gradle with Kotlin DSL (`build.gradle.kts`)
- **Java Version**: Java 8 (sourceCompatibility/targetCompatibility = VERSION_1_8)
- **Gradle Version**: 8.5
- **Test Framework**: JUnit 5 (Jupiter)
- **Architecture Testing**: ArchUnit
- **Code Formatting**: Spotless with Google Java Format
- **Logging**: SLF4J + Logback

### Source Directories

- `src/main/java/` - Main source code
- `src/main/java-gen/` - Generated ASN.1 code (do not edit manually)
- `src/test/java/` - Test source code
- `src/test/resources/` - Test resources (SCL files, etc.)

### Package Structure

- `com.beanit.iec61850bean` - Main IEC 61850 library
- `com.beanit.iec61850bean.app` - Console applications (client/server)
- `com.beanit.iec61850bean.internal` - Internal implementation (not public API)
- `com.beanit.iec61850bean.clientgui` - GUI client
- `com.beanit.josistack` - JOSI stack (ACSE/Presentation layers)
- `com.beanit.jositransport` - Transport layer (RFC 1006)

## Code Style Guidelines

### Formatting

- **Indent**: 2 spaces (Google Java Format standard)
- **Max line length**: 100 characters
- **Braces**: Same line (K&R style)
- **Imports**: Organized by Spotless, no wildcard imports

Always run `./gradlew spotlessApply` before committing.

### Imports

```java
// Standard Java imports first
import java.io.IOException;
import java.util.List;

// Third-party imports
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Project imports last
import com.beanit.iec61850bean.ClientAssociation;
```

### Naming Conventions

- **Classes**: PascalCase (e.g., `ClientAssociation`, `ServerModel`)
- **Methods**: camelCase (e.g., `getDataValues()`, `setValue()`)
- **Fields**: camelCase, private with getters/setters
- **Constants**: UPPER_SNAKE_CASE with `static final`
- **Abstract classes**: Start with base name (e.g., `BasicDataAttribute`)
- **Interfaces**: Nouns or adjectives (e.g., `ServerEventListener`)

### Types

- Use primitive types where possible (e.g., `int` instead of `Integer`)
- Use `final` for method parameters and local variables when not reassigned
- Avoid raw types, use generics
- Use `List<>` instead of `ArrayList<>` for field/parameter types

### Error Handling

```java
// Use checked exceptions for recoverable errors
public void connect() throws IOException {
    // ...
}

// Document exceptions in Javadoc
/**
 * @throws IOException if connection fails
 * @throws ServiceError if service request fails
 */

// RuntimeException for programming errors
if (parameter == null) {
    throw new IllegalArgumentException("Parameter cannot be null");
}
```

### Javadoc

- All public classes and methods must have Javadoc
- Use `@param`, `@return`, `@throws` tags
- Keep first sentence concise (summary)
- Example:
```java
/**
 * Connects to the server at the given address.
 *
 * @param address the server address
 * @param port the server port
 * @return the client association
 * @throws IOException if connection fails
 */
```

### Testing

- Test classes end with `Test` or `ITest` (for integration tests)
- Use JUnit 5 annotations: `@Test`, `@BeforeEach`, `@AfterEach`
- Test methods should be package-private (no `public` modifier)
- Static imports for assertions:
```java
import static org.junit.jupiter.api.Assertions.*;
```

### Architecture Rules

- Internal packages (`*.internal.*`) should not be accessed from outside
- ArchUnit tests enforce package dependencies
- Keep public API minimal and well-documented

## Important Notes

- Do not modify files in `src/main/java-gen/` - they are auto-generated from ASN.1
- Run `./gradlew spotlessCheck` before committing
- Integration tests may require network resources
- The project uses OSGi bundle configuration (Bnd)
- Lombok is available but used sparingly
