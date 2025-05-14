# Fundraising Events - Backend API

## Overview
A backend system for managing charity collection boxes during fundraising events. The application handles multi-currency donations through integration with [ExchangeRate-API](https://www.exchangerate-api.com/), automatically converting funds to an event's base currency during transfers.
## API Endpoints

### Fundraising Events
| Method | Endpoint                     | Description                      |
|--------|------------------------------|----------------------------------|
| POST   | `/api/events/create`         | Create new event                 |
| GET    | `/api/events/financialReport`| Get financial report             |

### Collection Boxes
| Method | Endpoint                          | Description                      |
|--------|-----------------------------------|----------------------------------|
| POST   | `/api/boxes/create`              | Register new box                 |
| GET    | `/api/boxes`                     | List all boxes                   |
| DELETE | `/api/boxes/{boxId}/delete`      | Unregister box                   |
| PUT    | `/api/boxes/{boxId}/assign/{eventId}` | Assign box to event        |
| POST   | `/api/boxes/{boxId}/addMoney`    | Add money to box                 |
| POST   | `/api/boxes/{boxId}/empty`       | Transfer money to event          |

## Instruction 

1. **Clone & Build**
```bash
git clone https://github.com/mat3usq/Fundrising-Events
cd fundraising-app
mvn clean install
```

2. **Run Application**
```bash
mvn spring-boot:run
```

3. **Access API** 
```bash
http://localhost:8080
```

4. **Database Access**
```bash
H2 Console: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:fundrising
Username: sa 
Password:
```
