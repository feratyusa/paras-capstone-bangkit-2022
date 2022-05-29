# Routes API

## /login

### POST

#### Input:

Form Data

| field    | value           |
| -------- | --------------- |
| username | prabu           |
| password | halosayang      |
| email    | prabu@email.com |
| phone    | 0812121212      |
| photo    | (attach a file) |

#### Response

- 200: Success. The response will return user data
- 409: Failed login (Email doesn't exist or password is wrong)

## /register

### POST

#### Input

Form Data

| field    | value           |
| -------- | --------------- |
| username | prabu           |
| password | halosayang      |
| email    | prabu@email.com |
| phone    | 0812121212      |
| photo    | (attach a file) |

#### Response

- 201: User creation successful. The response will return user data in JSON
- 409: Email already exist on server

## /account/{username}

### GET

#### Input

Only need authorization fill username with any username

```
/account/prabu
```

#### Response

- 200: The response will return all the corresponding user data
- 404: Username not found

## /history/{username}

### GET

#### Input

Only need authorization, fill username with any username

```
/history/prabu
```

#### Response

- 200: The response will return all the corresponding user histories image prediction OR an empty history.
- 404: Username not found

## /predict-photo

### POST

#### Input

Form Data

| field    | value           |
| -------- | --------------- |
| username | prabu           |
| image    | bear.jpg (file) |

#### Response

- 200: Success identifying the image
