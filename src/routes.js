const { predictHandler, predictPhotoHandler } = require("./handler");

const routes = [
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
