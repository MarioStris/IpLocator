# IpLocator

## Table of Contents
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Authentication](#authentication)
- [API Endpoints](#api-endpoints)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Usage Examples](#usage-examples)
- [Development](#development)
- [Contributing](#contributing)
- [Swagger](#swagger)

A Spring Boot application that provides IP geolocation services through multiple providers with rate limiting and caching capabilities.

## Features

- **Multiple IP Location Providers**: Support for various IP geolocation services
- **Rate Limiting**: Built-in request rate limiting to prevent abuse
- **Validation**: IP address format validation
- **RESTful API**: Clean REST endpoints for IP location lookup
- **Provider-specific Queries**: Ability to query specific geolocation providers
- **Error Handling**: Comprehensive exception handling with meaningful error messages

## Technology Stack

- **Java 17+**
- **Spring Boot 3.x**
- **Gradle** - Build tool
- **Spring Web** - REST API framework
- **Spring Test** - Testing framework
- **MockMvc** - Integration testing

## Authentication

The application uses Basic Authentication for API access. Credentials are configured in application properties.

### Configuration

Add the following properties to your `application.properties`:

```properties
auth.username=USERNAME
auth.password=PASSWORD
```

### Usage

Include the Basic Authentication header in your API requests:

```bash
# Using curl
curl -H "Authorization: Basic YWRtaW46cGFzc3dvcmQxMjM=" http://localhost:8080/api/endpoint

# Using username:password (curl will encode automatically)
curl -u USERNAME:PASSWORD http://localhost:8080/api/endpoint
```

### Implementation

The authentication is handled by `BasicAuthFilter` which:
- Validates Basic Authentication headers
- Compares credentials against application properties
- Sets Spring Security context for authenticated requests
- Allows unauthenticated access to `/api/auth/**`, `/swagger-ui/**`, and `/v3/api-docs/**`

### Security Notes

- Change default credentials in production
- Use environment variables for sensitive credentials
- Consider using encrypted property values for passwords


## API Endpoints

### Get IP Location by Provider

```properties
GET /api/ip-location/lookup/provider/{provider}/ip/{ip}
```

**Parameters:**

```properties
- `provider` (path) - The geolocation provider (e.g., "IPAPI")
- `ip` (path) - The IP address to lookup (e.g., "8.8.8.8")
```

**Example Request:**

```bash
GET /api/ip-location/lookup/provider/IPAPI/ip/8.8.8.8
```
**Example Response:**

```properties
{
"ip": "8.8.8.8",
"countryName": "United States",
"cityName": "Mountain View",
"regionName": "California",
"latitude": 37.4056,
"longitude": -122.0775
}
```

**Error Responses:**
```properties
 `400 Bad Request` - Invalid IP address format
 `500 Internal Server Error` - Provider service unavailable
```
## Project Structure


```bash
src/
├── main/
│   └── java/
│       └── org/locator/iplocator/
│           ├── controllers/         # REST controllers
│           ├── model/              # Data models
│           ├── validation/         # Input validation
│           └── IpLocatorApplication.java
└── test/
└── java/
└── org/locator/iplocator/   # Unit and integration tests
```
## Getting Started

### Prerequisites

- Java 17 or higher
- Gradle 7.x or higher

### Running the Application

1. Clone the repository:
```properties
   git clone <repository-url>
   cd IpLocator
```

2. Build the project:
```properties
   ./gradlew build
```

3. Run the application:

```properties
   ./gradlew bootRun
```

The application will start on `http://localhost:8095`

### Running Tests

./gradlew test
```bash
   ./gradlew bootRun
```
## Configuration

The application can be configured through `application.properties` or `application.yml`:

# Server configuration
```properties
 server.port=8095
```

# Rate limiting configuration
```properties
app.rate-limit.requests-per-minute=60
```

# Provider configuration
```properties
app.providers.ipapi.enabled=true
app.providers.ipapi.url=http://ip-api.com/json/
```

## Usage Examples

### Basic IP Lookup
```properties
curl "http://localhost:8080/api/ip-location/lookup/provider/IPAPI/ip/8.8.8.8"
```

### Error Handling

# Invalid IP format
```properties
curl "http://localhost:8080/api/ip-location/lookup/provider/IPAPI/ip/invalid.ip"
Returns: 400 Bad Request with error message
```

## Swagger

Once the application is running, you can access the interactive API documentation at:

```properties
**Swagger UI**: `http://localhost:8095/swagger-ui.html`
**OpenAPI JSON**: `http://localhost:8095/v3/api-docs`
```

## Development

### Adding New Providers

1. Implement the provider interface in the service layer
2. Add provider configuration
3. Update the facade to include the new provider
4. Add comprehensive tests

### Testing

The project includes:
- Unit tests for individual components
- Integration tests for API endpoints
- MockMvc tests for controller behavior

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request