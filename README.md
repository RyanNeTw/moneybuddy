# moneybuddy

Welcome to moneybuddy's backend.

### First step

```bash
cp .env.example .env
```

insert the right values to variables

### Second step

```bash
docker compose up --build
```

### Third step

Go to [Localhost](http://localhost:8080)

You can know dev, enjoy !

## API Reference

#### Register

```http
  POST /api/auth/register
```

| Parameter         | Type     | Description   |
| :---------------- | :------- | :------------ |
| `email`           | `string` | **Required**. |
| `password`        | `string` | **Required**. |
| `confirmPassword` | `string` | **Required**. |
| `pin`             | `string` | **Required**. |

#### Login

```http
  POST /api/auth/login
```

| Parameter  | Type     | Description   |
| :--------- | :------- | :------------ |
| `email`    | `string` | **Required**. |
| `password` | `string` | **Required**. |

#### Get Me

```http
  GET /api/auth/me
```

| Parameter  | Type     | Description                          |
| :--------- | :------- | :----------------------------------- |
| `JwtToken` | `string` | **Required**. Authorization : Bearer |

#### SubAccount Login

```http
  POST /api/auth/subAccount/login
```

| Parameter  | Type     | Description                         |
| :--------- | :------- | :---------------------------------- |
| `JwtToken` | `string` | **Required**. Authorization: Bearer |
| `id`       | `string` | **Required**.                       |
| `pin`      | `string` | **Required For Parent SubAccount**. |

#### Get SubAccount Me

```http
  GET /api/auth/subAccount/me
```

| Parameter  | Type     | Description                          |
| :--------- | :------- | :----------------------------------- |
| `JwtToken` | `string` | **Required**. Authorization : Bearer |
