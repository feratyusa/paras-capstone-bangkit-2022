const {
  predictPhotoHandler,
  createUserHandler,
  loginHandler,
  getUserByUsername,
} = require("./handler");

const routes = [
  {
    method: "POST",
    path: "/login",
    options: {
      payload: {
        auth: false,
        allow: ["multipart/form-data", "application/json"],
        multipart: true,
      },
      handler: loginHandler,
    },
  },
  {
    method: "POST",
    path: "/register",
    options: {
      payload: {
        auth: false,
        allow: ["multipart/form-data", "application/json"],
        multipart: true,
      },
      handler: createUserHandler,
    },
  },
  {
    method: "GET",
    path: "/account/{username}",
    handler: getUserByUsername,
  },
  {
    method: "POST",
    path: "/predict-photo",
    options: {
      payload: {
        output: "file",
        allow: "multipart/form-data",
        multipart: true,
      },
      handler: predictPhotoHandler,
    },
  },
];

module.exports = routes;
