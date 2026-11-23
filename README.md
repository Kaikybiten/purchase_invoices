# Purchase Invoices

A full-stack application for reading Brazilian electronic consumer invoices (NFC-e) through QR Codes, extracting product information, and recording purchases in a PostgreSQL database.

## Demo

![Purchase Invoices Demo](./docs/demo.gif)

**Live application:** [purchase-invoices.onrender.com](https://purchase-invoices.onrender.com/)

## Features

- **QR Code Scanning:** Read NFC-e QR Codes using a device's camera.
- **Invoice Extraction:** Retrieve invoice information directly from the URL encoded in the QR Code.
- **Product Processing:** Extract product descriptions, codes, quantities, unit prices, and total prices.
- **Data Validation:** Validate extracted product information before persistence.
- **Duplicate Prevention:** Prevent an invoice from being registered more than once using its access token.
- **Purchase History:** Persist invoices and their associated products.
- **Product Reuse:** Identify previously registered products based on their codes and unit prices.
- **Anonymous Sessions:** Control access to protected endpoints without requiring user registration.
- **Rate Limiting:** Restrict excessive requests to protect the API.

## Technologies

### Backend
- Java 17
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- Spring Security
- Hibernate
- PostgreSQL
- Flyway
- Jsoup
- Bucket4j
- Maven

### Frontend
- React 19
- TypeScript
- Vite
- Tailwind CSS
- ZXing Browser

### Infrastructure
- Docker
- Docker Compose

### Testing
- JUnit 5
- Mockito

## Architecture

The application follows a layered architecture, separating HTTP requests, business logic, data persistence, and external HTML extraction.

```text
                Browser / Mobile
                       |
                       v
                React + TypeScript
                       |
                       v
                 REST API
                       |
          +------------+------------+
          |            |            |
          v            v            v
      Controller    Security     Validation
          |
          v
        Service
          |
     +----+----------------+
     |                     |
     v                     v
 Repository            HTML Scraper
     |                     |
     v                     v
 PostgreSQL            SEFAZ Website
```

### Backend organization

| Package | Responsibility |
|---|---|
| `config` | Security and validation configuration |
| `controller` | REST API endpoints |
| `dto` | Response data transfer objects |
| `exception` | Centralized exception handling |
| `filter` | Anonymous session validation and rate limiting |
| `model` | JPA entities and request models |
| `repository` | Database access |
| `scrapper` | External HTML retrieval and data extraction |
| `service` | Business rules and application logic |
| `response` | Standardized API responses |

### Invoice processing flow

1. The frontend accesses the device camera and scans the NFC-e QR Code using ZXing.
2. The QR Code URL is sent to the backend through a REST request.
3. The backend retrieves the corresponding SEFAZ page using Java HttpClient.
4. Jsoup parses the HTML and extracts invoice and product information.
5. Extracted values undergo validation, including price and quantity consistency checks.
6. The application checks whether the invoice has already been registered.
7. Existing products are reused, while new products are persisted.
8. The invoice and its associated purchases are stored in PostgreSQL.
9. The API returns the processed invoice and product information.

## Database

PostgreSQL stores three main entities:

- **Invoice:** Stores the invoice access token and issue date.
- **Product:** Stores product information, including description, code, unit of measurement, and unit price.
- **RecordedPurchases:** Associates invoices with products, recording quantities, total prices, and purchase dates.

Database schema changes are managed through Flyway migrations, while Hibernate validates the schema at application startup.

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/session` | Creates an anonymous session |
| POST | `/invoices` | Processes and records an NFC-e invoice |
| GET | `/product` | Retrieves registered products |
| GET | `/product/{id}` | Retrieves a product by ID |

### Invoice processing

**Request:**

```http
POST /invoices
Content-Type: application/json
```

```json
{
  "url": "NFC-e_QR_CODE_URL"
}
```

**Successful response:**

Returns the invoice access token, invoice date, and associated product information.

**Error handling:**

- `400 Bad Request`: No products could be extracted from the invoice.
- `401 Unauthorized`: Missing or expired anonymous session.
- `409 Conflict`: Invoice already registered.
- `429 Too Many Requests`: Request rate limit exceeded.

## Getting Started

### Prerequisites

- Git
- Docker
- Docker Compose

### 1. Clone the repository

```bash
git clone https://github.com/Kaikybiten/purchase_invoices.git

cd purchase_invoices
```

### 2. Configure environment variables

Create a `.env` file in the project root:

```env
DATABASE_URL=jdbc:postgresql://postgres:5432/purchase_invoices
DATABASE_USERNAME=invoice_app
DATABASE_PASSWORD=your_password
```

Use your own database credentials.

### 3. Start the application

```bash
docker compose up --build -d
```

Docker Compose starts the PostgreSQL database and builds the application image.

The frontend and backend are served through the same application.

Access the application at:

```text
http://localhost:8080
```

### 4. Stop the application

```bash
docker compose down
```

To remove containers and associated volumes:

```bash
docker compose down -v
```

**Warning:** Removing database volumes permanently deletes their stored data.

## Running Tests

Execute the automated tests using Maven Wrapper:

```bash
./mvnw test
```

The project uses JUnit 5 and Mockito to validate service-layer behavior, including interactions with mocked repositories and dependent services.

## Project Structure

```text
purchase_invoices/
├── docs/
│   └── demo.gif
├── frontend/
│   └── src/
│       ├── components/
│       │   ├── invoice/
│       │   └── reader/
│       ├── service/
│       └── types/
├── src/
│   ├── main/
│   │   ├── java/com/example/purchase_invoices/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── exception/
│   │   │   ├── filter/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   ├── response/
│   │   │   ├── scrapper/
│   │   │   └── service/
│   │   └── resources/
│   │       └── db/migration/
│   └── test/
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

## Author

**Kaiky Bitencourt**

- GitHub: [@Kaikybiten](https://github.com/Kaikybiten)

---

Developed as a full-stack project focused on Java, Spring Boot, REST APIs, React, PostgreSQL, and automated testing.