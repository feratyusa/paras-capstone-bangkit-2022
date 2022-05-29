const {
  predictPhotoHandler,
  createUserHandler,
  loginHandler,
  getUserByUsername,
  getHistoryHandler,
  predictTestPhotoHandler,
} = require("./handler");

const routes = [
  {
    method: "POST",
    path: "/login",
    options: {
      auth: false,
      payload: {
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
      auth: false,
      payload: {
        output: "file",
        allow: "multipart/form-data",
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
    method: "GET",
    path: "/history/{username}",
    handler: getHistoryHandler,
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
  {
    method: "POST",
    path: "/test-predict-photo",
    options: {
      auth: false,
      payload: {
        output: "file",
        allow: "multipart/form-data",
        multipart: true,
      },
      handler: predictTestPhotoHandler,
    },
  },
];

module.exports = routes;
