# Routes API

## /login

### POST

#### Input:

Form Data

| field     | value           |
| --------- | --------------- |
| username  | prabu           |
| password  | halosayang      |
| email     | prabu@email.com |
| handphone | 0812121212      |
| photo     | (attach a file) |

#### Response

- 200: Success. The response will return user data
- 409: Failed login (Email doesn't exist or password is wrong)

## /register

### POST

#### Input

Form Data

| field     | value           |
| --------- | --------------- |
| username  | prabu           |
| password  | halosayang      |
| email     | prabu@email.com |
| handphone | 0812121212      |
| photo     | (attach a file) |

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

## /account/{username}/edit

### POST

#### Input

Form Data, takes only one input. Just email or handphone or photo

| field | value           |
| ----- | --------------- |
| email | prabu@email.com |

or

| field     | value       |
| --------- | ----------- |
| handphone | 08123456789 |

or

| field | value         |
| ----- | ------------- |
| photo | (attach file) |

#### Response

- 200: The response will return the corresponding user data

## /history/{username}

### GET

#### Input

Only need authorization, fill `username` with any username

```
/history/prabu
```

#### Response

- 200: The response will return all the corresponding user histories image prediction OR an empty history.
- 404: Username not found

## /history/{username}/{id}

### GET

#### Input

Only need authorization, fill `username` with username and `id` with history id

```
    /history/prabu/aBcDeFgHiJkalmnOpqrST (history id has 20 characters)
```

#### Response

- 200: The response will return the corresponding user's history data with corresponding id
- 404: Cannot find user's history data

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
