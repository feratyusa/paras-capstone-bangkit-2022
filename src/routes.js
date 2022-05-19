const predictHandler = require("./handler");

const routes = [
  {
    method: "GET",
    path: "/predict",
    handler: () => "Try to use POST method",
  },
  {
    method: "POST",
    path: "/predict",
    handler: predictHandler,
  },
];

module.exports = routes;
