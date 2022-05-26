const { predictPhotoHandler, createUserHandler, loginHandler } = require("./handler");

const routes = [
  {
    method: "POST",
    path: "/login",
    options: {
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
      payload: {
        allow: ["multipart/form-data", "application/json"],
        multipart: true,
      },
      handler: createUserHandler,
    },
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
