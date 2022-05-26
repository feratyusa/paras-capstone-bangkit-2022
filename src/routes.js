const {
  predictHandler,
  predictPhotoHandler,
  createUserHandler,
  loginHandler,
} = require("./handler");

const routes = [
  {
    method: "POST",
    path: "/create-user",
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
    path: "/login",
    handler: loginHandler,
  },
  {
    method: "GET",
    path: "/predict",
    handler: () => "Try to use POST method",
  },
  {
    method: "POST",
    path: "/predict-photourl",
    handler: predictHandler,
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
