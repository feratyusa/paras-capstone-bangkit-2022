# Routes API

## /login

### POST

#### Input:

Form Data or JSON

```
{
    'email': 'prabu@email.com',
    'password': 'bangkitjaya'
}
```

| field    | value           |
| -------- | --------------- |
| email    | prabu@email.com |
| password | bangkitjaya     |

#### Response

- 200: Success login
- 409: Failed login (Email doesn't exist or password is wrong)

## /register

### POST

#### Input

JSON or Form Data

```
{
    'email': 'prabu@email.com',
    'password': 'bangkitjaya'
}
```

| field    | value           |
| -------- | --------------- |
| email    | prabu@email.com |
| password | bangkitjaya     |

#### Response

- 201: User creation successful
- 409: Email already exist on server
- 500: Server error when creating user

## /predict-photo

### POST

#### Input

Form Data

| field | value           |
| ----- | --------------- |
| image | bear.jpg (file) |

#### Response

- 200: Success identifying the image
